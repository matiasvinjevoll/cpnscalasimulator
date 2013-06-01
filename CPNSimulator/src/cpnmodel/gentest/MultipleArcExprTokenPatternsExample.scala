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
import scala.collection.immutable.Queue
import simulator.CollectionExtensions._

object MultipleArcExprTokenPatternsExample extends App {


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
val ID1467276591 = Place("ID1467276591", "B", Queue[(Int,String)]())
net.addPlace(ID1467276591)

val ID1467274787 = Place("ID1467274787", "A", Multiset[(Int,String)]((1, "Col"), (2, "our"), (3, "ed "),(4,"pet")))
net.addPlace(ID1467274787)

val ID1467277149 = Place("ID1467277149", "C", Multiset[Packet]())
net.addPlace(ID1467277149)

// ========================================================================
// Generated code for transition T2 and its arcs
// ========================================================================

case class BindingElement_ID1467277169(
	n: Option[Int],
	d: Option[String],
	m: Option[Int],
	e: Option[String]) extends BindingElement

val compatible_ID1467277169 = (b1: BindingElement_ID1467277169, b2: BindingElement_ID1467277169) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.m == b2.m || b1.m == None || b2.m == None) &&
	(b1.e == b2.e || b1.e == None || b2.e == None)

val merge_ID1467277169 = (b1: BindingElement_ID1467277169, b2: BindingElement_ID1467277169) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	val m = if (b1.m == None) b2.m else b1.m
	val e = if (b1.e == None) b2.e else b1.e
	new BindingElement_ID1467277169(n, d, m, e)
}

val fullBindingElement_ID1467277169 = (be: BindingElement_ID1467277169) =>
	be.n != None &&
	be.d != None &&
	be.m != None &&
	be.e != None


val guard_ID1467277169 = List()

val enumBindings_ID1467277169 = List(
	)

val ID1467277169 = Transition("ID1467277169","T2",2,compatible_ID1467277169,merge_ID1467277169,fullBindingElement_ID1467277169,guard_ID1467277169,enumBindings_ID1467277169)
net.addTransition(ID1467277169)

// ------------------ Arcs ------------------

// B ----> T2

val pattern_ID1467277364_1 = Pattern((token: Any) => token match {
	case scala.Tuple2((m: Int), (e: String)) =>
		BindingElement_ID1467277169(None, None, Some(m), Some(e))
}, 0)
val pattern_ID1467277364_0 = Pattern((token: Any) => token match {
	case scala.Tuple2((n: Int), (d: String)) =>
		BindingElement_ID1467277169(Some(n), Some(d), None, None)
}, 1)
val eval_ID1467277364 = (be: BindingElement_ID1467277169) => (be.n,be.d,be.m,be.e) match {
	case (Some(n),Some(d),Some(m),Some(e)) =>
		Queue(scala.Tuple2(n, d), scala.Tuple2(m, e))
}
val ID1467277364 = Arc("ID1467277364", ID1467276591, ID1467277169, In) (eval_ID1467277364)
net.addArc(ID1467277364)

// T2 ----> C


val eval_ID1467277595 = (be: BindingElement_ID1467277169) => (be.n,be.d,be.m,be.e) match {
	case (Some(n),Some(d),Some(m),Some(e)) =>
		Multiset(Packet(n, d), Packet(m, e))
}
val ID1467277595 = Arc("ID1467277595", ID1467277149, ID1467277169, Out) (eval_ID1467277595)
net.addArc(ID1467277595)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T2

ID1467277169.orderedPatternBindingBasis = List(
	ArcPattern(ID1467277364, pattern_ID1467277364_1),
	ArcPattern(ID1467277364, pattern_ID1467277364_0))



// ========================================================================
// Generated code for transition T1 and its arcs
// ========================================================================

case class BindingElement_ID1467274801(
	n: Option[Int],
	d: Option[String],
	m: Option[Int],
	e: Option[String]) extends BindingElement

val compatible_ID1467274801 = (b1: BindingElement_ID1467274801, b2: BindingElement_ID1467274801) =>
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None) &&
	(b1.m == b2.m || b1.m == None || b2.m == None) &&
	(b1.e == b2.e || b1.e == None || b2.e == None)

val merge_ID1467274801 = (b1: BindingElement_ID1467274801, b2: BindingElement_ID1467274801) => {
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	val m = if (b1.m == None) b2.m else b1.m
	val e = if (b1.e == None) b2.e else b1.e
	new BindingElement_ID1467274801(n, d, m, e)
}

val fullBindingElement_ID1467274801 = (be: BindingElement_ID1467274801) =>
	be.n != None &&
	be.d != None &&
	be.m != None &&
	be.e != None


val guard_ID1467274801 = List()

val enumBindings_ID1467274801 = List(
	)

val ID1467274801 = Transition("ID1467274801","T1",2,compatible_ID1467274801,merge_ID1467274801,fullBindingElement_ID1467274801,guard_ID1467274801,enumBindings_ID1467274801)
net.addTransition(ID1467274801)

// ------------------ Arcs ------------------

// A ----> T1

val pattern_ID1467274828_1 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((m: Int), (e: String))) =>
		BindingElement_ID1467274801(None, None, Some(m), Some(e))
}, 0)
val pattern_ID1467274828_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n: Int), (d: String))) =>
		BindingElement_ID1467274801(Some(n), Some(d), None, None)
}, 1)
val eval_ID1467274828 = (be: BindingElement_ID1467274801) => (be.n,be.d,be.m,be.e) match {
	case (Some(n),Some(d),Some(m),Some(e)) =>
		Multiset(scala.Tuple2(1, scala.Tuple2(n, d)), scala.Tuple2(1, scala.Tuple2(m, e)))
}
val ID1467274828 = Arc("ID1467274828", ID1467274787, ID1467274801, In) (eval_ID1467274828)
net.addArc(ID1467274828)

// T1 ----> B


val eval_ID1467276810 = (be: BindingElement_ID1467274801) => (be.n,be.d,be.m,be.e) match {
	case (Some(n),Some(d),Some(m),Some(e)) =>
		Queue(scala.Tuple2(n, d), scala.Tuple2(m, e))
}
val ID1467276810 = Arc("ID1467276810", ID1467276591, ID1467274801, Out) (eval_ID1467276810)
net.addArc(ID1467276810)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T1

ID1467274801.orderedPatternBindingBasis = List(
	ArcPattern(ID1467274828, pattern_ID1467274828_1),
	ArcPattern(ID1467274828, pattern_ID1467274828_0))



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.run(net)

}
