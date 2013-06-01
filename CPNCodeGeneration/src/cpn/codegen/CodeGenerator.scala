package cpn.codegen

import scala.collection.JavaConversions._
import cpn.model.{ CPNPlace => Place, CPNTransition => Transition, CPNArc => Arc }
import cpn.model.Parameter
import cpn.codegen.model.Guard
import cpn.codegen.model.BindGuard
import cpn.codegen.model.EvalGuard
import cpn.codegen.model.ArcExprData
import cpn.codegen.model.ArcPattern
import cpn.model.CPNArc.Direction
import scala.collection.mutable.HashMap
import java.util.{ List => JList }
import cpn.codegen.model.EnumData
import cpn.codegen.model.ExpressionElement
import cpn.codegen.model.BindGuard
import cpn.codegen.model.TransitionBindingData
import cpn.codegen.model.ModuleData
import cpn.codegen.model.ParamData
import cpn.model.ParamArc

object CodeGenerator {

  def generateCPNNet(
    includeComments: Boolean = true,
    globalPlaces: List[Place],
    declarations: List[String],
    moduleData: List[ModuleData],
    enumData: List[EnumData]) {

    // Calculations

    val netPackage = "package cpnmodel"

    val imports =
      "import prototype.collections.Multiset\n" +
        "import prototype.collections.CPNQueue\n" +
        "import model.Transition\n" +
        "import model.CPNGraph\n" +
        "import model.Arc\n" +
        "import model.GuardConjunct\n" +
        "import model.Place\n" +
        "import model.Pattern\n" +
        "import model.BindingElement\n" +
        "import simulator.Simulator\n" +
        "import model.BindGuard\n" +
        "import model.EvalGuard\n" +
        "import model.ArcPattern\n" +
        "import model.Direction._\n" +
        "import simulator.Conversions._"

    val beginObject = "object GeneratedNet extends App {"
    val netName = "net"
    val netDecl = "val %s = CPNGraph()" format netName

    val declarationsCode = declarations.collect {
      case decl if !decl.startsWith("cpnvar") => decl
    } mkString ("\n")

    val globalPlacesCode = globalPlaces.map(p => generatePlace(p, netName)).mkString("\n\n")

    val modulesCode = moduleData.map(md => {
      // ----- Building body of module ----
      val sbBody = new StringBuilder()

      val placesCode = md.module.getPlaces.map { case (_, place) => generatePlace(place, netName) }.mkString("\n\n")
      if (includeComments) sbBody ++= "// Places\n"
      sbBody ++= placesCode
      sbBody ++= "\n\n"

      val transitiosCode = md.module.getTransitions.map { case (_, transition) => generateTransitionAndArcs(transition, md, enumData, netName, includeComments) }.mkString("\n\n")
      sbBody ++= transitiosCode
      sbBody ++= "\n\n"

      if (includeComments) sbBody ++= "// Wire modules\n"
      sbBody ++= wireSubModules(md, netName)
      sbBody ++= "\n\n"

      val body = sbBody.toString
      // ----- /Building body of module ----

      val moduleComment = if (includeComments) {
        "// ########################################################################\n" +
          "// Generated code for module " + md.module.getName + "\n" +
          "// ########################################################################\n"
      } else ""

      val module = generateModule(md, netName, body)

      val wireT = if (md.module.getSuperModules == 0) wireTop(md.module.getName, netName) else ""

      moduleComment + module + "\n\n" + wireT

    }).mkString("\n\n")
    val runSimCode = "Simulator.runInteractive(%s)" format (netName)

    //val placesCode = places.map(place => generatePlace(place, netName)) mkString ("\n")

    /*
     * Currently, a model is written to the console. All output is from the lines below,
     * and can be changed for writing to a file.
     */
    println(netPackage)
    println(imports)
    println
    println(beginObject)
    println
    println(netDecl)
    println
    if (includeComments) println("// Declarations")
    println
    println(declarationsCode)
    println
    if (includeComments) println("// Global Places")
    println
    println(globalPlacesCode)
    println
    println(modulesCode)
    println
    println(runSimCode)
    println
    println("}")
  }
  //========================================================================================
  //==================== Code Generation for Modules =======================================
  //========================================================================================

