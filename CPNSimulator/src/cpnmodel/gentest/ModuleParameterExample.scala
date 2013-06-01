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

object ModuleParameterExample extends App {


val net = CPNGraph()

// Declarations



// Global Places



// ########################################################################
// Generated code for module Top
// ########################################################################
class Top(){

val net = CPNGraph()

// Places
val ID1470722564 = Place("ID1470722564", "B", Multiset[(String,Int)]())
net.addPlace(ID1470722564)

val ID1470720731 = Place("ID1470720731", "A", Multiset("en","to","tre"))
net.addPlace(ID1470720731)



// Wire modules
val st0 = new cSubPage(ID1470720731, ID1470722564) 
net.addSubstitutionTransition(st0.net)


}

val top0 = new Top() 
net.addSubstitutionTransition(top0.net)

// ########################################################################
// Generated code for module cSubPage[T]
// ########################################################################
class cSubPage[T, T0, T1](ID1470720874: Place[T1, Multiset[T]], ID1470721115: Place[T0, Multiset[scala.Tuple2[T, Int]]])(
implicit ev0: Multiset[scala.Tuple2[T, Int]] => Traversable[T0], ev1: Multiset[T] => Traversable[T1]){

val net = CPNGraph()

// Places
val ID1470726618 = Place("ID1470726618", "C", Multiset(1))
net.addPlace(ID1470726618)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1470720940(
	t: Option[T],
	n: Option[Int]) extends BindingElement

val compatible_ID1470720940 = (b1: BindingElement_ID1470720940, b2: BindingElement_ID1470720940) =>
	(b1.t == b2.t || b1.t == None || b2.t == None) &&
	(b1.n == b2.n || b1.n == None || b2.n == None)

val merge_ID1470720940 = (b1: BindingElement_ID1470720940, b2: BindingElement_ID1470720940) => {
	val t = if (b1.t == None) b2.t else b1.t
	val n = if (b1.n == None) b2.n else b1.n
	new BindingElement_ID1470720940(t, n)
}

val fullBindingElement_ID1470720940 = (be: BindingElement_ID1470720940) =>
	be.t != None &&
	be.n != None

val guard_ID1470720940_0 = EvalGuard((be: BindingElement_ID1470720940) => (be.n) match {
	case (Some(n)) => {
		n.$less(10)
	}
}, 1)
val guard_ID1470720940 = List(guard_ID1470720940_0)

val enumBindings_ID1470720940 = List(
	)

val ID1470720940 = Transition("ID1470720940","T",2,compatible_ID1470720940,merge_ID1470720940,fullBindingElement_ID1470720940,guard_ID1470720940,enumBindings_ID1470720940)
net.addTransition(ID1470720940)

// ------------------ Arcs ------------------

// C ----> T

val pattern_ID1470727011_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (n: Int)) =>
		BindingElement_ID1470720940(None, Some(n))
}, 1)
val eval_ID1470727011 = (be: BindingElement_ID1470720940) => (be.n) match {
	case (Some(n)) =>
		n
}
val ID1470727011 = Arc("ID1470727011", ID1470726618, ID1470720940, In) (eval_ID1470727011)
net.addArc(ID1470727011)

// a ----> T

val pattern_ID1470721028IN_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (t: T)) =>
		BindingElement_ID1470720940(Some(t), None)
}, 0)
val eval_ID1470721028IN = (be: BindingElement_ID1470720940) => (be.t) match {
	case (Some(t)) =>
		t
}
val ID1470721028IN = Arc("ID1470721028IN", ID1470720874, ID1470720940, In) (eval_ID1470721028IN)
net.addArc(ID1470721028IN)

// T ----> C


val eval_ID1470727217 = (be: BindingElement_ID1470720940) => (be.n) match {
	case (Some(n)) =>
		n.$plus(1)
}
val ID1470727217 = Arc("ID1470727217", ID1470726618, ID1470720940, Out) (eval_ID1470727217)
net.addArc(ID1470727217)

// T ----> a


val eval_ID1470721028OUT = (be: BindingElement_ID1470720940) => (be.t) match {
	case (Some(t)) =>
		t
}
val ID1470721028OUT = Arc("ID1470721028OUT", ID1470720874, ID1470720940, Out) (eval_ID1470721028OUT)
net.addArc(ID1470721028OUT)

// T ----> b


val eval_ID1470721226 = (be: BindingElement_ID1470720940) => (be.t,be.n) match {
	case (Some(t),Some(n)) =>
		scala.Tuple2(t, n)
}
val ID1470721226 = Arc("ID1470721226", ID1470721115, ID1470720940, Out) (eval_ID1470721226)
net.addArc(ID1470721226)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1470720940.orderedPatternBindingBasis = List(
	ArcPattern(ID1470721028IN, pattern_ID1470721028IN_0),
	ArcPattern(ID1470727011, pattern_ID1470727011_0))



// Wire modules



}



Simulator.runInteractive(net)

}
