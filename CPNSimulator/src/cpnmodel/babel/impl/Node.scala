package cpnmodel.babel.impl
import cpnmodel.babel.impl.BabelMethods._
import cpnmodel.babel.impl.Scenario._
import cpnmodel.babel.impl.Util._

// Data structures for nodes

case class SeqNos(seqno: Int, helloSeqno: Int) {

  def createHello(node: Int, dest: Address) = Packet(Unicast(node), dest, HelloTLV(helloSeqno))

  def incHelloSeqNo = SeqNos(seqno, helloSeqno + 1)

}

case class NeighbourTableEntry(neighbour: Int, history: Int, txcost: Int, helloSeqno: Int) {
  def rxcost = if (history == 1) 1 else infinite
}

case class NeighbourTable(neighbours: List[NeighbourTableEntry]) {

  def getNeighbour(neighbour: Int) = neighbours.find { _.neighbour == neighbour }

  // A node only accepts packets from neighbours and
  // packets containing a HelloTlv from non-neighbours
  def acceptPacket(sender: Int, tlv: TLV) = tlv match {
    case HelloTLV(_) => true
    case _ => neighbours.exists(n => n.neighbour == sender)
  }

  def ignorePacket(sender: Int, tlv: TLV) = tlv match {
    case HelloTLV(_) => false
    case _ => !neighbours.exists(n => n.neighbour == sender)
  }

  def updateTxCost(neighbour: Int, ihu: IHUTLV) =
    NeighbourTable(
      neighbours map {
        case NeighbourTableEntry(neigh, history, txcost, helloSeqno) if neigh == neighbour =>
          NeighbourTableEntry(neigh, history, ihu.rxcost, helloSeqno)
        case x => x
      })

  def hasNeighbour(node: Int) = neighbours exists (n => n.neighbour == node)

  def acquireNeighbour(node: Int) = NeighbourTable(NeighbourTableEntry(node, 0, infinite, 0) :: neighbours)

  import LinkStatus._
  def updateHistory(linkStatus: LinkStatus.Value, neighbour: Int, hello: HelloTLV) =
    neighbours find (n => n.neighbour == neighbour) match {
      case Some(NeighbourTableEntry(neigh, _, txcost, helloSeqno)) =>
        val history = if (linkStatus == Up) 1 else 0
        NeighbourTable(
          NeighbourTableEntry(
            neigh,
            history,
            txcost,
            helloSeqno max hello.seqno) :: removeFirst(neighbours) { _.neighbour == neighbour })
      case None => println("her4"); this
    }

  def createIHU(node: Int) =
    neighbours.map(n => Packet(Unicast(node), Unicast(n.neighbour), IHUTLV(n.history)))
}
case class SourceTableEntry(routerId: Int, seqno: Int, metric: Int)

case class SourceTable(sources: List[SourceTableEntry]) {

  def init = if (sources isEmpty) SourceTable(sources) else SourceTable(sources.init)

  def getSource(source: Int) = sources find { _.routerId == source }

  // returns true if update is feasible - false if unfeasible or route retraction (infinite metric)
  // TODO: May be error in this method
  def feasibleRoute(update: UpdateTLV) =
    sources.find(s => s.routerId == update.routerId) match {
      case Some(src) =>
        src.seqno < update.seqno ||
          (src.seqno == update.seqno && src.metric > update.metric)
      case None =>
        update.metric < infinite
    }

  def feasibleUpdate(update: UpdateTLV) = update match {
    case UpdateTLV(seqno, metric, routerId) =>
      metric == infinite ||
        (sources.find(s => s.routerId == routerId) match {
          case Some(src) =>
            src.seqno < seqno ||
              (src.seqno == seqno && src.metric > metric)
          case None => false
        })
  }

  def seqNoReqPacket(sender: Int, reciever: Int, update: UpdateTLV) =
    sources.find(s => s.routerId == update.routerId) match {
      case Some(src) =>
        Packet(Unicast(sender), Unicast(reciever), SeqNoReqTLV(src.seqno, hopCount, update.routerId)) :: Nil
      case None => List()
    }

  def createSeqNoReqs(node: Int, dest: Address) =
    sources.map(s => Packet(Unicast(node), dest, SeqNoReqTLV(s.seqno + 1, hopCount, s.routerId)))

  def updateFD(update: UpdateTLV) =
    if (update.metric == infinite) this
    else
      sources.find { _.routerId == update.routerId } match {
        case Some(s) =>
          if (update.seqno > s.seqno ||
            (update.seqno == s.seqno && s.metric > update.metric))
            SourceTable(
              SourceTableEntry(update.routerId, update.seqno, update.metric) ::
                removeFirst(sources) { _.routerId == update.routerId })
          else this
        case None => SourceTable(
          SourceTableEntry(update.routerId, update.seqno, update.metric) :: sources)
      }
}

case class RouteTableEntry(source: Int, neighbour: Int, metric: Int, seqno: Int, selected: Boolean)

