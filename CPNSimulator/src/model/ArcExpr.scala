package model

case class Pattern[Be <: BindingElement](
  partialBind: Any => Be,
  bindingGroup: Int)