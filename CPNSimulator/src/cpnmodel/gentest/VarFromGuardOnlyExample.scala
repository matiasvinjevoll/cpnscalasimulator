package cpnmodel.gentest

import model.CPNGraph
import model.Place
import model.BindingElement
import model.BindGuard
import model.Transition
import model.Pattern
import model.Arc
import model.ArcPattern
import simulator.Simulator
import model.Direction._
import simulator.Conversions._
import collection.Multiset

object VarFromGuardOnlyExample  extends App {

  
val net = CPNGraph()

// Declarations

case class Packet(n: Int, d: String)

// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1467353819 = Place("ID1467353819", "A", Multiset((1,(1,"COL"))))
net.addPlace(ID1467353819)

val ID1467353904 = Place("ID1467353904", "B", Multiset[Packet]())
net.addPlace(ID1467353904)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1467353855(
	n: Option[Int],
	d: Option[String],
	p: Option[Packet]) extends BindingElement

val compatible_ID1467353855 = (b1: BindingElement_ID1467353855, b2: BindingElement_ID1467353855) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.p == b2.p || b1.p == None || b2.p == None)

val merge_ID1467353855 = (b1: BindingElement_ID1467353855, b2: BindingElement_ID1467353855) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	val p = if (b1.p == None) b2.p else b1.p
	new BindingElement_ID1467353855(n, d, p)
}

val fullBindingElement_ID1467353855 = (be: BindingElement_ID1467353855) =>
	be.n != None &&
	be.d != None &&
	be.p != None

val guard_ID1467353855_0 = BindGuard((be: BindingElement_ID1467353855) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		val p = Packet(n, d)
		BindingElement_ID1467353855(be.n, be.d, Some(p))
}, 0)
val guard_ID1467353855 = List(guard_ID1467353855_0)

val enumBindings_ID1467353855 = List(
	)

val ID1467353855 = Transition("ID1467353855","T",1,compatible_ID1467353855,merge_ID1467353855,fullBindingElement_ID1467353855,guard_ID1467353855,enumBindings_ID1467353855)
net.addTransition(ID1467353855)

// ------------------ Arcs ------------------

// A ----> T

val pattern_ID1467353973_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n: Int), (d: String))) =>
		BindingElement_ID1467353855(Some(n), Some(d), None)
}, 0)
val eval_ID1467353973 = (be: BindingElement_ID1467353855) => (be.n,be.d) match {
	case (Some(n),Some(d)) =>
		scala.Tuple2(1, scala.Tuple2(n, d))
}
val ID1467353973 = Arc("ID1467353973", ID1467353819, ID1467353855, In) (eval_ID1467353973)
net.addArc(ID1467353973)

// T ----> B


val eval_ID1467354017 = (be: BindingElement_ID1467353855) => (be.p) match {
	case (Some(p)) =>
		p
}
val ID1467354017 = Arc("ID1467354017", ID1467353904, ID1467353855, Out) (eval_ID1467354017)
net.addArc(ID1467354017)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1467353855.orderedPatternBindingBasis = List(
	ArcPattern(ID1467353973, pattern_ID1467353973_0),
	guard_ID1467353855_0)



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.run(net)

}
