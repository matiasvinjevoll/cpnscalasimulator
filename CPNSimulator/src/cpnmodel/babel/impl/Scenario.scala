package cpnmodel.babel.impl

import collection.Multiset

object Scenario {

  // Constants

  val n = 5
  val infinite = 65535
  val hopCount = 10
  val historyCount = 4
  val minHellos = 1
  val resend = 2
  val significantMetricChange = 1

  // Initialization
  val initTopology =
    Multiset[(Int, List[Int])](
      (1, List(2, 3)),
      (2, List(1, 4)),
      (3, List(1, 5)),
      (4, List(2, 5)),
      (5, List(3, 4)))

  val initNeighbours: Multiset[(Int, NeighbourTable)] =
    Multiset(
      (1, NeighbourTable(List(
        NeighbourTableEntry(2, 1, 1, 0),
        NeighbourTableEntry(3, 1, 1, 0)))),
      (2, NeighbourTable(List(
        NeighbourTableEntry(4, 1, 1, 0),
        NeighbourTableEntry(1, 1, 1, 0)))),
      (3, NeighbourTable(List(
        NeighbourTableEntry(5, 1, 1, 0),
        NeighbourTableEntry(1, 1, 1, 0)))),
      (4, NeighbourTable(List(
        NeighbourTableEntry(5, 1, 1, 0),
        NeighbourTableEntry(2, 1, 1, 0)))),
      (5, NeighbourTable(List(
        NeighbourTableEntry(4, 1, 1, 0),
        NeighbourTableEntry(3, 1, 1, 0)))))

  val initRoutes: Multiset[(Int, RouteTable)] =
    Multiset(
      (1, RouteTable(List(
        RouteTableEntry(1, 1, 0, 1, true)))),
      (2, RouteTable(List(
        RouteTableEntry(2, 2, 0, 1, true),
        RouteTableEntry(1, 1, 0, 1, true)))),
      (3, RouteTable(List(
        RouteTableEntry(3, 3, 0, 1, true),
        RouteTableEntry(1, 1, 0, 1, true)))),
      (4, RouteTable(List(
        RouteTableEntry(4, 4, 0, 1, true),
        RouteTableEntry(1, 2, 1, 1, true)))),
      (5, RouteTable(List(
        RouteTableEntry(5, 5, 0, 1, true),
        RouteTableEntry(1, 3, 1, 1, true)))))

  val initSources: Multiset[(Int, SourceTable)] =
    Multiset(
      (1, SourceTable(List(SourceTableEntry(1, 1, 0)))),
      (2, SourceTable(List(
        SourceTableEntry(1, 1, 1),
        SourceTableEntry(2, 1, 0)))),
      (3, SourceTable(List(
        SourceTableEntry(1, 1, 1),
        SourceTableEntry(3, 1, 0)))),
      (4, SourceTable(List(
        SourceTableEntry(1, 1, 2),
        SourceTableEntry(4, 1, 0)))),
      (5, SourceTable(List(
        SourceTableEntry(1, 1, 2),
        SourceTableEntry(5, 1, 0)))))

  def initPendingReqs(n: Int): Multiset[(Int, PendingReqTable)] = n match {
    case 0 => Multiset()
    case n => initPendingReqs(n - 1) ++ Multiset((1, (n, PendingReqTable(List[PendingReqTableEntry]()))))
  }

  def initSeqNo(n: Int): Multiset[(Int, SeqNos)] = n match {
    case 0 => Multiset()
    case n => initSeqNo(n - 1) ++ Multiset((1,(n, SeqNos(1, 1))))
  }

}