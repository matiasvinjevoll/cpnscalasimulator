package cpnmodel.gentest

import model.CPNGraph
import model.Place
import model.BindingElement
import model.BindGuard
import model.Transition
import model.Pattern
import collection.Multiset
import model.Arc
import model.ArcPattern
import simulator.Simulator
import model.Direction._
import simulator.Conversions._
import model.EvalGuard

object OPBBExample extends App {


val net = CPNGraph()

// Declarations

case class Packet(seqno: Int, data: String)
case class CInt(i: Int) {
    def inc = CInt(i + 1)
  }

// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1467279457 = Place("ID1467279457", "B", Multiset(CInt(1)))
net.addPlace(ID1467279457)

val ID1467279512 = Place("ID1467279512", "C", Multiset[Packet]())
net.addPlace(ID1467279512)

val ID1467279425 = Place("ID1467279425", "A", Multiset[(Int,String)]((1, "Col"), (2, "our"), (3, "ed ")))
net.addPlace(ID1467279425)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1467279439(
	n: Option[Int],
	o: Option[Int],
	e: Option[String],
	k: Option[Int],
	d: Option[String]) extends BindingElement

val compatible_ID1467279439 = (b1: BindingElement_ID1467279439, b2: BindingElement_ID1467279439) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.o == b2.o || b1.o == None || b2.o == None) &&
	(b1.e == b2.e || b1.e == None || b2.e == None) &&
	(b1.k == b2.k || b1.k == None || b2.k == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None)

val merge_ID1467279439 = (b1: BindingElement_ID1467279439, b2: BindingElement_ID1467279439) => {
	val n = if (b1.n == None) b2.n else b1.n
	val o = if (b1.o == None) b2.o else b1.o
	val e = if (b1.e == None) b2.e else b1.e
	val k = if (b1.k == None) b2.k else b1.k
	val d = if (b1.d == None) b2.d else b1.d
	new BindingElement_ID1467279439(n, o, e, k, d)
}

val fullBindingElement_ID1467279439 = (be: BindingElement_ID1467279439) =>
	be.n != None &&
	be.o != None &&
	be.e != None &&
	be.k != None &&
	be.d != None

val guard_ID1467279439_0 = BindGuard((be: BindingElement_ID1467279439) => (be.n) match {
	case (Some(n)) =>
		val k = n
		BindingElement_ID1467279439(be.n, be.o, be.e, Some(k), be.d)
}, 1)
val guard_ID1467279439_1 = EvalGuard((be: BindingElement_ID1467279439) => (be.n,be.k) match {
	case (Some(n),Some(k)) => {
		n.$eq$eq(k)
	}
}, 1)
val guard_ID1467279439 = List(guard_ID1467279439_0,guard_ID1467279439_1)

val enumBindings_ID1467279439 = List(
	)

val ID1467279439 = Transition("ID1467279439","T",2,compatible_ID1467279439,merge_ID1467279439,fullBindingElement_ID1467279439,guard_ID1467279439,enumBindings_ID1467279439)
net.addTransition(ID1467279439)

// ------------------ Arcs ------------------

// B ----> T

val pattern_ID1467279899_0 = Pattern((token: Any) => token match {
	case Tuple2(_, CInt((k: Int))) =>
		BindingElement_ID1467279439(None, None, None, Some(k), None)
}, 1)
val eval_ID1467279899 = (be: BindingElement_ID1467279439) => (be.k) match {
	case (Some(k)) =>
		CInt(k)
}
val ID1467279899 = Arc("ID1467279899", ID1467279457, ID1467279439, In) (eval_ID1467279899)
net.addArc(ID1467279899)

// A ----> T

val pattern_ID1467279791IN_1 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((o: Int), (e: String))) =>
		BindingElement_ID1467279439(None, Some(o), Some(e), None, None)
}, 0)
val pattern_ID1467279791IN_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n: Int), (d: String))) =>
		BindingElement_ID1467279439(Some(n), None, None, None, Some(d))
}, 1)
val eval_ID1467279791IN = (be: BindingElement_ID1467279439) => (be.n,be.d,be.o,be.e) match {
	case (Some(n),Some(d),Some(o),Some(e)) =>
		Multiset(scala.Tuple2(1, scala.Tuple2(n, d)), scala.Tuple2(1, scala.Tuple2(o, e)))
}
val ID1467279791IN = Arc("ID1467279791IN", ID1467279425, ID1467279439, In) (eval_ID1467279791IN)
net.addArc(ID1467279791IN)

// T ----> B


val eval_ID1467280429 = (be: BindingElement_ID1467279439) => (be.k) match {
	case (Some(k)) =>
		CInt(k).inc
}
val ID1467280429 = Arc("ID1467280429", ID1467279457, ID1467279439, Out) (eval_ID1467280429)
net.addArc(ID1467280429)

// T ----> C


val eval_ID1467280140 = (be: BindingElement_ID1467279439) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Multiset(scala.Tuple2(1, Packet(n, d)))
}
val ID1467280140 = Arc("ID1467280140", ID1467279512, ID1467279439, Out) (eval_ID1467280140)
net.addArc(ID1467280140)

// T ----> A


val eval_ID1467279791OUT = (be: BindingElement_ID1467279439) => (be.n,be.d,be.o,be.e) match {
	case (Some(n),Some(d),Some(o),Some(e)) =>
		Multiset(scala.Tuple2(1, scala.Tuple2(n, d)), scala.Tuple2(1, scala.Tuple2(o, e)))
}
val ID1467279791OUT = Arc("ID1467279791OUT", ID1467279425, ID1467279439, Out) (eval_ID1467279791OUT)
net.addArc(ID1467279791OUT)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1467279439.orderedPatternBindingBasis = List(
	ArcPattern(ID1467279791IN, pattern_ID1467279791IN_1),
	ArcPattern(ID1467279791IN, pattern_ID1467279791IN_0),
	guard_ID1467279439_0)



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.run(net)

}
