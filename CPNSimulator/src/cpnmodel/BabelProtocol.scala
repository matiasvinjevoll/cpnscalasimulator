package cpnmodel
import model.Transition
import model.CPNGraph
import model.Arc
import model.Guard
import model.Place
import model.Pattern
import model.BindingElement
import simulator.Simulator
import model.BindGuard
import model.ArcPattern
import model.EvalGuard
import model.Direction._
import simulator.Conversions._
import scala.collection.immutable.Queue
import simulator.CollectionExtensions._
import cpnmodel.babel.impl.BabelMethods._
import cpnmodel.babel.impl._
import cpnmodel.babel.impl.Scenario._
import cpnmodel.babel.impl.TLV
import cpnmodel.babel.impl.Util
import simulator.AutomaticSimulator
import util.Benchmark
import collection.Multiset

object BabelProtocol extends App {
  println("start")

val net = CPNGraph()

// Global Places

val ID1435604444 = Place("ID1435604444", "Pending Req Table", initPendingReqs(5):
  Multiset[
    (Int,
    PendingReqTable)])
net.addPlace(ID1435604444)

val ID1431111582 = Place("ID1431111582", "Route Table", initRoutes: Multiset[(Int,RouteTable)])
net.addPlace(ID1431111582)

val ID1434194711 = Place("ID1434194711", "SeqNo", initSeqNo(5): Multiset[(Int,SeqNos)])
net.addPlace(ID1434194711)

val ID1430786899 = Place("ID1430786899", "Source Table", initSources: Multiset[(Int,SourceTable)])
net.addPlace(ID1430786899)

val ID1434189908 = Place("ID1434189908", "Neighbour Table", initNeighbours: Multiset[(Int,NeighbourTable)])
net.addPlace(ID1434189908)

// ########################################################################
// Generated code for module RouteSelection
// ########################################################################
class RouteSelection[T0, T1, T2, T3](ID1434184387: Place[T1, Multiset[RouteAdv]], ID1450873816: Place[T2, Multiset[scala.Tuple2[Int, Int]]], ID1454890427: Place[T0, Multiset[Packet]], ID1455504015: Place[T3, Multiset[scala.Tuple3[Int, Int, RouteTable]]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1], ev2: Multiset[scala.Tuple2[Int, Int]] => Traversable[T2], ev3: Multiset[scala.Tuple3[Int, Int, RouteTable]] => Traversable[T3]){

val net = CPNGraph()

// Places
val ID1433446906 = Place("ID1433446906", "New Route Selection", Multiset[RouteChanges]())
net.addPlace(ID1433446906)

// ========================================================================
// Generated code for transition Route Selection  for Route Update and its arcs
// ========================================================================

case class BindingElement_ID1455615367(
	routeSrc: Option[Int],
	routes: Option[RouteTable],
	oldRoutes: Option[RouteTable],
	node: Option[Int],
	neighbours: Option[NeighbourTable],
	newRoutes: Option[RouteTable]) extends BindingElement

val compatible_ID1455615367 = (b1: BindingElement_ID1455615367, b2: BindingElement_ID1455615367) =>
	(b1.routeSrc == b2.routeSrc || b1.routeSrc == None || b2.routeSrc == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.oldRoutes == b2.oldRoutes || b1.oldRoutes == None || b2.oldRoutes == None) &&
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None) &&
	(b1.newRoutes == b2.newRoutes || b1.newRoutes == None || b2.newRoutes == None)

val merge_ID1455615367 = (b1: BindingElement_ID1455615367, b2: BindingElement_ID1455615367) => {
	val routeSrc = if (b1.routeSrc == None) b2.routeSrc else b1.routeSrc
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val oldRoutes = if (b1.oldRoutes == None) b2.oldRoutes else b1.oldRoutes
	val node = if (b1.node == None) b2.node else b1.node
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	val newRoutes = if (b1.newRoutes == None) b2.newRoutes else b1.newRoutes
	new BindingElement_ID1455615367(routeSrc, routes, oldRoutes, node, neighbours, newRoutes)
}

val fullBindingElement_ID1455615367 = (be: BindingElement_ID1455615367) =>
	be.routeSrc != None &&
	be.routes != None &&
	be.oldRoutes != None &&
	be.node != None &&
	be.neighbours != None &&
	be.newRoutes != None

val guard_ID1455615367_0 = BindGuard((be: BindingElement_ID1455615367) => (be.node,be.routeSrc,be.routes,be.neighbours) match {
	case (Some(node),Some(routeSrc),Some(routes),Some(neighbours)) =>
		val newRoutes = routeSelection(node, routeSrc, routes, neighbours)
		BindingElement_ID1455615367(be.routeSrc, be.routes, be.oldRoutes, be.node, be.neighbours, Some(newRoutes))
}, 0)
val guard_ID1455615367 = List(guard_ID1455615367_0)

val enumBindings_ID1455615367 = List(
	)

val ID1455615367 = Transition("ID1455615367","Route Selection  for Route Update",1,compatible_ID1455615367,merge_ID1455615367,fullBindingElement_ID1455615367,guard_ID1455615367,enumBindings_ID1455615367)
net.addTransition(ID1455615367)

// ------------------ Arcs ------------------

// route update ----> Route Selection  for Route Update

val pattern_ID1455631309_0 = Pattern((token: Any) => token match {
	case Tuple2(_, scala.Tuple3((node: Int), (routeSrc: Int), (oldRoutes: RouteTable))) =>
		BindingElement_ID1455615367(Some(routeSrc), None, Some(oldRoutes), Some(node), None, None)
}, 0)
val eval_ID1455631309 = (be: BindingElement_ID1455615367) => (be.node,be.routeSrc,be.oldRoutes) match {
	case (Some(node),Some(routeSrc),Some(oldRoutes)) =>
		scala.Tuple3(node, routeSrc, oldRoutes)
}
val ID1455631309 = Arc("ID1455631309", ID1455504015, ID1455615367, In) (eval_ID1455631309)
net.addArc(ID1455631309)

// Neighbour Table ----> Route Selection  for Route Update

val pattern_ID1455654174IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1455615367(None, None, None, Some(node), Some(neighbours), None)
}, 0)
val eval_ID1455654174IN = (be: BindingElement_ID1455615367) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1455654174IN = Arc("ID1455654174IN", ID1434189908, ID1455615367, In) (eval_ID1455654174IN)
net.addArc(ID1455654174IN)

// Route Table ----> Route Selection  for Route Update

val pattern_ID1455635237_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routes: RouteTable))) =>
		BindingElement_ID1455615367(None, Some(routes), None, Some(node), None, None)
}, 0)
val eval_ID1455635237 = (be: BindingElement_ID1455615367) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1455635237 = Arc("ID1455635237", ID1431111582, ID1455615367, In) (eval_ID1455635237)
net.addArc(ID1455635237)

// Route Selection  for Route Update ----> New Route Selection


val eval_ID1455666536 = (be: BindingElement_ID1455615367) => (be.node,be.routes,be.routeSrc,be.newRoutes) match {
	case (Some(node),Some(routes),Some(routeSrc),Some(newRoutes)) =>
		RouteChanges(node, routes.getRoutes(routeSrc), newRoutes.getRoutes(routeSrc))
}
val ID1455666536 = Arc("ID1455666536", ID1433446906, ID1455615367, Out) (eval_ID1455666536)
net.addArc(ID1455666536)

// Route Selection  for Route Update ----> Neighbour Table


val eval_ID1455654174OUT = (be: BindingElement_ID1455615367) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1455654174OUT = Arc("ID1455654174OUT", ID1434189908, ID1455615367, Out) (eval_ID1455654174OUT)
net.addArc(ID1455654174OUT)

// Route Selection  for Route Update ----> Route Table


val eval_ID1455636337 = (be: BindingElement_ID1455615367) => (be.node,be.newRoutes) match {
	case (Some(node),Some(newRoutes)) =>
		scala.Tuple2(1, scala.Tuple2(node, newRoutes))
}
val ID1455636337 = Arc("ID1455636337", ID1431111582, ID1455615367, Out) (eval_ID1455636337)
net.addArc(ID1455636337)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Route Selection  for Route Update

ID1455615367.orderedPatternBindingBasis = List(
	ArcPattern(ID1455631309, pattern_ID1455631309_0),
	ArcPattern(ID1455635237, pattern_ID1455635237_0),
	ArcPattern(ID1455654174IN, pattern_ID1455654174IN_0),
	guard_ID1455615367_0)



// ========================================================================
// Generated code for transition Route Selection for Link Change / New Route and its arcs
// ========================================================================

case class BindingElement_ID1450830443(
	routeSrc: Option[Int],
	routes: Option[RouteTable],
	node: Option[Int],
	neighbours: Option[NeighbourTable],
	newRoutes: Option[RouteTable]) extends BindingElement

val compatible_ID1450830443 = (b1: BindingElement_ID1450830443, b2: BindingElement_ID1450830443) =>
	(b1.routeSrc == b2.routeSrc || b1.routeSrc == None || b2.routeSrc == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None) &&
	(b1.newRoutes == b2.newRoutes || b1.newRoutes == None || b2.newRoutes == None)

val merge_ID1450830443 = (b1: BindingElement_ID1450830443, b2: BindingElement_ID1450830443) => {
	val routeSrc = if (b1.routeSrc == None) b2.routeSrc else b1.routeSrc
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val node = if (b1.node == None) b2.node else b1.node
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	val newRoutes = if (b1.newRoutes == None) b2.newRoutes else b1.newRoutes
	new BindingElement_ID1450830443(routeSrc, routes, node, neighbours, newRoutes)
}

val fullBindingElement_ID1450830443 = (be: BindingElement_ID1450830443) =>
	be.routeSrc != None &&
	be.routes != None &&
	be.node != None &&
	be.neighbours != None &&
	be.newRoutes != None

val guard_ID1450830443_0 = BindGuard((be: BindingElement_ID1450830443) => (be.node,be.routeSrc,be.routes,be.neighbours) match {
	case (Some(node),Some(routeSrc),Some(routes),Some(neighbours)) =>
		val newRoutes = routeSelection(node, routeSrc, routes, neighbours)
		BindingElement_ID1450830443(be.routeSrc, be.routes, be.node, be.neighbours, Some(newRoutes))
}, 0)
val guard_ID1450830443 = List(guard_ID1450830443_0)

val enumBindings_ID1450830443 = List(
	)

val ID1450830443 = Transition("ID1450830443","Route Selection for Link Change / New Route",1,compatible_ID1450830443,merge_ID1450830443,fullBindingElement_ID1450830443,guard_ID1450830443,enumBindings_ID1450830443)
net.addTransition(ID1450830443)

// ------------------ Arcs ------------------

// link change / new route ----> Route Selection for Link Change / New Route

val pattern_ID1450892472_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routeSrc: Int))) =>
		BindingElement_ID1450830443(Some(routeSrc), None, Some(node), None, None)
}, 0)
val eval_ID1450892472 = (be: BindingElement_ID1450830443) => (be.node,be.routeSrc) match {
	case (Some(node),Some(routeSrc)) =>
		scala.Tuple2(1, scala.Tuple2(node, routeSrc))
}
val ID1450892472 = Arc("ID1450892472", ID1450873816, ID1450830443, In) (eval_ID1450892472)
net.addArc(ID1450892472)

// Neighbour Table ----> Route Selection for Link Change / New Route

val pattern_ID1450853140IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1450830443(None, None, Some(node), Some(neighbours), None)
}, 0)
val eval_ID1450853140IN = (be: BindingElement_ID1450830443) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1450853140IN = Arc("ID1450853140IN", ID1434189908, ID1450830443, In) (eval_ID1450853140IN)
net.addArc(ID1450853140IN)

// Route Table ----> Route Selection for Link Change / New Route

val pattern_ID1450844448_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routes: RouteTable))) =>
		BindingElement_ID1450830443(None, Some(routes), Some(node), None, None)
}, 0)
val eval_ID1450844448 = (be: BindingElement_ID1450830443) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1450844448 = Arc("ID1450844448", ID1431111582, ID1450830443, In) (eval_ID1450844448)
net.addArc(ID1450844448)

// Route Selection for Link Change / New Route ----> New Route Selection


val eval_ID1450858971 = (be: BindingElement_ID1450830443) => (be.node,be.routes,be.routeSrc,be.newRoutes) match {
	case (Some(node),Some(routes),Some(routeSrc),Some(newRoutes)) =>
		RouteChanges(node, routes.getRoutes(routeSrc), newRoutes.getRoutes(routeSrc))
}
val ID1450858971 = Arc("ID1450858971", ID1433446906, ID1450830443, Out) (eval_ID1450858971)
net.addArc(ID1450858971)

// Route Selection for Link Change / New Route ----> Neighbour Table


val eval_ID1450853140OUT = (be: BindingElement_ID1450830443) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1450853140OUT = Arc("ID1450853140OUT", ID1434189908, ID1450830443, Out) (eval_ID1450853140OUT)
net.addArc(ID1450853140OUT)

// Route Selection for Link Change / New Route ----> Route Table


val eval_ID1450841809 = (be: BindingElement_ID1450830443) => (be.node,be.newRoutes) match {
	case (Some(node),Some(newRoutes)) =>
		scala.Tuple2(1, scala.Tuple2(node, newRoutes))
}
val ID1450841809 = Arc("ID1450841809", ID1431111582, ID1450830443, Out) (eval_ID1450841809)
net.addArc(ID1450841809)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Route Selection for Link Change / New Route

ID1450830443.orderedPatternBindingBasis = List(
	ArcPattern(ID1450844448, pattern_ID1450844448_0),
	ArcPattern(ID1450853140IN, pattern_ID1450853140IN_0),
	ArcPattern(ID1450892472, pattern_ID1450892472_0),
	guard_ID1450830443_0)



// Wire modules
val st0 = new EvaluateRouteChanges(ID1433446906, ID1434184387, ID1454890427) 
net.addSubstitutionTransition(st0.net)


}



// ########################################################################
// Generated code for module ProcessUpdate
// ########################################################################
class ProcessUpdate[T0, T1, T2, T3](ID1447561206: Place[T1, Multiset[RecTLV]], ID1447561217: Place[T2, Multiset[scala.Tuple2[Int, Int]]], ID1448497372: Place[T0, Multiset[Packet]], ID1455431526: Place[T3, Multiset[scala.Tuple3[Int, Int, RouteTable]]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RecTLV] => Traversable[T1], ev2: Multiset[scala.Tuple2[Int, Int]] => Traversable[T2], ev3: Multiset[scala.Tuple3[Int, Int, RouteTable]] => Traversable[T3]){

val net = CPNGraph()

// Places




// Wire modules
val st0 = new ProcessNewRoute(ID1447561206, ID1447561217) 
net.addSubstitutionTransition(st0.net)

val st1 = new ProcessExistingRoute(ID1447561206, ID1448497372, ID1455431526) 
net.addSubstitutionTransition(st1.net)


}



// ########################################################################
// Generated code for module TransmitPacket
// ########################################################################
class TransmitPacket[T0, T1](ID1435172484: Place[T0, Multiset[Packet]], ID1435174505: Place[T0, Multiset[Packet]], ID1451193706: Place[T1, Multiset[scala.Tuple2[Int, List[Int]]]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[scala.Tuple2[Int, List[Int]]] => Traversable[T1]){

val net = CPNGraph()

// Places
val ID1446243268 = Place("ID1446243268", "Reliability", Multiset(true))
net.addPlace(ID1446243268)

// ========================================================================
// Generated code for transition Transmit and its arcs
// ========================================================================

case class BindingElement_ID1435187098(
	node: Option[Int],
	adjList: Option[List[Int]],
	packet: Option[Packet],
	success: Option[Boolean]) extends BindingElement

val compatible_ID1435187098 = (b1: BindingElement_ID1435187098, b2: BindingElement_ID1435187098) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.adjList == b2.adjList || b1.adjList == None || b2.adjList == None) &&
	(b1.packet == b2.packet || b1.packet == None || b2.packet == None) &&
	(b1.success == b2.success || b1.success == None || b2.success == None)

val merge_ID1435187098 = (b1: BindingElement_ID1435187098, b2: BindingElement_ID1435187098) => {
	val node = if (b1.node == None) b2.node else b1.node
	val adjList = if (b1.adjList == None) b2.adjList else b1.adjList
	val packet = if (b1.packet == None) b2.packet else b1.packet
	val success = if (b1.success == None) b2.success else b1.success
	new BindingElement_ID1435187098(node, adjList, packet, success)
}

val fullBindingElement_ID1435187098 = (be: BindingElement_ID1435187098) =>
	be.node != None &&
	be.adjList != None &&
	be.packet != None &&
	be.success != None

val guard_ID1435187098_0 = EvalGuard((be: BindingElement_ID1435187098) => (be.node,be.packet) match {
	case (Some(node),Some(packet)) => {
		Unicast(node).$eq$eq(packet.src)
	}
}, 0)
val guard_ID1435187098 = List(guard_ID1435187098_0)

val enumBindings_ID1435187098 = List(
	List(BindingElement_ID1435187098(None, None, None, Some(true)), BindingElement_ID1435187098(None, None, None, Some(false))))

val ID1435187098 = Transition("ID1435187098","Transmit",3,compatible_ID1435187098,merge_ID1435187098,fullBindingElement_ID1435187098,guard_ID1435187098,enumBindings_ID1435187098)
net.addTransition(ID1435187098)

// ------------------ Arcs ------------------

// Reliability ----> Transmit

val pattern_ID1446305554IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (success: Boolean)) =>
		BindingElement_ID1435187098(None, None, None, Some(success))
}, 2)
val eval_ID1446305554IN = (be: BindingElement_ID1435187098) => (be.success) match {
	case (Some(success)) =>
		success
}
val ID1446305554IN = Arc("ID1446305554IN", ID1446243268, ID1435187098, In) (eval_ID1446305554IN)
net.addArc(ID1446305554IN)

// babel to link ----> Transmit

val pattern_ID1435191076_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (packet: Packet)) =>
		BindingElement_ID1435187098(None, None, Some(packet), None)
}, 1)
val eval_ID1435191076 = (be: BindingElement_ID1435187098) => (be.packet) match {
	case (Some(packet)) =>
		packet
}
val ID1435191076 = Arc("ID1435191076", ID1435172484, ID1435187098, In) (eval_ID1435191076)
net.addArc(ID1435191076)

// topology ----> Transmit

val pattern_ID1451241284IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (adjList: List[Int]))) =>
		BindingElement_ID1435187098(Some(node), Some(adjList), None, None)
}, 0)
val eval_ID1451241284IN = (be: BindingElement_ID1435187098) => (be.node,be.adjList) match {
	case (Some(node),Some(adjList)) =>
		scala.Tuple2(1, scala.Tuple2(node, adjList))
}
val ID1451241284IN = Arc("ID1451241284IN", ID1451193706, ID1435187098, In) (eval_ID1451241284IN)
net.addArc(ID1451241284IN)

// Transmit ----> Reliability


val eval_ID1446305554OUT = (be: BindingElement_ID1435187098) => (be.success) match {
	case (Some(success)) =>
		success
}
val ID1446305554OUT = Arc("ID1446305554OUT", ID1446243268, ID1435187098, Out) (eval_ID1446305554OUT)
net.addArc(ID1446305554OUT)

