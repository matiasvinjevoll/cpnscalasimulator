package model

abstract class Guard[Be <: BindingElement]

case class BindGuard[Be <: BindingElement](
  bind: Be => Be,
  bindingGroup: Int)
  extends Guard[Be] with PatternBindingBasisObject[Be]

case class EvalGuard[Be <: BindingElement](
  eval: Be => Boolean,
  bindingGroup: Int) extends Guard[Be]

case class TransitionAction[Be <: BindingElement](
  action: Be => Unit) extends Guard[Be]