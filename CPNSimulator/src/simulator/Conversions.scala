package simulator

import model.BindingElement
import scala.collection.immutable.Queue
import collection.Multiset

object Conversions {

  implicit def evalQueueToken[Be <: BindingElement, A](fn: (Be => A)) =
    (b: Be) => Queue(fn(b))

  implicit def evalQueueTokenList[Be, A](fn: (Be => List[A])) = ((b: Be) => Queue(fn(b): _*))

  implicit def evalMultisetToken[Be, A](fn: (Be => A)) = ((b: Be) => Multiset(fn(b)))

  implicit def evalMultisetToken2[Be, A](fn: (Be => (Int, A))) = ((b: Be) => Multiset(fn(b)))

  implicit def evalMultisetTokenList[Be, A](fn: (Be => List[A])) = ((b: Be) => Multiset(fn(b): _*))

  implicit def evalMultisetTokenList2[Be,A](fn: (Be => List[(Int, A)])) = ((b: Be) => Multiset(fn(b): _*))
}