// Transmit ----> link to babel


val eval_ID1435257026 = (be: BindingElement_ID1435187098) => (be.packet,be.adjList,be.success) match {
	case (Some(packet),Some(adjList),Some(success)) =>
		transmit(packet, adjList, success)
}
val ID1435257026 = Arc("ID1435257026", ID1435174505, ID1435187098, Out) (eval_ID1435257026)
net.addArc(ID1435257026)

// Transmit ----> topology


val eval_ID1451241284OUT = (be: BindingElement_ID1435187098) => (be.node,be.adjList) match {
	case (Some(node),Some(adjList)) =>
		scala.Tuple2(1, scala.Tuple2(node, adjList))
}
val ID1451241284OUT = Arc("ID1451241284OUT", ID1451193706, ID1435187098, Out) (eval_ID1451241284OUT)
net.addArc(ID1451241284OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Transmit

ID1435187098.orderedPatternBindingBasis = List(
	ArcPattern(ID1451241284IN, pattern_ID1451241284IN_0),
	ArcPattern(ID1435191076, pattern_ID1435191076_0),
	ArcPattern(ID1446305554IN, pattern_ID1446305554IN_0))



// Wire modules



}



// ########################################################################
// Generated code for module ProcessExistingRoute
// ########################################################################
class ProcessExistingRoute[T0, T1, T2](ID1447922727: Place[T1, Multiset[RecTLV]], ID1448477344: Place[T0, Multiset[Packet]], ID1448546622: Place[T2, Multiset[scala.Tuple3[Int, Int, RouteTable]]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RecTLV] => Traversable[T1], ev2: Multiset[scala.Tuple3[Int, Int, RouteTable]] => Traversable[T2]){

val net = CPNGraph()

// Places
val ID1447976039 = Place("ID1447976039", "Updated Route", Multiset[RecTLV]())
net.addPlace(ID1447976039)

// ========================================================================
// Generated code for transition Ignore Unfeasible Update and its arcs
// ========================================================================

case class BindingElement_ID1448006232(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV],
	sources: Option[SourceTable]) extends BindingElement

val compatible_ID1448006232 = (b1: BindingElement_ID1448006232, b2: BindingElement_ID1448006232) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None)

val merge_ID1448006232 = (b1: BindingElement_ID1448006232, b2: BindingElement_ID1448006232) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	val sources = if (b1.sources == None) b2.sources else b1.sources
	new BindingElement_ID1448006232(routes, srcNode, destNode, updateTlv, sources)
}

val fullBindingElement_ID1448006232 = (be: BindingElement_ID1448006232) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None &&
	be.sources != None

val guard_ID1448006232_0 = EvalGuard((be: BindingElement_ID1448006232) => (be.sources,be.updateTlv) match {
	case (Some(sources),Some(updateTlv)) => {
		sources.feasibleUpdate(updateTlv).unary_$bang
	}
}, 0)
val guard_ID1448006232 = List(guard_ID1448006232_0)

val enumBindings_ID1448006232 = List(
	)

val ID1448006232 = Transition("ID1448006232","Ignore Unfeasible Update",1,compatible_ID1448006232,merge_ID1448006232,fullBindingElement_ID1448006232,guard_ID1448006232,enumBindings_ID1448006232)
net.addTransition(ID1448006232)

// ------------------ Arcs ------------------

// Updated Route ----> Ignore Unfeasible Update

val pattern_ID1448126192_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1448006232(None, Some(srcNode), Some(destNode), Some(updateTlv), None)
}, 0)
val eval_ID1448126192 = (be: BindingElement_ID1448006232) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1448126192 = Arc("ID1448126192", ID1447976039, ID1448006232, In) (eval_ID1448126192)
net.addArc(ID1448126192)

// Source Table ----> Ignore Unfeasible Update

val pattern_ID1448099205IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (sources: SourceTable))) =>
		BindingElement_ID1448006232(None, None, Some(destNode), None, Some(sources))
}, 0)
val eval_ID1448099205IN = (be: BindingElement_ID1448006232) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1448099205IN = Arc("ID1448099205IN", ID1430786899, ID1448006232, In) (eval_ID1448099205IN)
net.addArc(ID1448099205IN)

// Route Table ----> Ignore Unfeasible Update

val pattern_ID1448063274IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1448006232(Some(routes), None, Some(destNode), None, None)
}, 0)
val eval_ID1448063274IN = (be: BindingElement_ID1448006232) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1448063274IN = Arc("ID1448063274IN", ID1431111582, ID1448006232, In) (eval_ID1448063274IN)
net.addArc(ID1448063274IN)

// Ignore Unfeasible Update ----> babel to link


val eval_ID1448485939 = (be: BindingElement_ID1448006232) => (be.sources,be.destNode,be.srcNode,be.updateTlv) match {
	case (Some(sources),Some(destNode),Some(srcNode),Some(updateTlv)) =>
		sources.seqNoReqPacket(destNode, srcNode, updateTlv)
}
val ID1448485939 = Arc("ID1448485939", ID1448477344, ID1448006232, Out) (eval_ID1448485939)
net.addArc(ID1448485939)

// Ignore Unfeasible Update ----> Source Table


val eval_ID1448099205OUT = (be: BindingElement_ID1448006232) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1448099205OUT = Arc("ID1448099205OUT", ID1430786899, ID1448006232, Out) (eval_ID1448099205OUT)
net.addArc(ID1448099205OUT)

// Ignore Unfeasible Update ----> Route Table


val eval_ID1448063274OUT = (be: BindingElement_ID1448006232) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1448063274OUT = Arc("ID1448063274OUT", ID1431111582, ID1448006232, Out) (eval_ID1448063274OUT)
net.addArc(ID1448063274OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Ignore Unfeasible Update

ID1448006232.orderedPatternBindingBasis = List(
	ArcPattern(ID1448126192, pattern_ID1448126192_0),
	ArcPattern(ID1448063274IN, pattern_ID1448063274IN_0),
	ArcPattern(ID1448099205IN, pattern_ID1448099205IN_0))



// ========================================================================
// Generated code for transition Process Existing Route and its arcs
// ========================================================================

case class BindingElement_ID1447931085(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV]) extends BindingElement

val compatible_ID1447931085 = (b1: BindingElement_ID1447931085, b2: BindingElement_ID1447931085) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None)

val merge_ID1447931085 = (b1: BindingElement_ID1447931085, b2: BindingElement_ID1447931085) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	new BindingElement_ID1447931085(routes, srcNode, destNode, updateTlv)
}

val fullBindingElement_ID1447931085 = (be: BindingElement_ID1447931085) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None

val guard_ID1447931085_0 = EvalGuard((be: BindingElement_ID1447931085) => (be.routes,be.srcNode,be.updateTlv) match {
	case (Some(routes),Some(srcNode),Some(updateTlv)) => {
		routes.hasRouteFromNeighbour(srcNode, updateTlv)
	}
}, 0)
val guard_ID1447931085 = List(guard_ID1447931085_0)

val enumBindings_ID1447931085 = List(
	)

val ID1447931085 = Transition("ID1447931085","Process Existing Route",1,compatible_ID1447931085,merge_ID1447931085,fullBindingElement_ID1447931085,guard_ID1447931085,enumBindings_ID1447931085)
net.addTransition(ID1447931085)

// ------------------ Arcs ------------------

// recieved tlv ----> Process Existing Route

val pattern_ID1447932151_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1447931085(None, Some(srcNode), Some(destNode), Some(updateTlv))
}, 0)
val eval_ID1447932151 = (be: BindingElement_ID1447931085) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1447932151 = Arc("ID1447932151", ID1447922727, ID1447931085, In) (eval_ID1447932151)
net.addArc(ID1447932151)

// Route Table ----> Process Existing Route

val pattern_ID1447942848IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1447931085(Some(routes), None, Some(destNode), None)
}, 0)
val eval_ID1447942848IN = (be: BindingElement_ID1447931085) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1447942848IN = Arc("ID1447942848IN", ID1431111582, ID1447931085, In) (eval_ID1447942848IN)
net.addArc(ID1447942848IN)

// Process Existing Route ----> Updated Route


val eval_ID1447987876 = (be: BindingElement_ID1447931085) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1447987876 = Arc("ID1447987876", ID1447976039, ID1447931085, Out) (eval_ID1447987876)
net.addArc(ID1447987876)

// Process Existing Route ----> Route Table


val eval_ID1447942848OUT = (be: BindingElement_ID1447931085) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1447942848OUT = Arc("ID1447942848OUT", ID1431111582, ID1447931085, Out) (eval_ID1447942848OUT)
net.addArc(ID1447942848OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Process Existing Route

ID1447931085.orderedPatternBindingBasis = List(
	ArcPattern(ID1447932151, pattern_ID1447932151_0),
	ArcPattern(ID1447942848IN, pattern_ID1447942848IN_0))



// ========================================================================
// Generated code for transition Update Route Table and its arcs
// ========================================================================

case class BindingElement_ID1448023951(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV],
	sources: Option[SourceTable]) extends BindingElement

val compatible_ID1448023951 = (b1: BindingElement_ID1448023951, b2: BindingElement_ID1448023951) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None)

val merge_ID1448023951 = (b1: BindingElement_ID1448023951, b2: BindingElement_ID1448023951) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	val sources = if (b1.sources == None) b2.sources else b1.sources
	new BindingElement_ID1448023951(routes, srcNode, destNode, updateTlv, sources)
}

val fullBindingElement_ID1448023951 = (be: BindingElement_ID1448023951) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None &&
	be.sources != None

val guard_ID1448023951_0 = EvalGuard((be: BindingElement_ID1448023951) => (be.sources,be.updateTlv) match {
	case (Some(sources),Some(updateTlv)) => {
		sources.feasibleUpdate(updateTlv)
	}
}, 0)
val guard_ID1448023951 = List(guard_ID1448023951_0)

val enumBindings_ID1448023951 = List(
	)

val ID1448023951 = Transition("ID1448023951","Update Route Table",1,compatible_ID1448023951,merge_ID1448023951,fullBindingElement_ID1448023951,guard_ID1448023951,enumBindings_ID1448023951)
net.addTransition(ID1448023951)

// ------------------ Arcs ------------------

// Updated Route ----> Update Route Table

val pattern_ID1448127799_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1448023951(None, Some(srcNode), Some(destNode), Some(updateTlv), None)
}, 0)
val eval_ID1448127799 = (be: BindingElement_ID1448023951) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1448127799 = Arc("ID1448127799", ID1447976039, ID1448023951, In) (eval_ID1448127799)
net.addArc(ID1448127799)

// Source Table ----> Update Route Table

val pattern_ID1448100746IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (sources: SourceTable))) =>
		BindingElement_ID1448023951(None, None, Some(destNode), None, Some(sources))
}, 0)
val eval_ID1448100746IN = (be: BindingElement_ID1448023951) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1448100746IN = Arc("ID1448100746IN", ID1430786899, ID1448023951, In) (eval_ID1448100746IN)
net.addArc(ID1448100746IN)

// Route Table ----> Update Route Table

val pattern_ID1448064743_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1448023951(Some(routes), None, Some(destNode), None, None)
}, 0)
val eval_ID1448064743 = (be: BindingElement_ID1448023951) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1448064743 = Arc("ID1448064743", ID1431111582, ID1448023951, In) (eval_ID1448064743)
net.addArc(ID1448064743)

// Update Route Table ----> route update


val eval_ID1448980136 = (be: BindingElement_ID1448023951) => (be.destNode,be.updateTlv,be.routes) match {
	case (Some(destNode),Some(updateTlv),Some(routes)) =>
		scala.Tuple3(destNode, updateTlv.routerId, routes)
}
val ID1448980136 = Arc("ID1448980136", ID1448546622, ID1448023951, Out) (eval_ID1448980136)
net.addArc(ID1448980136)

// Update Route Table ----> Source Table


val eval_ID1448100746OUT = (be: BindingElement_ID1448023951) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1448100746OUT = Arc("ID1448100746OUT", ID1430786899, ID1448023951, Out) (eval_ID1448100746OUT)
net.addArc(ID1448100746OUT)

// Update Route Table ----> Route Table


val eval_ID1448542821 = (be: BindingElement_ID1448023951) => (be.destNode,be.routes,be.srcNode,be.updateTlv) match {
	case (Some(destNode),Some(routes),Some(srcNode),Some(updateTlv)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes.updateRoute(srcNode, updateTlv)))
}
val ID1448542821 = Arc("ID1448542821", ID1431111582, ID1448023951, Out) (eval_ID1448542821)
net.addArc(ID1448542821)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Update Route Table

ID1448023951.orderedPatternBindingBasis = List(
	ArcPattern(ID1448127799, pattern_ID1448127799_0),
	ArcPattern(ID1448064743, pattern_ID1448064743_0),
	ArcPattern(ID1448100746IN, pattern_ID1448100746IN_0))



// Wire modules



}



// ########################################################################
// Generated code for module SeqNoReqResponse
// ########################################################################
class SeqNoReqResponse[T0, T1, T2](ID1443683830: Place[T2, Multiset[scala.Tuple4[Int, Int, RouteTable, SeqNoReqTLV]]], ID1443717323: Place[T0, Multiset[Packet]], ID1443732308: Place[T1, Multiset[RouteAdv]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1], ev2: Multiset[scala.Tuple4[Int, Int, RouteTable, SeqNoReqTLV]] => Traversable[T2]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Update Current Node and its arcs
// ========================================================================

case class BindingElement_ID1444220191(
	routes: Option[RouteTable],
	updatedRoutes: Option[RouteTable],
	hsn: Option[Int],
	srcNode: Option[Int],
	destNode: Option[Int],
	sn: Option[Int],
	snrTlv: Option[SeqNoReqTLV]) extends BindingElement

val compatible_ID1444220191 = (b1: BindingElement_ID1444220191, b2: BindingElement_ID1444220191) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.updatedRoutes == b2.updatedRoutes || b1.updatedRoutes == None || b2.updatedRoutes == None) &&
	(b1.hsn == b2.hsn || b1.hsn == None || b2.hsn == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.sn == b2.sn || b1.sn == None || b2.sn == None) &&
	(b1.snrTlv == b2.snrTlv || b1.snrTlv == None || b2.snrTlv == None)

val merge_ID1444220191 = (b1: BindingElement_ID1444220191, b2: BindingElement_ID1444220191) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val updatedRoutes = if (b1.updatedRoutes == None) b2.updatedRoutes else b1.updatedRoutes
	val hsn = if (b1.hsn == None) b2.hsn else b1.hsn
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val sn = if (b1.sn == None) b2.sn else b1.sn
	val snrTlv = if (b1.snrTlv == None) b2.snrTlv else b1.snrTlv
	new BindingElement_ID1444220191(routes, updatedRoutes, hsn, srcNode, destNode, sn, snrTlv)
}

val fullBindingElement_ID1444220191 = (be: BindingElement_ID1444220191) =>
	be.routes != None &&
	be.updatedRoutes != None &&
	be.hsn != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.sn != None &&
	be.snrTlv != None

val guard_ID1444220191_0 = EvalGuard((be: BindingElement_ID1444220191) => (be.routes,be.destNode,be.snrTlv) match {
	case (Some(routes),Some(destNode),Some(snrTlv)) => {
		routes.doUpdateCurrentNodesSeqNo(destNode, snrTlv)
	}
}, 0)
val guard_ID1444220191_1 = BindGuard((be: BindingElement_ID1444220191) => (be.routes,be.snrTlv) match {
	case (Some(routes),Some(snrTlv)) =>
		val updatedRoutes = routes.incSeqNoRoute(snrTlv.routerId)
		BindingElement_ID1444220191(be.routes, Some(updatedRoutes), be.hsn, be.srcNode, be.destNode, be.sn, be.snrTlv)
}, 0)
val guard_ID1444220191 = List(guard_ID1444220191_0,guard_ID1444220191_1)

val enumBindings_ID1444220191 = List(
	)

val ID1444220191 = Transition("ID1444220191","Update Current Node",1,compatible_ID1444220191,merge_ID1444220191,fullBindingElement_ID1444220191,guard_ID1444220191,enumBindings_ID1444220191)
net.addTransition(ID1444220191)

// ------------------ Arcs ------------------

// validated seqnoreqs ----> Update Current Node

val pattern_ID1444328280_0 = Pattern((token: Any) => token match {
	case Tuple2(_, scala.Tuple4((srcNode: Int), (destNode: Int), (routes: RouteTable), (snrTlv: SeqNoReqTLV))) =>
		BindingElement_ID1444220191(Some(routes), None, None, Some(srcNode), Some(destNode), None, Some(snrTlv))
}, 0)
val eval_ID1444328280 = (be: BindingElement_ID1444220191) => (be.srcNode,be.destNode,be.routes,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(routes),Some(snrTlv)) =>
		scala.Tuple4(srcNode, destNode, routes, snrTlv)
}
val ID1444328280 = Arc("ID1444328280", ID1443683830, ID1444220191, In) (eval_ID1444328280)
net.addArc(ID1444328280)

// SeqNo ----> Update Current Node

val pattern_ID1444273984_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), SeqNos((sn: Int), (hsn: Int)))) =>
		BindingElement_ID1444220191(None, None, Some(hsn), None, Some(destNode), Some(sn), None)
}, 0)
val eval_ID1444273984 = (be: BindingElement_ID1444220191) => (be.destNode,be.sn,be.hsn) match {
	case (Some(destNode),Some(sn),Some(hsn)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, SeqNos(sn, hsn)))
}
val ID1444273984 = Arc("ID1444273984", ID1434194711, ID1444220191, In) (eval_ID1444273984)
net.addArc(ID1444273984)

// Route Table ----> Update Current Node

val pattern_ID1444408561_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1444220191(Some(routes), None, None, None, Some(destNode), None, None)
}, 0)
val eval_ID1444408561 = (be: BindingElement_ID1444220191) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1444408561 = Arc("ID1444408561", ID1431111582, ID1444220191, In) (eval_ID1444408561)
net.addArc(ID1444408561)

// Update Current Node ----> route advertisement


val eval_ID1444236696 = (be: BindingElement_ID1444220191) => (be.updatedRoutes,be.destNode,be.srcNode,be.snrTlv) match {
	case (Some(updatedRoutes),Some(destNode),Some(srcNode),Some(snrTlv)) =>
		updatedRoutes.advertiseRoutes(destNode, srcNode, snrTlv.routerId)
}
val ID1444236696 = Arc("ID1444236696", ID1443732308, ID1444220191, Out) (eval_ID1444236696)
net.addArc(ID1444236696)

// Update Current Node ----> SeqNo


val eval_ID1444290641 = (be: BindingElement_ID1444220191) => (be.destNode,be.sn,be.hsn) match {
	case (Some(destNode),Some(sn),Some(hsn)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, SeqNos(sn.$plus(1), hsn)))
}
val ID1444290641 = Arc("ID1444290641", ID1434194711, ID1444220191, Out) (eval_ID1444290641)
net.addArc(ID1444290641)

// Update Current Node ----> Route Table


val eval_ID1444383097 = (be: BindingElement_ID1444220191) => (be.destNode,be.updatedRoutes) match {
	case (Some(destNode),Some(updatedRoutes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, updatedRoutes))
}
val ID1444383097 = Arc("ID1444383097", ID1431111582, ID1444220191, Out) (eval_ID1444383097)
net.addArc(ID1444383097)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Update Current Node