  def generateModule(moduleData: ModuleData, netName: String, body: String) = {

    val moduleTemplate = "class %1$s(%2$s)%3$s{\n\n%4$s\n\n%5$s\n}"

    val instNet = s"val $netName = CPNGraph()"

    val paramTemplate = "%1$s: Place[%2$s, %3$s[%4$s]]"

    val params = moduleData.paramData.map {
      case ParamData(portId, _, objectType, tokenType, collectionType) =>
        paramTemplate format (
          portId, objectType, collectionType, tokenType)
    }.mkString(", ")

    val implicitTemplate = "ev%1$s: %2$s[%3$s] => Traversable[%4$s]"

    val implicitLst = moduleData.collTokenTravTyps.zipWithIndex.map {
      case ((collTyp, tokenTyp, travTyp), index) =>
        implicitTemplate format (index, collTyp, tokenTyp, travTyp)
    }

    val implicits =
      if (implicitLst.length > 0)
        "(\nimplicit " + implicitLst.mkString(", ") + ")"
      else ""

    moduleTemplate format (moduleData.typedConstructor, params, implicits, instNet, body)
  }

  //========================================================================================
  //==================== Code Generation Wiering Top Module ================================
  //========================================================================================
  /**
   * Generate code that adds modules that are not sub modules of any other modules to
   * the top net. Does not support parameters or type parameters
   */
  def wireTop(moduleName: String, netName: String) = {
    val wireTemplate =
      "val %1$s = new %2$s() \n" +
        "%3$s.addSubstitutionTransition(%1$s.%3$s)"

    wireTemplate format (moduleName.toLowerCase + 0, moduleName, netName)
  }

  //========================================================================================
  //==================== Code Generation Wiering Sub Modules ===============================
  //========================================================================================

  def wireSubModules(moduleData: ModuleData, netName: String) = {
    val namePrefix = "st"
    val wireTemplate =
      "val %1$s = new %2$s(%3$s) \n" +
        "%4$s.addSubstitutionTransition(%1$s.%4$s)"
    moduleData.module.getSubstitutionTransitions.zipWithIndex.map {
      case (st, index) => {
        val name = st.getModule.getName.takeWhile(_ != '[') // drop type parameter
        val args = st.getArgs.map(arg => {
          if (arg.isInstanceOf[Place]) {
            arg.asInstanceOf[Place].getId
          } else {
            arg.asInstanceOf[Parameter].getId
          }
        }).mkString(", ")
        wireTemplate format (namePrefix + index, name, args, netName)
      }
    }.mkString("\n\n")
  }

  //========================================================================================
  //==================== Code Generation for Places ========================================
  //========================================================================================

  def generatePlace(place: Place, netName: String) = {

    val placeTemplate = "val %1$s = Place(\"%1$s\", \"%2$s\", %3$s)\n" +
      "%4$s.addPlace(%1$s)"

    placeTemplate format (place.getId, place.getName, place.getInitialMarking, netName)
  }

