package cpn.codegen.test

import org.scalatest.FunSuite
import cpn.codegen.ModelData._
import cpn.codegen.model.BindGuard
import cpn.codegen.model.EvalGuard
import cpn.codegen.model.ArcPattern
import cpn.codegen.BindingCalculation._
import cpn.codegen.model.ArcExprData

class BindingCalculationTest extends FunSuite {

  test("opbb1") {
    val g1 = BindGuard(0, "m", Set("n"), "m = n", -1)
    val g2 = EvalGuard(1, Set("d"), "", -1)

    val ap1 = ArcPattern(1, "", "", Set("m", "d"), -1)
    val a1 = ArcExprData(Set("m", "d"), "", ap1 :: Nil)
    val ap2 = ArcPattern(1, "", "", Set("n"), -1)
    val a2 = ArcExprData(Set("n"), "", ap2 :: Nil)
    val bindingGroupNum = calculateBindingGroups(g1 :: g2 :: Nil, a1 :: a2 :: Nil)
    assert(1 === bindingGroupNum)
    assert(0 === ap1.bindingGroup)
    assert(0 === ap2.bindingGroup)
    assert(0 === g1.bindingGroup)
    assert(0 === g2.bindingGroup)

    val opbb = calculateOPBB(bindingGroupNum, g1 :: g2 :: Nil, a1 :: a2 :: Nil)
    assert(ap1 :: ap2 :: g1 :: Nil === opbb)
  }
  test("opbb2") {
    val g1 = BindGuard(0, "m", Set("n"), "m = n", -1)

    val ap1 = ArcPattern(1, "", "", Set("n", "d"), -1)
    val ap2 = ArcPattern(1, "", "", Set("m", "o"), -1)
    val a1 = ArcExprData(Set("m", "n", "o", "d"), "", ap1 :: ap2 :: Nil)

    val ap3 = ArcPattern(1, "", "", Set("n"), -1)
    val a2 = ArcExprData(Set("n"), "", ap3 :: Nil)

    val bindingGroupNum = calculateBindingGroups(g1 :: Nil, a1 :: a2 :: Nil)
    assert(1 === bindingGroupNum)
    assert(0 === ap1.bindingGroup)
    assert(0 === ap2.bindingGroup)
    assert(0 === ap3.bindingGroup)
    assert(0 === g1.bindingGroup)

    val opbb = calculateOPBB(bindingGroupNum, g1 :: Nil, a1 :: a2 :: Nil)
    assert(ap2 :: ap1 :: g1 :: Nil === opbb)
  }
  test("opbb3") {
    val ap1 = ArcPattern(1, "", "", Set("n", "d"), -1)
    val ap2 = ArcPattern(1, "", "", Set("m", "o"), -1)
    val a1 = ArcExprData(Set("m", "n", "o", "d"), "", ap1 :: ap2 :: Nil)

    val ap3 = ArcPattern(1, "", "", Set("n"), -1)
    val a2 = ArcExprData(Set("n"), "", ap3 :: Nil)

    val bindingGroupNum = calculateBindingGroups(Nil, a1 :: a2 :: Nil)
    assert(2 === bindingGroupNum)
    assert(1 === ap1.bindingGroup)
    assert(0 === ap2.bindingGroup)
    assert(1 === ap3.bindingGroup)

    val opbb = calculateOPBB(bindingGroupNum, Nil, a1 :: a2 :: Nil)
    assert(ap2 :: ap1 :: Nil === opbb)
  }

}