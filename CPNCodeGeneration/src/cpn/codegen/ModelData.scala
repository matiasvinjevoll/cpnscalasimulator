package cpn.codegen

import cpn.model.CPNNet
import scala.collection.JavaConversions._
import java.util.{ List => JList }
import java.io._
import scala.reflect.runtime.{ universe => ru }
import scala.reflect.runtime.universe.runtimeMirror
import scala.reflect.runtime.universe.Tree
import scala.reflect.runtime.universe.Ident
import scala.reflect.runtime.universe.ModuleDef
import scala.reflect.runtime.universe.Assign
import scala.reflect.runtime.universe.Apply
import scala.reflect.runtime.universe.Literal
import scala.reflect.runtime.universe.Constant
import scala.reflect.runtime.universe.Typed
import scala.reflect.runtime.universe.ValDef
import scala.reflect.runtime.universe.Transformer
import scala.reflect.runtime.universe.Traverser
import scala.reflect.runtime.universe.AppliedTypeTree
import scala.reflect.runtime.universe.TypeApply
import scala.reflect.runtime.universe.Select
import scala.reflect.runtime.universe.Template
import scala.reflect.runtime.universe.newTermName
import scala.reflect.runtime.universe.newTypeName
import scala.reflect.runtime.universe.showRaw
import scala.reflect.runtime.universe.Name
import scala.tools.reflect.ToolBox
import cpn.model.{ CPNArc => Arc }
import TreeAddons._
import cpn.model.CPNArc.Direction
import cpn.codegen.model.BindGuard
import cpn.codegen.model.EvalGuard
import cpn.codegen.model.ArcExprData
import cpn.codegen.model.ArcPattern
import cpn.codegen.model.EnumData
import cpn.codegen.model.Guard
import cpn.codegen.model.ParamData
import scala.collection.mutable

object ModelData {

  val tb = runtimeMirror(getClass.getClassLoader).mkToolBox()

  def parseCode(code: String) = tb.parse(code)

  def printAsTree(code: String) = tb.parse(code).showFormatted

  def getEnumData(declarations: List[String]) = {
    val trees = declarations.map(x => tb.parse(x))

    trees.collect {
      case tree @ ModuleDef(_, name, Template(parents, _, body)) if parents.
        exists(ptree => ptree.exists {
          case Ident(pname) if pname.toString == "Enumeration" => true
          case _ => false
        }) =>

        val valDefs = body.flatMap(btree => btree.collect {
          case ValDef(_, valName, _, Ident(value)) if value.toString == "Value" =>
            valName.toString
        })

        EnumData(name.toString, valDefs)
    }
  }

  def getTypedFreeVars(varDecls: JList[String]) =
    varDecls.map(decl => decl.drop(7).split(':').toList match {
      case cpnvar :: typ :: Nil =>
        (cpnvar.trim, typ.trim)
    }).toList

  // TODO: Change return type to Set
  def getFreeVars(freeVars: List[String])(tree: Tree) = {
    tree.collect {
      case Ident(name) if freeVars.exists(x =>
        x == name.toString) => name.toString
      case name: Name if freeVars.exists(x =>
        x == name.toString) => name.toString
    }.distinct
  }

  def getGuardData(guard: String,
    freeVars: List[String]): List[Guard] = {
    val guardTree = tb.parse(guard)

    if (guardTree.toString == "null") {
      List()
    } else {

      def getConjunctData(tree: Tree, index: Int) = {
        val gfvl = getFreeVars(freeVars) _
        tree match {
          // Binding of free variable
          case Assign(Ident(n), rhs) =>
            val rhsVars = gfvl(rhs)
            BindGuard(index, n.toString, rhsVars.toSet, tree.toString)

          // Evaluation of expressions where all free variables are bound
          case Apply(lhs, rhs) =>
            val lhsvars = gfvl(lhs)
            val rhsvars = rhs.flatMap(gfvl(_))
            val vars = lhsvars ::: rhsvars
            EvalGuard(index, vars.toSet, tree.toString)

          // TODO: case Assign(Select(x, y), rhs) => side-effect

          // treat everything else as EvalGuards
          case _ =>
            val vars = gfvl(tree).toSet
            EvalGuard(index, vars, tree.toString)
        }
      }

      guardTree match {
        // In case of multiple guards, the guard is a tuple,
        // hence, the root of tree must match Apply
        case Apply(fun, tupleElems) if (fun findFunName match {
          case Some(funName) if funName.startsWith("Tuple") => true
          case _ => false
        }) =>
          tupleElems.zipWithIndex.map {
            case (tree, index) => getConjunctData(tree, index)
          }
        case singleElem => List(getConjunctData(singleElem, 0))
      }
    }
  }

