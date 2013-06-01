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

object EnumExample extends App {


val net = CPNGraph()

// Declarations

object Transmit extends Enumeration {
  val Single, Duplicate = Value
}

// Global Places



// ########################################################################
// Generated code for module NewPage
// ########################################################################
class NewPage(){

val net = CPNGraph()

// Places
val ID1467285393 = Place("ID1467285393", "A", Multiset("a","b","c"))
net.addPlace(ID1467285393)

val ID1467285410 = Place("ID1467285410", "B", Multiset[String]())
net.addPlace(ID1467285410)

// ========================================================================
// Generated code for transition T and its arcs
// ========================================================================

case class BindingElement_ID1467285374(
	s: Option[String],
	success: Option[Boolean],
	transmit: Option[Transmit.Value]) extends BindingElement

val compatible_ID1467285374 = (b1: BindingElement_ID1467285374, b2: BindingElement_ID1467285374) =>
	(b1.s == b2.s || b1.s == None || b2.s == None) &&
	(b1.success == b2.success || b1.success == None || b2.success == None) &&
	(b1.transmit == b2.transmit || b1.transmit == None || b2.transmit == None)

val merge_ID1467285374 = (b1: BindingElement_ID1467285374, b2: BindingElement_ID1467285374) => {
	val s = if (b1.s == None) b2.s else b1.s
	val success = if (b1.success == None) b2.success else b1.success
	val transmit = if (b1.transmit == None) b2.transmit else b1.transmit
	new BindingElement_ID1467285374(s, success, transmit)
}

val fullBindingElement_ID1467285374 = (be: BindingElement_ID1467285374) =>
	be.s != None &&
	be.success != None &&
	be.transmit != None


val guard_ID1467285374 = List()

import Transmit._
val enumBindings_ID1467285374 = List(
	List(BindingElement_ID1467285374(None, Some(true), None), BindingElement_ID1467285374(None, Some(false), None)),
	List(BindingElement_ID1467285374(None, None, Some(Single)), BindingElement_ID1467285374(None, None, Some(Duplicate))))

val ID1467285374 = Transition("ID1467285374","T",1,compatible_ID1467285374,merge_ID1467285374,fullBindingElement_ID1467285374,guard_ID1467285374,enumBindings_ID1467285374)
net.addTransition(ID1467285374)

// ------------------ Arcs ------------------

// A ----> T

val pattern_ID1467285737_0 = Pattern((token: Any) => token match {
	case Tuple2(_, (s: String)) =>
		BindingElement_ID1467285374(Some(s), None, None)
}, 0)
val eval_ID1467285737 = (be: BindingElement_ID1467285374) => (be.s) match {
	case (Some(s)) =>
		s
}
val ID1467285737 = Arc("ID1467285737", ID1467285393, ID1467285374, In) (eval_ID1467285737)
net.addArc(ID1467285737)

// T ----> B


val eval_ID1467285796 = (be: BindingElement_ID1467285374) => (be.success,be.transmit,be.s) match {
	case (Some(success),Some(transmit),Some(s)) =>
		if (success)
  if (transmit.$eq$eq(Duplicate))
    {
      val x$1 = scala.Tuple2(2, s);
      Nil.$colon$colon(x$1)
    }
  else
    {
      val x$2 = scala.Tuple2(1, s);
      Nil.$colon$colon(x$2)
    }
else
  Nil
}
val ID1467285796 = Arc("ID1467285796", ID1467285410, ID1467285374, Out) (eval_ID1467285796)
net.addArc(ID1467285796)



// ------------------ /Arcs -----------------

// Set ordered pattern binding basis for T

ID1467285374.orderedPatternBindingBasis = List(
	ArcPattern(ID1467285737, pattern_ID1467285737_0))



// Wire modules



}

val newpage0 = new NewPage() 
net.addSubstitutionTransition(newpage0.net)

Simulator.run(net)

}
