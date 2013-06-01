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

object NonPatternInputArcExample extends App {


val net = CPNGraph()

// Declarations

case class Packet(n:Int, d:String) {
  def foo = Packet(n,d)
}

// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1470721224 = Place("ID1470721224", "C", Multiset("Col","our"))
net.addPlace(ID1470721224)

val ID1470721122 = Place("ID1470721122", "B", Multiset(1))
net.addPlace(ID1470721122)

val ID1470721032 = Place("ID1470721032", "A", Multiset(Packet(1,"Col"), Packet(2,"our")))
net.addPlace(ID1470721032)

val ID1470721588 = Place("ID1470721588", "D", Multiset[Packet]())
net.addPlace(ID1470721588)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1470720922(
	n: Option[Int],
	d: Option[String]) extends BindingElement

val compatible_ID1470720922 = (b1: BindingElement_ID1470720922, b2: BindingElement_ID1470720922) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None)

val merge_ID1470720922 = (b1: BindingElement_ID1470720922, b2: BindingElement_ID1470720922) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	new BindingElement_ID1470720922(n, d)
}

val fullBindingElement_ID1470720922 = (be: BindingElement_ID1470720922) =>
	be.n != None &&
	be.d != None


val guard_ID1470720922 = List()

val enumBindings_ID1470720922 = List(
	)

val ID1470720922 = Transition("ID1470720922","T",2,compatible_ID1470720922,merge_ID1470720922,fullBindingElement_ID1470720922,guard_ID1470720922,enumBindings_ID1470720922)
net.addTransition(ID1470720922)

// ------------------ Arcs ------------------

// B ----> T

val pattern_ID1470722472_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (n: Int)) =>
		BindingElement_ID1470720922(Some(n), None)
}, 1)
val eval_ID1470722472 = (be: BindingElement_ID1470720922) => (be.n) match {
	case (Some(n)) =>
		n
}
val ID1470722472 = Arc("ID1470722472", ID1470721122, ID1470720922, In) (eval_ID1470722472)
net.addArc(ID1470722472)

// C ----> T

val pattern_ID1470722688_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (d: String)) =>
		BindingElement_ID1470720922(None, Some(d))
}, 0)
val eval_ID1470722688 = (be: BindingElement_ID1470720922) => (be.d) match {
	case (Some(d)) =>
		d
}
val ID1470722688 = Arc("ID1470722688", ID1470721224, ID1470720922, In) (eval_ID1470722688)
net.addArc(ID1470722688)

// A ----> T


val eval_ID1470722174 = (be: BindingElement_ID1470720922) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Packet(n, d).foo
}
val ID1470722174 = Arc("ID1470722174", ID1470721032, ID1470720922, In) (eval_ID1470722174)
net.addArc(ID1470722174)

// T ----> B


val eval_ID1470723909 = (be: BindingElement_ID1470720922) => (be.n) match {
	case (Some(n)) =>
		n.$plus(1)
}
val ID1470723909 = Arc("ID1470723909", ID1470721122, ID1470720922, Out) (eval_ID1470723909)
net.addArc(ID1470723909)

// T ----> D


val eval_ID1470721747 = (be: BindingElement_ID1470720922) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		Packet(n, d)
}
val ID1470721747 = Arc("ID1470721747", ID1470721588, ID1470720922, Out) (eval_ID1470721747)
net.addArc(ID1470721747)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1470720922.orderedPatternBindingBasis = List(
	ArcPattern(ID1470722688, pattern_ID1470722688_0),
	ArcPattern(ID1470722472, pattern_ID1470722472_0))



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.runInteractive(net)

}