ID1444220191.orderedPatternBindingBasis = List(
	ArcPattern(ID1444328280, pattern_ID1444328280_0),
	guard_ID1444220191_1,
	ArcPattern(ID1444273984, pattern_ID1444273984_0))



// ========================================================================
// Generated code for transition Forward Request and its arcs
// ========================================================================

case class BindingElement_ID1443830936(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	preqs: Option[PendingReqTable],
	snrTlv: Option[SeqNoReqTLV]) extends BindingElement

val compatible_ID1443830936 = (b1: BindingElement_ID1443830936, b2: BindingElement_ID1443830936) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None) &&
	(b1.snrTlv == b2.snrTlv || b1.snrTlv == None || b2.snrTlv == None)

val merge_ID1443830936 = (b1: BindingElement_ID1443830936, b2: BindingElement_ID1443830936) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	val snrTlv = if (b1.snrTlv == None) b2.snrTlv else b1.snrTlv
	new BindingElement_ID1443830936(routes, srcNode, destNode, preqs, snrTlv)
}

val fullBindingElement_ID1443830936 = (be: BindingElement_ID1443830936) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.preqs != None &&
	be.snrTlv != None

val guard_ID1443830936_0 = EvalGuard((be: BindingElement_ID1443830936) => (be.routes,be.destNode,be.snrTlv) match {
	case (Some(routes),Some(destNode),Some(snrTlv)) => {
		routes.doForwardSNR(destNode, snrTlv)
	}
}, 0)
val guard_ID1443830936 = List(guard_ID1443830936_0)

val enumBindings_ID1443830936 = List(
	)

val ID1443830936 = Transition("ID1443830936","Forward Request",1,compatible_ID1443830936,merge_ID1443830936,fullBindingElement_ID1443830936,guard_ID1443830936,enumBindings_ID1443830936)
net.addTransition(ID1443830936)

// ------------------ Arcs ------------------

// validated seqnoreqs ----> Forward Request

val pattern_ID1443846273_0 = Pattern((token: Any) => token match {
	case Tuple2(_, scala.Tuple4((srcNode: Int), (destNode: Int), (routes: RouteTable), (snrTlv: SeqNoReqTLV))) =>
		BindingElement_ID1443830936(Some(routes), Some(srcNode), Some(destNode), None, Some(snrTlv))
}, 0)
val eval_ID1443846273 = (be: BindingElement_ID1443830936) => (be.srcNode,be.destNode,be.routes,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(routes),Some(snrTlv)) =>
		scala.Tuple4(srcNode, destNode, routes, snrTlv)
}
val ID1443846273 = Arc("ID1443846273", ID1443683830, ID1443830936, In) (eval_ID1443846273)
net.addArc(ID1443846273)

// Pending Req Table ----> Forward Request

val pattern_ID1454012660_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1443830936(None, None, Some(destNode), Some(preqs), None)
}, 0)
val eval_ID1454012660 = (be: BindingElement_ID1443830936) => (be.destNode,be.preqs) match {
	case (Some(destNode),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs))
}
val ID1454012660 = Arc("ID1454012660", ID1435604444, ID1443830936, In) (eval_ID1454012660)
net.addArc(ID1454012660)

// Forward Request ----> babel to link


val eval_ID1443931382 = (be: BindingElement_ID1443830936) => (be.routes,be.destNode,be.snrTlv) match {
	case (Some(routes),Some(destNode),Some(snrTlv)) =>
		routes.forwardSNR(destNode, snrTlv)
}
val ID1443931382 = Arc("ID1443931382", ID1443717323, ID1443830936, Out) (eval_ID1443931382)
net.addArc(ID1443931382)

// Forward Request ----> Pending Req Table


val eval_ID1454008782 = (be: BindingElement_ID1443830936) => (be.destNode,be.preqs,be.srcNode,be.snrTlv) match {
	case (Some(destNode),Some(preqs),Some(srcNode),Some(snrTlv)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs.add(srcNode, snrTlv)))
}
val ID1454008782 = Arc("ID1454008782", ID1435604444, ID1443830936, Out) (eval_ID1454008782)
net.addArc(ID1454008782)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Forward Request

ID1443830936.orderedPatternBindingBasis = List(
	ArcPattern(ID1443846273, pattern_ID1443846273_0),
	ArcPattern(ID1454012660, pattern_ID1454012660_0))



// ========================================================================
// Generated code for transition Send Update For Route and its arcs
// ========================================================================

case class BindingElement_ID1444126223(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	snrTlv: Option[SeqNoReqTLV]) extends BindingElement

val compatible_ID1444126223 = (b1: BindingElement_ID1444126223, b2: BindingElement_ID1444126223) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.snrTlv == b2.snrTlv || b1.snrTlv == None || b2.snrTlv == None)

val merge_ID1444126223 = (b1: BindingElement_ID1444126223, b2: BindingElement_ID1444126223) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val snrTlv = if (b1.snrTlv == None) b2.snrTlv else b1.snrTlv
	new BindingElement_ID1444126223(routes, srcNode, destNode, snrTlv)
}

val fullBindingElement_ID1444126223 = (be: BindingElement_ID1444126223) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.snrTlv != None

val guard_ID1444126223_0 = EvalGuard((be: BindingElement_ID1444126223) => (be.routes,be.snrTlv) match {
	case (Some(routes),Some(snrTlv)) => {
		routes.routeUpToDate(snrTlv)
	}
}, 0)
val guard_ID1444126223 = List(guard_ID1444126223_0)

val enumBindings_ID1444126223 = List(
	)

val ID1444126223 = Transition("ID1444126223","Send Update For Route",1,compatible_ID1444126223,merge_ID1444126223,fullBindingElement_ID1444126223,guard_ID1444126223,enumBindings_ID1444126223)
net.addTransition(ID1444126223)

// ------------------ Arcs ------------------

// validated seqnoreqs ----> Send Update For Route

val pattern_ID1444130290_0 = Pattern((token: Any) => token match {
	case Tuple2(_, scala.Tuple4((srcNode: Int), (destNode: Int), (routes: RouteTable), (snrTlv: SeqNoReqTLV))) =>
		BindingElement_ID1444126223(Some(routes), Some(srcNode), Some(destNode), Some(snrTlv))
}, 0)
val eval_ID1444130290 = (be: BindingElement_ID1444126223) => (be.srcNode,be.destNode,be.routes,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(routes),Some(snrTlv)) =>
		scala.Tuple4(srcNode, destNode, routes, snrTlv)
}
val ID1444130290 = Arc("ID1444130290", ID1443683830, ID1444126223, In) (eval_ID1444130290)
net.addArc(ID1444130290)

// Send Update For Route ----> route advertisement


val eval_ID1444162798 = (be: BindingElement_ID1444126223) => (be.routes,be.destNode,be.srcNode,be.snrTlv) match {
	case (Some(routes),Some(destNode),Some(srcNode),Some(snrTlv)) =>
		routes.advertiseRoutes(destNode, srcNode, snrTlv.routerId)
}
val ID1444162798 = Arc("ID1444162798", ID1443732308, ID1444126223, Out) (eval_ID1444162798)
net.addArc(ID1444162798)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send Update For Route

ID1444126223.orderedPatternBindingBasis = List(
	ArcPattern(ID1444130290, pattern_ID1444130290_0))



// Wire modules



}



// ########################################################################
// Generated code for module ProcessHello
// ########################################################################
class ProcessHello[T0, T1, T2](ID1430179365: Place[T2, Multiset[RecTLV]], ID1450621345: Place[T1, Multiset[scala.Tuple2[Int, Int]]], ID1452739800: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[scala.Tuple2[Int, Int]] => Traversable[T1], ev2: Multiset[RecTLV] => Traversable[T2]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Update Neighbour's History and its arcs
// ========================================================================

case class BindingElement_ID1450526452(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	linkStatus: Option[LinkStatus.Value],
	neighbours: Option[NeighbourTable],
	helloTlv: Option[HelloTLV]) extends BindingElement

val compatible_ID1450526452 = (b1: BindingElement_ID1450526452, b2: BindingElement_ID1450526452) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.linkStatus == b2.linkStatus || b1.linkStatus == None || b2.linkStatus == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None) &&
	(b1.helloTlv == b2.helloTlv || b1.helloTlv == None || b2.helloTlv == None)

val merge_ID1450526452 = (b1: BindingElement_ID1450526452, b2: BindingElement_ID1450526452) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val linkStatus = if (b1.linkStatus == None) b2.linkStatus else b1.linkStatus
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	val helloTlv = if (b1.helloTlv == None) b2.helloTlv else b1.helloTlv
	new BindingElement_ID1450526452(routes, srcNode, destNode, linkStatus, neighbours, helloTlv)
}

val fullBindingElement_ID1450526452 = (be: BindingElement_ID1450526452) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.linkStatus != None &&
	be.neighbours != None &&
	be.helloTlv != None

val guard_ID1450526452_0 = EvalGuard((be: BindingElement_ID1450526452) => (be.neighbours,be.srcNode) match {
	case (Some(neighbours),Some(srcNode)) => {
		neighbours.hasNeighbour(srcNode)
	}
}, 0)
val guard_ID1450526452 = List(guard_ID1450526452_0)

import LinkStatus._
val enumBindings_ID1450526452 = List(
	List(BindingElement_ID1450526452(None, None, None, Some(Up), None, None), BindingElement_ID1450526452(None, None, None, Some(Down), None, None)))

val ID1450526452 = Transition("ID1450526452","Update Neighbour's History",1,compatible_ID1450526452,merge_ID1450526452,fullBindingElement_ID1450526452,guard_ID1450526452,enumBindings_ID1450526452)
net.addTransition(ID1450526452)

// ------------------ Arcs ------------------

// received tlv ----> Update Neighbour's History

val pattern_ID1450526959_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (helloTlv: HelloTLV))) =>
		BindingElement_ID1450526452(None, Some(srcNode), Some(destNode), None, None, Some(helloTlv))
}, 0)
val eval_ID1450526959 = (be: BindingElement_ID1450526452) => (be.srcNode,be.destNode,be.helloTlv) match {
	case (Some(srcNode),Some(destNode),Some(helloTlv)) =>
		RecTLV(srcNode, destNode, helloTlv)
}
val ID1450526959 = Arc("ID1450526959", ID1430179365, ID1450526452, In) (eval_ID1450526959)
net.addArc(ID1450526959)

// Neighbour Table ----> Update Neighbour's History

val pattern_ID1450712746_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1450526452(None, None, Some(destNode), None, Some(neighbours), None)
}, 0)
val eval_ID1450712746 = (be: BindingElement_ID1450526452) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1450712746 = Arc("ID1450712746", ID1434189908, ID1450526452, In) (eval_ID1450712746)
net.addArc(ID1450712746)

// Route Table ----> Update Neighbour's History

val pattern_ID1468539827IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1450526452(Some(routes), None, Some(destNode), None, None, None)
}, 0)
val eval_ID1468539827IN = (be: BindingElement_ID1450526452) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1468539827IN = Arc("ID1468539827IN", ID1431111582, ID1450526452, In) (eval_ID1468539827IN)
net.addArc(ID1468539827IN)

// Update Neighbour's History ----> link change


val eval_ID1450699120 = (be: BindingElement_ID1450526452) => (be.routes,be.srcNode,be.destNode) match {
	case (Some(routes),Some(srcNode),Some(destNode)) =>
		routes.neighbouringRoutes(srcNode).map(((r) => scala.Tuple2(1, scala.Tuple2(destNode, r.source))))
}
val ID1450699120 = Arc("ID1450699120", ID1450621345, ID1450526452, Out) (eval_ID1450699120)
net.addArc(ID1450699120)

// Update Neighbour's History ----> Neighbour Table


val eval_ID1450747622 = (be: BindingElement_ID1450526452) => (be.destNode,be.linkStatus,be.srcNode,be.helloTlv,be.neighbours) match {
	case (Some(destNode),Some(linkStatus),Some(srcNode),Some(helloTlv),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours.updateHistory(linkStatus, srcNode, helloTlv)))
}
val ID1450747622 = Arc("ID1450747622", ID1434189908, ID1450526452, Out) (eval_ID1450747622)
net.addArc(ID1450747622)

// Update Neighbour's History ----> Route Table


val eval_ID1468539827OUT = (be: BindingElement_ID1450526452) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1468539827OUT = Arc("ID1468539827OUT", ID1431111582, ID1450526452, Out) (eval_ID1468539827OUT)
net.addArc(ID1468539827OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Update Neighbour's History

ID1450526452.orderedPatternBindingBasis = List(
	ArcPattern(ID1450526959, pattern_ID1450526959_0),
	ArcPattern(ID1468539827IN, pattern_ID1468539827IN_0),
	ArcPattern(ID1450712746, pattern_ID1450712746_0))



// ========================================================================
// Generated code for transition Acquire Neighbour and its arcs
// ========================================================================

case class BindingElement_ID1451854683(
	srcNode: Option[Int],
	destNode: Option[Int],
	neighbours: Option[NeighbourTable],
	helloTlv: Option[HelloTLV]) extends BindingElement

val compatible_ID1451854683 = (b1: BindingElement_ID1451854683, b2: BindingElement_ID1451854683) =>
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None) &&
	(b1.helloTlv == b2.helloTlv || b1.helloTlv == None || b2.helloTlv == None)

val merge_ID1451854683 = (b1: BindingElement_ID1451854683, b2: BindingElement_ID1451854683) => {
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	val helloTlv = if (b1.helloTlv == None) b2.helloTlv else b1.helloTlv
	new BindingElement_ID1451854683(srcNode, destNode, neighbours, helloTlv)
}

val fullBindingElement_ID1451854683 = (be: BindingElement_ID1451854683) =>
	be.srcNode != None &&
	be.destNode != None &&
	be.neighbours != None &&
	be.helloTlv != None

val guard_ID1451854683_0 = EvalGuard((be: BindingElement_ID1451854683) => (be.neighbours,be.srcNode) match {
	case (Some(neighbours),Some(srcNode)) => {
		neighbours.hasNeighbour(srcNode).unary_$bang
	}
}, 0)
val guard_ID1451854683 = List(guard_ID1451854683_0)

val enumBindings_ID1451854683 = List(
	)

val ID1451854683 = Transition("ID1451854683","Acquire Neighbour",1,compatible_ID1451854683,merge_ID1451854683,fullBindingElement_ID1451854683,guard_ID1451854683,enumBindings_ID1451854683)
net.addTransition(ID1451854683)

// ------------------ Arcs ------------------

// received tlv ----> Acquire Neighbour

val pattern_ID1451863465_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (helloTlv: HelloTLV))) =>
		BindingElement_ID1451854683(Some(srcNode), Some(destNode), None, Some(helloTlv))
}, 0)
val eval_ID1451863465 = (be: BindingElement_ID1451854683) => (be.srcNode,be.destNode,be.helloTlv) match {
	case (Some(srcNode),Some(destNode),Some(helloTlv)) =>
		RecTLV(srcNode, destNode, helloTlv)
}
val ID1451863465 = Arc("ID1451863465", ID1430179365, ID1451854683, In) (eval_ID1451863465)
net.addArc(ID1451863465)

// Neighbour Table ----> Acquire Neighbour

val pattern_ID1451873682_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1451854683(None, Some(destNode), Some(neighbours), None)
}, 0)
val eval_ID1451873682 = (be: BindingElement_ID1451854683) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1451873682 = Arc("ID1451873682", ID1434189908, ID1451854683, In) (eval_ID1451873682)
net.addArc(ID1451873682)

// Acquire Neighbour ----> babel to link


val eval_ID1452740960 = (be: BindingElement_ID1451854683) => (be.destNode,be.srcNode) match {
	case (Some(destNode),Some(srcNode)) =>
		Packet(Unicast(destNode), Unicast(srcNode), RouteReqTLV(0))
}
val ID1452740960 = Arc("ID1452740960", ID1452739800, ID1451854683, Out) (eval_ID1452740960)
net.addArc(ID1452740960)

// Acquire Neighbour ----> Neighbour Table


val eval_ID1452262371 = (be: BindingElement_ID1451854683) => (be.destNode,be.neighbours,be.srcNode) match {
	case (Some(destNode),Some(neighbours),Some(srcNode)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours.acquireNeighbour(srcNode)))
}
val ID1452262371 = Arc("ID1452262371", ID1434189908, ID1451854683, Out) (eval_ID1452262371)
net.addArc(ID1452262371)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Acquire Neighbour

ID1451854683.orderedPatternBindingBasis = List(
	ArcPattern(ID1451863465, pattern_ID1451863465_0),
	ArcPattern(ID1451873682, pattern_ID1451873682_0))



// Wire modules



}



// ########################################################################
// Generated code for module ProcessPacket
// ########################################################################
class ProcessPacket[T0, T1, T2, T3](ID1441357095: Place[T1, Multiset[RouteAdv]], ID1441357647: Place[T0, Multiset[Packet]], ID1450654252: Place[T2, Multiset[scala.Tuple2[Int, Int]]], ID1452473182: Place[T0, Multiset[Packet]], ID1455303415: Place[T3, Multiset[scala.Tuple3[Int, Int, RouteTable]]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1], ev2: Multiset[scala.Tuple2[Int, Int]] => Traversable[T2], ev3: Multiset[scala.Tuple3[Int, Int, RouteTable]] => Traversable[T3]){

val net = CPNGraph()

// Places
val ID1441307857 = Place("ID1441307857", "Received TLV", Multiset[RecTLV]())
net.addPlace(ID1441307857)

// ========================================================================
// Generated code for transition Ignore Packet and its arcs
// ========================================================================

case class BindingElement_ID1452393860(
	srcNode: Option[Int],
	destNode: Option[Int],
	tlv: Option[TLV],
	neighbours: Option[NeighbourTable]) extends BindingElement

val compatible_ID1452393860 = (b1: BindingElement_ID1452393860, b2: BindingElement_ID1452393860) =>
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.tlv == b2.tlv || b1.tlv == None || b2.tlv == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None)

val merge_ID1452393860 = (b1: BindingElement_ID1452393860, b2: BindingElement_ID1452393860) => {
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val tlv = if (b1.tlv == None) b2.tlv else b1.tlv
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	new BindingElement_ID1452393860(srcNode, destNode, tlv, neighbours)
}

val fullBindingElement_ID1452393860 = (be: BindingElement_ID1452393860) =>
	be.srcNode != None &&
	be.destNode != None &&
	be.tlv != None &&
	be.neighbours != None

val guard_ID1452393860_0 = EvalGuard((be: BindingElement_ID1452393860) => (be.neighbours,be.srcNode,be.tlv) match {
	case (Some(neighbours),Some(srcNode),Some(tlv)) => {
		neighbours.ignorePacket(srcNode, tlv)
	}
}, 0)
val guard_ID1452393860 = List(guard_ID1452393860_0)

val enumBindings_ID1452393860 = List(
	)

val ID1452393860 = Transition("ID1452393860","Ignore Packet",1,compatible_ID1452393860,merge_ID1452393860,fullBindingElement_ID1452393860,guard_ID1452393860,enumBindings_ID1452393860)
net.addTransition(ID1452393860)

// ------------------ Arcs ------------------

