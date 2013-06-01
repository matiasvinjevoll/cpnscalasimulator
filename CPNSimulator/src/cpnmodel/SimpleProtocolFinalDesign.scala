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
import collection.Multiset
import simulator.AutomaticSimulator

object SimpleProtocolFinalDesign extends App {

val net = CPNGraph()


abstract class Packet
case class Data(n: Int, d: String) extends Packet
case class Ack(n: Int) extends Packet
case class No(n: Int) {
  def next = Multiset(No(n))
}




class Top(){

val net = CPNGraph()

val id44 = Place("id44", "Packets To Send", Multiset(
Data(1,"COL"),
Data(2,"OUR"),
Data(3,"ED "),
Data(4,"PET"),
Data(5,"RI "),
Data(6,"NET")))
net.addPlace(id44)

val id71 = Place("id71", "Received", Multiset(""))
net.addPlace(id71)

val id80 = Place("id80", "NextRec", Multiset(1))
net.addPlace(id80)

val id58 = Place("id58", "D", Multiset[Packet]())
net.addPlace(id58)

val id93 = Place("id93", "C", Multiset[Packet]())
net.addPlace(id93)

val id87 = Place("id87", "B", Multiset[Packet]())
net.addPlace(id87)

val id64 = Place("id64", "A", Multiset[Packet]())
net.addPlace(id64)

val id51 = Place("id51", "NextSend", Multiset(No(1)))
net.addPlace(id51)

case class BindingElement_id35(
	n: Option[Int],
	k: Option[Int]) extends BindingElement

val compatible_id35 = (b1: BindingElement_id35, b2: BindingElement_id35) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.k == b2.k || b1.k == None || b2.k == None)

val merge_id35 = (b1: BindingElement_id35, b2: BindingElement_id35) => {
	val n = if (b1.n == None) b2.n else b1.n
	val k = if (b1.k == None) b2.k else b1.k
	new BindingElement_id35(n, k)
}

val fullBindingElement_id35 = (be: BindingElement_id35) =>
	be.n != None &&
	be.k != None


val guard_id35 = List()

val enumBindings_id35 = List(
	)

val id35 = Transition("id35","ReceiveAck",2,compatible_id35,merge_id35,fullBindingElement_id35,guard_id35,enumBindings_id35)
net.addTransition(id35)

val pattern_id187_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Ack((n: Int))) =>
		BindingElement_id35(Some(n), None)
}, 1)
val eval_id187 = (be: BindingElement_id35) => (be.n) match {
	case (Some(n)) =>
		Ack(n)
}
val id187 = Arc("id187", id58, id35, In) (eval_id187)
net.addArc(id187)

val pattern_id193_0 = Pattern((token: Any) => token match {
	case Tuple2(_, No((k: Int))) =>
		BindingElement_id35(None, Some(k))
}, 0)
val eval_id193 = (be: BindingElement_id35) => (be.k) match {
	case (Some(k)) =>
		No(k)
}
val id193 = Arc("id193", id51, id35, In) (eval_id193)
net.addArc(id193)


val eval_id199 = (be: BindingElement_id35) => (be.n) match {
	case (Some(n)) =>
		No(n)
}
val id199 = Arc("id199", id51, id35, Out) (eval_id199)
net.addArc(id199)

id35.orderedPatternBindingBasis = List(
	ArcPattern(id193, pattern_id193_0),
	ArcPattern(id187, pattern_id187_0))



case class BindingElement_id23(
	success: Option[Boolean],
	packet: Option[Packet]) extends BindingElement

val compatible_id23 = (b1: BindingElement_id23, b2: BindingElement_id23) =>
	(b1.success == b2.success || b1.success == None || b2.success == None) &&
	(b1.packet == b2.packet || b1.packet == None || b2.packet == None)

val merge_id23 = (b1: BindingElement_id23, b2: BindingElement_id23) => {
	val success = if (b1.success == None) b2.success else b1.success
	val packet = if (b1.packet == None) b2.packet else b1.packet
	new BindingElement_id23(success, packet)
}