  //========================================================================================
  //==================== Code Generation for Transition and Arcs ===========================
  //========================================================================================
  def generateTransitionAndArcs(transition: Transition, moduleData: ModuleData, enumData: List[EnumData], netName: String, includeComments: Boolean) = {
    val sb = new StringBuilder()

    if (includeComments) {
      sb ++= "// ========================================================================\n"
      sb ++= "// Generated code for transition " + transition.getName + " and its arcs\n"
      sb ++= "// ========================================================================\n\n"
    }

    // Generate code for ´transition´
    val tid = transition.getId
    (moduleData.guardData.get(tid), moduleData.transitionBindingData.get(tid)) match {

      case (Some(gdata), Some(TransitionBindingData(freeVarsTransition, numBindingGroups, opbb))) =>

        // Generate code for transition
        val transitionCode =
          generateTransition(
            tid, transition.getName, freeVarsTransition.toList, gdata,
            netName, numBindingGroups, enumData)

        sb ++= transitionCode
        sb ++= "\n\n"

        val allArcs =
          (transition.getInputArcs ++ transition.getOutputArcs).toList

        if (includeComments) sb ++= "// ------------------ Arcs ------------------\n\n"

        allArcs.foreach(arcId => {
          val arc = moduleData.module.getArcs.get(arcId)

          def arcComment(place: String, direction: Direction) = {
            val p = place.replace("\n", " ")
            val t = transition.getName.replace("\n", " ")
            if (includeComments && direction == Direction.IN)
              sb ++= "// " + p + " ----> " + t + "\n\n"
            else if (includeComments)
              sb ++= "// " + t + " ----> " + p + "\n\n"
          }

          if (arc != null) {
            arcComment(arc.getPlace.getName, arc.getDirection)
            moduleData.arcData.get(arcId) match {
              case Some(arcData) =>
                val arcCode = generateArc(arc.getId, arc.getPlace.getId, tid, arc.getDirection, freeVarsTransition, arcData, netName)
                sb ++= arcCode
                sb ++= "\n\n"
            }

          } else {
            moduleData.module.getParamArcs.find(x => x.getId == arcId) match {
              case Some(arc) =>
                moduleData.arcData.get(arcId) match {
                  case Some(arcData) =>
                    arcComment(arc.getParam.getName, arc.getDirection)
                    val arcCode = generateArc(arc.getId, arc.getParam.getId, tid, arc.getDirection, freeVarsTransition, arcData, netName)
                    sb ++= arcCode
                    sb ++= "\n\n"
                }
            }
          }
        })
        if (includeComments) sb ++= "\n\n// ------------------ /Arcs -----------------\n\n"

        val opbbCode = generateOPBB(tid, opbb)

        if (includeComments) sb ++= "// Set ordered pattern binding basis for " + transition.getName + "\n\n"
        sb ++= opbbCode
        sb ++= "\n\n"

        sb.toString
      case (None, _) =>
        throw new NoSuchElementException(
          "No guard data for transition " + transition.getName)
      case (_, None) =>
        throw new NoSuchElementException(
          "No data for transition " + transition.getName)
    }
  }

  //========================================================================================
  //==================== Code Generation for Transition ====================================
  //========================================================================================

