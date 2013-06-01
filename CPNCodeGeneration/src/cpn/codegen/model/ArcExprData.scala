package cpn.codegen.model

case class ArcExprData(freeVars: Set[String], expr: String, patterns: List[ArcPattern])

case class ArcPattern(
  override val id: Int,
  arcId: String,
  expr: String,
  freeVars: Set[String],
  var bindingGroup: Int = -1) extends ExpressionElement