val fullBindingElement_id23 = (be: BindingElement_id23) =>
	be.success != None &&
	be.packet != None


val guard_id23 = List()

val enumBindings_id23 = List(
	List(BindingElement_id23(Some(true), None), BindingElement_id23(Some(false), None)))

val id23 = Transition("id23","Transmit Packet",1,compatible_id23,merge_id23,fullBindingElement_id23,guard_id23,enumBindings_id23)
net.addTransition(id23)

val pattern_id124_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (packet: Packet)) =>
		BindingElement_id23(None, Some(packet))
}, 0)
val eval_id124 = (be: BindingElement_id23) => (be.packet) match {
	case (Some(packet)) =>
		packet
}
val id124 = Arc("id124", id64, id23, In) (eval_id124)
net.addArc(id124)


val eval_id130 = (be: BindingElement_id23) => (be.success,be.packet) match {
	case (Some(success),Some(packet)) =>
		if (success)
  Multiset(packet)
else
  Multiset[Packet]()
}
val id130 = Arc("id130", id87, id23, Out) (eval_id130)
net.addArc(id130)

id23.orderedPatternBindingBasis = List(
	ArcPattern(id124, pattern_id124_0))



case class BindingElement_id39(
	success: Option[Boolean],
	packet: Option[Packet]) extends BindingElement

val compatible_id39 = (b1: BindingElement_id39, b2: BindingElement_id39) =>
	(b1.success == b2.success || b1.success == None || b2.success == None) &&
	(b1.packet == b2.packet || b1.packet == None || b2.packet == None)

val merge_id39 = (b1: BindingElement_id39, b2: BindingElement_id39) => {
	val success = if (b1.success == None) b2.success else b1.success
	val packet = if (b1.packet == None) b2.packet else b1.packet
	new BindingElement_id39(success, packet)
}

val fullBindingElement_id39 = (be: BindingElement_id39) =>
	be.success != None &&
	be.packet != None


val guard_id39 = List()

val enumBindings_id39 = List(
	List(BindingElement_id39(Some(true), None), BindingElement_id39(Some(false), None)))

val id39 = Transition("id39","TransmitAck",1,compatible_id39,merge_id39,fullBindingElement_id39,guard_id39,enumBindings_id39)
net.addTransition(id39)

val pattern_id163_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (packet: Packet)) =>
		BindingElement_id39(None, Some(packet))
}, 0)
val eval_id163 = (be: BindingElement_id39) => (be.packet) match {
	case (Some(packet)) =>
		packet
}
val id163 = Arc("id163", id93, id39, In) (eval_id163)
net.addArc(id163)


val eval_id181 = (be: BindingElement_id39) => (be.success,be.packet) match {
	case (Some(success),Some(packet)) =>
		if (success)
  Multiset(packet)
else
  Multiset[Packet]()
}
val id181 = Arc("id181", id58, id39, Out) (eval_id181)
net.addArc(id181)

id39.orderedPatternBindingBasis = List(
	ArcPattern(id163, pattern_id163_0))



case class BindingElement_id27(
	n: Option[Int],
	a: Option[Int],
	k: Option[Int],
	d: Option[String],
	data: Option[String]) extends BindingElement

val compatible_id27 = (b1: BindingElement_id27, b2: BindingElement_id27) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.a == b2.a || b1.a == None || b2.a == None) &&
	(b1.k == b2.k || b1.k == None || b2.k == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.data == b2.data || b1.data == None || b2.data == None)

val merge_id27 = (b1: BindingElement_id27, b2: BindingElement_id27) => {
	val n = if (b1.n == None) b2.n else b1.n
	val a = if (b1.a == None) b2.a else b1.a
	val k = if (b1.k == None) b2.k else b1.k
	val d = if (b1.d == None) b2.d else b1.d
	val data = if (b1.data == None) b2.data else b1.data
	new BindingElement_id27(n, a, k, d, data)
}

