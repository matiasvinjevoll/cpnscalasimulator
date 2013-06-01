package cpnmodel.babel.test

import org.scalatest.FunSuite
import cpnmodel.babel.impl.BabelMethods._
import cpnmodel.babel.impl._
import Scenario._

class BabelTests extends FunSuite {
  test("nochange") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, 20, 1, true))).noChanges)
  }
  test("insignMetricChange") {
    assert(false === // no change
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, 20, 1, true))).insMetricChange)
  }
  test("signMetricChange") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, 21, 1, true))).signMetricChange)
  }
  test("signMetricChange2") {
    assert(false ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, 20, 1, true))).signMetricChange)
  }
  test("routeRetracted") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, infinite, 1, true))).routeRetracted)
  }
  test("routeRetracted2") {
    assert(false ===
      RouteChanges(5, List(RouteTableEntry(1, 3, infinite, 1, true)), List(RouteTableEntry(1, 3, infinite, 1, true))).routeRetracted)
  }
  test("routeRetracted3") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List()).routeRetracted)
  }
  test("routeRetracted4") {
    assert(false ===
      RouteChanges(5, List(RouteTableEntry(1, 3, infinite, 1, true)), List()).routeRetracted)
  }
  test("nexthopchanged1") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 2, 20, 1, true))).nextHopChanged)
  }
  test("nexthopchanged2") {
    assert(false ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 3, 20, 1, true))).nextHopChanged)
  }
  test("seqNoChanged1") {
    assert(false ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 2, 20, 1, true))).seqNoChanged)
  }
  test("seqNoChanged2") {
    assert(true ===
      RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 2, 20, 2, true))).seqNoChanged)
  }
  test("newRoute1") {
  	assert(true ===
  			RouteChanges(5, List(), List(RouteTableEntry(1, 2, 20, 1, true))).newRoute)
  }
  test("newRoute2") {
  	assert(false ===
  			RouteChanges(5, List(RouteTableEntry(1, 3, 20, 1, true)), List(RouteTableEntry(1, 2, 20, 1, true))).newRoute)
  }  
}