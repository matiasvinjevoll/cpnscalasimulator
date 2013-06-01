package collection

trait CPNCollection[Repr] {

  def >>=(that: Repr): Boolean

  def --(that: Repr): Repr

  def +++(that: Repr): Repr
}