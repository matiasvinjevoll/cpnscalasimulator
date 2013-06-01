package cpn.codegen

import cpn.codegen.model.ArcExprData
import cpn.codegen.model.Guard
import cpn.codegen.model.BindGuard
import cpn.codegen.model.EvalGuard
import cpn.codegen.model.ArcPattern
import cpn.codegen.model.ExpressionElement

object BindingCalculation {

  /**
   * Finds free variables over a transition
   */
  def findFreeVarsTransition(guardConjunctDataLst: List[Guard],
    arcExprDataLst: List[ArcExprData]) = {

    arcExprDataLst.
      foldLeft(Set[String]())((res, current) => res ++ current.freeVars) ++
      guardConjunctDataLst.
      foldLeft(Set[String]())((res, current) => current match {
        case BindGuard(_, lhsvar, rhsvars, _, _) => res ++ rhsvars + lhsvar
        case EvalGuard(_, vars, _, _) => res ++ vars
      })
  }

  /**
   * Calculates binding groups and set binding groups of objects in parameter as a side effect.
   * @return: The number of binding elements
   */
  def calculateBindingGroups(guardConjunctDataLst: List[Guard],
    arcExprDataLst: List[ArcExprData]) = {

    // List of list with free variables for each expression qualified
    // to be in pattern binding basis, i.e. assignment guard conjuncts
    // and arc patterns
    val bindingGroupData = guardConjunctDataLst.collect {
      case BindGuard(_, lhsvar, rhsvars, _, _) => rhsvars + lhsvar
    } :::
      arcExprDataLst.flatMap(_.patterns.map(x => {
        x.freeVars
      }))

    // Merging list of lists containing strings, where the result is such that no two lists contains
    // any equal strings
    def merge(xss: List[Set[String]], res: List[Set[String]] = List()): List[Set[String]] = xss match {
      case Nil => res
      case xs :: xss =>
        val (yss, zss) = xss.partition(ys => xs.map(ys.contains(_)).contains(true))
        if (yss.length > 0)
          merge((xs ++ yss.flatten) :: zss, res)
        else merge(xss, xs :: res)
    }

    val bindingGroups = merge(bindingGroupData).zipWithIndex

    // finds the binding group of a variable, naively returns -1 if not found - if
    // that happens, something is wrong somewhere else...
    def findBindingGroup(freeVar: String) = {
      var found = false
      var bindingGroup = -1
      for ((freeVars, id) <- bindingGroups if !found) {
        if (freeVars.contains(freeVar)) {
          found = true
          bindingGroup = id
        }
      }
      bindingGroup
    }

    // Set binding groups for guard conjuncts
    guardConjunctDataLst.foreach {
      // find the binding group based on any free variable of the guard conjunct;
      // hence any free variable in the expression element can be used.
      case g @ BindGuard(_, lhsvar, _, _, _) => g.bindingGroup = findBindingGroup(lhsvar)
      case g @ EvalGuard(_, vars, _, _) =>
        if (vars.isEmpty)
          g.bindingGroup = 0 // TODO: If there are no free variables in the guard guard, binding group is set to 0.
        else
          g.bindingGroup = findBindingGroup(vars.head)
    }

    // Set binding groups for arc patterns
    arcExprDataLst.foreach(aedl => aedl.patterns.foreach(p => {
      if (!p.freeVars.isEmpty)
        p.bindingGroup = findBindingGroup(p.freeVars head)
    }))

    bindingGroups.length
  }

  /**
   * Sorts a list of ´ArcPattern´s in regards of number of free variables they contain,
   * in descending order.
   */
  private def sortArcPatternsDesc(lst: List[ArcPattern]): List[ArcPattern] =
    if (lst.length <= 1) lst
    else {
      val (less, greater) = lst.tail.partition(_.freeVars.size <= lst.head.freeVars.size)
      sortArcPatternsDesc(greater) ::: lst.head :: sortArcPatternsDesc(less)
    }

  /**
   *  Calculates ordered pattern binding basis. Assumes that ´patterns´
   *  are sorted in descending order in regards of free variables.
   *  All guard candidates are included, while only the set of arcs
   *  necessary to cover all free variables.
   *  @return: List[PBBElement] of ordered pattern binding basis
   */
  private def calculateOPBBforBindingGroup(guardConjuncts: List[BindGuard],
    patterns: List[ArcPattern], coveredVars: Set[String] = Set(),
    res: List[ExpressionElement] = List()): List[ExpressionElement] = patterns match {
    case Nil => res.reverse ::: guardConjuncts
    case x :: xs =>
      val (appConjuncts, recConjuncts) = guardConjuncts.partition(
        gc => gc.rhsvars.subsetOf(coveredVars))
      if (appConjuncts.length > 0) {
        // Add guards where rhs contain covered free variabeles
        val cv = coveredVars ++ appConjuncts.map(_.lhsvar).toSet
        calculateOPBBforBindingGroup(recConjuncts, patterns, cv, appConjuncts ::: res)
      } else {
        if (x.freeVars subsetOf (coveredVars))
          calculateOPBBforBindingGroup(guardConjuncts, xs, coveredVars, res)
        else
          calculateOPBBforBindingGroup(guardConjuncts, xs, coveredVars ++ x.freeVars, x :: res)
      }
  }

  def calculateOPBB(numBindingGroups: Int, guardConjunctDataLst: List[Guard],
    arcExprDataLst: List[ArcExprData]) = {

    var groupedGuardConjuncts =
      Array.fill(numBindingGroups)(List[BindGuard]())

    // Add all guard conjuncts that are assignments to the n'th list of
    // ´groupedGuardConjuncts´ where n == guardConjunct.bindingGroup
    guardConjunctDataLst.foreach {
      case guard @ BindGuard(_, _, _, _, bindingGroup) =>
        groupedGuardConjuncts(bindingGroup) = guard :: groupedGuardConjuncts(bindingGroup)
      case _ =>
    }

    var groupedArcPatterns =
      Array.fill(numBindingGroups)(List[ArcPattern]())

    // Add patterns for all arcs to the n'th list of ´groupedArcPatterns´
    // where n == pattern.bindingGroup
    arcExprDataLst.foreach(_.patterns.foreach(pattern => {
      val bg = pattern.bindingGroup
      groupedArcPatterns(bg) = pattern :: groupedArcPatterns(bg)
    }))

    (groupedGuardConjuncts, groupedArcPatterns).zipped.toList.map {
      case (g, ap) =>
        val sortedAp = sortArcPatternsDesc(ap)
        calculateOPBBforBindingGroup(g, sortedAp)
    }.flatten
  }

}