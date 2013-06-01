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
import util.Benchmark
import simulator.AutomaticSimulator

object SimpleProtocol extends App {

val net = CPNGraph()

// Declarations

abstract class Packet
case class Data(n: Int, d: String) extends Packet
case class Ack(n: Int) extends Packet
case class No(n: Int) {
  def next = Multiset(No(n+1))
}

// ########################################################################
// Generated code for module Top
// ########################################################################
class Top(){

val net = CPNGraph()

// Places
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

val id58 = Place("id58", "D", Queue[Ack]())
net.addPlace(id58)

val id93 = Place("id93", "C", Multiset[Ack]())
net.addPlace(id93)

val id87 = Place("id87", "B", Queue[Data]())
net.addPlace(id87)

val id64 = Place("id64", "A", Multiset[Data]())
net.addPlace(id64)

val id51 = Place("id51", "NextSend", Multiset(1))
net.addPlace(id51)

// ========================================================================
// Generated code for transition ReceiveAck and its arcs
// ========================================================================

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

// ------------------ Arcs ------------------

// D ----> ReceiveAck

val pattern_id187_0 = Pattern((token: Any) => token match {
	case Ack((n: Int)) =>
		BindingElement_id35(Some(n), None)
}, 1)
val eval_id187 = (be: BindingElement_id35) => (be.n) match {
	case (Some(n)) =>
		Ack(n)
}
val id187 = Arc("id187", id58, id35, In) (eval_id187)
net.addArc(id187)

// NextSend ----> ReceiveAck

val pattern_id193_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (k: Int)) =>
		BindingElement_id35(None, Some(k))
}, 0)
val eval_id193 = (be: BindingElement_id35) => (be.k) match {
	case (Some(k)) =>
		k
}
val id193 = Arc("id193", id51, id35, In) (eval_id193)
net.addArc(id193)

// ReceiveAck ----> NextSend


val eval_id199 = (be: BindingElement_id35) => (be.n) match {
	case (Some(n)) =>
		n
}
val id199 = Arc("id199", id51, id35, Out) (eval_id199)
net.addArc(id199)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for ReceiveAck

id35.orderedPatternBindingBasis = List(
	ArcPattern(id193, pattern_id193_0),
	ArcPattern(id187, pattern_id187_0))



// ========================================================================
// Generated code for transition Receive Packet and its arcs
// ========================================================================

case class BindingElement_id27(
	n: Option[Int],
	d: Option[String],
	k: Option[Int],
	data: Option[String]) extends BindingElement

val compatible_id27 = (b1: BindingElement_id27, b2: BindingElement_id27) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.k == b2.k || b1.k == None || b2.k == None) &&
	(b1.data == b2.data || b1.data == None || b2.data == None)

val merge_id27 = (b1: BindingElement_id27, b2: BindingElement_id27) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	val k = if (b1.k == None) b2.k else b1.k
	val data = if (b1.data == None) b2.data else b1.data
	new BindingElement_id27(n, d, k, data)
}

val fullBindingElement_id27 = (be: BindingElement_id27) =>
	be.n != None &&
	be.d != None &&
	be.k != None &&
	be.data != None


val guard_id27 = List()

val enumBindings_id27 = List(
	)

val id27 = Transition("id27","Receive Packet",3,compatible_id27,merge_id27,fullBindingElement_id27,guard_id27,enumBindings_id27)
net.addTransition(id27)

// ------------------ Arcs ------------------

// NextRec ----> Receive Packet

val pattern_id169_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (k: Int)) =>
		BindingElement_id27(None, None, Some(k), None)
}, 2)
val eval_id169 = (be: BindingElement_id27) => (be.k) match {
	case (Some(k)) =>
		k
}
val id169 = Arc("id169", id80, id27, In) (eval_id169)
net.addArc(id169)

// B ----> Receive Packet

val pattern_id145_0 = Pattern((token: Any) => token match {
	case Data((n: Int), (d: String)) =>
		BindingElement_id27(Some(n), Some(d), None, None)
}, 1)
val eval_id145 = (be: BindingElement_id27) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id145 = Arc("id145", id87, id27, In) (eval_id145)
net.addArc(id145)

// Received ----> Receive Packet