// link to babel ----> Ignore Packet

val pattern_ID1452476401_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Packet(Unicast((srcNode: Int)), Unicast((destNode: Int)), (tlv: TLV))) =>
		BindingElement_ID1452393860(Some(srcNode), Some(destNode), Some(tlv), None)
}, 0)
val eval_ID1452476401 = (be: BindingElement_ID1452393860) => (be.srcNode,be.destNode,be.tlv) match {
	case (Some(srcNode),Some(destNode),Some(tlv)) =>
		Packet(Unicast(srcNode), Unicast(destNode), tlv)
}
val ID1452476401 = Arc("ID1452476401", ID1452473182, ID1452393860, In) (eval_ID1452476401)
net.addArc(ID1452476401)

// Neighbour Table ----> Ignore Packet

val pattern_ID1452465672IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1452393860(None, Some(destNode), None, Some(neighbours))
}, 0)
val eval_ID1452465672IN = (be: BindingElement_ID1452393860) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1452465672IN = Arc("ID1452465672IN", ID1434189908, ID1452393860, In) (eval_ID1452465672IN)
net.addArc(ID1452465672IN)

// Ignore Packet ----> Neighbour Table


val eval_ID1452465672OUT = (be: BindingElement_ID1452393860) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1452465672OUT = Arc("ID1452465672OUT", ID1434189908, ID1452393860, Out) (eval_ID1452465672OUT)
net.addArc(ID1452465672OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Ignore Packet

ID1452393860.orderedPatternBindingBasis = List(
	ArcPattern(ID1452476401, pattern_ID1452476401_0),
	ArcPattern(ID1452465672IN, pattern_ID1452465672IN_0))



// ========================================================================
// Generated code for transition Process Packet and its arcs
// ========================================================================

case class BindingElement_ID1452477588(
	srcNode: Option[Int],
	destNode: Option[Int],
	tlv: Option[TLV],
	neighbours: Option[NeighbourTable]) extends BindingElement

val compatible_ID1452477588 = (b1: BindingElement_ID1452477588, b2: BindingElement_ID1452477588) =>
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.tlv == b2.tlv || b1.tlv == None || b2.tlv == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None)

val merge_ID1452477588 = (b1: BindingElement_ID1452477588, b2: BindingElement_ID1452477588) => {
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val tlv = if (b1.tlv == None) b2.tlv else b1.tlv
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	new BindingElement_ID1452477588(srcNode, destNode, tlv, neighbours)
}

val fullBindingElement_ID1452477588 = (be: BindingElement_ID1452477588) =>
	be.srcNode != None &&
	be.destNode != None &&
	be.tlv != None &&
	be.neighbours != None

val guard_ID1452477588_0 = EvalGuard((be: BindingElement_ID1452477588) => (be.neighbours,be.srcNode,be.tlv) match {
	case (Some(neighbours),Some(srcNode),Some(tlv)) => {
		neighbours.ignorePacket(srcNode, tlv).unary_$bang
	}
}, 0)
val guard_ID1452477588 = List(guard_ID1452477588_0)

val enumBindings_ID1452477588 = List(
	)

val ID1452477588 = Transition("ID1452477588","Process Packet",1,compatible_ID1452477588,merge_ID1452477588,fullBindingElement_ID1452477588,guard_ID1452477588,enumBindings_ID1452477588)
net.addTransition(ID1452477588)

// ------------------ Arcs ------------------

// link to babel ----> Process Packet

val pattern_ID1452480618_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Packet(Unicast((srcNode: Int)), Unicast((destNode: Int)), (tlv: TLV))) =>
		BindingElement_ID1452477588(Some(srcNode), Some(destNode), Some(tlv), None)
}, 0)
val eval_ID1452480618 = (be: BindingElement_ID1452477588) => (be.srcNode,be.destNode,be.tlv) match {
	case (Some(srcNode),Some(destNode),Some(tlv)) =>
		Packet(Unicast(srcNode), Unicast(destNode), tlv)
}
val ID1452480618 = Arc("ID1452480618", ID1452473182, ID1452477588, In) (eval_ID1452480618)
net.addArc(ID1452480618)

// Neighbour Table ----> Process Packet

val pattern_ID1452481214IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1452477588(None, Some(destNode), None, Some(neighbours))
}, 0)
val eval_ID1452481214IN = (be: BindingElement_ID1452477588) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1452481214IN = Arc("ID1452481214IN", ID1434189908, ID1452477588, In) (eval_ID1452481214IN)
net.addArc(ID1452481214IN)

// Process Packet ----> Received TLV


val eval_ID1452479751 = (be: BindingElement_ID1452477588) => (be.srcNode,be.destNode,be.tlv) match {
	case (Some(srcNode),Some(destNode),Some(tlv)) =>
		RecTLV(srcNode, destNode, tlv)
}
val ID1452479751 = Arc("ID1452479751", ID1441307857, ID1452477588, Out) (eval_ID1452479751)
net.addArc(ID1452479751)

// Process Packet ----> Neighbour Table


val eval_ID1452481214OUT = (be: BindingElement_ID1452477588) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1452481214OUT = Arc("ID1452481214OUT", ID1434189908, ID1452477588, Out) (eval_ID1452481214OUT)
net.addArc(ID1452481214OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Process Packet

ID1452477588.orderedPatternBindingBasis = List(
	ArcPattern(ID1452480618, pattern_ID1452480618_0),
	ArcPattern(ID1452481214IN, pattern_ID1452481214IN_0))



// Wire modules
val st0 = new ProcessSeqNoReq(ID1441307857, ID1441357095, ID1441357647) 
net.addSubstitutionTransition(st0.net)

val st1 = new ProcessRouteReq(ID1441307857, ID1441357095) 
net.addSubstitutionTransition(st1.net)

val st2 = new ProcessHello(ID1441307857, ID1450654252, ID1441357647) 
net.addSubstitutionTransition(st2.net)

val st3 = new ProcessIHU(ID1441307857, ID1450654252) 
net.addSubstitutionTransition(st3.net)

val st4 = new ProcessUpdate(ID1441307857, ID1450654252, ID1441357647, ID1455303415) 
net.addSubstitutionTransition(st4.net)


}



// ########################################################################
// Generated code for module System
// ########################################################################
class System(){

val net = CPNGraph()

// Places
val ID1429754090 = Place("ID1429754090", "Link to Babel", Multiset[Packet]())
net.addPlace(ID1429754090)

val ID1429753664 = Place("ID1429753664", "Babel to Link", Multiset[Packet]())
net.addPlace(ID1429753664)



// Wire modules
val st0 = new Link(ID1429753664, ID1429754090) 
net.addSubstitutionTransition(st0.net)

val st1 = new BabelProtocol(ID1429754090, ID1429753664) 
net.addSubstitutionTransition(st1.net)


}

val system0 = new System() 
net.addSubstitutionTransition(system0.net)

// ########################################################################
// Generated code for module ProcessIHU
// ########################################################################
class ProcessIHU[T0, T1](ID1430207241: Place[T0, Multiset[RecTLV]], ID1451179389: Place[T1, Multiset[scala.Tuple2[Int, Int]]])(
implicit ev0: Multiset[RecTLV] => Traversable[T0], ev1: Multiset[scala.Tuple2[Int, Int]] => Traversable[T1]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Update Txcost and its arcs
// ========================================================================

case class BindingElement_ID1451167919(
	ihuTlv: Option[IHUTLV],
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	neighbours: Option[NeighbourTable]) extends BindingElement

val compatible_ID1451167919 = (b1: BindingElement_ID1451167919, b2: BindingElement_ID1451167919) =>
	(b1.ihuTlv == b2.ihuTlv || b1.ihuTlv == None || b2.ihuTlv == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None)

val merge_ID1451167919 = (b1: BindingElement_ID1451167919, b2: BindingElement_ID1451167919) => {
	val ihuTlv = if (b1.ihuTlv == None) b2.ihuTlv else b1.ihuTlv
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	new BindingElement_ID1451167919(ihuTlv, routes, srcNode, destNode, neighbours)
}

val fullBindingElement_ID1451167919 = (be: BindingElement_ID1451167919) =>
	be.ihuTlv != None &&
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.neighbours != None


val guard_ID1451167919 = List()

val enumBindings_ID1451167919 = List(
	)

val ID1451167919 = Transition("ID1451167919","Update Txcost",1,compatible_ID1451167919,merge_ID1451167919,fullBindingElement_ID1451167919,guard_ID1451167919,enumBindings_ID1451167919)
net.addTransition(ID1451167919)

// ------------------ Arcs ------------------

// received tlv ----> Update Txcost

val pattern_ID1451168092_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (ihuTlv: IHUTLV))) =>
		BindingElement_ID1451167919(Some(ihuTlv), None, Some(srcNode), Some(destNode), None)
}, 0)
val eval_ID1451168092 = (be: BindingElement_ID1451167919) => (be.srcNode,be.destNode,be.ihuTlv) match {
	case (Some(srcNode),Some(destNode),Some(ihuTlv)) =>
		RecTLV(srcNode, destNode, ihuTlv)
}
val ID1451168092 = Arc("ID1451168092", ID1430207241, ID1451167919, In) (eval_ID1451168092)
net.addArc(ID1451168092)

// Neighbour Table ----> Update Txcost

val pattern_ID1451169250_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1451167919(None, None, None, Some(destNode), Some(neighbours))
}, 0)
val eval_ID1451169250 = (be: BindingElement_ID1451167919) => (be.destNode,be.neighbours) match {
	case (Some(destNode),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours))
}
val ID1451169250 = Arc("ID1451169250", ID1434189908, ID1451167919, In) (eval_ID1451169250)
net.addArc(ID1451169250)

// Route Table ----> Update Txcost

val pattern_ID1468361650IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1451167919(None, Some(routes), None, Some(destNode), None)
}, 0)
val eval_ID1468361650IN = (be: BindingElement_ID1451167919) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1468361650IN = Arc("ID1468361650IN", ID1431111582, ID1451167919, In) (eval_ID1468361650IN)
net.addArc(ID1468361650IN)

// Update Txcost ----> link change


val eval_ID1451179675 = (be: BindingElement_ID1451167919) => (be.routes,be.srcNode,be.destNode) match {
	case (Some(routes),Some(srcNode),Some(destNode)) =>
		routes.neighbouringRoutes(srcNode).map(((r) => scala.Tuple2(1, scala.Tuple2(destNode, r.source))))
}
val ID1451179675 = Arc("ID1451179675", ID1451179389, ID1451167919, Out) (eval_ID1451179675)
net.addArc(ID1451179675)

// Update Txcost ----> Neighbour Table


val eval_ID1451173698 = (be: BindingElement_ID1451167919) => (be.destNode,be.neighbours,be.srcNode,be.ihuTlv) match {
	case (Some(destNode),Some(neighbours),Some(srcNode),Some(ihuTlv)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, neighbours.updateTxCost(srcNode, ihuTlv)))
}
val ID1451173698 = Arc("ID1451173698", ID1434189908, ID1451167919, Out) (eval_ID1451173698)
net.addArc(ID1451173698)

// Update Txcost ----> Route Table


val eval_ID1468361650OUT = (be: BindingElement_ID1451167919) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1468361650OUT = Arc("ID1468361650OUT", ID1431111582, ID1451167919, Out) (eval_ID1468361650OUT)
net.addArc(ID1468361650OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Update Txcost

ID1451167919.orderedPatternBindingBasis = List(
	ArcPattern(ID1451168092, pattern_ID1451168092_0),
	ArcPattern(ID1468361650IN, pattern_ID1468361650IN_0),
	ArcPattern(ID1451169250, pattern_ID1451169250_0))



// Wire modules



}



// ########################################################################
// Generated code for module PeriodicMessages
// ########################################################################
class PeriodicMessages[T0, T1](ID1450969104: Place[T0, Multiset[Packet]], ID1459457274: Place[T1, Multiset[RouteAdv]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Send SeqNo Request and its arcs
// ========================================================================

case class BindingElement_ID1455975516(
	node: Option[Int],
	sources: Option[SourceTable]) extends BindingElement

val compatible_ID1455975516 = (b1: BindingElement_ID1455975516, b2: BindingElement_ID1455975516) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None)

val merge_ID1455975516 = (b1: BindingElement_ID1455975516, b2: BindingElement_ID1455975516) => {
	val node = if (b1.node == None) b2.node else b1.node
	val sources = if (b1.sources == None) b2.sources else b1.sources
	new BindingElement_ID1455975516(node, sources)
}

val fullBindingElement_ID1455975516 = (be: BindingElement_ID1455975516) =>
	be.node != None &&
	be.sources != None


val guard_ID1455975516 = List()

val enumBindings_ID1455975516 = List(
	)

val ID1455975516 = Transition("ID1455975516","Send SeqNo Request",1,compatible_ID1455975516,merge_ID1455975516,fullBindingElement_ID1455975516,guard_ID1455975516,enumBindings_ID1455975516)
net.addTransition(ID1455975516)

// ------------------ Arcs ------------------

// Source Table ----> Send SeqNo Request

val pattern_ID1456010962IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (sources: SourceTable))) =>
		BindingElement_ID1455975516(Some(node), Some(sources))
}, 0)
val eval_ID1456010962IN = (be: BindingElement_ID1455975516) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1456010962IN = Arc("ID1456010962IN", ID1430786899, ID1455975516, In) (eval_ID1456010962IN)
net.addArc(ID1456010962IN)

// Send SeqNo Request ----> babel to link


val eval_ID1456013258 = (be: BindingElement_ID1455975516) => (be.sources,be.node) match {
	case (Some(sources),Some(node)) =>
		sources.createSeqNoReqs(node, Multicast())
}
val ID1456013258 = Arc("ID1456013258", ID1450969104, ID1455975516, Out) (eval_ID1456013258)
net.addArc(ID1456013258)

// Send SeqNo Request ----> Source Table


val eval_ID1456010962OUT = (be: BindingElement_ID1455975516) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1456010962OUT = Arc("ID1456010962OUT", ID1430786899, ID1455975516, Out) (eval_ID1456010962OUT)
net.addArc(ID1456010962OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send SeqNo Request

ID1455975516.orderedPatternBindingBasis = List(
	ArcPattern(ID1456010962IN, pattern_ID1456010962IN_0))



// ========================================================================
// Generated code for transition Advertise Route and its arcs
// ========================================================================

case class BindingElement_ID1459434354(
	node: Option[Int],
	routes: Option[RouteTable]) extends BindingElement

val compatible_ID1459434354 = (b1: BindingElement_ID1459434354, b2: BindingElement_ID1459434354) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None)

val merge_ID1459434354 = (b1: BindingElement_ID1459434354, b2: BindingElement_ID1459434354) => {
	val node = if (b1.node == None) b2.node else b1.node
	val routes = if (b1.routes == None) b2.routes else b1.routes
	new BindingElement_ID1459434354(node, routes)
}

val fullBindingElement_ID1459434354 = (be: BindingElement_ID1459434354) =>
	be.node != None &&
	be.routes != None


val guard_ID1459434354 = List()

val enumBindings_ID1459434354 = List(
	)

val ID1459434354 = Transition("ID1459434354","Advertise Route",1,compatible_ID1459434354,merge_ID1459434354,fullBindingElement_ID1459434354,guard_ID1459434354,enumBindings_ID1459434354)
net.addTransition(ID1459434354)

// ------------------ Arcs ------------------

// Route Table ----> Advertise Route

val pattern_ID1459451678IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routes: RouteTable))) =>
		BindingElement_ID1459434354(Some(node), Some(routes))
}, 0)
val eval_ID1459451678IN = (be: BindingElement_ID1459434354) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1459451678IN = Arc("ID1459451678IN", ID1431111582, ID1459434354, In) (eval_ID1459451678IN)
net.addArc(ID1459451678IN)

// Advertise Route ----> route advertisement


val eval_ID1459460298 = (be: BindingElement_ID1459434354) => (be.routes,be.node) match {
	case (Some(routes),Some(node)) =>
		routes.advertiseRoutes(node)
}
val ID1459460298 = Arc("ID1459460298", ID1459457274, ID1459434354, Out) (eval_ID1459460298)
net.addArc(ID1459460298)

// Advertise Route ----> Route Table


val eval_ID1459451678OUT = (be: BindingElement_ID1459434354) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1459451678OUT = Arc("ID1459451678OUT", ID1431111582, ID1459434354, Out) (eval_ID1459451678OUT)
net.addArc(ID1459451678OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Advertise Route

ID1459434354.orderedPatternBindingBasis = List(
	ArcPattern(ID1459451678IN, pattern_ID1459451678IN_0))



// ========================================================================
// Generated code for transition Send IHU and its arcs
// ========================================================================

case class BindingElement_ID1451025222(
	node: Option[Int],
	neighbours: Option[NeighbourTable]) extends BindingElement

val compatible_ID1451025222 = (b1: BindingElement_ID1451025222, b2: BindingElement_ID1451025222) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None)

val merge_ID1451025222 = (b1: BindingElement_ID1451025222, b2: BindingElement_ID1451025222) => {
	val node = if (b1.node == None) b2.node else b1.node
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	new BindingElement_ID1451025222(node, neighbours)
}

val fullBindingElement_ID1451025222 = (be: BindingElement_ID1451025222) =>
	be.node != None &&
	be.neighbours != None


val guard_ID1451025222 = List()

val enumBindings_ID1451025222 = List(
	)

val ID1451025222 = Transition("ID1451025222","Send IHU",1,compatible_ID1451025222,merge_ID1451025222,fullBindingElement_ID1451025222,guard_ID1451025222,enumBindings_ID1451025222)
net.addTransition(ID1451025222)

// ------------------ Arcs ------------------

// Neighbour Table ----> Send IHU

val pattern_ID1451041341IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1451025222(Some(node), Some(neighbours))
}, 0)
val eval_ID1451041341IN = (be: BindingElement_ID1451025222) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1451041341IN = Arc("ID1451041341IN", ID1434189908, ID1451025222, In) (eval_ID1451041341IN)
net.addArc(ID1451041341IN)

// Send IHU ----> babel to link


val eval_ID1451028405 = (be: BindingElement_ID1451025222) => (be.neighbours,be.node) match {
	case (Some(neighbours),Some(node)) =>
		neighbours.createIHU(node)
}
val ID1451028405 = Arc("ID1451028405", ID1450969104, ID1451025222, Out) (eval_ID1451028405)
net.addArc(ID1451028405)

// Send IHU ----> Neighbour Table


val eval_ID1451041341OUT = (be: BindingElement_ID1451025222) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1451041341OUT = Arc("ID1451041341OUT", ID1434189908, ID1451025222, Out) (eval_ID1451041341OUT)
net.addArc(ID1451041341OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send IHU

ID1451025222.orderedPatternBindingBasis = List(
	ArcPattern(ID1451041341IN, pattern_ID1451041341IN_0))



// ========================================================================
// Generated code for transition Send Route Request and its arcs
// ========================================================================

case class BindingElement_ID1455975805(
	node: Option[Int],
	routes: Option[RouteTable]) extends BindingElement

val compatible_ID1455975805 = (b1: BindingElement_ID1455975805, b2: BindingElement_ID1455975805) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None)

