package model

/**
 * A Transition
 *
 * Takes a number of functions as parameter specific to
 * the transition to be instantiated
 *
 * @param id of the transition
 * @param name of the transition
 * @param bindingGroups: number of binding groups of the transition
 * @param compatibleBindingElements function determines whether two binding elements are compatible
 * @param mergeBindingElements function takes two binding elements and
 * merges them
 * @param fullBindingElement function determines whether a binding element is full
 * @param guard: list of guards
 * the resulting binding element
 * @param enumBindings list of partial binding elements generated from free variables of type boolean and enumeration
 */

case class Transition[Be <: BindingElement](
  id: String,
  name: String,
  bindingGroups: Int,
  compatibleBindingElements: (Be, Be) => Boolean,
  mergeBindingElements: (Be, Be) => Be,
  fullBindingElement: Be => Boolean,
  guards: List[Guard[Be]],
  enumBindings: List[Iterable[Be]]) extends Element with Node {

  var in = List[Arc[Be, _, _]]()
  var out = List[Arc[Be, _, _]]()
  var orderedPatternBindingBasis = List[PatternBindingBasisObject[Be]]()

  def addIn(arc: Arc[Be, _, _]) {
    in = arc :: in
  }

  def addOut(arc: Arc[Be, _, _]) {
    out = arc :: out
  }

  // TODO: Make the removal of an element in lists of arcs (in, out) more efficient
  def removeIn(arc: Arc[_, _, _]) {
    in = in.filterNot(_ == arc)
  }

  def removeOut(arc: Arc[_, _, _]) {
    out = out.filterNot(_ == arc)
  }
}