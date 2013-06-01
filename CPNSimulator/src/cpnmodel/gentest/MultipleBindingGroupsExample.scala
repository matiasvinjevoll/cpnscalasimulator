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

object MultipleBindingGroupsExample extends App {


val net = CPNGraph()

// Declarations



// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1467353847 = Place("ID1467353847", "B", Multiset(1,2,3))
net.addPlace(ID1467353847)

val ID1467354227 = Place("ID1467354227", "C", Multiset[(Int,String)]())
net.addPlace(ID1467354227)

val ID1467353817 = Place("ID1467353817", "A", Multiset("en", "to", "tre"))
net.addPlace(ID1467353817)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1467353802(
	n: Option[Int],
	s: Option[String]) extends BindingElement

val compatible_ID1467353802 = (b1: BindingElement_ID1467353802, b2: BindingElement_ID1467353802) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.s == b2.s || b1.s == None || b2.s == None)

val merge_ID1467353802 = (b1: BindingElement_ID1467353802, b2: BindingElement_ID1467353802) => {
	val n = if (b1.n == None) b2.n else b1.n
	val s = if (b1.s == None) b2.s else b1.s
	new BindingElement_ID1467353802(n, s)
}

val fullBindingElement_ID1467353802 = (be: BindingElement_ID1467353802) =>
	be.n != None &&
	be.s != None


val guard_ID1467353802 = List()

val enumBindings_ID1467353802 = List(
	)

val ID1467353802 = Transition("ID1467353802","T",2,compatible_ID1467353802,merge_ID1467353802,fullBindingElement_ID1467353802,guard_ID1467353802,enumBindings_ID1467353802)
net.addTransition(ID1467353802)

// ------------------ Arcs ------------------

// B ----> T

val pattern_ID1467354081_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (n: Int)) =>
		BindingElement_ID1467353802(Some(n), None)
}, 1)
val eval_ID1467354081 = (be: BindingElement_ID1467353802) => (be.n) match {
	case (Some(n)) =>
		n
}
val ID1467354081 = Arc("ID1467354081", ID1467353847, ID1467353802, In) (eval_ID1467354081)
net.addArc(ID1467354081)

// A ----> T

val pattern_ID1467354134_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (s: String)) =>
		BindingElement_ID1467353802(None, Some(s))
}, 0)
val eval_ID1467354134 = (be: BindingElement_ID1467353802) => (be.s) match {
	case (Some(s)) =>
		s
}
val ID1467354134 = Arc("ID1467354134", ID1467353817, ID1467353802, In) (eval_ID1467354134)
net.addArc(ID1467354134)

// T ----> C


val eval_ID1467354344 = (be: BindingElement_ID1467353802) => (be.n,be.s) match {
	case (Some(n),Some(s)) =>
		scala.Tuple2(n, s)
}
val ID1467354344 = Arc("ID1467354344", ID1467354227, ID1467353802, Out) (eval_ID1467354344)
net.addArc(ID1467354344)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1467353802.orderedPatternBindingBasis = List(
	ArcPattern(ID1467354134, pattern_ID1467354134_0),
	ArcPattern(ID1467354081, pattern_ID1467354081_0))



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.runInteractive(net)

}