val pattern_id217_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (data: String)) =>
		BindingElement_id27(None, None, None, Some(data))
}, 0)
val eval_id217 = (be: BindingElement_id27) => (be.data) match {
	case (Some(data)) =>
		data
}
val id217 = Arc("id217", id71, id27, In) (eval_id217)
net.addArc(id217)

// Receive Packet ----> C


val eval_id157 = (be: BindingElement_id27) => (be.n,be.k) match {
	case (Some(n),Some(k)) =>
		if (n.$eq$eq(k))
  Ack(k.$plus(1))
else
  Ack(k)
}
val id157 = Arc("id157", id93, id27, Out) (eval_id157)
net.addArc(id157)

// Receive Packet ----> Received


val eval_id151 = (be: BindingElement_id27) => (be.n,be.k,be.data,be.d) match {
	case (Some(n),Some(k),Some(data),Some(d)) =>
		if (n.$eq$eq(k))
  Multiset(data.$plus(d))
else
  Multiset(data)
}
val id151 = Arc("id151", id71, id27, Out) (eval_id151)
net.addArc(id151)

// Receive Packet ----> NextRec


val eval_id175 = (be: BindingElement_id27) => (be.n,be.k) match {
	case (Some(n),Some(k)) =>
		if (n.$eq$eq(k))
  k.$plus(1)
else
  k
}
val id175 = Arc("id175", id80, id27, Out) (eval_id175)
net.addArc(id175)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Receive Packet

id27.orderedPatternBindingBasis = List(
	ArcPattern(id217, pattern_id217_0),
	ArcPattern(id145, pattern_id145_0),
	ArcPattern(id169, pattern_id169_0))



// ========================================================================
// Generated code for transition Send Packet and its arcs
// ========================================================================

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

// ------------------ Arcs ------------------

// Packets To Send ----> Send Packet

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

// NextSend ----> Send Packet

val pattern_id211IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (n: Int)) =>
		BindingElement_id19(Some(n), None)
}, 0)
val eval_id211IN = (be: BindingElement_id19) => (be.n) match {
	case (Some(n)) =>
		n
}
val id211IN = Arc("id211IN", id51, id19, In) (eval_id211IN)
net.addArc(id211IN)

// Send Packet ----> A


val eval_id118 = (be: BindingElement_id19) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id118 = Arc("id118", id64, id19, Out) (eval_id118)
net.addArc(id118)

// Send Packet ----> NextSend


val eval_id211OUT = (be: BindingElement_id19) => (be.n) match {
	case (Some(n)) =>
		n
}
val id211OUT = Arc("id211OUT", id51, id19, Out) (eval_id211OUT)
net.addArc(id211OUT)

// Send Packet ----> Packets To Send


val eval_id136OUT = (be: BindingElement_id19) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Data(n, d)
}
val id136OUT = Arc("id136OUT", id44, id19, Out) (eval_id136OUT)
net.addArc(id136OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Send Packet

id19.orderedPatternBindingBasis = List(
	ArcPattern(id136IN, pattern_id136IN_0))



// Wire modules
val st0 = new Network(id64, id87, id58, id93) 
net.addSubstitutionTransition(st0.net)


}

val top0 = new Top() 
net.addSubstitutionTransition(top0.net)