val merge_ID1455975805 = (b1: BindingElement_ID1455975805, b2: BindingElement_ID1455975805) => {
	val node = if (b1.node == None) b2.node else b1.node
	val routes = if (b1.routes == None) b2.routes else b1.routes
	new BindingElement_ID1455975805(node, routes)
}

val fullBindingElement_ID1455975805 = (be: BindingElement_ID1455975805) =>
	be.node != None &&
	be.routes != None

val guard_ID1455975805_0 = EvalGuard((be: BindingElement_ID1455975805) => (be.routes) match {
	case (Some(routes)) => {
		routes.length.$less(1)
	}
}, 0)
val guard_ID1455975805 = List(guard_ID1455975805_0)

val enumBindings_ID1455975805 = List(
	)

val ID1455975805 = Transition("ID1455975805","Send Route Request",1,compatible_ID1455975805,merge_ID1455975805,fullBindingElement_ID1455975805,guard_ID1455975805,enumBindings_ID1455975805)
net.addTransition(ID1455975805)

// ------------------ Arcs ------------------

// Route Table ----> Send Route Request

val pattern_ID1467020577IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routes: RouteTable))) =>
		BindingElement_ID1455975805(Some(node), Some(routes))
}, 0)
val eval_ID1467020577IN = (be: BindingElement_ID1455975805) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1467020577IN = Arc("ID1467020577IN", ID1431111582, ID1455975805, In) (eval_ID1467020577IN)
net.addArc(ID1467020577IN)

// Send Route Request ----> babel to link


val eval_ID1456058275 = (be: BindingElement_ID1455975805) => (be.routes,be.node) match {
	case (Some(routes),Some(node)) =>
		routes.createRouteReq(node)
}
val ID1456058275 = Arc("ID1456058275", ID1450969104, ID1455975805, Out) (eval_ID1456058275)
net.addArc(ID1456058275)

// Send Route Request ----> Route Table


val eval_ID1467020577OUT = (be: BindingElement_ID1455975805) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1467020577OUT = Arc("ID1467020577OUT", ID1431111582, ID1455975805, Out) (eval_ID1467020577OUT)
net.addArc(ID1467020577OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send Route Request

ID1455975805.orderedPatternBindingBasis = List(
	ArcPattern(ID1467020577IN, pattern_ID1467020577IN_0))



// ========================================================================
// Generated code for transition Send Hello and its arcs
// ========================================================================

case class BindingElement_ID1450989389(
	node: Option[Int],
	seqNums: Option[SeqNos]) extends BindingElement

val compatible_ID1450989389 = (b1: BindingElement_ID1450989389, b2: BindingElement_ID1450989389) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.seqNums == b2.seqNums || b1.seqNums == None || b2.seqNums == None)

val merge_ID1450989389 = (b1: BindingElement_ID1450989389, b2: BindingElement_ID1450989389) => {
	val node = if (b1.node == None) b2.node else b1.node
	val seqNums = if (b1.seqNums == None) b2.seqNums else b1.seqNums
	new BindingElement_ID1450989389(node, seqNums)
}

val fullBindingElement_ID1450989389 = (be: BindingElement_ID1450989389) =>
	be.node != None &&
	be.seqNums != None


val guard_ID1450989389 = List()

val enumBindings_ID1450989389 = List(
	)

val ID1450989389 = Transition("ID1450989389","Send Hello",1,compatible_ID1450989389,merge_ID1450989389,fullBindingElement_ID1450989389,guard_ID1450989389,enumBindings_ID1450989389)
net.addTransition(ID1450989389)

// ------------------ Arcs ------------------

// SeqNo ----> Send Hello

val pattern_ID1451001911_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (seqNums: SeqNos))) =>
		BindingElement_ID1450989389(Some(node), Some(seqNums))
}, 0)
val eval_ID1451001911 = (be: BindingElement_ID1450989389) => (be.node,be.seqNums) match {
	case (Some(node),Some(seqNums)) =>
		scala.Tuple2(1, scala.Tuple2(node, seqNums))
}
val ID1451001911 = Arc("ID1451001911", ID1434194711, ID1450989389, In) (eval_ID1451001911)
net.addArc(ID1451001911)

// Send Hello ----> babel to link


val eval_ID1450995129 = (be: BindingElement_ID1450989389) => (be.seqNums,be.node) match {
	case (Some(seqNums),Some(node)) =>
		seqNums.createHello(node, Multicast())
}
val ID1450995129 = Arc("ID1450995129", ID1450969104, ID1450989389, Out) (eval_ID1450995129)
net.addArc(ID1450995129)

// Send Hello ----> SeqNo


val eval_ID1451002903 = (be: BindingElement_ID1450989389) => (be.node,be.seqNums) match {
	case (Some(node),Some(seqNums)) =>
		scala.Tuple2(1, scala.Tuple2(node, seqNums.incHelloSeqNo))
}
val ID1451002903 = Arc("ID1451002903", ID1434194711, ID1450989389, Out) (eval_ID1451002903)
net.addArc(ID1451002903)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send Hello

ID1450989389.orderedPatternBindingBasis = List(
	ArcPattern(ID1451001911, pattern_ID1451001911_0))



// Wire modules



}



// ########################################################################
// Generated code for module ProcessNewRoute
// ########################################################################
class ProcessNewRoute[T0, T1](ID1449110656: Place[T0, Multiset[RecTLV]], ID1449535344: Place[T1, Multiset[scala.Tuple2[Int, Int]]])(
implicit ev0: Multiset[RecTLV] => Traversable[T0], ev1: Multiset[scala.Tuple2[Int, Int]] => Traversable[T1]){

val net = CPNGraph()

// Places
val ID1449135071 = Place("ID1449135071", "Route Acquisition", Multiset[RecTLV]())
net.addPlace(ID1449135071)

// ========================================================================
// Generated code for transition Add New Route and its arcs
// ========================================================================

case class BindingElement_ID1449148410(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV],
	sources: Option[SourceTable]) extends BindingElement

val compatible_ID1449148410 = (b1: BindingElement_ID1449148410, b2: BindingElement_ID1449148410) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None)

val merge_ID1449148410 = (b1: BindingElement_ID1449148410, b2: BindingElement_ID1449148410) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	val sources = if (b1.sources == None) b2.sources else b1.sources
	new BindingElement_ID1449148410(routes, srcNode, destNode, updateTlv, sources)
}

val fullBindingElement_ID1449148410 = (be: BindingElement_ID1449148410) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None &&
	be.sources != None

val guard_ID1449148410_0 = EvalGuard((be: BindingElement_ID1449148410) => (be.sources,be.updateTlv) match {
	case (Some(sources),Some(updateTlv)) => {
		sources.feasibleRoute(updateTlv)
	}
}, 0)
val guard_ID1449148410 = List(guard_ID1449148410_0)

val enumBindings_ID1449148410 = List(
	)

val ID1449148410 = Transition("ID1449148410","Add New Route",1,compatible_ID1449148410,merge_ID1449148410,fullBindingElement_ID1449148410,guard_ID1449148410,enumBindings_ID1449148410)
net.addTransition(ID1449148410)

// ------------------ Arcs ------------------

// Route Acquisition ----> Add New Route

val pattern_ID1449187704_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1449148410(None, Some(srcNode), Some(destNode), Some(updateTlv), None)
}, 0)
val eval_ID1449187704 = (be: BindingElement_ID1449148410) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1449187704 = Arc("ID1449187704", ID1449135071, ID1449148410, In) (eval_ID1449187704)
net.addArc(ID1449187704)

// Source Table ----> Add New Route

val pattern_ID1449201748IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (sources: SourceTable))) =>
		BindingElement_ID1449148410(None, None, Some(destNode), None, Some(sources))
}, 0)
val eval_ID1449201748IN = (be: BindingElement_ID1449148410) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1449201748IN = Arc("ID1449201748IN", ID1430786899, ID1449148410, In) (eval_ID1449201748IN)
net.addArc(ID1449201748IN)

// Route Table ----> Add New Route

val pattern_ID1449259809_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1449148410(Some(routes), None, Some(destNode), None, None)
}, 0)
val eval_ID1449259809 = (be: BindingElement_ID1449148410) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1449259809 = Arc("ID1449259809", ID1431111582, ID1449148410, In) (eval_ID1449259809)
net.addArc(ID1449259809)

// Add New Route ----> new route


val eval_ID1449602709 = (be: BindingElement_ID1449148410) => (be.destNode,be.updateTlv) match {
	case (Some(destNode),Some(updateTlv)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, updateTlv.routerId))
}
val ID1449602709 = Arc("ID1449602709", ID1449535344, ID1449148410, Out) (eval_ID1449602709)
net.addArc(ID1449602709)

// Add New Route ----> Source Table


val eval_ID1449201748OUT = (be: BindingElement_ID1449148410) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1449201748OUT = Arc("ID1449201748OUT", ID1430786899, ID1449148410, Out) (eval_ID1449201748OUT)
net.addArc(ID1449201748OUT)

// Add New Route ----> Route Table


val eval_ID1449269163 = (be: BindingElement_ID1449148410) => (be.destNode,be.routes,be.srcNode,be.updateTlv) match {
	case (Some(destNode),Some(routes),Some(srcNode),Some(updateTlv)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes.addRouteEntry(srcNode, updateTlv)))
}
val ID1449269163 = Arc("ID1449269163", ID1431111582, ID1449148410, Out) (eval_ID1449269163)
net.addArc(ID1449269163)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Add New Route

ID1449148410.orderedPatternBindingBasis = List(
	ArcPattern(ID1449187704, pattern_ID1449187704_0),
	ArcPattern(ID1449259809, pattern_ID1449259809_0),
	ArcPattern(ID1449201748IN, pattern_ID1449201748IN_0))



// ========================================================================
// Generated code for transition Process New Route and its arcs
// ========================================================================

case class BindingElement_ID1449105833(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV]) extends BindingElement

val compatible_ID1449105833 = (b1: BindingElement_ID1449105833, b2: BindingElement_ID1449105833) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None)

val merge_ID1449105833 = (b1: BindingElement_ID1449105833, b2: BindingElement_ID1449105833) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	new BindingElement_ID1449105833(routes, srcNode, destNode, updateTlv)
}

val fullBindingElement_ID1449105833 = (be: BindingElement_ID1449105833) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None

val guard_ID1449105833_0 = EvalGuard((be: BindingElement_ID1449105833) => (be.routes,be.srcNode,be.updateTlv) match {
	case (Some(routes),Some(srcNode),Some(updateTlv)) => {
		routes.hasRouteFromNeighbour(srcNode, updateTlv).unary_$bang
	}
}, 0)
val guard_ID1449105833 = List(guard_ID1449105833_0)

val enumBindings_ID1449105833 = List(
	)

val ID1449105833 = Transition("ID1449105833","Process New Route",1,compatible_ID1449105833,merge_ID1449105833,fullBindingElement_ID1449105833,guard_ID1449105833,enumBindings_ID1449105833)
net.addTransition(ID1449105833)

// ------------------ Arcs ------------------

// recieved update ----> Process New Route

val pattern_ID1449115499_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1449105833(None, Some(srcNode), Some(destNode), Some(updateTlv))
}, 0)
val eval_ID1449115499 = (be: BindingElement_ID1449105833) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1449115499 = Arc("ID1449115499", ID1449110656, ID1449105833, In) (eval_ID1449115499)
net.addArc(ID1449115499)

// Route Table ----> Process New Route

val pattern_ID1449246837IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1449105833(Some(routes), None, Some(destNode), None)
}, 0)
val eval_ID1449246837IN = (be: BindingElement_ID1449105833) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1449246837IN = Arc("ID1449246837IN", ID1431111582, ID1449105833, In) (eval_ID1449246837IN)
net.addArc(ID1449246837IN)

// Process New Route ----> Route Acquisition


val eval_ID1449180742 = (be: BindingElement_ID1449105833) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1449180742 = Arc("ID1449180742", ID1449135071, ID1449105833, Out) (eval_ID1449180742)
net.addArc(ID1449180742)

// Process New Route ----> Route Table


val eval_ID1449246837OUT = (be: BindingElement_ID1449105833) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1449246837OUT = Arc("ID1449246837OUT", ID1431111582, ID1449105833, Out) (eval_ID1449246837OUT)
net.addArc(ID1449246837OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Process New Route

ID1449105833.orderedPatternBindingBasis = List(
	ArcPattern(ID1449115499, pattern_ID1449115499_0),
	ArcPattern(ID1449246837IN, pattern_ID1449246837IN_0))



// ========================================================================
// Generated code for transition Ignore Unfeasible Route and its arcs
// ========================================================================

case class BindingElement_ID1449141716(
	sources: Option[SourceTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	updateTlv: Option[UpdateTLV]) extends BindingElement

val compatible_ID1449141716 = (b1: BindingElement_ID1449141716, b2: BindingElement_ID1449141716) =>
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None)

val merge_ID1449141716 = (b1: BindingElement_ID1449141716, b2: BindingElement_ID1449141716) => {
	val sources = if (b1.sources == None) b2.sources else b1.sources
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	new BindingElement_ID1449141716(sources, srcNode, destNode, updateTlv)
}

val fullBindingElement_ID1449141716 = (be: BindingElement_ID1449141716) =>
	be.sources != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.updateTlv != None

val guard_ID1449141716_0 = EvalGuard((be: BindingElement_ID1449141716) => (be.sources,be.updateTlv) match {
	case (Some(sources),Some(updateTlv)) => {
		sources.feasibleRoute(updateTlv).unary_$bang
	}
}, 0)
val guard_ID1449141716 = List(guard_ID1449141716_0)

val enumBindings_ID1449141716 = List(
	)

val ID1449141716 = Transition("ID1449141716","Ignore Unfeasible Route",1,compatible_ID1449141716,merge_ID1449141716,fullBindingElement_ID1449141716,guard_ID1449141716,enumBindings_ID1449141716)
net.addTransition(ID1449141716)

// ------------------ Arcs ------------------

// Route Acquisition ----> Ignore Unfeasible Route

val pattern_ID1449184217_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (updateTlv: UpdateTLV))) =>
		BindingElement_ID1449141716(None, Some(srcNode), Some(destNode), Some(updateTlv))
}, 0)
val eval_ID1449184217 = (be: BindingElement_ID1449141716) => (be.srcNode,be.destNode,be.updateTlv) match {
	case (Some(srcNode),Some(destNode),Some(updateTlv)) =>
		RecTLV(srcNode, destNode, updateTlv)
}
val ID1449184217 = Arc("ID1449184217", ID1449135071, ID1449141716, In) (eval_ID1449184217)
net.addArc(ID1449184217)

// Source Table ----> Ignore Unfeasible Route

val pattern_ID1449196454IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (sources: SourceTable))) =>
		BindingElement_ID1449141716(Some(sources), None, Some(destNode), None)
}, 0)
val eval_ID1449196454IN = (be: BindingElement_ID1449141716) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1449196454IN = Arc("ID1449196454IN", ID1430786899, ID1449141716, In) (eval_ID1449196454IN)
net.addArc(ID1449196454IN)

// Ignore Unfeasible Route ----> Source Table


val eval_ID1449196454OUT = (be: BindingElement_ID1449141716) => (be.destNode,be.sources) match {
	case (Some(destNode),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, sources))
}
val ID1449196454OUT = Arc("ID1449196454OUT", ID1430786899, ID1449141716, Out) (eval_ID1449196454OUT)
net.addArc(ID1449196454OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Ignore Unfeasible Route

ID1449141716.orderedPatternBindingBasis = List(
	ArcPattern(ID1449184217, pattern_ID1449184217_0),
	ArcPattern(ID1449196454IN, pattern_ID1449196454IN_0))



// Wire modules



}



// ########################################################################
// Generated code for module GenerateUpdate
// ########################################################################
class GenerateUpdate[T0, T1](ID1436571590: Place[T1, Multiset[RouteAdv]], ID1436585186: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Generate Update and its arcs
// ========================================================================

case class BindingElement_ID1436572879(
	updateTlv: Option[UpdateTLV],
	dest: Option[Address],
	node: Option[Int],
	sources: Option[SourceTable],
	neighbours: Option[NeighbourTable],
	route: Option[RouteTableEntry]) extends BindingElement

val compatible_ID1436572879 = (b1: BindingElement_ID1436572879, b2: BindingElement_ID1436572879) =>
	(b1.updateTlv == b2.updateTlv || b1.updateTlv == None || b2.updateTlv == None) &&
	(b1.dest == b2.dest || b1.dest == None || b2.dest == None) &&
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None) &&
	(b1.neighbours == b2.neighbours || b1.neighbours == None || b2.neighbours == None) &&
	(b1.route == b2.route || b1.route == None || b2.route == None)

val merge_ID1436572879 = (b1: BindingElement_ID1436572879, b2: BindingElement_ID1436572879) => {
	val updateTlv = if (b1.updateTlv == None) b2.updateTlv else b1.updateTlv
	val dest = if (b1.dest == None) b2.dest else b1.dest
	val node = if (b1.node == None) b2.node else b1.node
	val sources = if (b1.sources == None) b2.sources else b1.sources
	val neighbours = if (b1.neighbours == None) b2.neighbours else b1.neighbours
	val route = if (b1.route == None) b2.route else b1.route
	new BindingElement_ID1436572879(updateTlv, dest, node, sources, neighbours, route)
}

val fullBindingElement_ID1436572879 = (be: BindingElement_ID1436572879) =>
	be.updateTlv != None &&
	be.dest != None &&
	be.node != None &&
	be.sources != None &&
	be.neighbours != None &&
	be.route != None

val guard_ID1436572879_0 = BindGuard((be: BindingElement_ID1436572879) => (be.node,be.route,be.neighbours) match {
	case (Some(node),Some(route),Some(neighbours)) =>
		val updateTlv = generateUpdate(node, route, neighbours)
		BindingElement_ID1436572879(Some(updateTlv), be.dest, be.node, be.sources, be.neighbours, be.route)
}, 0)
val guard_ID1436572879 = List(guard_ID1436572879_0)

val enumBindings_ID1436572879 = List(
	)

val ID1436572879 = Transition("ID1436572879","Generate Update",1,compatible_ID1436572879,merge_ID1436572879,fullBindingElement_ID1436572879,guard_ID1436572879,enumBindings_ID1436572879)
net.addTransition(ID1436572879)

// ------------------ Arcs ------------------

// route advertisement ----> Generate Update

val pattern_ID1436574204_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RouteAdv((node: Int), (dest: Address), (route: RouteTableEntry))) =>
		BindingElement_ID1436572879(None, Some(dest), Some(node), None, None, Some(route))
}, 0)
val eval_ID1436574204 = (be: BindingElement_ID1436572879) => (be.node,be.dest,be.route) match {
	case (Some(node),Some(dest),Some(route)) =>
		RouteAdv(node, dest, route)
}
val ID1436574204 = Arc("ID1436574204", ID1436571590, ID1436572879, In) (eval_ID1436574204)
net.addArc(ID1436574204)

// Neighbour Table ----> Generate Update

val pattern_ID1436577849IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (neighbours: NeighbourTable))) =>
		BindingElement_ID1436572879(None, None, Some(node), None, Some(neighbours), None)
}, 0)
val eval_ID1436577849IN = (be: BindingElement_ID1436572879) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1436577849IN = Arc("ID1436577849IN", ID1434189908, ID1436572879, In) (eval_ID1436577849IN)
net.addArc(ID1436577849IN)

