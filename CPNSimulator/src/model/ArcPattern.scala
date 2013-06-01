package model

case class ArcPattern[Be <: BindingElement, A, Repr](
  arc: Arc[Be, A, Repr], pattern: Pattern[Be])
  extends PatternBindingBasisObject[Be]