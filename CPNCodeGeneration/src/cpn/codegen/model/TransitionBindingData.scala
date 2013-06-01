package cpn.codegen.model

case class TransitionBindingData(
  typedFreeVars: Set[(String, String)],
  numBindingGroups: Int,
  opbb: List[ExpressionElement])