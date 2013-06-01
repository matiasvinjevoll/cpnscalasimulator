package cpn.codegen.model

abstract class Guard

case class BindGuard(
  override val id: Int,
  lhsvar: String, rhsvars: Set[String],
  expr: String,
  var bindingGroup: Int = -1) extends Guard with ExpressionElement

case class EvalGuard(
  override val id: Int,
  vars: Set[String],
  expr: String,
  var bindingGroup: Int = -1) extends Guard with ExpressionElement