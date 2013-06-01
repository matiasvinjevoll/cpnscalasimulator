package cpn.codegen.test

import org.scalatest.FunSuite
import cpn.codegen.ModelData._
import cpn.codegen.model.BindGuard
import cpn.codegen.model.EvalGuard

class GuardTest extends FunSuite {

  val freeVars = "n" :: "m" :: "d" :: Nil

  test("guardTest1") {
    val guard :: Nil = getGuardData("m=n", freeVars)
    assert(BindGuard(0, "m", Set("n"), "m = n", -1) === guard)
  }
  test("guardTest2") {
    val guard :: Nil = getGuardData("m==n", freeVars)
    assert(EvalGuard(0, Set("m", "n"), "m.$eq$eq(n)", -1) === guard)
  }
  test("guardTest3") {
    val guard :: Nil = getGuardData("foo(d)", freeVars)
    assert(EvalGuard(0, Set("d"), "foo(d)", -1) === guard)
  }
  test("guardTest4") {
    val guard :: Nil = getGuardData("foo", freeVars)
    assert(EvalGuard(0, Set(), "foo", -1) === guard)
  }
  test("guardTest5") {
    val guard = getGuardData("(m=n,m==n,foo(d),foo)", freeVars)
    assert(
        BindGuard(0, "m", Set("n"), "m = n", -1) ::
        EvalGuard(1, Set("m", "n"), "m.$eq$eq(n)", -1) ::
        EvalGuard(2, Set("d"), "foo(d)", -1) ::
        EvalGuard(3, Set(), "foo", -1) :: Nil === guard)
  }

}