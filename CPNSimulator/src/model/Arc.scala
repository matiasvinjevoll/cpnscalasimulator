package model

// Direction of arc in regards to ´Transition´
object Direction extends Enumeration {
	  val In, Out = Value
	}

/**
 * An Arc connects a Transition to a Place or vice versa
 *
 * @param place
 * @param transition
 * @paran direction
 * @param evalExpr function evaluates the arc expression based on a binding element given as parameter.
 */
abstract class AbstractArc
case class Arc[Be <: BindingElement, A, Repr] (
  id: String,
  place: Place[A, Repr],
  transition: Transition[Be],
  direction: Direction.Value)
  (evalExpr: Be => Repr)
  extends AbstractArc with Element {

  override def toString = "place: " + place + " transition: " + transition
  
  def eval = evalExpr

}