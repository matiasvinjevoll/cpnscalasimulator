package cpn.codegen

object Util {

  /**
   * Usage:
   * printToFile(new File("filename"))(p => {
   * p.println("Data to print...")
   * })
   */
  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } catch { case e => println(e) } finally { p.close() }
  }
}