// Source Table ----> Generate Update

val pattern_ID1466040000_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (sources: SourceTable))) =>
		BindingElement_ID1436572879(None, None, Some(node), Some(sources), None, None)
}, 0)
val eval_ID1466040000 = (be: BindingElement_ID1436572879) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1466040000 = Arc("ID1466040000", ID1430786899, ID1436572879, In) (eval_ID1466040000)
net.addArc(ID1466040000)

// Generate Update ----> babel to link


val eval_ID1466077892 = (be: BindingElement_ID1436572879) => (be.node,be.dest,be.updateTlv) match {
	case (Some(node),Some(dest),Some(updateTlv)) =>
		Packet(Unicast(node), dest, updateTlv)
}
val ID1466077892 = Arc("ID1466077892", ID1436585186, ID1436572879, Out) (eval_ID1466077892)
net.addArc(ID1466077892)

// Generate Update ----> Neighbour Table


val eval_ID1436577849OUT = (be: BindingElement_ID1436572879) => (be.node,be.neighbours) match {
	case (Some(node),Some(neighbours)) =>
		scala.Tuple2(1, scala.Tuple2(node, neighbours))
}
val ID1436577849OUT = Arc("ID1436577849OUT", ID1434189908, ID1436572879, Out) (eval_ID1436577849OUT)
net.addArc(ID1436577849OUT)

// Generate Update ----> Source Table


val eval_ID1466056058 = (be: BindingElement_ID1436572879) => (be.node,be.sources,be.updateTlv) match {
	case (Some(node),Some(sources),Some(updateTlv)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources.updateFD(updateTlv)))
}
val ID1466056058 = Arc("ID1466056058", ID1430786899, ID1436572879, Out) (eval_ID1466056058)
net.addArc(ID1466056058)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Generate Update

ID1436572879.orderedPatternBindingBasis = List(
	ArcPattern(ID1436574204, pattern_ID1436574204_0),
	ArcPattern(ID1466040000, pattern_ID1466040000_0),
	ArcPattern(ID1436577849IN, pattern_ID1436577849IN_0),
	guard_ID1436572879_0)



// Wire modules



}



// ########################################################################
// Generated code for module BabelProtocol
// ########################################################################
class BabelProtocol[T0](ID1429812261: Place[T0, Multiset[Packet]], ID1435085084: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0]){

val net = CPNGraph()

// Places
val ID1455263737 = Place("ID1455263737", "Route Update", Multiset[(Int,Int,RouteTable)]())
net.addPlace(ID1455263737)

val ID1450645381 = Place("ID1450645381", "Link Change / New Route", Multiset[(Int,Int)]())
net.addPlace(ID1450645381)

val ID1436559953 = Place("ID1436559953", "Route Advertisement", Multiset[RouteAdv]())
net.addPlace(ID1436559953)



// Wire modules
val st0 = new RouteSelection(ID1436559953, ID1450645381, ID1435085084, ID1455263737) 
net.addSubstitutionTransition(st0.net)

val st1 = new GenerateUpdate(ID1436559953, ID1435085084) 
net.addSubstitutionTransition(st1.net)

val st2 = new ProcessPacket(ID1436559953, ID1435085084, ID1450645381, ID1429812261, ID1455263737) 
net.addSubstitutionTransition(st2.net)

val st3 = new PeriodicMessages(ID1435085084, ID1436559953) 
net.addSubstitutionTransition(st3.net)

val st4 = new Timeouts(ID1455263737) 
net.addSubstitutionTransition(st4.net)


}



// ########################################################################
// Generated code for module EvaluateRouteChanges
// ########################################################################
class EvaluateRouteChanges[T0, T1, T2](ID1442010737: Place[T2, Multiset[RouteChanges]], ID1442018806: Place[T1, Multiset[RouteAdv]], ID1449964991: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1], ev2: Multiset[RouteChanges] => Traversable[T2]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition NextHop Changed and its arcs
// ========================================================================

case class BindingElement_ID1442074455(
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442074455 = (b1: BindingElement_ID1442074455, b2: BindingElement_ID1442074455) =>
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442074455 = (b1: BindingElement_ID1442074455, b2: BindingElement_ID1442074455) => {
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442074455(rc)
}

val fullBindingElement_ID1442074455 = (be: BindingElement_ID1442074455) =>
	be.rc != None

val guard_ID1442074455_0 = EvalGuard((be: BindingElement_ID1442074455) => (be.rc) match {
	case (Some(rc)) => {
		rc.nextHopChanged
	}
}, 0)
val guard_ID1442074455_1 = EvalGuard((be: BindingElement_ID1442074455) => (be.rc) match {
	case (Some(rc)) => {
		rc.insigChanges
	}
}, 0)
val guard_ID1442074455 = List(guard_ID1442074455_0,guard_ID1442074455_1)

val enumBindings_ID1442074455 = List(
	)

val ID1442074455 = Transition("ID1442074455","NextHop Changed",1,compatible_ID1442074455,merge_ID1442074455,fullBindingElement_ID1442074455,guard_ID1442074455,enumBindings_ID1442074455)
net.addTransition(ID1442074455)

// ------------------ Arcs ------------------

// new route selection ----> NextHop Changed

val pattern_ID1442168343_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442074455(Some(rc))
}, 0)
val eval_ID1442168343 = (be: BindingElement_ID1442074455) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442168343 = Arc("ID1442168343", ID1442010737, ID1442074455, In) (eval_ID1442168343)
net.addArc(ID1442168343)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for NextHop Changed

ID1442074455.orderedPatternBindingBasis = List(
	ArcPattern(ID1442168343, pattern_ID1442168343_0))



// ========================================================================
// Generated code for transition Significant Metric Change and its arcs
// ========================================================================

case class BindingElement_ID1442082901(
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442082901 = (b1: BindingElement_ID1442082901, b2: BindingElement_ID1442082901) =>
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442082901 = (b1: BindingElement_ID1442082901, b2: BindingElement_ID1442082901) => {
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442082901(rc)
}

val fullBindingElement_ID1442082901 = (be: BindingElement_ID1442082901) =>
	be.rc != None

val guard_ID1442082901_0 = EvalGuard((be: BindingElement_ID1442082901) => (be.rc) match {
	case (Some(rc)) => {
		rc.signMetricChange
	}
}, 0)
val guard_ID1442082901 = List(guard_ID1442082901_0)

val enumBindings_ID1442082901 = List(
	)

val ID1442082901 = Transition("ID1442082901","Significant Metric Change",1,compatible_ID1442082901,merge_ID1442082901,fullBindingElement_ID1442082901,guard_ID1442082901,enumBindings_ID1442082901)
net.addTransition(ID1442082901)

// ------------------ Arcs ------------------

// new route selection ----> Significant Metric Change

val pattern_ID1442198861_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442082901(Some(rc))
}, 0)
val eval_ID1442198861 = (be: BindingElement_ID1442082901) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442198861 = Arc("ID1442198861", ID1442010737, ID1442082901, In) (eval_ID1442198861)
net.addArc(ID1442198861)

// Significant Metric Change ----> updated route


val eval_ID1442279228 = (be: BindingElement_ID1442082901) => (be.rc) match {
	case (Some(rc)) =>
		rc.updateMessageData(Multicast())
}
val ID1442279228 = Arc("ID1442279228", ID1442018806, ID1442082901, Out) (eval_ID1442279228)
net.addArc(ID1442279228)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Significant Metric Change

ID1442082901.orderedPatternBindingBasis = List(
	ArcPattern(ID1442198861, pattern_ID1442198861_0))



// ========================================================================
// Generated code for transition Insignificant Metric Change and its arcs
// ========================================================================

case class BindingElement_ID1442061858(
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442061858 = (b1: BindingElement_ID1442061858, b2: BindingElement_ID1442061858) =>
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442061858 = (b1: BindingElement_ID1442061858, b2: BindingElement_ID1442061858) => {
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442061858(rc)
}

val fullBindingElement_ID1442061858 = (be: BindingElement_ID1442061858) =>
	be.rc != None

val guard_ID1442061858_0 = EvalGuard((be: BindingElement_ID1442061858) => (be.rc) match {
	case (Some(rc)) => {
		rc.insMetricChange
	}
}, 0)
val guard_ID1442061858_1 = EvalGuard((be: BindingElement_ID1442061858) => (be.rc) match {
	case (Some(rc)) => {
		rc.insigChanges
	}
}, 0)
val guard_ID1442061858 = List(guard_ID1442061858_0,guard_ID1442061858_1)

val enumBindings_ID1442061858 = List(
	)

val ID1442061858 = Transition("ID1442061858","Insignificant Metric Change",1,compatible_ID1442061858,merge_ID1442061858,fullBindingElement_ID1442061858,guard_ID1442061858,enumBindings_ID1442061858)
net.addTransition(ID1442061858)

// ------------------ Arcs ------------------

// new route selection ----> Insignificant Metric Change

val pattern_ID1442159019_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442061858(Some(rc))
}, 0)
val eval_ID1442159019 = (be: BindingElement_ID1442061858) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442159019 = Arc("ID1442159019", ID1442010737, ID1442061858, In) (eval_ID1442159019)
net.addArc(ID1442159019)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Insignificant Metric Change

ID1442061858.orderedPatternBindingBasis = List(
	ArcPattern(ID1442159019, pattern_ID1442159019_0))



// ========================================================================
// Generated code for transition New Route and its arcs
// ========================================================================

case class BindingElement_ID1442102107(
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442102107 = (b1: BindingElement_ID1442102107, b2: BindingElement_ID1442102107) =>
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442102107 = (b1: BindingElement_ID1442102107, b2: BindingElement_ID1442102107) => {
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442102107(rc)
}

val fullBindingElement_ID1442102107 = (be: BindingElement_ID1442102107) =>
	be.rc != None

val guard_ID1442102107_0 = EvalGuard((be: BindingElement_ID1442102107) => (be.rc) match {
	case (Some(rc)) => {
		rc.newRoute
	}
}, 0)
val guard_ID1442102107 = List(guard_ID1442102107_0)

val enumBindings_ID1442102107 = List(
	)

val ID1442102107 = Transition("ID1442102107","New Route",1,compatible_ID1442102107,merge_ID1442102107,fullBindingElement_ID1442102107,guard_ID1442102107,enumBindings_ID1442102107)
net.addTransition(ID1442102107)

// ------------------ Arcs ------------------

// new route selection ----> New Route

val pattern_ID1442213393_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442102107(Some(rc))
}, 0)
val eval_ID1442213393 = (be: BindingElement_ID1442102107) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442213393 = Arc("ID1442213393", ID1442010737, ID1442102107, In) (eval_ID1442213393)
net.addArc(ID1442213393)

// New Route ----> updated route


val eval_ID1442294969 = (be: BindingElement_ID1442102107) => (be.rc) match {
	case (Some(rc)) =>
		rc.updateMessageData(Multicast())
}
val ID1442294969 = Arc("ID1442294969", ID1442018806, ID1442102107, Out) (eval_ID1442294969)
net.addArc(ID1442294969)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for New Route

ID1442102107.orderedPatternBindingBasis = List(
	ArcPattern(ID1442213393, pattern_ID1442213393_0))



// ========================================================================
// Generated code for transition Route Retraction and its arcs
// ========================================================================

case class BindingElement_ID1442089276(
	node: Option[Int],
	sources: Option[SourceTable],
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442089276 = (b1: BindingElement_ID1442089276, b2: BindingElement_ID1442089276) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None) &&
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442089276 = (b1: BindingElement_ID1442089276, b2: BindingElement_ID1442089276) => {
	val node = if (b1.node == None) b2.node else b1.node
	val sources = if (b1.sources == None) b2.sources else b1.sources
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442089276(node, sources, rc)
}

val fullBindingElement_ID1442089276 = (be: BindingElement_ID1442089276) =>
	be.node != None &&
	be.sources != None &&
	be.rc != None

val guard_ID1442089276_0 = BindGuard((be: BindingElement_ID1442089276) => (be.rc) match {
	case (Some(rc)) =>
		val node = rc.node
		BindingElement_ID1442089276(Some(node), be.sources, be.rc)
}, 0)
val guard_ID1442089276_1 = EvalGuard((be: BindingElement_ID1442089276) => (be.rc) match {
	case (Some(rc)) => {
		rc.routeRetracted
	}
}, 0)
val guard_ID1442089276 = List(guard_ID1442089276_0,guard_ID1442089276_1)

val enumBindings_ID1442089276 = List(
	)

val ID1442089276 = Transition("ID1442089276","Route Retraction",1,compatible_ID1442089276,merge_ID1442089276,fullBindingElement_ID1442089276,guard_ID1442089276,enumBindings_ID1442089276)
net.addTransition(ID1442089276)

// ------------------ Arcs ------------------

// new route selection ----> Route Retraction

val pattern_ID1442203607_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442089276(None, None, Some(rc))
}, 0)
val eval_ID1442203607 = (be: BindingElement_ID1442089276) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442203607 = Arc("ID1442203607", ID1442010737, ID1442089276, In) (eval_ID1442203607)
net.addArc(ID1442203607)

// Source Table ----> Route Retraction

val pattern_ID1450001923IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (sources: SourceTable))) =>
		BindingElement_ID1442089276(Some(node), Some(sources), None)
}, 0)
val eval_ID1450001923IN = (be: BindingElement_ID1442089276) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1450001923IN = Arc("ID1450001923IN", ID1430786899, ID1442089276, In) (eval_ID1450001923IN)
net.addArc(ID1450001923IN)

// Route Retraction ----> updated route


val eval_ID1442284539 = (be: BindingElement_ID1442089276) => (be.rc) match {
	case (Some(rc)) =>
		rc.updateMessageData(Multicast())
}
val ID1442284539 = Arc("ID1442284539", ID1442018806, ID1442089276, Out) (eval_ID1442284539)
net.addArc(ID1442284539)

// Route Retraction ----> babel to link


val eval_ID1449984116 = (be: BindingElement_ID1442089276) => (be.rc,be.sources) match {
	case (Some(rc),Some(sources)) =>
		rc.seqNoReqRetraction(sources)
}
val ID1449984116 = Arc("ID1449984116", ID1449964991, ID1442089276, Out) (eval_ID1449984116)
net.addArc(ID1449984116)

// Route Retraction ----> Source Table


val eval_ID1450001923OUT = (be: BindingElement_ID1442089276) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1450001923OUT = Arc("ID1450001923OUT", ID1430786899, ID1442089276, Out) (eval_ID1450001923OUT)
net.addArc(ID1450001923OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Route Retraction

ID1442089276.orderedPatternBindingBasis = List(
	ArcPattern(ID1450001923IN, pattern_ID1450001923IN_0),
	ArcPattern(ID1442203607, pattern_ID1442203607_0),
	guard_ID1442089276_0)



// ========================================================================
// Generated code for transition Requested Update and its arcs
// ========================================================================

case class BindingElement_ID1455785114(
	node: Option[Int],
	preqs: Option[PendingReqTable],
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1455785114 = (b1: BindingElement_ID1455785114, b2: BindingElement_ID1455785114) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None) &&
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1455785114 = (b1: BindingElement_ID1455785114, b2: BindingElement_ID1455785114) => {
	val node = if (b1.node == None) b2.node else b1.node
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1455785114(node, preqs, rc)
}

val fullBindingElement_ID1455785114 = (be: BindingElement_ID1455785114) =>
	be.node != None &&
	be.preqs != None &&
	be.rc != None

val guard_ID1455785114_0 = BindGuard((be: BindingElement_ID1455785114) => (be.rc) match {
	case (Some(rc)) =>
		val node = rc.node
		BindingElement_ID1455785114(Some(node), be.preqs, be.rc)
}, 0)
val guard_ID1455785114_1 = EvalGuard((be: BindingElement_ID1455785114) => (be.rc) match {
	case (Some(rc)) => {
		rc.seqNoChanged
	}
}, 0)
val guard_ID1455785114_2 = EvalGuard((be: BindingElement_ID1455785114) => (be.rc,be.preqs) match {
	case (Some(rc),Some(preqs)) => {
		rc.requestedUpdate(preqs)
	}
}, 0)
val guard_ID1455785114 = List(guard_ID1455785114_0,guard_ID1455785114_1,guard_ID1455785114_2)

val enumBindings_ID1455785114 = List(
	)

val ID1455785114 = Transition("ID1455785114","Requested Update",1,compatible_ID1455785114,merge_ID1455785114,fullBindingElement_ID1455785114,guard_ID1455785114,enumBindings_ID1455785114)
net.addTransition(ID1455785114)

// ------------------ Arcs ------------------

// new route selection ----> Requested Update

val pattern_ID1455790731_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1455785114(None, None, Some(rc))
}, 0)
val eval_ID1455790731 = (be: BindingElement_ID1455785114) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1455790731 = Arc("ID1455790731", ID1442010737, ID1455785114, In) (eval_ID1455790731)
net.addArc(ID1455790731)

// Pending Req Table ----> Requested Update

val pattern_ID1455821131_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1455785114(Some(node), Some(preqs), None)
}, 0)
val eval_ID1455821131 = (be: BindingElement_ID1455785114) => (be.node,be.preqs) match {
	case (Some(node),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs))
}
val ID1455821131 = Arc("ID1455821131", ID1435604444, ID1455785114, In) (eval_ID1455821131)
net.addArc(ID1455821131)

// Requested Update ----> updated route


val eval_ID1455969928 = (be: BindingElement_ID1455785114) => (be.rc) match {
	case (Some(rc)) =>
		rc.updateMessageData(Multicast())
}
val ID1455969928 = Arc("ID1455969928", ID1442018806, ID1455785114, Out) (eval_ID1455969928)
net.addArc(ID1455969928)

// Requested Update ----> Pending Req Table


val eval_ID1455831451 = (be: BindingElement_ID1455785114) => (be.node,be.preqs,be.rc) match {
	case (Some(node),Some(preqs),Some(rc)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs.remove(rc.newRoutes)))
}
val ID1455831451 = Arc("ID1455831451", ID1435604444, ID1455785114, Out) (eval_ID1455831451)
net.addArc(ID1455831451)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Requested Update

ID1455785114.orderedPatternBindingBasis = List(
	ArcPattern(ID1455821131, pattern_ID1455821131_0),
	ArcPattern(ID1455790731, pattern_ID1455790731_0),
	guard_ID1455785114_0)



// ========================================================================
// Generated code for transition SeqNo Changed and its arcs
// ========================================================================

case class BindingElement_ID1442068143(
	node: Option[Int],
	preqs: Option[PendingReqTable],
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1442068143 = (b1: BindingElement_ID1442068143, b2: BindingElement_ID1442068143) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None) &&
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1442068143 = (b1: BindingElement_ID1442068143, b2: BindingElement_ID1442068143) => {
	val node = if (b1.node == None) b2.node else b1.node
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1442068143(node, preqs, rc)
}

val fullBindingElement_ID1442068143 = (be: BindingElement_ID1442068143) =>
	be.node != None &&
	be.preqs != None &&
	be.rc != None

