package model

case class ArcPattern[Be <: BindingElement, A, Repr](
  arc: Arc[Be, A, Repr], pattern: Pattern[Be])
  //(implicit ev1: Repr <:< Traversable[A], ev2: Repr => CPNColl[Repr])
  extends PatternBindingBasisObject[Be]