case class RouteTable(routes: List[RouteTableEntry]) {
  // Must retain order of ´routes´!

  def length = routes.length

  def getRoutes(source: Int) = routes.filter { _.source == source }

  def hasRouteFromNeighbour(neighbour: Int, update: UpdateTLV) = {
    routes.exists(route =>
      route.neighbour == neighbour && route.source == update.routerId)
  }

  def addRouteEntry(neighbour: Int, update: UpdateTLV) = {
    val UpdateTLV(seqno, metric, routerId) = update
    RouteTable(RouteTableEntry(routerId, neighbour, metric, seqno, false) :: routes)
  }

  def updateRoute(neighbour: Int, update: UpdateTLV) = {
    RouteTable(
      routes.map {
        case RouteTableEntry(src, neigh, metric, seqno, selected) if neigh == neighbour && src == update.routerId =>
          RouteTableEntry(
            src,
            neigh,
            update.metric,
            update.seqno,
            (selected && update.metric < infinite))
        case x => x
      })
  }

  def neighbouringRoutes(neighbour: Int) = {
    routes.collect {
      case rt: RouteTableEntry if rt.neighbour == neighbour => rt
    }
  }

  def routeTableDump = {
    val (selected, rest) = routes.partition { _.selected }
    val retracted = rest filterNot (rr => selected.exists(sr => sr.source == rr.source))
    retracted.distinct ::: selected
  }

  def advertiseRoutes(sender: Int, reciever: Int, source: Int) = {
    if (source == 0)
      routeTableDump.map { RouteAdv(sender, Unicast(reciever), _) }
    else routes.find(r => r.selected && r.source == source) match {
      case Some(route) => RouteAdv(sender, Unicast(reciever), route) :: Nil
      case None => RouteAdv(sender, Unicast(reciever), RouteTableEntry(source, sender, infinite, 1, false)) :: Nil
    }
  }

  def advertiseRoutes(sender: Int) = {
    routeTableDump.map { RouteAdv(sender, Multicast(), _) }
  }

  def hasRoute(routerId: Int, requester: Int) = {
    routes.exists(r =>
      r.selected &&
        r.neighbour != requester &&
        r.source == routerId &&
        r.metric < infinite)
  }
  
  def doForwardSNR(node: Int, snreq: SeqNoReqTLV) = {
    routes.exists {
      case RouteTableEntry(source, neighbour, metric, seqno, selected) =>
        selected &&
          source == snreq.routerId &&
          seqno < snreq.seqno &&
          node != snreq.routerId &&
          snreq.hopCount > 1
    }
  }

  def forwardSNR(node: Int, snreq: SeqNoReqTLV) = {
    routes.find(r => r.source == snreq.routerId && r.selected) match {
      case Some(route) => Packet(Unicast(node), Unicast(route.neighbour), snreq) :: Nil
      case None => Nil
    }
  }

  def routeUpToDate(snreq: SeqNoReqTLV) = {
  	routes.exists(r => r.selected && r.source == snreq.routerId && r.seqno >= snreq.seqno)
  }

  def doUpdateCurrentNodesSeqNo(node: Int, snreq: SeqNoReqTLV) = {
  	routes.exists(r => r.selected && r.source == snreq.routerId && node == snreq.routerId && r.seqno < snreq.seqno)
  }

  def incSeqNoRoute(source: Int) = {
    routes.find(r => r.source == source && r.selected) match {
      case Some(RouteTableEntry(source, neighbour, metric, seqno, selected)) =>
        RouteTable(
          RouteTableEntry(source, neighbour, metric, seqno + 1, selected) ::
            removeFirst(routes)(r => r.source == source && r.selected))
      case None => this
    }
  }

  def setSelected(route: RouteTableEntry) = RouteTable(routes map {
    case current @ RouteTableEntry(source, neighbour, metric, seqno, _) =>
      if (route == current)
        RouteTableEntry(source, neighbour, metric, seqno, metric < infinite)
      else if (route.source == source)
        RouteTableEntry(source, neighbour, metric, seqno, false)
      else current
  })

  def unselectRoute(source: Int) = RouteTable(routes map {
    case current @ RouteTableEntry(src, neighbour, metric, seqno, selected) =>
      if (source == src)
        RouteTableEntry(src, neighbour, metric, seqno, false)
      else current
  })

  def createRouteReq(node: Int) = {
    val r = routes.filter { _.source != node }
    if (r.length > 0) {
      val RouteTableEntry(src, neighbour, metric, seqno, selected) = r.last
      Packet(Unicast(node), Unicast(neighbour), RouteReqTLV(src)) :: Nil
    } else Nil
  }

  def expireRoute(node: Int) = {
    val route :: tail = routes // Expire head of routes list
    RouteTable(
      //should not expire route when node is originating it. Moves route to the back of the list (reset timer)
      if (route.source == node)
        tail ::: route :: Nil
      else if (route.metric == infinite)
        tail
      else
        tail ::: route.copy(selected = false) :: Nil)
  }

  def triggerRouteSelection(node: Int) = {
    val route :: tail = routes
    if (route.metric < infinite && route.source != node)
      (node, route.source, this) :: Nil
    else Nil
  }

}

case class PendingReqTableEntry(routerId: Int, forwardedFrom: Int, resendNo: Int)

case class PendingReqTable(pendingReqs: List[PendingReqTableEntry]) {

  def init = if (pendingReqs.isEmpty) PendingReqTable(pendingReqs) else PendingReqTable(pendingReqs.init)

  def hasPendingReq(routerId: Int) = pendingReqs.exists { _.routerId == routerId }

  def add(from: Int, snreq: SeqNoReqTLV) =
    PendingReqTable(PendingReqTableEntry(snreq.routerId, from, resend) :: pendingReqs)

  def remove(routes: List[RouteTableEntry]) = routes find { _.selected } match {
    case Some(route) => PendingReqTable(removeFirst(pendingReqs) { _.routerId == route.source })
    case None => this
  }
}
