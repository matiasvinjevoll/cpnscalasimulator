package cpn.codegen.model

import scala.collection.mutable.HashMap
import cpn.model.Module

case class ModuleData(
  module: Module,
  typedConstructor: String,
  collTokenTravTyps: List[(String, String, String)],
  paramData: List[ParamData],
  guardData: HashMap[String, List[Guard]],
  arcData: HashMap[String, ArcExprData],
  transitionBindingData: HashMap[String, TransitionBindingData])