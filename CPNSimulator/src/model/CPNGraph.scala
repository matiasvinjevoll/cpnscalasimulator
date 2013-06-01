package model
import scala.collection.mutable.HashMap

class CPNGraph {
  val places = HashMap[String, Place[_,_]]()
  val transitions = HashMap[String, Transition[BindingElement]]()
	var arcs = List[Arc[_,_,_]]()
	var substitutionTransitions = List[CPNGraph]()
	
	def allTransitions: List[Transition[BindingElement]] =  transitions.values.toList ::: substitutionTransitions.flatMap(_.allTransitions)
	def allPlaces: List[Place[_,_]] =  places.values.toList ::: substitutionTransitions.flatMap(_.allPlaces)
	def allArcs: List[Arc[_,_,_]] =  arcs ::: substitutionTransitions.flatMap(_.allArcs)
	
	def addSubstitutionTransition(st: CPNGraph) = substitutionTransitions = st :: substitutionTransitions
	
	override def toString = {
	  "Places: " + places + "\n" +
	  "Transitions: " + transitions + "\n" +
	  "Arcs: " + arcs
	}
	
	def marking = (for (p <- allPlaces)
	  yield "%-20s: %s" format (p.name, p.currentMarking)).mkString("\n")

	
	def addPlace(place: Place[_,_]) = {
	  places += (place.id -> place)
	}
	
	def removePlace(place: Place[_,_]) {
	  import Direction._
	  places.remove(place.id)
	  arcs = arcs.filterNot(a => {
	   if (a.place.id == place.id) {
	     if (a.direction == In)
	       a.transition.removeIn(a)
	     else a.transition.removeOut(a)
	     true
	   } else false
	  })
	}
	
  def addTransition[Be <: BindingElement](transition: Transition[Be]) = {
    transition match {
      case t: Transition[BindingElement] => transitions += (t.id -> t)
    }
  }
  
  def removeTransition(transition: Transition[_]) {
    transitions.remove(transition.id)
    arcs = arcs.filterNot(a => a.transition.id == transition.id)
  }
	
	def addArc[Be <: BindingElement] (arc: Arc[Be,_,_]) {
	  //places(arc.place.id) // will cause exception if the place is not in the CPNGraph.
	  //transitions(arc.transition.id) // will cause exception if the transition is not in the CPNGraph.
	  	
	  
	  import Direction._
	  if(arc.direction == In){
	  	arc.transition.addIn(arc)
	  	arc.place.addOut(arc)
	  }
	  else{
	  	arc.transition.addOut(arc)
	  	arc.place.addIn(arc)
	  }
	  
	  arcs = arc :: arcs
	}
	
	def removeArc(arc: Arc[_,_,_]) {
	  val oldCount = arcs.size
	  arcs = arcs filterNot(a => a.id == arc.id)
	  val newCount = arcs.size
	  
	  if(oldCount == newCount)
	    throw new RuntimeException("Arc " + arc + " is not present in the graph")
	  
	  import Direction._
	  if(arc.direction == In)
	    arc.transition.removeIn(arc)
	  else
	    arc.transition.removeOut(arc)
	}
}

object CPNGraph {
  def apply() = new CPNGraph()
}