val fullBindingElement_id27 = (be: BindingElement_id27) =>
	be.n != None &&
	be.a != None &&
	be.k != None &&
	be.d != None &&
	be.data != None

val guard_id27_0 = EvalGuard((be: BindingElement_id27) => (be.n,be.k) match {
	case (Some(n),Some(k)) => {
		n.$eq$eq(k)
	}
}, 1)
val guard_id27_1 = BindGuard((be: BindingElement_id27) => (be.k) match {
	case (Some(k)) =>
		val a = k.$plus(1)
		BindingElement_id27(be.n, Some(a), be.k, be.d, be.data)
}, 2)
val guard_id27 = List(guard_id27_0,guard_id27_1)

val enumBindings_id27 = List(
	)

val id27 = Transition("id27","Receive Packet",3,compatible_id27,merge_id27,fullBindingElement_id27,guard_id27,enumBindings_id27)
net.addTransition(id27)

val pattern_id169_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (k: Int)) =>
		BindingElement_id27(None, None, Some(k), None, None)
}, 2)
val eval_id169 = (be: BindingElement_id27) => (be.k) match {
	case (Some(k)) =>
		k
}
val id169 = Arc("id169", id80, id27, In) (eval_id169)
net.addArc(id169)

val pattern_id145_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Data((n: Int), (d: String))) =>
		BindingElement_id27(Some(n), None, None, Some(d), None)
}, 1)
val eval_id145 = (be: BindingElement_id27) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id145 = Arc("id145", id87, id27, In) (eval_id145)
net.addArc(id145)

val pattern_id217_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (data: String)) =>
		BindingElement_id27(None, None, None, None, Some(data))
}, 0)
val eval_id217 = (be: BindingElement_id27) => (be.data) match {
	case (Some(data)) =>
		data
}
val id217 = Arc("id217", id71, id27, In) (eval_id217)
net.addArc(id217)


val eval_id157 = (be: BindingElement_id27) => (be.a) match {
	case (Some(a)) =>
		Ack(a)
}
val id157 = Arc("id157", id93, id27, Out) (eval_id157)
net.addArc(id157)


val eval_id151 = (be: BindingElement_id27) => (be.n,be.k,be.data,be.d) match {
	case (Some(n),Some(k),Some(data),Some(d)) =>
		if (n.$eq$eq(k))
  data.$plus(d)
else
  data
}
val id151 = Arc("id151", id71, id27, Out) (eval_id151)
net.addArc(id151)


val eval_id175 = (be: BindingElement_id27) => (be.k) match {
	case (Some(k)) =>
		k.$plus(1)
}
val id175 = Arc("id175", id80, id27, Out) (eval_id175)
net.addArc(id175)

id27.orderedPatternBindingBasis = List(
	ArcPattern(id217, pattern_id217_0),
	ArcPattern(id145, pattern_id145_0),
	ArcPattern(id169, pattern_id169_0),
	guard_id27_1)



case class BindingElement_id19(
	n: Option[Int],
	d: Option[String]) extends BindingElement

val compatible_id19 = (b1: BindingElement_id19, b2: BindingElement_id19) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None)

val merge_id19 = (b1: BindingElement_id19, b2: BindingElement_id19) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	new BindingElement_id19(n, d)
}

val fullBindingElement_id19 = (be: BindingElement_id19) =>
	be.n != None &&
	be.d != None


val guard_id19 = List()

val enumBindings_id19 = List(
	)

val id19 = Transition("id19","Send Packet",1,compatible_id19,merge_id19,fullBindingElement_id19,guard_id19,enumBindings_id19)
net.addTransition(id19)

val pattern_id136IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Data((n: Int), (d: String))) =>
		BindingElement_id19(Some(n), Some(d))
}, 0)
val eval_id136IN = (be: BindingElement_id19) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id136IN = Arc("id136IN", id44, id19, In) (eval_id136IN)
net.addArc(id136IN)


