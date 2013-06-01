package cpnmodel.babel.impl

import cpnmodel.babel.impl.Scenario._
import cpnmodel.babel.impl.Util._

object BabelMethods {

  // Misc

  case class RecTLV(sender: Int, node: Int, tlv: TLV)

  case class NodeRouteSrc(node: Int, src: Int)

  case class RouteAdv(sender: Int, reciever: Address, route: RouteTableEntry)

  // Assumed that sources for all entries in ´oldRoutes´ and ´newRoutes´ are equal.
  case class RouteChanges(node: Int, oldRoutes: List[RouteTableEntry], newRoutes: List[RouteTableEntry]) {

    val (os, ns) = (oldRoutes.find { _.selected }, newRoutes.find { _.selected })

    def noChanges = os == ns

    def insigChanges = !(signMetricChange || routeRetracted || newRoute)

    def insMetricChange = (os, ns) match {
      case (Some(os), Some(ns)) =>
        val change = (os.metric max ns.metric) - (os.metric min ns.metric)
        change < significantMetricChange && change != 0
      case _ => false
    }
    
    def signMetricChange = (os, ns) match {
      case (Some(os), Some(ns)) =>
        val change = (os.metric max ns.metric) - (os.metric min ns.metric)
        change >= significantMetricChange && change < infinite
      case _ => false
    }

    def routeRetracted = (os, ns) match {
      case (Some(os), Some(ns)) if os.metric < infinite => ns.metric == infinite
      case (Some(os), None) if os.metric < infinite => true
      case _ => false
    }

    def nextHopChanged = (os, ns) match {
      case (Some(os), Some(ns)) => os.neighbour != ns.neighbour
      case _ => false
    }

    def seqNoChanged = (os, ns) match {
      case (Some(os), Some(ns)) => os.seqno != ns.seqno
      case _ => false
    }
    
    def newRoute = (os, ns) match {
      case (None, Some(_)) => true
      case _ => false
    }

    def requestedUpdate(preqs: PendingReqTable) = ns match {
      case Some(ns) => preqs.hasPendingReq(ns.source)
      case None => false
    }

    def updateMessageData(dest: Address) = ns match {
      case Some(ns) => RouteAdv(node, dest, ns) :: Nil
      case None => os match {
        case Some(os) =>
          newRoutes.find(r => r.neighbour == os.neighbour) match {
            case Some(retractedRoute) => RouteAdv(node, dest, retractedRoute) :: Nil
            case None => Nil
          }
        case None => Nil
      }
    }

    def seqNoReqRetraction(sources: SourceTable) = os match {
      case Some(os) => sources.getSource(os.source) match {
        case Some(src) => Packet(Unicast(node), Multicast(), SeqNoReqTLV(src.seqno + 1, hopCount, os.source)) :: Nil
        case None => Nil
      }
      case None => Nil
    }
  }

  abstract class Address
  case class Unicast(target: Int) extends Address
  case class Multicast extends Address
  
  object LinkStatus extends Enumeration {
    val Up, Down = Value
  }

  case class Packet(src: Address, dest: Address, tlv: TLV)

  def computeLinkCost(node: Int, route: RouteTableEntry, neighbours: NeighbourTable) =
    if (node == route.source) 0
    else neighbours.getNeighbour(route.neighbour) match {
      case Some(neigh) => neigh.txcost max neigh.rxcost
      case None => infinite
    }
  def routeSelection(node: Int, source: Int, routes: RouteTable, neighbours: NeighbourTable) = {
    def routeMetric(route: RouteTableEntry) = route.metric + computeLinkCost(node, route, neighbours)

    def shortestRoute(srcRoutes: List[RouteTableEntry]): Option[RouteTableEntry] = srcRoutes match {
      case Nil => None
      case route :: Nil =>
        if (routeMetric(route) < infinite) Some(route)
        else None
      case head :: second :: tail =>
        if (routeMetric(head) < routeMetric(second))
          shortestRoute(head :: tail)
        else
          shortestRoute(second :: tail)
    }

    val srcRoutes = routes.getRoutes(source)

    shortestRoute(srcRoutes) match {
      case Some(route) =>
        routes.setSelected(route)
      case None =>
        routes.unselectRoute(source)
    }
  }
  
  def generateUpdate(node: Int, route: RouteTableEntry, neighbours: NeighbourTable) = {
    val linkCost = computeLinkCost(node, route, neighbours)
    if (route.metric + linkCost >= infinite)
      UpdateTLV(route.source, route.seqno, infinite)
    else
      UpdateTLV(route.source, route.seqno, route.metric + linkCost)

  }

  def transmit(packet: Packet, adjList: List[Int], success: Boolean) =
    if (success) {
      if (packet.dest == Multicast())
        adjList.map(x => packet.copy(dest = Unicast(x)))
      else // Unicast
        packet :: Nil
    } else Nil

  def linkExists(nl1: (Int, List[Int]), nl2: (Int, List[Int])) =
    nl1._2.exists(_ == nl2._1) && nl2._2.exists(_ == nl1._1)

  def addLink(nl1: (Int, List[Int]), nl2: (Int, List[Int])) =
    (nl1._1, nl2._1 :: nl1._2) ::
      (nl2._1, nl1._1 :: nl2._2) :: Nil

  def rmLink(nl1: (Int, List[Int]), nl2: (Int, List[Int])) =
    (nl1._1, removeFirst(nl1._2)(_ == nl2._1)) ::
      (nl2._1, removeFirst(nl2._2)(_ == nl1._1)) :: Nil
}