val guard_ID1442068143_0 = BindGuard((be: BindingElement_ID1442068143) => (be.rc) match {
	case (Some(rc)) =>
		val node = rc.node
		BindingElement_ID1442068143(Some(node), be.preqs, be.rc)
}, 0)
val guard_ID1442068143_1 = EvalGuard((be: BindingElement_ID1442068143) => (be.rc) match {
	case (Some(rc)) => {
		rc.seqNoChanged
	}
}, 0)
val guard_ID1442068143_2 = EvalGuard((be: BindingElement_ID1442068143) => (be.rc) match {
	case (Some(rc)) => {
		rc.insigChanges
	}
}, 0)
val guard_ID1442068143_3 = EvalGuard((be: BindingElement_ID1442068143) => (be.rc,be.preqs) match {
	case (Some(rc),Some(preqs)) => {
		rc.requestedUpdate(preqs).unary_$bang
	}
}, 0)
val guard_ID1442068143 = List(guard_ID1442068143_0,guard_ID1442068143_1,guard_ID1442068143_2,guard_ID1442068143_3)

val enumBindings_ID1442068143 = List(
	)

val ID1442068143 = Transition("ID1442068143","SeqNo Changed",1,compatible_ID1442068143,merge_ID1442068143,fullBindingElement_ID1442068143,guard_ID1442068143,enumBindings_ID1442068143)
net.addTransition(ID1442068143)

// ------------------ Arcs ------------------

// new route selection ----> SeqNo Changed

val pattern_ID1442163648_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1442068143(None, None, Some(rc))
}, 0)
val eval_ID1442163648 = (be: BindingElement_ID1442068143) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1442163648 = Arc("ID1442163648", ID1442010737, ID1442068143, In) (eval_ID1442163648)
net.addArc(ID1442163648)

// Pending Req Table ----> SeqNo Changed

val pattern_ID1455833265IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1442068143(Some(node), Some(preqs), None)
}, 0)
val eval_ID1455833265IN = (be: BindingElement_ID1442068143) => (be.node,be.preqs) match {
	case (Some(node),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs))
}
val ID1455833265IN = Arc("ID1455833265IN", ID1435604444, ID1442068143, In) (eval_ID1455833265IN)
net.addArc(ID1455833265IN)

// SeqNo Changed ----> Pending Req Table


val eval_ID1455833265OUT = (be: BindingElement_ID1442068143) => (be.node,be.preqs) match {
	case (Some(node),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs))
}
val ID1455833265OUT = Arc("ID1455833265OUT", ID1435604444, ID1442068143, Out) (eval_ID1455833265OUT)
net.addArc(ID1455833265OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for SeqNo Changed

ID1442068143.orderedPatternBindingBasis = List(
	ArcPattern(ID1455833265IN, pattern_ID1455833265IN_0),
	ArcPattern(ID1442163648, pattern_ID1442163648_0),
	guard_ID1442068143_0)



// ========================================================================
// Generated code for transition No Change and its arcs
// ========================================================================

case class BindingElement_ID1445704819(
	rc: Option[RouteChanges]) extends BindingElement

val compatible_ID1445704819 = (b1: BindingElement_ID1445704819, b2: BindingElement_ID1445704819) =>
	(b1.rc == b2.rc || b1.rc == None || b2.rc == None)

val merge_ID1445704819 = (b1: BindingElement_ID1445704819, b2: BindingElement_ID1445704819) => {
	val rc = if (b1.rc == None) b2.rc else b1.rc
	new BindingElement_ID1445704819(rc)
}

val fullBindingElement_ID1445704819 = (be: BindingElement_ID1445704819) =>
	be.rc != None

val guard_ID1445704819_0 = EvalGuard((be: BindingElement_ID1445704819) => (be.rc) match {
	case (Some(rc)) => {
		rc.noChanges
	}
}, 0)
val guard_ID1445704819_1 = EvalGuard((be: BindingElement_ID1445704819) => (be.rc) match {
	case (Some(rc)) => {
		rc.insigChanges
	}
}, 0)
val guard_ID1445704819 = List(guard_ID1445704819_0,guard_ID1445704819_1)

val enumBindings_ID1445704819 = List(
	)

val ID1445704819 = Transition("ID1445704819","No Change",1,compatible_ID1445704819,merge_ID1445704819,fullBindingElement_ID1445704819,guard_ID1445704819,enumBindings_ID1445704819)
net.addTransition(ID1445704819)

// ------------------ Arcs ------------------

// new route selection ----> No Change

val pattern_ID1445708918_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (rc: RouteChanges)) =>
		BindingElement_ID1445704819(Some(rc))
}, 0)
val eval_ID1445708918 = (be: BindingElement_ID1445704819) => (be.rc) match {
	case (Some(rc)) =>
		rc
}
val ID1445708918 = Arc("ID1445708918", ID1442010737, ID1445704819, In) (eval_ID1445708918)
net.addArc(ID1445708918)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for No Change

ID1445704819.orderedPatternBindingBasis = List(
	ArcPattern(ID1445708918, pattern_ID1445708918_0))



// Wire modules



}



// ########################################################################
// Generated code for module DataStructures
// ########################################################################
class DataStructures(){

val net = CPNGraph()

// Places




// Wire modules



}

val datastructures0 = new DataStructures() 
net.addSubstitutionTransition(datastructures0.net)

// ########################################################################
// Generated code for module Testing
// ########################################################################
class Testing(){

val net = CPNGraph()

// Places




// Wire modules



}

val testing0 = new Testing() 
net.addSubstitutionTransition(testing0.net)

// ########################################################################
// Generated code for module ProcessRouteReq
// ########################################################################
class ProcessRouteReq[T0, T1](ID1430309403: Place[T1, Multiset[RecTLV]], ID1470596684: Place[T0, Multiset[RouteAdv]])(
implicit ev0: Multiset[RouteAdv] => Traversable[T0], ev1: Multiset[RecTLV] => Traversable[T1]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Advertise Route and its arcs
// ========================================================================

case class BindingElement_ID1440441403(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	rrTlv: Option[RouteReqTLV]) extends BindingElement

val compatible_ID1440441403 = (b1: BindingElement_ID1440441403, b2: BindingElement_ID1440441403) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.rrTlv == b2.rrTlv || b1.rrTlv == None || b2.rrTlv == None)

val merge_ID1440441403 = (b1: BindingElement_ID1440441403, b2: BindingElement_ID1440441403) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val rrTlv = if (b1.rrTlv == None) b2.rrTlv else b1.rrTlv
	new BindingElement_ID1440441403(routes, srcNode, destNode, rrTlv)
}

val fullBindingElement_ID1440441403 = (be: BindingElement_ID1440441403) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.rrTlv != None


val guard_ID1440441403 = List()

val enumBindings_ID1440441403 = List(
	)

val ID1440441403 = Transition("ID1440441403","Advertise Route",1,compatible_ID1440441403,merge_ID1440441403,fullBindingElement_ID1440441403,guard_ID1440441403,enumBindings_ID1440441403)
net.addTransition(ID1440441403)

// ------------------ Arcs ------------------

// received tlv ----> Advertise Route

val pattern_ID1440553096_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (rrTlv: RouteReqTLV))) =>
		BindingElement_ID1440441403(None, Some(srcNode), Some(destNode), Some(rrTlv))
}, 0)
val eval_ID1440553096 = (be: BindingElement_ID1440441403) => (be.srcNode,be.destNode,be.rrTlv) match {
	case (Some(srcNode),Some(destNode),Some(rrTlv)) =>
		RecTLV(srcNode, destNode, rrTlv)
}
val ID1440553096 = Arc("ID1440553096", ID1430309403, ID1440441403, In) (eval_ID1440553096)
net.addArc(ID1440553096)

// Route Table ----> Advertise Route

val pattern_ID1440557799IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1440441403(Some(routes), None, Some(destNode), None)
}, 0)
val eval_ID1440557799IN = (be: BindingElement_ID1440441403) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1440557799IN = Arc("ID1440557799IN", ID1431111582, ID1440441403, In) (eval_ID1440557799IN)
net.addArc(ID1440557799IN)

// Advertise Route ----> hmm


val eval_ID1470596950 = (be: BindingElement_ID1440441403) => (be.routes,be.destNode,be.srcNode,be.rrTlv) match {
	case (Some(routes),Some(destNode),Some(srcNode),Some(rrTlv)) =>
		routes.advertiseRoutes(destNode, srcNode, rrTlv.routerId)
}
val ID1470596950 = Arc("ID1470596950", ID1470596684, ID1440441403, Out) (eval_ID1470596950)
net.addArc(ID1470596950)

// Advertise Route ----> Route Table


val eval_ID1440557799OUT = (be: BindingElement_ID1440441403) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1440557799OUT = Arc("ID1440557799OUT", ID1431111582, ID1440441403, Out) (eval_ID1440557799OUT)
net.addArc(ID1440557799OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Advertise Route

ID1440441403.orderedPatternBindingBasis = List(
	ArcPattern(ID1440553096, pattern_ID1440553096_0),
	ArcPattern(ID1440557799IN, pattern_ID1440557799IN_0))



// Wire modules



}



// ########################################################################
// Generated code for module Link
// ########################################################################
class Link[T0](ID1429778412: Place[T0, Multiset[Packet]], ID1429782166: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0]){

val net = CPNGraph()

// Places
val ID1451260133 = Place("ID1451260133", "Topology", initTopology: Multiset[(Int, List[Int])])
net.addPlace(ID1451260133)



// Wire modules
val st0 = new TransmitPacket(ID1429778412, ID1429782166, ID1451260133) 
net.addSubstitutionTransition(st0.net)

val st1 = new Mobility(ID1451260133) 
net.addSubstitutionTransition(st1.net)


}



// ########################################################################
// Generated code for module ProcessSeqNoReq
// ########################################################################
class ProcessSeqNoReq[T0, T1, T2](ID1430335795: Place[T2, Multiset[RecTLV]], ID1435484250: Place[T1, Multiset[RouteAdv]], ID1435644836: Place[T0, Multiset[Packet]])(
implicit ev0: Multiset[Packet] => Traversable[T0], ev1: Multiset[RouteAdv] => Traversable[T1], ev2: Multiset[RecTLV] => Traversable[T2]){

val net = CPNGraph()

// Places
val ID1435464489 = Place("ID1435464489", "Validated SeqNoReqs", Multiset[(Int,Int,RouteTable, SeqNoReqTLV)]())
net.addPlace(ID1435464489)

// ========================================================================
// Generated code for transition Process Request and its arcs
// ========================================================================

case class BindingElement_ID1435303156(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	preqs: Option[PendingReqTable],
	snrTlv: Option[SeqNoReqTLV]) extends BindingElement

val compatible_ID1435303156 = (b1: BindingElement_ID1435303156, b2: BindingElement_ID1435303156) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None) &&
	(b1.snrTlv == b2.snrTlv || b1.snrTlv == None || b2.snrTlv == None)

val merge_ID1435303156 = (b1: BindingElement_ID1435303156, b2: BindingElement_ID1435303156) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	val snrTlv = if (b1.snrTlv == None) b2.snrTlv else b1.snrTlv
	new BindingElement_ID1435303156(routes, srcNode, destNode, preqs, snrTlv)
}

val fullBindingElement_ID1435303156 = (be: BindingElement_ID1435303156) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.preqs != None &&
	be.snrTlv != None

val guard_ID1435303156_0 = EvalGuard((be: BindingElement_ID1435303156) => (be.preqs,be.snrTlv) match {
	case (Some(preqs),Some(snrTlv)) => {
		preqs.hasPendingReq(snrTlv.routerId)
	}
}, 0)
val guard_ID1435303156_1 = EvalGuard((be: BindingElement_ID1435303156) => (be.routes,be.snrTlv,be.srcNode) match {
	case (Some(routes),Some(snrTlv),Some(srcNode)) => {
		routes.hasRoute(snrTlv.routerId, srcNode)
	}
}, 0)
val guard_ID1435303156 = List(guard_ID1435303156_0,guard_ID1435303156_1)

val enumBindings_ID1435303156 = List(
	)

val ID1435303156 = Transition("ID1435303156","Process Request",1,compatible_ID1435303156,merge_ID1435303156,fullBindingElement_ID1435303156,guard_ID1435303156,enumBindings_ID1435303156)
net.addTransition(ID1435303156)

// ------------------ Arcs ------------------

// received tlv ----> Process Request

val pattern_ID1435304156_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (snrTlv: SeqNoReqTLV))) =>
		BindingElement_ID1435303156(None, Some(srcNode), Some(destNode), None, Some(snrTlv))
}, 0)
val eval_ID1435304156 = (be: BindingElement_ID1435303156) => (be.srcNode,be.destNode,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(snrTlv)) =>
		RecTLV(srcNode, destNode, snrTlv)
}
val ID1435304156 = Arc("ID1435304156", ID1430335795, ID1435303156, In) (eval_ID1435304156)
net.addArc(ID1435304156)

// Pending Req Table ----> Process Request

val pattern_ID1453979295IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1435303156(None, None, Some(destNode), Some(preqs), None)
}, 0)
val eval_ID1453979295IN = (be: BindingElement_ID1435303156) => (be.destNode,be.preqs) match {
	case (Some(destNode),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs))
}
val ID1453979295IN = Arc("ID1453979295IN", ID1435604444, ID1435303156, In) (eval_ID1453979295IN)
net.addArc(ID1453979295IN)

// Route Table ----> Process Request

val pattern_ID1435360255IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1435303156(Some(routes), None, Some(destNode), None, None)
}, 0)
val eval_ID1435360255IN = (be: BindingElement_ID1435303156) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1435360255IN = Arc("ID1435360255IN", ID1431111582, ID1435303156, In) (eval_ID1435360255IN)
net.addArc(ID1435360255IN)

// Process Request ----> Validated SeqNoReqs


val eval_ID1435470179 = (be: BindingElement_ID1435303156) => (be.srcNode,be.destNode,be.routes,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(routes),Some(snrTlv)) =>
		scala.Tuple4(srcNode, destNode, routes, snrTlv)
}
val ID1435470179 = Arc("ID1435470179", ID1435464489, ID1435303156, Out) (eval_ID1435470179)
net.addArc(ID1435470179)

// Process Request ----> Pending Req Table


val eval_ID1453979295OUT = (be: BindingElement_ID1435303156) => (be.destNode,be.preqs) match {
	case (Some(destNode),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs))
}
val ID1453979295OUT = Arc("ID1453979295OUT", ID1435604444, ID1435303156, Out) (eval_ID1453979295OUT)
net.addArc(ID1453979295OUT)

// Process Request ----> Route Table


val eval_ID1435360255OUT = (be: BindingElement_ID1435303156) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1435360255OUT = Arc("ID1435360255OUT", ID1431111582, ID1435303156, Out) (eval_ID1435360255OUT)
net.addArc(ID1435360255OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Process Request

ID1435303156.orderedPatternBindingBasis = List(
	ArcPattern(ID1435304156, pattern_ID1435304156_0),
	ArcPattern(ID1435360255IN, pattern_ID1435360255IN_0),
	ArcPattern(ID1453979295IN, pattern_ID1453979295IN_0))



// ========================================================================
// Generated code for transition Ignore Request and its arcs
// ========================================================================

case class BindingElement_ID1435390625(
	routes: Option[RouteTable],
	srcNode: Option[Int],
	destNode: Option[Int],
	preqs: Option[PendingReqTable],
	snrTlv: Option[SeqNoReqTLV]) extends BindingElement

val compatible_ID1435390625 = (b1: BindingElement_ID1435390625, b2: BindingElement_ID1435390625) =>
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None) &&
	(b1.srcNode == b2.srcNode || b1.srcNode == None || b2.srcNode == None) &&
	(b1.destNode == b2.destNode || b1.destNode == None || b2.destNode == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None) &&
	(b1.snrTlv == b2.snrTlv || b1.snrTlv == None || b2.snrTlv == None)

val merge_ID1435390625 = (b1: BindingElement_ID1435390625, b2: BindingElement_ID1435390625) => {
	val routes = if (b1.routes == None) b2.routes else b1.routes
	val srcNode = if (b1.srcNode == None) b2.srcNode else b1.srcNode
	val destNode = if (b1.destNode == None) b2.destNode else b1.destNode
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	val snrTlv = if (b1.snrTlv == None) b2.snrTlv else b1.snrTlv
	new BindingElement_ID1435390625(routes, srcNode, destNode, preqs, snrTlv)
}

val fullBindingElement_ID1435390625 = (be: BindingElement_ID1435390625) =>
	be.routes != None &&
	be.srcNode != None &&
	be.destNode != None &&
	be.preqs != None &&
	be.snrTlv != None

val guard_ID1435390625_0 = EvalGuard((be: BindingElement_ID1435390625) => (be.preqs,be.snrTlv) match {
	case (Some(preqs),Some(snrTlv)) => {
		preqs.hasPendingReq(snrTlv.routerId).unary_$bang
	}
}, 0)
val guard_ID1435390625_1 = EvalGuard((be: BindingElement_ID1435390625) => (be.routes,be.snrTlv,be.srcNode) match {
	case (Some(routes),Some(snrTlv),Some(srcNode)) => {
		routes.hasRoute(snrTlv.routerId, srcNode).unary_$bang
	}
}, 0)
val guard_ID1435390625 = List(guard_ID1435390625_0,guard_ID1435390625_1)

val enumBindings_ID1435390625 = List(
	)

val ID1435390625 = Transition("ID1435390625","Ignore Request",1,compatible_ID1435390625,merge_ID1435390625,fullBindingElement_ID1435390625,guard_ID1435390625,enumBindings_ID1435390625)
net.addTransition(ID1435390625)

// ------------------ Arcs ------------------

// received tlv ----> Ignore Request

val pattern_ID1435401358_0 = Pattern((token: Any) => token match {
	case Tuple2(_, RecTLV((srcNode: Int), (destNode: Int), (snrTlv: SeqNoReqTLV))) =>
		BindingElement_ID1435390625(None, Some(srcNode), Some(destNode), None, Some(snrTlv))
}, 0)
val eval_ID1435401358 = (be: BindingElement_ID1435390625) => (be.srcNode,be.destNode,be.snrTlv) match {
	case (Some(srcNode),Some(destNode),Some(snrTlv)) =>
		RecTLV(srcNode, destNode, snrTlv)
}
val ID1435401358 = Arc("ID1435401358", ID1430335795, ID1435390625, In) (eval_ID1435401358)
net.addArc(ID1435401358)

// Pending Req Table ----> Ignore Request

val pattern_ID1453938513IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1435390625(None, None, Some(destNode), Some(preqs), None)
}, 0)
val eval_ID1453938513IN = (be: BindingElement_ID1435390625) => (be.destNode,be.preqs) match {
	case (Some(destNode),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs))
}
val ID1453938513IN = Arc("ID1453938513IN", ID1435604444, ID1435390625, In) (eval_ID1453938513IN)
net.addArc(ID1453938513IN)

// Route Table ----> Ignore Request

val pattern_ID1435396826IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((destNode: Int), (routes: RouteTable))) =>
		BindingElement_ID1435390625(Some(routes), None, Some(destNode), None, None)
}, 0)
val eval_ID1435396826IN = (be: BindingElement_ID1435390625) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1435396826IN = Arc("ID1435396826IN", ID1431111582, ID1435390625, In) (eval_ID1435396826IN)
net.addArc(ID1435396826IN)

// Ignore Request ----> Pending Req Table


