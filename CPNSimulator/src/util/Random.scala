package util

import scala.util.Random
import scala.collection.mutable.ListBuffer

object Random {
	def selectRandom[T](it: Iterable[T]): T = {
	  val list = it.toList
	  val r = new Random();
    list(r.nextInt(list.length))
	}
	
	def removeRandom[T](it: Iterable[T]): (T, List[T]) = {
	  val list = it.toList
	  val buffer = new ListBuffer[T]()
	  it.copyToBuffer(buffer)
	  val r = new Random();
    val el = buffer.remove(r.nextInt(list.length))
    (el, buffer.toList)
	}
}