val eval_ID1470809943 = (be: BindingElement_id19) => (be.n) match {
	case (Some(n)) =>
		No(n).next
}
val ID1470809943 = Arc("ID1470809943", id51, id19, In) (eval_ID1470809943)
net.addArc(ID1470809943)


val eval_id118 = (be: BindingElement_id19) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id118 = Arc("id118", id64, id19, Out) (eval_id118)
net.addArc(id118)


val eval_ID1467300343 = (be: BindingElement_id19) => (be.n) match {
	case (Some(n)) =>
		No(n)
}
val ID1467300343 = Arc("ID1467300343", id51, id19, Out) (eval_ID1467300343)
net.addArc(ID1467300343)


val eval_id136OUT = (be: BindingElement_id19) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id136OUT = Arc("id136OUT", id44, id19, Out) (eval_id136OUT)
net.addArc(id136OUT)

id19.orderedPatternBindingBasis = List(
	ArcPattern(id136IN, pattern_id136IN_0))



case class BindingElement_ID1467255679(
	n: Option[Int],
	d: Option[String],
	k: Option[Int]) extends BindingElement

val compatible_ID1467255679 = (b1: BindingElement_ID1467255679, b2: BindingElement_ID1467255679) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.k == b2.k || b1.k == None || b2.k == None)

val merge_ID1467255679 = (b1: BindingElement_ID1467255679, b2: BindingElement_ID1467255679) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	val k = if (b1.k == None) b2.k else b1.k
	new BindingElement_ID1467255679(n, d, k)
}

val fullBindingElement_ID1467255679 = (be: BindingElement_ID1467255679) =>
	be.n != None &&
	be.d != None &&
	be.k != None

val guard_ID1467255679_0 = EvalGuard((be: BindingElement_ID1467255679) => (be.n,be.k) match {
	case (Some(n),Some(k)) => {
		n.$bang$eq(k)
	}
}, 1)
val guard_ID1467255679 = List(guard_ID1467255679_0)

val enumBindings_ID1467255679 = List(
	)

val ID1467255679 = Transition("ID1467255679","Discard Packet",2,compatible_ID1467255679,merge_ID1467255679,fullBindingElement_ID1467255679,guard_ID1467255679,enumBindings_ID1467255679)
net.addTransition(ID1467255679)

val pattern_ID1467257828_0 = Pattern((token: Any) => token match {
	case Tuple2(_, Data((n: Int), (d: String))) =>
		BindingElement_ID1467255679(Some(n), Some(d), None)
}, 1)
val eval_ID1467257828 = (be: BindingElement_ID1467255679) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val ID1467257828 = Arc("ID1467257828", id87, ID1467255679, In) (eval_ID1467257828)
net.addArc(ID1467257828)

val pattern_ID1467256281IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (k: Int)) =>
		BindingElement_ID1467255679(None, None, Some(k))
}, 0)
val eval_ID1467256281IN = (be: BindingElement_ID1467255679) => (be.k) match {
	case (Some(k)) =>
		k
}
val ID1467256281IN = Arc("ID1467256281IN", id80, ID1467255679, In) (eval_ID1467256281IN)
net.addArc(ID1467256281IN)


val eval_ID1467268435 = (be: BindingElement_ID1467255679) => (be.k) match {
	case (Some(k)) =>
		Ack(k)
}
val ID1467268435 = Arc("ID1467268435", id93, ID1467255679, Out) (eval_ID1467268435)
net.addArc(ID1467268435)


val eval_ID1467256281OUT = (be: BindingElement_ID1467255679) => (be.k) match {
	case (Some(k)) =>
		k
}
val ID1467256281OUT = Arc("ID1467256281OUT", id80, ID1467255679, Out) (eval_ID1467256281OUT)
net.addArc(ID1467256281OUT)

ID1467255679.orderedPatternBindingBasis = List(
	ArcPattern(ID1467256281IN, pattern_ID1467256281IN_0),
	ArcPattern(ID1467257828, pattern_ID1467257828_0))






}

val top0 = new Top() 
net.addSubstitutionTransition(top0.net)

Simulator.run(net)

}