val eval_ID1453938513OUT = (be: BindingElement_ID1435390625) => (be.destNode,be.preqs) match {
	case (Some(destNode),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, preqs))
}
val ID1453938513OUT = Arc("ID1453938513OUT", ID1435604444, ID1435390625, Out) (eval_ID1453938513OUT)
net.addArc(ID1453938513OUT)

// Ignore Request ----> Route Table


val eval_ID1435396826OUT = (be: BindingElement_ID1435390625) => (be.destNode,be.routes) match {
	case (Some(destNode),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(destNode, routes))
}
val ID1435396826OUT = Arc("ID1435396826OUT", ID1431111582, ID1435390625, Out) (eval_ID1435396826OUT)
net.addArc(ID1435396826OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Ignore Request

ID1435390625.orderedPatternBindingBasis = List(
	ArcPattern(ID1435401358, pattern_ID1435401358_0),
	ArcPattern(ID1435396826IN, pattern_ID1435396826IN_0),
	ArcPattern(ID1453938513IN, pattern_ID1453938513IN_0))



// Wire modules
val st0 = new SeqNoReqResponse(ID1435464489, ID1435644836, ID1435484250) 
net.addSubstitutionTransition(st0.net)


}



// ########################################################################
// Generated code for module Mobility
// ########################################################################
class Mobility[T0](ID1451291486: Place[T0, Multiset[scala.Tuple2[Int, List[Int]]]])(
implicit ev0: Multiset[scala.Tuple2[Int, List[Int]]] => Traversable[T0]){

val net = CPNGraph()

// Places
val ID1451303483 = Place("ID1451303483", "Topology Changes", Queue((1,2)))
net.addPlace(ID1451303483)

// ========================================================================
// Generated code for transition Add Link and its arcs
// ========================================================================

case class BindingElement_ID1451310874(
	n1: Option[Int],
	n2: Option[Int],
	adjList1: Option[List[Int]],
	adjList2: Option[List[Int]]) extends BindingElement

val compatible_ID1451310874 = (b1: BindingElement_ID1451310874, b2: BindingElement_ID1451310874) =>
	(b1.n1 == b2.n1 || b1.n1 == None || b2.n1 == None) &&
	(b1.n2 == b2.n2 || b1.n2 == None || b2.n2 == None) &&
	(b1.adjList1 == b2.adjList1 || b1.adjList1 == None || b2.adjList1 == None) &&
	(b1.adjList2 == b2.adjList2 || b1.adjList2 == None || b2.adjList2 == None)

val merge_ID1451310874 = (b1: BindingElement_ID1451310874, b2: BindingElement_ID1451310874) => {
	val n1 = if (b1.n1 == None) b2.n1 else b1.n1
	val n2 = if (b1.n2 == None) b2.n2 else b1.n2
	val adjList1 = if (b1.adjList1 == None) b2.adjList1 else b1.adjList1
	val adjList2 = if (b1.adjList2 == None) b2.adjList2 else b1.adjList2
	new BindingElement_ID1451310874(n1, n2, adjList1, adjList2)
}

val fullBindingElement_ID1451310874 = (be: BindingElement_ID1451310874) =>
	be.n1 != None &&
	be.n2 != None &&
	be.adjList1 != None &&
	be.adjList2 != None

val guard_ID1451310874_0 = EvalGuard((be: BindingElement_ID1451310874) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) => {
		linkExists(scala.Tuple2(n1, adjList1), scala.Tuple2(n2, adjList2)).unary_$bang
	}
}, 0)
val guard_ID1451310874_1 = EvalGuard((be: BindingElement_ID1451310874) => () match {
	case () => {
		false
	}
}, 0)
val guard_ID1451310874 = List(guard_ID1451310874_0,guard_ID1451310874_1)

val enumBindings_ID1451310874 = List(
	)

val ID1451310874 = Transition("ID1451310874","Add Link",1,compatible_ID1451310874,merge_ID1451310874,fullBindingElement_ID1451310874,guard_ID1451310874,enumBindings_ID1451310874)
net.addTransition(ID1451310874)

// ------------------ Arcs ------------------

// Topology Changes ----> Add Link

val pattern_ID1451323812_0 = Pattern((token: Any) => token match {
	case scala.Tuple2((n1: Int), (n2: Int)) =>
		BindingElement_ID1451310874(Some(n1), Some(n2), None, None)
}, 0)
val eval_ID1451323812 = (be: BindingElement_ID1451310874) => (be.n1,be.n2) match {
	case (Some(n1),Some(n2)) =>
		scala.Tuple2(n1, n2)
}
val ID1451323812 = Arc("ID1451323812", ID1451303483, ID1451310874, In) (eval_ID1451323812)
net.addArc(ID1451323812)

// topology ----> Add Link

val pattern_ID1451352656_1 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n2: Int), (adjList2: List[Int]))) =>
		BindingElement_ID1451310874(None, Some(n2), None, Some(adjList2))
}, 0)
val pattern_ID1451352656_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n1: Int), (adjList1: List[Int]))) =>
		BindingElement_ID1451310874(Some(n1), None, Some(adjList1), None)
}, 0)
val eval_ID1451352656 = (be: BindingElement_ID1451310874) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) =>
		Multiset(scala.Tuple2(1, scala.Tuple2(n1, adjList1)), scala.Tuple2(1, scala.Tuple2(n2, adjList2)))
}
val ID1451352656 = Arc("ID1451352656", ID1451291486, ID1451310874, In) (eval_ID1451352656)
net.addArc(ID1451352656)

// Add Link ----> topology


val eval_ID1451348539 = (be: BindingElement_ID1451310874) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) =>
		Multiset[scala.Tuple2[Int, List[Int]]]((addLink(scala.Tuple2(n1, adjList1), scala.Tuple2(n2, adjList2)): _*))
}
val ID1451348539 = Arc("ID1451348539", ID1451291486, ID1451310874, Out) (eval_ID1451348539)
net.addArc(ID1451348539)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Add Link

ID1451310874.orderedPatternBindingBasis = List(
	ArcPattern(ID1451352656, pattern_ID1451352656_1),
	ArcPattern(ID1451352656, pattern_ID1451352656_0))



// ========================================================================
// Generated code for transition Remove Link and its arcs
// ========================================================================

case class BindingElement_ID1451312555(
	n1: Option[Int],
	n2: Option[Int],
	adjList1: Option[List[Int]],
	adjList2: Option[List[Int]]) extends BindingElement

val compatible_ID1451312555 = (b1: BindingElement_ID1451312555, b2: BindingElement_ID1451312555) =>
	(b1.n1 == b2.n1 || b1.n1 == None || b2.n1 == None) &&
	(b1.n2 == b2.n2 || b1.n2 == None || b2.n2 == None) &&
	(b1.adjList1 == b2.adjList1 || b1.adjList1 == None || b2.adjList1 == None) &&
	(b1.adjList2 == b2.adjList2 || b1.adjList2 == None || b2.adjList2 == None)

val merge_ID1451312555 = (b1: BindingElement_ID1451312555, b2: BindingElement_ID1451312555) => {
	val n1 = if (b1.n1 == None) b2.n1 else b1.n1
	val n2 = if (b1.n2 == None) b2.n2 else b1.n2
	val adjList1 = if (b1.adjList1 == None) b2.adjList1 else b1.adjList1
	val adjList2 = if (b1.adjList2 == None) b2.adjList2 else b1.adjList2
	new BindingElement_ID1451312555(n1, n2, adjList1, adjList2)
}

val fullBindingElement_ID1451312555 = (be: BindingElement_ID1451312555) =>
	be.n1 != None &&
	be.n2 != None &&
	be.adjList1 != None &&
	be.adjList2 != None

val guard_ID1451312555_0 = EvalGuard((be: BindingElement_ID1451312555) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) => {
		linkExists(scala.Tuple2(n1, adjList1), scala.Tuple2(n2, adjList2))
	}
}, 0)
val guard_ID1451312555_1 = EvalGuard((be: BindingElement_ID1451312555) => () match {
	case () => {
		false
	}
}, 0)
val guard_ID1451312555 = List(guard_ID1451312555_0,guard_ID1451312555_1)

val enumBindings_ID1451312555 = List(
	)

val ID1451312555 = Transition("ID1451312555","Remove Link",1,compatible_ID1451312555,merge_ID1451312555,fullBindingElement_ID1451312555,guard_ID1451312555,enumBindings_ID1451312555)
net.addTransition(ID1451312555)

// ------------------ Arcs ------------------

// Topology Changes ----> Remove Link

val pattern_ID1451341426_0 = Pattern((token: Any) => token match {
	case scala.Tuple2((n1: Int), (n2: Int)) =>
		BindingElement_ID1451312555(Some(n1), Some(n2), None, None)
}, 0)
val eval_ID1451341426 = (be: BindingElement_ID1451312555) => (be.n1,be.n2) match {
	case (Some(n1),Some(n2)) =>
		scala.Tuple2(n1, n2)
}
val ID1451341426 = Arc("ID1451341426", ID1451303483, ID1451312555, In) (eval_ID1451341426)
net.addArc(ID1451341426)

// topology ----> Remove Link

val pattern_ID1451349712_1 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n2: Int), (adjList2: List[Int]))) =>
		BindingElement_ID1451312555(None, Some(n2), None, Some(adjList2))
}, 0)
val pattern_ID1451349712_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n1: Int), (adjList1: List[Int]))) =>
		BindingElement_ID1451312555(Some(n1), None, Some(adjList1), None)
}, 0)
val eval_ID1451349712 = (be: BindingElement_ID1451312555) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) =>
		Multiset(scala.Tuple2(1, scala.Tuple2(n1, adjList1)), scala.Tuple2(1, scala.Tuple2(n2, adjList2)))
}
val ID1451349712 = Arc("ID1451349712", ID1451291486, ID1451312555, In) (eval_ID1451349712)
net.addArc(ID1451349712)

// Remove Link ----> topology


val eval_ID1451353077 = (be: BindingElement_ID1451312555) => (be.n1,be.adjList1,be.n2,be.adjList2) match {
	case (Some(n1),Some(adjList1),Some(n2),Some(adjList2)) =>
		Multiset[scala.Tuple2[Int, List[Int]]]((rmLink(scala.Tuple2(n1, adjList1), scala.Tuple2(n2, adjList2)): _*))
}
val ID1451353077 = Arc("ID1451353077", ID1451291486, ID1451312555, Out) (eval_ID1451353077)
net.addArc(ID1451353077)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Remove Link

ID1451312555.orderedPatternBindingBasis = List(
	ArcPattern(ID1451349712, pattern_ID1451349712_1),
	ArcPattern(ID1451349712, pattern_ID1451349712_0))



// Wire modules



}



// ########################################################################
// Generated code for module Timeouts
// ########################################################################
class Timeouts[T0](ID1467223847: Place[T0, Multiset[scala.Tuple3[Int, Int, RouteTable]]])(
implicit ev0: Multiset[scala.Tuple3[Int, Int, RouteTable]] => Traversable[T0]){

val net = CPNGraph()

// Places


// ========================================================================
// Generated code for transition Expire Source and its arcs
// ========================================================================

case class BindingElement_ID1470616173(
	node: Option[Int],
	sources: Option[SourceTable]) extends BindingElement

val compatible_ID1470616173 = (b1: BindingElement_ID1470616173, b2: BindingElement_ID1470616173) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.sources == b2.sources || b1.sources == None || b2.sources == None)

val merge_ID1470616173 = (b1: BindingElement_ID1470616173, b2: BindingElement_ID1470616173) => {
	val node = if (b1.node == None) b2.node else b1.node
	val sources = if (b1.sources == None) b2.sources else b1.sources
	new BindingElement_ID1470616173(node, sources)
}

val fullBindingElement_ID1470616173 = (be: BindingElement_ID1470616173) =>
	be.node != None &&
	be.sources != None


val guard_ID1470616173 = List(EvalGuard((be: BindingElement_ID1470616173) => false,0))

val enumBindings_ID1470616173 = List(
	)

val ID1470616173 = Transition("ID1470616173","Expire Source",1,compatible_ID1470616173,merge_ID1470616173,fullBindingElement_ID1470616173,guard_ID1470616173,enumBindings_ID1470616173)
net.addTransition(ID1470616173)

// ------------------ Arcs ------------------

// Source Table ----> Expire Source

val pattern_ID1470617742_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (sources: SourceTable))) =>
		BindingElement_ID1470616173(Some(node), Some(sources))
}, 0)
val eval_ID1470617742 = (be: BindingElement_ID1470616173) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources))
}
val ID1470617742 = Arc("ID1470617742", ID1430786899, ID1470616173, In) (eval_ID1470617742)
net.addArc(ID1470617742)

// Expire Source ----> Source Table


val eval_ID1470620565 = (be: BindingElement_ID1470616173) => (be.node,be.sources) match {
	case (Some(node),Some(sources)) =>
		scala.Tuple2(1, scala.Tuple2(node, sources.init))
}
val ID1470620565 = Arc("ID1470620565", ID1430786899, ID1470616173, Out) (eval_ID1470620565)
net.addArc(ID1470620565)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Expire Source

ID1470616173.orderedPatternBindingBasis = List(
	ArcPattern(ID1470617742, pattern_ID1470617742_0))



// ========================================================================
// Generated code for transition Timeout Request and its arcs
// ========================================================================

case class BindingElement_ID1470622574(
	node: Option[Int],
	preqs: Option[PendingReqTable]) extends BindingElement

val compatible_ID1470622574 = (b1: BindingElement_ID1470622574, b2: BindingElement_ID1470622574) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.preqs == b2.preqs || b1.preqs == None || b2.preqs == None)

val merge_ID1470622574 = (b1: BindingElement_ID1470622574, b2: BindingElement_ID1470622574) => {
	val node = if (b1.node == None) b2.node else b1.node
	val preqs = if (b1.preqs == None) b2.preqs else b1.preqs
	new BindingElement_ID1470622574(node, preqs)
}

val fullBindingElement_ID1470622574 = (be: BindingElement_ID1470622574) =>
	be.node != None &&
	be.preqs != None


val guard_ID1470622574 = List(EvalGuard((be: BindingElement_ID1470622574) => false,0))

val enumBindings_ID1470622574 = List(
	)

val ID1470622574 = Transition("ID1470622574","Timeout Request",1,compatible_ID1470622574,merge_ID1470622574,fullBindingElement_ID1470622574,guard_ID1470622574,enumBindings_ID1470622574)
net.addTransition(ID1470622574)

// ------------------ Arcs ------------------

// Pending Req Table ----> Timeout Request

val pattern_ID1470624297_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (preqs: PendingReqTable))) =>
		BindingElement_ID1470622574(Some(node), Some(preqs))
}, 0)
val eval_ID1470624297 = (be: BindingElement_ID1470622574) => (be.node,be.preqs) match {
	case (Some(node),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs))
}
val ID1470624297 = Arc("ID1470624297", ID1435604444, ID1470622574, In) (eval_ID1470624297)
net.addArc(ID1470624297)

// Timeout Request ----> Pending Req Table


val eval_ID1470626473 = (be: BindingElement_ID1470622574) => (be.node,be.preqs) match {
	case (Some(node),Some(preqs)) =>
		scala.Tuple2(1, scala.Tuple2(node, preqs.init))
}
val ID1470626473 = Arc("ID1470626473", ID1435604444, ID1470622574, Out) (eval_ID1470626473)
net.addArc(ID1470626473)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Timeout Request

ID1470622574.orderedPatternBindingBasis = List(
	ArcPattern(ID1470624297, pattern_ID1470624297_0))



// ========================================================================
// Generated code for transition Expire Route and its arcs
// ========================================================================

case class BindingElement_ID1470609302(
	node: Option[Int],
	routes: Option[RouteTable]) extends BindingElement

val compatible_ID1470609302 = (b1: BindingElement_ID1470609302, b2: BindingElement_ID1470609302) =>
	(b1.node == b2.node || b1.node == None || b2.node == None) &&
	(b1.routes == b2.routes || b1.routes == None || b2.routes == None)

val merge_ID1470609302 = (b1: BindingElement_ID1470609302, b2: BindingElement_ID1470609302) => {
	val node = if (b1.node == None) b2.node else b1.node
	val routes = if (b1.routes == None) b2.routes else b1.routes
	new BindingElement_ID1470609302(node, routes)
}

val fullBindingElement_ID1470609302 = (be: BindingElement_ID1470609302) =>
	be.node != None &&
	be.routes != None


val guard_ID1470609302 = List(EvalGuard((be: BindingElement_ID1470609302) => false,0))

val enumBindings_ID1470609302 = List(
	)

val ID1470609302 = Transition("ID1470609302","Expire Route",1,compatible_ID1470609302,merge_ID1470609302,fullBindingElement_ID1470609302,guard_ID1470609302,enumBindings_ID1470609302)
net.addTransition(ID1470609302)

// ------------------ Arcs ------------------

// Route Table ----> Expire Route

val pattern_ID1470612232_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((node: Int), (routes: RouteTable))) =>
		BindingElement_ID1470609302(Some(node), Some(routes))
}, 0)
val eval_ID1470612232 = (be: BindingElement_ID1470609302) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes))
}
val ID1470612232 = Arc("ID1470612232", ID1431111582, ID1470609302, In) (eval_ID1470612232)
net.addArc(ID1470612232)

// Expire Route ----> route update


val eval_ID1470610267 = (be: BindingElement_ID1470609302) => (be.routes,be.node) match {
	case (Some(routes),Some(node)) =>
		routes.triggerRouteSelection(node)
}
val ID1470610267 = Arc("ID1470610267", ID1467223847, ID1470609302, Out) (eval_ID1470610267)
net.addArc(ID1470610267)

// Expire Route ----> Route Table


val eval_ID1470611431 = (be: BindingElement_ID1470609302) => (be.node,be.routes) match {
	case (Some(node),Some(routes)) =>
		scala.Tuple2(1, scala.Tuple2(node, routes.expireRoute(node)))
}
val ID1470611431 = Arc("ID1470611431", ID1431111582, ID1470609302, Out) (eval_ID1470611431)
net.addArc(ID1470611431)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Expire Route

ID1470609302.orderedPatternBindingBasis = List(
	ArcPattern(ID1470612232, pattern_ID1470612232_0))



// Wire modules



}

//val startTimeMs = System.currentTimeMillis
//val startSystemTime = Benchmark.getSystemTime


val n = 100
val startUserTime = Benchmark.getUserTime
val startCpuTime = Benchmark.getCpuTime

val (c, becalc, stepTime) = AutomaticSimulator.run(net, n)
//val finalNet = Simulator.run(net,1000)
//val timeMs = System.currentTimeMillis - startTimeMs
//val timeSystem = Benchmark.getSystemTime - startSystemTime

val timeUser = Benchmark.getUserTime - startUserTime
val timeCpu = Benchmark.getCpuTime - startCpuTime
//println("Time ms: " + timeMs)
//println("Time system: " + timeSystem)
println("Time user: " + timeUser)
println("Time cpu: " + timeCpu)
println("Binding elements calculated: " + becalc + " in steps: " + c)
println("Calculated time: " + ((n.toDouble/becalc)*timeUser))
println("Step Time: " + stepTime)
//finalNet.allPlaces.foreach(p =>{
//  println(p.name)
//println(p.currentMarking)
//println
//})
}