  def generateTransition(
    transitionId: String,
    transitionName: String,
    freeVars: List[(String, String)],
    //typedFreeVars: List[(String, String)],
    guardData: List[Guard],
    netName: String,
    numBindingGroups: Int,
    enumData: List[EnumData]) = {

    val bindingElementName = "BindingElement_" + transitionId

    // Inner defs to generate parameters for transition

    def generateBindingElement = {
      val bindingElementTemplate = "case class %s(\n\t%s) extends BindingElement"
      val parameterTemplate = "%s: Option[%s]"
      val params = freeVars.map {
        case (v, t) => parameterTemplate format (v, t)
      }.mkString(",\n\t")

      bindingElementTemplate format (bindingElementName, params)
    }

    def generateCompatibleBindingElements = {
      val valName = "compatible_" + transitionId
      val compatibleTemplate =
        "val %1$s = (b1: %2$s, b2: %2$s) =>\n%3$s"
      val compatibleVarTemplate = "\t(b1.%1$s == b2.%1$s || b1.%1$s == None || b2.%1$s == None)"

      val lines = freeVars.map {
        case (x, _) =>
          compatibleVarTemplate format (x)
      }.mkString(" &&\n")

      (valName, compatibleTemplate format (valName, bindingElementName, lines))
    }

    def generateMergeBindingElements = {
      val valName = "merge_" + transitionId
      val mergeTemplate =
        "val %1$s = (b1: %2$s, b2: %2$s) => {\n" +
          "%3$s\n" +
          "\tnew %2$s(%4$s)\n}"
      val mergeVarTemplate = "\tval %1$s = if (b1.%1$s == None) b2.%1$s else b1.%1$s"
      val lines = freeVars.map { case (v, _) => mergeVarTemplate format (v) }.mkString("\n")
      val vars = freeVars.map { case (v, _) => v }.mkString(", ")

      (valName, mergeTemplate format (valName, bindingElementName, lines, vars))
    }

    def generateFullBindingElement = {
      val valName = "fullBindingElement_" + transitionId
      val fullBindingElementTemplate =
        "val %1$s = (be: %2$s) =>\n" +
          "%3$s"
      val valCondTemplate = "\tbe.%1$s != None"
      val lines = freeVars.map { case (v, _) => valCondTemplate format (v) }.mkString(" &&\n")

      (valName, fullBindingElementTemplate format (valName, bindingElementName, lines))
    }

    def generateGuard = {

      var conjunctVals = List[String]()

      val conjValNamePrefix = "guard_" + transitionId + "_"

      val conjuncts = guardData.map {
        case g @ BindGuard(index, lhsvar, rhsvars, expr, bindingGroup) =>
          val freeVars = rhsvars + lhsvar
          val conjValName = conjValNamePrefix + index
          conjunctVals = conjValName :: conjunctVals

          generateBindGuardConjuncts(
            conjValName, g, freeVars, bindingGroup)

        case g @ EvalGuard(index, vars, expr, bindingGroup) =>
          val conjValName = conjValNamePrefix + index
          conjunctVals = conjValName :: conjunctVals

          generateEvalGuardConjuncts(
            conjValName, g, bindingGroup)
      }
      val valName = "guard_" + transitionId
      val guard = "val %1$s = List(%2$s)" format (
        valName, conjunctVals.reverse.mkString(","))

      (valName, conjuncts.mkString("\n") + "\n" + guard)
    }

    def generateBindGuardConjuncts(valName: String,
      guardConjunct: BindGuard, bindingElementVars: Set[String],
      bindingGroup: Int) = {

      val BindGuard(_, lhsvar, rhsvars, expr, _) = guardConjunct

      val bindTemplate =
        "val %1$s = BindGuard((be: BindingElement_%2$s) => (%3$s) match {\n" +
          "\tcase (%4$s) =>\n" +
          "\t\tval %5$s\n" +
          "\t\tBindingElement_%2$s(%6$s)\n" +
          "}, %7$s)"

      val matchArgs = rhsvars.map("be." + _).mkString(",")
      val caseArgs = rhsvars.map("Some(" + _ + ")").mkString(",")

      val bindingElArgs = freeVars.map {
        case (v, _) =>
          if (v == lhsvar) "Some(" + lhsvar + ")"
          else "be." + v
      }.mkString(", ")

      bindTemplate format (valName, transitionId, matchArgs,
        caseArgs, expr, bindingElArgs, bindingGroup)
    }

    def generateEvalGuardConjuncts(valName: String,
      guardConjunct: EvalGuard, bindingGroup: Int) = {

      val EvalGuard(_, vars, expr, _) = guardConjunct

      val evalTemplate =
        "val %1$s = EvalGuard((be: BindingElement_%2$s) => (%3$s) match {\n" +
          "\tcase (%4$s) => {\n" +
          "\t\t%5$s\n" +
          "\t}\n" +
          "}, %6$s)"

      val matchArgs = vars.map("be." + _).mkString(",")
      val caseArgs = vars.map("Some(" + _ + ")").mkString(",")

      evalTemplate format (valName, transitionId,
        matchArgs, caseArgs, expr, bindingGroup)
    }

    def generateEnumBindings = {

      def generateEnumBindingArgs(cpnvar: String, values: List[String]) =
        values.map(value =>
          "BindingElement_%s(%s)" format (
            transitionId,
            freeVars.toList.map {
              case (v, _) if v == cpnvar => "Some(" + value + ")"
              case _ => "None"
            }.mkString(", "))).mkString(", ")

      // Enumeration names used must be collected to generate code to import their values
      var usedEnums = List[String]()

      val eb = freeVars.toList.collect {
        case (v, "Boolean") =>
          "List(%s)" format (generateEnumBindingArgs(v, List("true", "false")))
        case (v, enumval) if enumval.endsWith(".Value") =>
          enumData.find(x => enumval.startsWith(x.name)) match {
            case Some(EnumData(name, values)) =>
              usedEnums = name :: usedEnums
              "List(%s)" format (generateEnumBindingArgs(v, values))
            case x =>
              throw new RuntimeException("Type " + enumval + " cannot be resolved.")
          }
      }

      val enumImportTemplate = "import %s._\n"

      val enumsImport = usedEnums.map(x => enumImportTemplate format (x)).mkString("\n")

      val valName = "enumBindings_" + transitionId

      val enumBindingsVal =
        (eb mkString ",\n\t")

      val enumBindingTemplate = "%sval %s = List(\n\t%s)"

      (valName, enumBindingTemplate format (enumsImport, valName, enumBindingsVal))
    }

    // ---------------------------------------------------------------------------

    val transitionTemplate =
      "val %1$s = Transition(" +
        "\"%1$s\"," +
        "\"%2$s\"," +
        "%3$s," +
        "%4$s," +
        "%5$s," +
        "%6$s," +
        "%7$s," +
        "%8$s)\n" +
        "%9$s.addTransition(%1$s)"

    val bindingElement = generateBindingElement

    val (compRef, compatible) = generateCompatibleBindingElements

    val (mergeRef, merge) = generateMergeBindingElements

    val (fullRef, fullBe) = generateFullBindingElement

    val (guardRef, guard) = generateGuard

    val (ebRef, enumBindings) = generateEnumBindings

    val transitionCode =
      transitionTemplate format (
        transitionId, transitionName, numBindingGroups, compRef, mergeRef, fullRef, guardRef, ebRef, netName)

    // Return code for transition
    bindingElement +
      "\n\n" +
      compatible +
      "\n\n" +
      merge +
      "\n\n" +
      fullBe +
      "\n\n" +
      guard +
      "\n\n" +
      enumBindings +
      "\n\n" +
      transitionCode
  }

