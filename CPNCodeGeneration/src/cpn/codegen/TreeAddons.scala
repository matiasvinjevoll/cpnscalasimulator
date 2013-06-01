package cpn.codegen
import scala.reflect.runtime.universe.Tree
import scala.reflect.runtime.universe.Ident
import scala.reflect.runtime.universe.Select
import scala.reflect.runtime.universe.TypeApply
import scala.reflect.runtime.universe.Apply
import scala.reflect.runtime.universe.Typed
import scala.reflect.runtime.universe.AppliedTypeTree
import scala.reflect.runtime.universe.showRaw
import scala.reflect.runtime.universe.Name

object TreeAddons {

  implicit def treeToRichTree(tree: Tree) = new RichTree(tree)

  class RichTree(val tree: Tree) {

    // May be represented in multiple ways in AST based on how a fun is defined.
    // E.g. (A,B), Tuple2(A,B)
    def matchingFunTree(funName: String) = tree match {
      case Ident(elname) if (elname.toString == funName) => true
      case Select(_, elname) if (elname.toString == funName) => true
      // TODO: Case where token elements is of type Tuple2 with type parameters (Tuple2[A,B](A,B)).
      case _ => false
    }

    def findFunName = findFunNameRec(tree)

    private def findFunNameRec(tree: Tree): Option[String] = tree match {
      case Ident(name) => Some(name.toString)
      case Apply(Ident(name), _) => Some(name.toString)
      case Apply(TypeApply(Ident(name), _), _) => Some(name.toString)
      // TODO: Following case should be recursive in case of nested namespaces (only supports one nesting)
      // E.g. supports scala.Tuple2, not ns1.ns2.[... nsN].Foo
      case Apply(Select(Ident(_), name), _) => Some(name.toString)
      case Select(Ident(_), name) => Some(name.toString)
      case TypeApply(Ident(name), _) => Some(name.toString)
      // From type parameter:
      case Typed(_, tt) => findFunNameRec(tt)
      case AppliedTypeTree(Ident(name), _) => Some(name.toString)
      case _ => None
    }

    /**
     * Used to determine if two trees represents the same (semantically in Scala code).
     * The method is not complete!
     */
    // TODO: Support for lists
    def matchingStructure(that: Tree): Boolean = {

      def applyMatch(name: Name, params1: List[Tree]) = that match {
        case Apply(fun, params2) =>
          fun.matchingFunTree(name.toString) &&
            params1.zip(params2).forall { case (x, y) => x.matchingStructure(y) }

        // Not recursive on type params
        case AppliedTypeTree(fun, params2) => fun matchingFunTree (name.toString)

        case _ => false
      }

      tree match {
        case Ident(_) => that match {
          case Ident(_) => true
          case _ => false
        }

        case Apply(Ident(name), params) => applyMatch(name, params)

        case Apply(Select(_, name), params) => applyMatch(name, params)

        case _ => false
      }
    }

    def showFormatted = {
      val ast = showRaw(tree)
      var indent = ""
      ast.map {
        case '(' =>
          indent += "    "
          "(\n" + indent
        case ')' =>
          indent = indent drop 4
          '\n' + indent + ')'
        case ',' => ",\n" + indent
        case ' ' => ""
        case c => c
      }.mkString
    }

  }

}