  private def intConstOrVar(tree: Tree, typedFreeVars: List[(String, String)]) = tree match {
    case Literal(Constant(c: Int)) => true
    case Ident(freeVar) =>
      typedFreeVars.exists {
        case (x, "Int") => x == freeVar.toString
        case _ => false
      }
    case _ => false
  }

  /**
   * Special case for multiset, where a the multiset coefficient is not matched (replaced
   * with _)
   */
  private def generateMultisetElemPattern(elemTrees: List[Tree], expand: Boolean) = {
    if (expand)
      elemTrees.map(token =>
        Apply(Ident(newTermName("Tuple2")), Ident(newTermName("_")) :: token :: Nil))
    else
      elemTrees.map {
        // if ´expand´ == false, the elements of ´elemTrees´ are on the form Tuple2[Int,A],
        // where _1 is the coefficient, which has to be replaced with a wildcard for pattern match.
        case Apply(fun, coefficient :: token :: Nil) =>
          Apply(fun, Ident(newTermName("_")) :: token :: Nil)
      }
  }

  /**
   * Generate pattern for multiset element where the arc expression does not have
   * type parameter. Special case: When params is of type Tuple2[Int,A], then the
   * type will be Multiset[A].
   * @param args: List of arguments, as Trees, to a Multiset.
   * @param typedFreeVars: typed free variables defined for the CPN net.
   */
  private def generateMultisetElemPattern_NoTypeParam(args: List[Tree],
    typedFreeVars: List[(String, String)]) = {

    // ´expand´ == false if elems of´elemTrees´ have the same structure as elements in a
    // multiset, i.e., Tuple2[Int,A] where A is the token type. If A is of type
    // Tuple2[Int,B], then expression must reflect this, e.g. Multiset((1,(1,d))) where d: B.
    // Sufficient to check first element (mismatch of rest of the elements will not compile).
    val expand = args.length > 0 && (args head match {
      case Apply(fun, fst :: snd :: Nil) if fun.matchingFunTree("Tuple2") && intConstOrVar(fst, typedFreeVars) => false
      case _ => true
    })

    generateMultisetElemPattern(args, expand)
  }

  /**
   * Finds the name of the collection used for an initial marking.
   * @param initMark: Initial marking code represented as a string.
   */
  private def getCollectionName(initMark: String) = {
    val initMarkTree = tb.parse(initMark)
    initMarkTree.findFunName match {
      case Some(name) => name
      case None => throw new Exception("Collection for place not specified.")
    }
  }

  /**
   * Transforms a tree by replacing free any free variable in the tree with
   * the variable and its type.
   * @param tree: An arbitrary Tree
   * @param typedFreeVars: List containing typed free variables defined in the CPN net.
   */
  private def inferTypes(tree: Tree, typedFreeVars: List[(String, String)]) = {

    object InferTypesTransformer extends Transformer {
      override def transform(tree: Tree): Tree = tree match {
        case ident @ Ident(name) => typedFreeVars.find { case (x, _) => x == name.toString } match {
          case Some((v, t)) => Typed(ident, Ident(newTypeName(t)))
          case None => ident
        }
        case _ => super.transform(tree)
      }
    }

    InferTypesTransformer.transform(tree)

  }

