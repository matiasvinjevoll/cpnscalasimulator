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

object PatternCoefficientExample extends App {

val net = CPNGraph()

// Declarations

val P_HIGH = 100;
val P_NORMAL = 1000;
val P_LOW = 10000;

// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1467279529 = Place("ID1467279529", "A", Multiset((2,(1, "Col")), (1,(2, "our")), (2,(3, "ed ")),(1,(4,"pet"))))
net.addPlace(ID1467279529)

val ID1467279481 = Place("ID1467279481", "B", Multiset((1,2)))
net.addPlace(ID1467279481)

val ID1467279589 = Place("ID1467279589", "C", Multiset[(Int,String)]())
net.addPlace(ID1467279589)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1467279425(
	k: Option[Int],
	n: Option[Int],
	d: Option[String]) extends BindingElement

val compatible_ID1467279425 = (b1: BindingElement_ID1467279425, b2: BindingElement_ID1467279425) =>
	(b1.k == b2.k || b1.k == None || b2.k == None) &&
	(b1.n == b2.n || b1.n == None || b2.n == None) &&
	(b1.d == b2.d || b1.d == None || b2.d == None)

val merge_ID1467279425 = (b1: BindingElement_ID1467279425, b2: BindingElement_ID1467279425) => {
	val k = if (b1.k == None) b2.k else b1.k
	val n = if (b1.n == None) b2.n else b1.n
	val d = if (b1.d == None) b2.d else b1.d
	new BindingElement_ID1467279425(k, n, d)
}

val fullBindingElement_ID1467279425 = (be: BindingElement_ID1467279425) =>
	be.k != None &&
	be.n != None &&
	be.d != None

val guard_ID1467279425_0 = EvalGuard((be: BindingElement_ID1467279425) => (be.k) match {
	case (Some(k)) => {
		k.$greater(0)
	}
}, 1)
val guard_ID1467279425 = List(guard_ID1467279425_0)

val enumBindings_ID1467279425 = List(
	)

val ID1467279425 = Transition("ID1467279425","T",2,compatible_ID1467279425,merge_ID1467279425,fullBindingElement_ID1467279425,guard_ID1467279425,enumBindings_ID1467279425)
net.addTransition(ID1467279425)

// ------------------ Arcs ------------------

// B ----> T

val pattern_ID1467279950_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, (k: Int)) =>
		BindingElement_ID1467279425(Some(k), None, None)
}, 1)
val eval_ID1467279950 = (be: BindingElement_ID1467279425) => (be.k) match {
	case (Some(k)) =>
		Multiset(scala.Tuple2(1, k))
}
val ID1467279950 = Arc("ID1467279950", ID1467279481, ID1467279425, In) (eval_ID1467279950)
net.addArc(ID1467279950)

// A ----> T

val pattern_ID1467279885_0 = Pattern((token: Any) => token match {
	case scala.Tuple2(_, scala.Tuple2((n: Int), (d: String))) =>
		BindingElement_ID1467279425(None, Some(n), Some(d))
}, 0)
val eval_ID1467279885 = (be: BindingElement_ID1467279425) => (be.k,be.n,be.d) match {
	case (Some(k),Some(n),Some(d)) =>
		Multiset(scala.Tuple2(k, scala.Tuple2(n, d)))
}
val ID1467279885 = Arc("ID1467279885", ID1467279529, ID1467279425, In) (eval_ID1467279885)
net.addArc(ID1467279885)

// T ----> C


val eval_ID1467280018 = (be: BindingElement_ID1467279425) => (be.k,be.n,be.d) match {
	case (Some(k),Some(n),Some(d)) =>
		Multiset(scala.Tuple2(k, scala.Tuple2(n, d)))
}
val ID1467280018 = Arc("ID1467280018", ID1467279589, ID1467279425, Out) (eval_ID1467280018)
net.addArc(ID1467280018)

// T ----> B


val eval_ID1467280444 = (be: BindingElement_ID1467279425) => (be.k) match {
	case (Some(k)) =>
		Multiset(scala.Tuple2(1, k.$minus(1)))
}
val ID1467280444 = Arc("ID1467280444", ID1467279481, ID1467279425, Out) (eval_ID1467280444)
net.addArc(ID1467280444)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1467279425.orderedPatternBindingBasis = List(
	ArcPattern(ID1467279885, pattern_ID1467279885_0),
	ArcPattern(ID1467279950, pattern_ID1467279950_0))



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.run(net)

}
