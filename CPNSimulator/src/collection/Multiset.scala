package collection
import scala.collection.IterableLike
import scala.collection.generic.TraversableFactory
import scala.collection.mutable.Builder
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer
import scala.collection.generic.GenericCompanion
import scala.collection.GenTraversableOnce
import scala.collection.mutable.Map
import scala.collection.generic.GenMapFactory
import scala.collection.generic.GenericTraversableTemplate
import collection.CPNCollection

class Multiset[T] protected (protected val multiset: Map[T, Int])
  extends CPNCollection[Multiset[T]]
  with Iterable[(Int, T)]
  with IterableLike[(Int, T), Multiset[T]] {
  
  override protected[this] def newBuilder: Builder[(Int, T), Multiset[T]] = Multiset.newBuilder[T]

  override def toString = "Multiset [" + multiset.mkString(", ") + "]"

  def iterator: Iterator[(Int, T)] = for ((el, no) <- multiset.iterator) yield (no, el)

  // Additional methods

  def +(n: Int, elem: T): Multiset[T] = {
    val ms = multiset
    var count = n
    if (ms.contains(elem))
      count += ms(elem)
    if (count > 0)
      ms += (elem -> count)
    else if (count == 0)
      ms -= (elem)
    else
      throw new IllegalArgumentException("Not sufficient tokens to remove in multiset")
    new Multiset(ms)
  }

  def +(elem: T): Multiset[T] = this + (1, elem)

  def -(elem: T) = this + (-1, elem)

  def -(n: Int, elem: T) = this + (-n, elem)

  private def equality(that: Any, cons: (Int, Int) => Boolean): Boolean = {
    that match {
      case other: Multiset[T] => {
        var cond = false
        for ((k, v) <- multiset if !cond) {
          if (other.multiset.contains(k))
            cond = cons(multiset(k), other.multiset(k))
        }
        cond
      }
      case _ => false
    }
  }

  override def equals(other: Any) = other match {
    case that: Multiset[T] =>
      if (this.size == that.size)
        equality(that, (x, y) => (x == y))
      else
        false
    case _ => false
  }

  def <<(other: Multiset[T]): Boolean = {
    equality(other, (x, y) => (x < y))
  }

  def >>(other: Multiset[T]): Boolean = {
    other << this
  }

  def <<=(other: Multiset[T]): Boolean = {
    equality(other, (x, y) => (x <= y))
  }

  def >>=(that: Multiset[T]): Boolean =
    equality(that, (x, y) => (x >= y))

  def +++(other: Multiset[T]) = this ++ other

  def --(other: Multiset[T]): Multiset[T] = {
    if (this >>= other) {

      var ms = multiset
      other.multiset.foreach {
        case (el, c1) => ms.get(el) match {
          case Some(c2) =>
            val nc = c2 - c1
            if (nc > 0)
              ms = ms + (el -> nc)
            else
              ms = ms - el
        }
      }
      new Multiset(ms)
    } else {
      throw new IllegalArgumentException("Cannot subtract larger multiset from multiset")
    }
  }

  def removeToken(token: T) = this - token

  def addTokens(that: Any) = {
    that match {
      case other: Multiset[T] => this ++ other
      case _ => throw new IllegalArgumentException("Cannot add unknown collection from Multiset")
    }
  }

  def evalQueueToken[Be](fn: Be => T): Be => Multiset[T] = ((b: Be) => Multiset(fn(b)))

}

trait MultisetFactory[CC[A] <: Multiset[A] with IterableLike[(Int,A), CC[A]]]
{
  
  type Coll = CC[_]
  
  def fromSeq[A](buf: Seq[(Int,A)]): CC[A]
  
  def newBuilder[A]: Builder[(Int, A), CC[A]] = new ArrayBuffer[(Int, A)] mapResult fromSeq
  
  class MultisetCanBuildFrom[A] extends CanBuildFrom[Coll, (Int, A), CC[A]]{
    def apply(from: Coll) = newBuilder[A]
    def apply() = newBuilder
  }
}

object Multiset extends MultisetFactory[Multiset] {

  def fromSeq[T](buf: Seq[(Int, T)]): Multiset[T] = {
    val map = Map[T, Int]()
    buf.foreach(tp => tp match {
      case (no, el) =>
        var count = no
        if (map.contains(el))
          count = map(el) + no
        map += (el -> count)
    })
    new Multiset(map)
  }

  def apply[T]() = new Multiset[T](Map[T, Int]())

  def apply[T](elems: T*)(implicit d: DummyImplicit) = {
    var map = Map[T, Int]()
    elems.foreach(el => map += (el -> 1))
    new Multiset(map)
  }

  def apply[T](elems: (Int, T)*)(implicit d1: DummyImplicit, d2: DummyImplicit) = fromSeq(elems)
  
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, (Int, A), Multiset[A]] = new MultisetCanBuildFrom[A]

  def unapplySeq[T](ms: Multiset[T]): Option[Seq[(Int, T)]] = {
    if (ms.multiset.size > 0)
      Some(ms.multiset.toSeq.map(el => (el._2, el._1)))
    else None
  }
}
