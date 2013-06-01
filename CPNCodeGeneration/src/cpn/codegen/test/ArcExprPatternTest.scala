package cpn.codegen.test

import org.scalatest.FunSuite
import cpn.codegen.ModelData._

class ArcExprPatternTest extends FunSuite {

  val freeVars = List(
    ("c", "Int"),
    ("n", "Int"),
    ("nt", "Int"),
    ("d", "String"),
    ("o", "Int"),
    ("e", "String"),
    ("token", "(Int, String)"))

  test("multisetPattern1") {
    val aetree = tb.parse("Multiset((n,d),(o,e))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, (d: String))")
    assert(p2.toString === "scala.Tuple2(_, (e: String))")
  }

  test("multisetPattern2") {
    val aetree = tb.parse("Multiset((1,d),(2,e))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, (d: String))")
    assert(p2.toString === "scala.Tuple2(_, (e: String))")
  }

  test("multisetPattern3") {
    val aetree = tb.parse("Multiset((1,(n,d)),(1,(o,e)))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, scala.Tuple2((n: Int), (d: String)))")
    assert(p2.toString === "scala.Tuple2(_, scala.Tuple2((o: Int), (e: String)))")
  }

  test("multisetPattern4") {
    val aetree = tb.parse("Multiset((c,(n,d)),(c,(o,e)))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, scala.Tuple2((n: Int), (d: String)))")
    assert(p2.toString === "scala.Tuple2(_, scala.Tuple2((o: Int), (e: String)))")
  }

  test("multisetPattern5") {
    val aetree = tb.parse("Multiset(d)")
    val p :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p.toString === "Tuple2(_, (d: String))")
  }

  test("multisetPattern6") {
    val aetree = tb.parse("Multiset(d,e)")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "Tuple2(_, (d: String))")
    assert(p2.toString === "Tuple2(_, (e: String))")
  }

  test("multisetPattern7") {
    val aetree = tb.parse("Multiset((d,e))")
    val p :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p.toString === "Tuple2(_, scala.Tuple2((d: String), (e: String)))")
  }

  test("multisetTypeParamPattern1") {
    val aetree = tb.parse("Multiset[Tuple2[Int,String]]((n,d))")
    val p :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p.toString === "Tuple2(_, scala.Tuple2((n: Int), (d: String)))")
  }

  test("multisetTypeParamPattern2") {
    val aetree = tb.parse("Multiset[Tuple2[Int,String]]((n,d),(o,e))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "Tuple2(_, scala.Tuple2((n: Int), (d: String)))")
    assert(p2.toString === "Tuple2(_, scala.Tuple2((o: Int), (e: String)))")
  }

  test("multisetTypeParamPattern3") {
    val aetree = tb.parse("Multiset[Packet](Packet(n,d),Packet(o,e))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "Tuple2(_, Packet((n: Int), (d: String)))")
    assert(p2.toString === "Tuple2(_, Packet((o: Int), (e: String)))")
  }

  test("multisetTypeParamPattern4") {
    val aetree = tb.parse("Multiset[Packet]((1,Packet(n,d)),(1,Packet(o,e)))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, Packet((n: Int), (d: String)))")
    assert(p2.toString === "scala.Tuple2(_, Packet((o: Int), (e: String)))")
  }

  test("multisetTypeParamPattern5") {
    val aetree = tb.parse("Multiset[(Int,Packet[String])]((n,Packet(n,d)),(o,Packet(o,e)))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "Tuple2(_, scala.Tuple2((n: Int), Packet((n: Int), (d: String))))")
    assert(p2.toString === "Tuple2(_, scala.Tuple2((o: Int), Packet((o: Int), (e: String))))")
  }

  test("multisetTypeParamPattern6") {
    val aetree = tb.parse("Multiset[(Int,String)]((1,(n,d)),Tuple2(1,(o,e)))")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "", freeVars)
    assert(p1.toString === "scala.Tuple2(_, scala.Tuple2((n: Int), (d: String)))")
    assert(p2.toString === "Tuple2(_, scala.Tuple2((o: Int), (e: String)))")
  }

  test("multisetTokenPattern1") {
    val aetree = tb.parse("token")
    val p :: Nil = generatePatterns(aetree, "Multiset()", freeVars)
    assert(p.toString === "Tuple2(_, (token: (Int, String)))")
  }

  test("multisetTokenPattern2") {
    val aetree = tb.parse("(d,e)")
    val p :: Nil = generatePatterns(aetree, "Multiset()", freeVars)
    assert(p.toString === "Tuple2(_, scala.Tuple2((d: String), (e: String)))")
  }

  test("multisetTokenPattern3") {
    val aetree = tb.parse("(n,d)")
    val p :: Nil = generatePatterns(aetree, "Multiset()", freeVars)
    assert(p.toString === "scala.Tuple2(_, (d: String))")
  }

  test("multisetTokenPattern4") {
    val aetree = tb.parse("((n,d),(o,e))")
    val p :: Nil = generatePatterns(aetree, "Multiset()", freeVars)
    assert(p.toString === "Tuple2(_, scala.Tuple2(scala.Tuple2((n: Int), (d: String)), scala.Tuple2((o: Int), (e: String))))")
  }

  test("multisetTokenPattern5") {
    val aetree = tb.parse("Packet(n,d)")
    val p :: Nil = generatePatterns(aetree, "Multiset()", freeVars)
    assert(p.toString === "Tuple2(_, Packet((n: Int), (d: String)))")
  }

  test("tokenPattern1") {
    val aetree = tb.parse("token")
    val p :: Nil = generatePatterns(aetree, "Queue()", freeVars)
    assert(p.toString === "(token: (Int, String))")
  }

  test("tokenPattern2") {
    val aetree = tb.parse("(n,d)")
    val p :: Nil = generatePatterns(aetree, "Queue()", freeVars)
    assert(p.toString === "scala.Tuple2((n: Int), (d: String))")
  }

  test("tokenPattern3") {
    val aetree = tb.parse("((n,d),(o,e))")
    val p :: Nil = generatePatterns(aetree, "Queue()", freeVars)
    assert(p.toString === "scala.Tuple2(scala.Tuple2((n: Int), (d: String)), scala.Tuple2((o: Int), (e: String)))")
  }

  test("tokenPattern4") {
    val aetree = tb.parse("Queue(n,d)")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "Queue()", freeVars)
    assert(p1.toString === "(n: Int)")
    assert(p2.toString === "(d: String)")
  }

  test("simpleCollection1") {
    val aetree = tb.parse("Queue[String](n,d)")
    val p1 :: p2 :: Nil = generatePatterns(aetree, "Queue()", freeVars)
    assert(p1.toString === "(n: Int)")
    assert(p2.toString === "(d: String)")
  }

}