  /**
   * Filters patterns from of an arc expression tree, i.e. generates a tree
   * where non-patterns are removed.
   * @param arcExprTree: Tree representing an arc expression.
   * @param placeInitMarking: String representing the initial marking on
   * the place connected to the current arc.
   * @return: Option[Tree] which represents either a Some collection where
   * parameters are filtered to only those that can be pattern matched,
   * or Some Tree representing a single pattern or variable. None in other cases.
   */
  def filterPatterns(arcExprTree: Tree, placeInitMarking: String) = {

    /**
     * Check whether a given tree is a pattern.
     * @param tree: An arbitrary Tree.
     */
    def isPattern(tree: Tree) = try {
      tb.parse("def ptger(x: Any) = x match { case %s => }" format (tree))
      true
    } catch {
      case _: Throwable => false
    }

    arcExprTree match {
      case app @ Apply(fun, args) =>
        fun.findFunName match {
          case Some(name) =>

            // if name of ´fun´ equals the collection name of the initial marking,
            // then ´args´ are expressions evaluating to tokens
            if (getCollectionName(placeInitMarking) == name) {
              val patternArgs = args.filter(isPattern(_))
              Some(Apply(fun, patternArgs))

              // if name of ´fun´ is not equal to the collection name of the initial
              // marking, then ´app´ is treated as an expression evaluating to a
              // single token.
            } else if (isPattern(app)) {
              Some(app)
            } else None
          // case _ =>  If not found, missing case in method ´findFunName´
        }
      case freeVar @ Ident(_) => Some(freeVar)
      case _ => None
    }
  }

  /**
   * Generates a list of token patterns from the ´arcExprTree´ (without types).
   * The arc expression tree is either parsed as a collection (equal to the collection
   * used for the initial marking on the place connected to the arc) with patterns, or
   * a single pattern (or no patterns).
   * @param arcExprTree: Tree representing an arc expression that has been transformed
   * by ´filterPatterns´.
   * @param placeInitMarking: String representing the initial marking on
   * the place connected to the current arc.
   * @param typedFreeVars: List containing typed free variables defined in the CPN net.
   */
  def generatePatterns(arcExprTree: Tree, placeInitMarking: String,
    typedFreeVars: List[(String, String)]) = {

    val ms = "Multiset"

    val patterns = arcExprTree match {

      case app @ Apply(fun, args) => fun match {

        case Ident(name) =>

          // If arc expr is a Multiset instantiation, special case for pattern match
          // applies, regarding multiset coefficient.
          // If ´name´ is not equal to the collection name of the initial
          // marking, then ´app´ is treated as an expression evaluating to a
          // single token.

          if (name.toString == ms)
            generateMultisetElemPattern_NoTypeParam(args, typedFreeVars)

          else if (getCollectionName(placeInitMarking) == ms)
            generateMultisetElemPattern_NoTypeParam(app :: Nil, typedFreeVars)

          else if (name.toString == getCollectionName(placeInitMarking))
            args

          else app :: Nil

        case TypeApply(Ident(name), typ :: Nil) =>
          // For collections with exactly one type parameter.

          if (name.toString == ms) typ match {
            // If arc expression is a Multiset instantiation with type parameter of the form
            // Tuple2[Int,A], the special case regarding Multiset may be avoided:
            // In this case, tokens (parameters) of Multiset instantiation will be on
            // the form Tuple2[Int,A], while traversable elements of the multiset will
            // be on the form Tuple2[Int, Tuple2[Int,A]].

            case typTree @
              AppliedTypeTree(tpfun, typArgs @ Ident(arg1name) :: arg2 :: Nil) if arg1name.
              toString == "Int" && tpfun.matchingFunTree("Tuple2") =>

              args.head match {
                // Sufficient to check first arg. If args are not consistent, it will
                // not compile.
                // Match type param on the form Tuple2[Int,_]
                case Apply(fun, fst :: second :: Nil) if fun.
                  matchingFunTree("Tuple2") && intConstOrVar(fst, typedFreeVars) =>
                  // ´expand´ determines if token patterns should be expanded with
                  // coefficient.
                  val expand = second.matchingStructure(arg2)
                  generateMultisetElemPattern(args, expand)

                // case _ => Should not happen since it has been checked that type param is
                // (Int,A), from where this method is called => param elems must be on that form.
              }

            case _ => // Treated as if there is no type parameter
              generateMultisetElemPattern_NoTypeParam(args, typedFreeVars)
          }
          else if (name.toString == getCollectionName(placeInitMarking))
            args
          else app :: Nil

        // TODO: Following case should be recursive in case of nested namespaces (only supports one nesting)
        // E.g. supports scala.Tuple2, not ns1.ns2.[... nsN].Foo
        // Assume that name == Tuple2 in this case
        case Select(_, name) =>
          if (getCollectionName(placeInitMarking) == ms)
            generateMultisetElemPattern_NoTypeParam(app :: Nil, typedFreeVars)
          else app :: Nil
      }

      case freeVar @ Ident(name) =>
        if (getCollectionName(placeInitMarking) == ms)
          generateMultisetElemPattern(freeVar :: Nil, true)
        else freeVar :: Nil
    }

    patterns map (inferTypes(_, typedFreeVars))
  }

