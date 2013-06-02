package cpn.codegen

import java.util.NoSuchElementException
import scala.collection.JavaConversions._
import scala.collection.mutable.HashMap
import BindingCalculation._
import ModelData.getArcExprData
import ModelData.getGuardData
import ModelData.getTypedFreeVars
import ModelData.getEnumData
import ModelData.generateModuleData
import cpn.codegen.model.ArcExprData
import cpn.codegen.model.BindGuard
import CodeGenerator._
import cpn.codegen.model.ExpressionElement
import cpn.codegen.model.Guard
import cpn.codegen.model.TransitionBindingData
import cpn.codegen.model.ModuleData
import cpn.TransformNet

object Main extends App {

  val tnet =
    new TransformNet(
      "file:\\...")
  val net = tnet.transformNet()
  // -------------------------------------------------------------------
  // ------------------------- Process CPN -----------------------------
  // -------------------------------------------------------------------

  // partition cpnvar declarations from Scala-declarations
  val (cpnvarDecls, declarations) =
    net.getDeclarations.partition(decl =>
      if (decl.startsWith("cpnvar")) true else false)

  val enumData = getEnumData(declarations.toList)

  // typed free vars defined in the net
  val typedFreeVars = getTypedFreeVars(cpnvarDecls)

  // free vars without types, defined in the net
  val freeVars = typedFreeVars.map { case (x, _) => x }

  var moduleData = List[ModuleData]();

  net.getModules.foreach {
    case (moduleId, module) =>
      {
        // key: transition id
        val guardData = HashMap[String, List[Guard]]()
        //println("Module " + module.getName)
        // Populate ´guardData´
        module.getTransitions.foreach {
          case (id, transition) =>
            val guard = getGuardData(transition.getGuard, freeVars)
            guardData += (id -> guard)
        }

        // key: arc id
        val arcData = HashMap[String, ArcExprData]()

        // populate ´arcData´
        module.getArcs.foreach {
          case (id, arc) =>
            val arcExprData = getArcExprData(id, arc.getExpression,
                arc.getPlace.getInitialMarking, arc.getDirection, freeVars, typedFreeVars)
            arcData += (id -> arcExprData)
        }
        module.getParamArcs.foreach(arc => {
          val arcExprData = getArcExprData(arc.getId, arc.getExpression,
              arc.getParam.getCollection, arc.getDirection, freeVars, typedFreeVars)
          arcData += (arc.getId -> arcExprData)
        })

        // key: transition id
        val transitionBindingData = HashMap[String, TransitionBindingData]()

        // Populate ´transitionBindingData´
        module.getTransitions.foreach {
          case (id, transition) =>

            val guardDataList = guardData.get(id) match { case Some(gdl) => gdl }

            val inArcExprDataLst =
              transition.getInputArcs.map(arcId => {
                arcData.get(arcId) match {
                  case Some(arcData) => arcData
                }
              }).toList

            val outArcExprDataLst = transition.getOutputArcs.map(arcId => {
              arcData.get(arcId) match {
                case Some(arcData) => arcData
              }
            }).toList

            val freeVarsTransition = findFreeVarsTransition(
              guardDataList, inArcExprDataLst ::: outArcExprDataLst)

            val typedFreeVarsTransition = typedFreeVars.filter {
              case (v, t) => freeVarsTransition contains (v)
            }.toSet

            val numBindingGroups =
              calculateBindingGroups(guardDataList, inArcExprDataLst ::: outArcExprDataLst)

            val oPBB = calculateOPBB(numBindingGroups, guardDataList, inArcExprDataLst)
            transitionBindingData += (id -> TransitionBindingData(typedFreeVarsTransition, numBindingGroups, oPBB))
        }

        val (typedConstructor, tokenTravTyps, paramData) =
          generateModuleData(module.getName, module.getParams)
        moduleData = ModuleData(
          module,
          typedConstructor,
          tokenTravTyps,
          paramData,
          guardData,
          arcData,
          transitionBindingData) :: moduleData

      }
  }
  

  // -------------------------------------------------------------------
  // ------------------------- Code Generation -------------------------
  // -------------------------------------------------------------------

  generateCPNNet(
    true,
    net.getPlaces().values.toList,
    net.getDeclarations.toList,
    moduleData,
    enumData)
    
}