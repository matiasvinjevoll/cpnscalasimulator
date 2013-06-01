package simulator

import model.Transition
import model.BindingElement
import model.CPNGraph
import model.Arc
import scala.reflect.runtime.universe._

object Occurrence {

  /**
   * Occurrence function for generation of the marking resulting from the
   * occurrence of an given transition that are enabled in a given marking.
   */
  def occur[Be <: BindingElement](
    transition: Transition[Be],
    //net: CPNGraph,
    bindingElement: Be) = {

    // Remove tokens from input places
    transition.in foreach (arc =>
      arc.place.removeTokens(arc.eval(bindingElement)))

    // Add tokens to output places
    transition.out foreach(arc =>
      arc.place.addTokens(arc.eval(bindingElement)))
  }
  
}