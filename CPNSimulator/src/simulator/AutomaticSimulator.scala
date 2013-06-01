package simulator
import model.CPNGraph
import model.Transition
import model.BindingElement
import util.Random
import scala.collection.mutable.HashMap
import util.Benchmark

object AutomaticSimulator {

  def run(net: CPNGraph, steps: Int = 50) = {
    // cannot use transition id as key in map because there may be
    // multiple instances of a transition, hence, the same id
    val indexedTransitions = net.allTransitions.zipWithIndex.map{
      case (t,index) => (t.id+index, t)
    }
    val unknowns = HashMap(indexedTransitions:_*)
    val disabled = HashMap[String, Transition[BindingElement]]()
    var c = 0
    var becalc = 0
    var stepTime: Long = 0
    while (!unknowns.isEmpty && steps > becalc) {

      val (tId, transition) = Random.selectRandom(unknowns)

      val startCpuTime = Benchmark.getCpuTime
      val bindingElements = EnablingInference.enabledBindings(transition)

      //val beslen = bindingElements.length 
      if (bindingElements isEmpty) {
        // In this case, the chosen transition is disabled and is
        // removed from unknowns and added to disabled
        unknowns.remove(tId) match {
          case Some(t) => disabled.put(tId, t)
        }
      } else {
        c += 1
        becalc += bindingElements.length
        // Else, the transition occurs

        val bindingElement = Random.selectRandom(bindingElements)
        Occurrence.occur(transition, bindingElement)
        stepTime += (Benchmark.getCpuTime - startCpuTime)

        // Move potential enabled transitions from disabled to unknown
        transition.out.foreach(toutarc => {
          toutarc.place.out.foreach(poutarc => {
            val candidate = poutarc.transition
            disabled.find{
              case (id, t) => t eq candidate
            } match {
              case Some((id, t)) =>
                disabled -= id
                unknowns += (id -> t)
              case None =>
            }
          })
        })
        
      }

//      println("\nNew marking:\n" + net.marking)
//      println()

    }
    (c ,becalc, stepTime)
  }
  /*
  def runRandom(net: CPNGraph, steps: Int = 50) = {
    // cannot use transition id as key in map because there may be
    // multiple instances of a transition, hence, the same id
    val indexedTransitions = net.allTransitions.zipWithIndex.map{
      case (t,index) => (t.id+index, t)
    }
    val unknowns = HashMap(indexedTransitions:_*)
    val disabled = HashMap[String, Transition[BindingElement]]()
    var c = 0
    while (unknowns.size > 0 && steps > c) {

      val (tId, transition) = Random.selectRandom(unknowns)

      val bindingElement = EnablingInference.enabledBinding(transition)

      bindingElement match {
        case None =>
        // In this case, the chosen transition is disabled and is
        // removed from unknowns and added to disabled
        unknowns.remove(tId) match {
          case Some(t) => disabled.put(tId, t)
        }
      
        case Some(be) =>
        c += 1
        // Else, the transition occurs

        Occurrence.occur(transition, be)

        // Move potential enabled transitions from disabled to unknown
        transition.out.foreach(toutarc => {
          toutarc.place.out.foreach(poutarc => {
            val candidate = poutarc.transition
            disabled.find{
              case (id, t) => t eq candidate
            } match {
              case Some((id, t)) =>
                disabled -= id
                unknowns += (id -> t)
              case None =>
            }
          })
        })
      }
    }
    c
  }*/
}