  //========================================================================================
  //==================== Code Generation for Arcs ==========================================
  //========================================================================================

  def generateArc(
    arcId: String,
    placeId: String,
    transitionId: String,
    direction: Direction,
    freeVarsTransition: Set[(String, String)],
    arcData: ArcExprData,
    netName: String) = {

    // Inner defs to generate parameters for Arc

    def generatePattern(pattern: ArcPattern) = {
      val partialBindFunTemplate =
        "(token: Any) => token match {\n" +
          "\tcase %1$s =>\n" +
          "\t\tBindingElement_%2$s(%3$s)\n" +
          "}"

      val partialBindingElement = freeVarsTransition.toList.map {
        case (v, _) =>
          if (pattern.freeVars.contains(v))
            "Some(" + v + ")"
          else "None"
      }.mkString(", ")

      val partialBindFun = partialBindFunTemplate format (
        pattern.expr, transitionId, partialBindingElement)

      val patternTemplate =
        "val %1$s = " +
          "Pattern(%2$s, %3$s)"

      val pName = "pattern_%1$s_%2$s" format (arcId, pattern.id)
      (pName, patternTemplate format (pName, partialBindFun, pattern.bindingGroup))
    }

    def generateEvalArc = {
      val evalTemplate =
        "val %1$s = (be: BindingElement_%2$s) => (%3$s) match {\n" +
          "\tcase (%4$s) =>\n" +
          "\t\t%5$s\n" +
          "}"

      val evalName = "eval_" + arcId

      val freeVars = arcData.freeVars.toList

      val matchArgs = freeVars.map("be." + _).mkString(",")

      val caseArgs = freeVars.map("Some(" + _ + ")").mkString(",")

      (evalName,
        evalTemplate format (
          evalName, transitionId, matchArgs, caseArgs, arcData.expr))
    }

    // ---------------------------------------------------------------------------

    val patternsRefs = arcData.patterns.map(
      pattern => generatePattern(pattern))

    val (patternRefs, patternCode) = patternsRefs.foldLeft(
      (List[String](), List[String]())) {
        case ((r1, r2), (c1, c2)) => (c1 :: r1, c2 :: r2)
      }

    val (evRef, evalCode) = generateEvalArc

    val arcTemplate =
      "val %1$s = Arc(\"%1$s\", %2$s, %3$s, %4$s) (%5$s)\n" +
        "%6$s.addArc(%1$s)"

    val arcDirection =
      (if (direction == Direction.IN) "In"
      else "Out")

    val arcCode = arcTemplate format (
      arcId, placeId, transitionId, arcDirection, evRef, netName)

    (patternCode mkString ("\n")) +
      "\n" +
      evalCode +
      "\n" +
      arcCode

  }

  //========================================================================================
  //==================== Code Generation for OPBB ==========================================
  //========================================================================================

  def generateOPBB(transitionId: String, opbb: List[ExpressionElement]) = {

    val opbbTemplate = "%1$s.orderedPatternBindingBasis = List(\n\t%2$s)"

    val params = opbb.map {
      case ArcPattern(id, arcId, _, _, _) =>
        "ArcPattern(%1$s, pattern_%1$s_%2$s)" format (arcId, id)
      case BindGuard(id, _, _, _, _) =>
        "guard_%1$s_%2$s" format (transitionId, id)
    }.mkString(",\n\t")

    opbbTemplate format (transitionId, params)
  }

}