  def getArcExprData(arcId: String, arcExpr: String, initMark: String, dir: Direction, freeVars: List[String],
    typedFreeVars: List[(String, String)]) = {

    val arcExprTree = tb.parse(arcExpr)

    val patterns =
      if (dir == Direction.IN)
        filterPatterns(arcExprTree, initMark) match {
          case Some(patternExpr) =>
            generatePatterns(patternExpr, initMark, typedFreeVars)
          case None => List()
        }
      else List()

    val gfvl = getFreeVars(freeVars) _

    val pv = patterns.zipWithIndex.map {
      case (tree, index) =>
        ArcPattern(index, arcId, tree.toString, gfvl(tree).toSet)
    }

    val freeVarsArc = gfvl(arcExprTree)
    ArcExprData(freeVarsArc.toSet, arcExprTree.toString, pv)
  }

  private def constrElemTypes(tree: Tree): (String, List[String]) = tree match {
    case Apply(TypeApply(Ident(collTyp), ttypParams: List[Tree]), _) =>
      val typParams = ttypParams.map { case typ => typ.toString }
      (collTyp.toString, typParams)
    case TypeApply(Ident(collTyp), ttypParams: List[Tree]) =>
      val typParams = ttypParams.map { case typ => typ.toString }
      (collTyp.toString, typParams)
    case AppliedTypeTree(Ident(collTyp), ttypParams: List[Tree]) =>
      val typParams = ttypParams.map { case typ => typ.toString }
      (collTyp.toString, typParams)
    case Typed(Ident(x), t) => constrElemTypes(t)
  }

  private def addTypeParams(tree: Tree, params: List[String]) = {
    val newParams = params.map(Ident(_))
    tree match {
      case TypeApply(Ident(collName), typParamsIdent: List[Ident]) =>
        TypeApply(Ident(collName), typParamsIdent ::: newParams).toString
      case coll @ Ident(collName) =>
        if (newParams.length > 0)
          TypeApply(Ident(collName), newParams).toString
        else
          coll.toString
    }
  }

  import cpn.model.Parameter
  def generateModuleData(className: String, params: JList[Parameter]) = {

    val classTree = parseCode(className)

    // key: collection name (with type param)
    // value: collection type and token type
    val typParamsMap = mutable.Map[String, (String, String)]()

    // Tuple: (paramId, paramName, collection type, token type)
    val collTokenParamTyps = params.map(param => {
      val coll = param.getCollection // initial marking of port place
      val (collTyp, tokenTyp :: Nil) = constrElemTypes(parseCode(coll))
      typParamsMap += (coll -> (collTyp, tokenTyp))
      (param.getId, param.getName, collTyp, tokenTyp)
    })

    // for all distinct types of the type parameters, a new type
    // parameter for the module class must be added (traversable type)
    val collTokenTravTyps = typParamsMap.values.zipWithIndex.map {
      case ((collTyp, elemTyp), index) => (collTyp, elemTyp, "T" + index)
    }.toList

    // Add the new type parameters to the constructor
    val newParams = collTokenTravTyps.map(_._3)
    val typedConstructor = addTypeParams(classTree, newParams)

    val paramData = collTokenParamTyps.map {
      case (id, name, collTyp, elemTyp) => collTokenTravTyps.find {
        case (ct, et, _) => collTyp == ct && elemTyp == et
      } match {
        case Some(el) =>
          ParamData(id, name, el._3, elemTyp, collTyp)
      }
    }.toList

    (typedConstructor, collTokenTravTyps, paramData)
  }
}