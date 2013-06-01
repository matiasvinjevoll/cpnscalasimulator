package simulator

import model.CPNGraph
import util.Random
import model.Transition
import model.BindingElement

object Simulator {
  private def enabledTransitions(transitions: List[Transition[BindingElement]]) = {
    transitions.filter(transition => {
      val be = EnablingInference.enabledBindings(transition)
      be.length > 0
    })
  }

  def enabledBindings(transitions: List[Transition[BindingElement]]) = enabledTransitions(transitions).size > 0

  def run(net: CPNGraph, steps: Int = 50) = {
    
    val allTransitions = net.allTransitions
    
    println("---- Simulation ----")
    println("Initial marking:\n" + net.marking)
    println()
    var c = 0
    while (enabledBindings(allTransitions) && steps > c) {
//      println("---- New Occurrence ----")

      val transitions = enabledTransitions(allTransitions)

      println("Enabled transitions: " + transitions)
      val transition = Random.selectRandom(transitions)

      val bindingElements = EnablingInference.enabledBindings(transition)

      println("Chosen transition to occur: " + transition)

      println("Enabled Bindings for " + transition.name + ": ")
      bindingElements.foreach(b => println("\t" + b))

      val bindingElement = Random.selectRandom(bindingElements)
      println("Chosen binding: " + bindingElement)

      Occurrence.occur(transition, bindingElement)
      println("New marking:\n" + net.marking)
      println()
      c += 1
    }
    if (c == steps)
      println("---- " + steps + " completed ----")
    else
      println("---- No more enabled transitions ----")
    net
  }

  def readOption(max: Int): Int = try {
    readLine match {
      case "q" => -1
      case x =>
        val n = x.toInt
        if (n >= 0 && n <= max)
          n
        else {
          println(n + " is not a valid option, try again (q to quit):\n> ")
          readOption(max)
        }
    }
  } catch {
    case _ =>
      println("Invalid input, try again (q to quit):\n> ")
      readOption(max)
  }

  def runInteractive(net: CPNGraph) {
    
    val allTransitions = net.allTransitions
    
    println("---- Simulation ----\n")
    println("Initial marking:\n" + net.marking)
    println()
    var quit = false
    while (enabledBindings(allTransitions) && !quit) {
      println("---- New Occurrence ----")

      val transitions = enabledTransitions(allTransitions)

      println("\nChoose transition to occur (q to quit):\n")
      transitions.zipWithIndex.foreach {
        case (t, i) => println(i + ": " + t)
      }
      print("\n> ")
      readOption(transitions.length - 1) match {
        case -1 => quit = true
        case i =>
          val transition = transitions(i)

          val bindingElements = EnablingInference.enabledBindings(transition)

          println("\nChoose binding element to occur for " + transition.name + " (q to quit):\n")
          bindingElements.zipWithIndex.foreach {
            case (be, i) =>
              println(i + ": " + be)
          }
          print("\n> ")

          readOption(bindingElements.length - 1) match {
            case -1 => quit = true
            case i =>
              val bindingElement = bindingElements(i)

              Occurrence.occur(transition, bindingElement)
              println("\nNew marking:\n" + net.marking)
              println()
          }
      }

    }

    if (quit)
      println("---- Simulation ended: Stopped by user ----")
    else
      println("---- Simulation ended: No more enabled transitions ----")
  }
}