package simulator
import scala.collection.TraversableLike
import scala.collection.generic.CanBuildFrom
import util.Random
import model.Transition
import model.CPNGraph
import model.BindingElement
import model.Arc
import model.Guard
import model.BindGuard
import model.EvalGuard
import model.TransitionAction
import model.ArcPattern
import model.Pattern

/**
 * An object containing generic implementation of enabling inference for any generated transition.
 */
object EnablingInference {

  // TODO: Not working. Must check if there are enough tokens on input places and backtrack if not.
  def enabledBinding[Be <: BindingElement](transition: Transition[Be]): Option[Be] = {

    val C = enablingInference(transition)

    var guards = Array.fill[List[EvalGuard[Be]]](transition.bindingGroups)(List[EvalGuard[Be]]())
    transition.guards.foreach {
      case eg @ EvalGuard(_, bg) => guards(bg) = eg :: guards(bg)
      case _ =>
    }

    def findPbe(pbes: List[Be], bg: Int): Option[Be] = {
      if (pbes isEmpty)
        None
      else {
        val (pb, rest) = Random.removeRandom(pbes)
        if (guards(bg).forall(g => g.eval(pb)))
          Some(pb)
        else
        	findPbe(rest, bg)
      }
    }
    
    val pbes = for(i <- 0 until C.length) yield findPbe(C(i), i)
    
    def mergeBindingElements(pbes: List[Option[Be]]) = pbes match {
      case Some(be) :: xs => mergeBindingElementsRec(xs, be)
      case _ => None
    }
    
    def mergeBindingElementsRec(pbes: List[Option[Be]], temp: Be): Option[Be] = pbes match {
      case Nil =>
        if (transition.fullBindingElement(temp))
          Some(temp)
          else None
      case Some(be) :: xs =>
        mergeBindingElementsRec(xs, transition.mergeBindingElements(be,temp))
      case _ => None
    }

    mergeBindingElements(pbes.toList)
  }

  def enabledBindings[Be <: BindingElement](transition: Transition[Be]): List[Be] = {
    val merge = mergeBindingElements(
      transition.compatibleBindingElements,
      transition.mergeBindingElements) _

    val C = enablingInference(transition)

    // Merging binding groups to obtain full binding elements
    // (except values of type Boolean or some Enumeration)
    var bindingElements = C./:(List[Be]())(merge(_, _))

    // If there are some binding elements after merging of binding
    // groups, then those binding elements must be merged with
    // enumBindings.
    if (bindingElements.length > 0)
      transition.enumBindings.foreach(eb =>
        bindingElements = merge(bindingElements, eb.toList))

    // In many situations there will be duplcate (full) binding elements.
    // I.e. in the MultipleArcExprTokenPatternsExample, when B occurs with
    // the marking Queue((1,"Col"),(1,"Col")), the two patterns on the output
    // arc will be bound to each token, and merging will give four binding elements
    // with the same values.
    // This should maybe be handled in some way before this point for efficiency.
    bindingElements = bindingElements.distinct

    bindingElements = bindingElements.filter(transition.fullBindingElement(_))

    // Check that guard is not violated
    // Suffices to check those guard conjuncts which were not part of the
    // ordered pattern binding basis.
    // TODO: What about actions?
    transition.guards foreach {
      case EvalGuard(eval, bg) => bindingElements = bindingElements.filter(be => eval(be))
      case BindGuard(_, _) =>
      // All bind guards should be in the OPBB
    }

    // Check whether there are sufficient tokens on input places
    // Must check all input places even though related arc expr is
    // part of PBB, in case of multiple patterns of a given arc expr.
    bindingElements = bindingElements filter (binding =>
      transition.in forall (arc =>
        arc.place.hasTokens(arc.eval(binding))))

    bindingElements
  }

  /**
   * Implements the inference algorithm.
   */
  def enablingInference[Be <: BindingElement](transition: Transition[Be]) = {

    val merge = mergeBindingElements(
      transition.compatibleBindingElements,
      transition.mergeBindingElements) _

    // Array containing a list for each binding group to hold partial binding elements
    var C = Array.fill[List[Be]](transition.bindingGroups)(List[Be]())

    transition.orderedPatternBindingBasis.foreach {
      case ArcPattern(Arc(_, place, _, _), Pattern(partialBind, bindingGroup)) =>
        val Cm = place.pb(partialBind)
        C(bindingGroup) = merge(C(bindingGroup), Cm)

      case BindGuard(bind, bindingGroup) =>
        val Cm = C(bindingGroup).collect {
          // This will discard partial binding elements where
          // bound variables are altered by a guard conjunct
          case be if {
            try {
              val bound = bind(be)
              transition.compatibleBindingElements(bound, be)
            } catch {
              case _: MatchError => false
            }
          } => bind(be) // TODO: Called second time, should be avoided
        }
        C(bindingGroup) = merge(C(bindingGroup), Cm)
    }

    C
  }

  /**
   * Merges all binding elements of list ´bindingElements2´
   * with binding elements of ´bindingElements1´. Adapted
   * only to work with OPBB: if a binding element ´b2´ in
   * ´bindingElements2´ is not compatible with any binding
   * element ´b1´ in ´bindingElements1´, ´b2´will not be added
   * to the result. Further, if a binding element ´b1´ in
   * ´bindingElements1´ is not compatible with any binding
   * element ´b2´ in ´bindingElements2´, ´b1´ will not be
   * added to the result, since it will then not be possible
   * to achieve a full binding element based on ´b1´.
   */
  private def mergeBindingElements[B <: BindingElement](
    compatible: (B, B) => Boolean, merge: (B, B) => B)(bes1: List[B], bes2: List[B]) =
    if (bes1.isEmpty || bes2.isEmpty)
      bes1 ::: bes2
    else
      bes2.flatMap(b2 => bes1.collect { case b1 if compatible(b1, b2) => merge(b1, b2) })
}

