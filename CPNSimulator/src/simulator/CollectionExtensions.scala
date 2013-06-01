package simulator

import scala.collection.immutable.Queue
import collection.CPNCollection

object CollectionExtensions {

  class RichQueue[A](queue: Queue[A]) extends CPNCollection[Queue[A]] {

    def >>=(that: Queue[A]): Boolean = {
      var temp = queue
      var other = that
      var res = true
      while (other.nonEmpty && res) {
        if (temp.isEmpty) res = false
        else {
          res = temp.head == other.head
          temp = temp.dequeue._2
          other = other.dequeue._2
        }
      }
      res
    }
    
    def +++(that: Queue[A]) = queue ++ that

    def --(that: Queue[A]) = {
      var tempThis = queue
      var other = that
      while (other.nonEmpty) {
        val (frontOther, restOther) = other.dequeue
        val (frontThis, restThis) = tempThis.dequeue
        if (frontOther == frontThis)
          tempThis = restThis
        else
          throw new IllegalArgumentException("Element " + frontOther + " is not on top of queue.")
        other = restOther
      }
      tempThis
    }
  }

  implicit def richQueue[A](x: Queue[A]): RichQueue[A] = new RichQueue[A](x)

}