// ########################################################################
// Generated code for module Transmit[T]
// ########################################################################
class Transmit[T, T0, T1](ID1470600535: Place[T1, Multiset[T]], ID1470600775: Place[T0, Queue[T]])(
implicit ev0: Queue[T] => Traversable[T0], ev1: Multiset[T] => Traversable[T1]){

val net = CPNGraph()

// Places
val ID1470668325 = Place("ID1470668325", "Hm", Multiset(1))
net.addPlace(ID1470668325)

// ========================================================================
// Generated code for transition Transmit and its arcs
// ========================================================================

case class BindingElement_ID1470601027(
	success: Option[Boolean],
	a: Option[Int],
	p: Option[T]) extends BindingElement

val compatible_ID1470601027 = (b1: BindingElement_ID1470601027, b2: BindingElement_ID1470601027) =>
	(b1.success == b2.success || b1.success == None || b2.success == None) &&
	(b1.a == b2.a || b1.a == None || b2.a == None) &&
	(b1.p == b2.p || b1.p == None || b2.p == None)

val merge_ID1470601027 = (b1: BindingElement_ID1470601027, b2: BindingElement_ID1470601027) => {
	val success = if (b1.success == None) b2.success else b1.success
	val a = if (b1.a == None) b2.a else b1.a
	val p = if (b1.p == None) b2.p else b1.p
	new BindingElement_ID1470601027(success, a, p)
}

val fullBindingElement_ID1470601027 = (be: BindingElement_ID1470601027) =>
	be.success != None &&
	be.a != None &&
	be.p != None


val guard_ID1470601027 = List()

val enumBindings_ID1470601027 = List(
	List(BindingElement_ID1470601027(Some(true), None, None), BindingElement_ID1470601027(Some(false), None, None)))

val ID1470601027 = Transition("ID1470601027","Transmit",2,compatible_ID1470601027,merge_ID1470601027,fullBindingElement_ID1470601027,guard_ID1470601027,enumBindings_ID1470601027)
net.addTransition(ID1470601027)

// ------------------ Arcs ------------------

// Hm ----> Transmit

val pattern_ID1470671605IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (a: Int)) =>
		BindingElement_ID1470601027(None, Some(a), None)
}, 1)
val eval_ID1470671605IN = (be: BindingElement_ID1470601027) => (be.a) match {
	case (Some(a)) =>
		a
}
val ID1470671605IN = Arc("ID1470671605IN", ID1470668325, ID1470601027, In) (eval_ID1470671605IN)
net.addArc(ID1470671605IN)

// in ----> Transmit

val pattern_ID1470601570_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (p: T)) =>
		BindingElement_ID1470601027(None, None, Some(p))
}, 0)
val eval_ID1470601570 = (be: BindingElement_ID1470601027) => (be.p) match {
	case (Some(p)) =>
		p
}
val ID1470601570 = Arc("ID1470601570", ID1470600535, ID1470601027, In) (eval_ID1470601570)
net.addArc(ID1470601570)

// Transmit ----> Hm


val eval_ID1470671605OUT = (be: BindingElement_ID1470601027) => (be.a) match {
	case (Some(a)) =>
		a+1
}
val ID1470671605OUT = Arc("ID1470671605OUT", ID1470668325, ID1470601027, Out) (eval_ID1470671605OUT)
net.addArc(ID1470671605OUT)

// Transmit ----> out


val eval_ID1470601722 = (be: BindingElement_ID1470601027) => (be.success,be.p) match {
	case (Some(success),Some(p)) =>
		if (success)
  {
    val x$1 = p;
    Nil.$colon$colon(x$1)
  }
else
  Nil
}
val ID1470601722 = Arc("ID1470601722", ID1470600775, ID1470601027, Out) (eval_ID1470601722)
net.addArc(ID1470601722)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for Transmit

ID1470601027.orderedPatternBindingBasis = List(
	ArcPattern(ID1470601570, pattern_ID1470601570_0),
	ArcPattern(ID1470671605IN, pattern_ID1470671605IN_0))



// Wire modules



}



// ########################################################################
// Generated code for module Network[A,B]
// ########################################################################
class Network[A, B, T0, T1, T2, T3](ID1467354690: Place[T3, Multiset[A]], ID1467354875: Place[T1, Queue[A]], ID1467355087: Place[T2, Queue[B]], ID1467355326: Place[T0, Multiset[B]])(
implicit ev0: Multiset[B] => Traversable[T0], ev1: Queue[A] => Traversable[T1], ev2: Queue[B] => Traversable[T2], ev3: Multiset[A] => Traversable[T3]){

val net = CPNGraph()

// Places




// Wire modules
val st0 = new Transmit(ID1467354690, ID1467354875) 
net.addSubstitutionTransition(st0.net)

val st1 = new Transmit(ID1467355326, ID1467355087) 
net.addSubstitutionTransition(st1.net)


}


val startTimeMs = System.currentTimeMillis
val startSystemTime = Benchmark.getSystemTime
val startUserTime = Benchmark.getUserTime
val startCpuTime = Benchmark.getCpuTime
AutomaticSimulator.run(net)
val timeMs = System.currentTimeMillis - startTimeMs
val timeSystem = Benchmark.getSystemTime - startSystemTime
val timeUser = Benchmark.getUserTime - startUserTime
val timeCpu = Benchmark.getCpuTime - startCpuTime
println("Time ms: " + timeMs)
println("Time system: " + timeSystem)
println("Time user: " + timeUser)
println("Time cpu: " + timeCpu)

}
