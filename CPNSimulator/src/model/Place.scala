package model
import collection.CPNCollection

case class Place[A, Repr](
  id: String,
  name: String,
  initialMarking: Repr)(implicit ev0: Repr <:< Traversable[A],
    ev1: Repr => CPNCollection[Repr]) extends Element with Node {

  var currentMarking = initialMarking

  override def toString = super.toString + "[" + currentMarking + "]"

  def initialState() = currentMarking = initialMarking

  def pb[Be <: BindingElement](
    partialBind: Any => Be): List[Be] = {
    currentMarking.map(token => try {
      Some(partialBind(token))
    } catch {
      // A token on the place may not match the partial bind for a given arc expression,
      // if the exression is binding a specific subtype of the tokens the place holds
      case _: MatchError => None
    }).toList.collect { case Some(be) => be }.asInstanceOf[List[Be]]
  }

  def hasTokens(tokens: Any) = currentMarking >>= tokens.asInstanceOf[Repr]

  def removeTokens(tokens: Any) = currentMarking = currentMarking -- tokens.asInstanceOf[Repr]

  def addTokens(tokens: Any) = currentMarking = currentMarking +++ tokens.asInstanceOf[Repr]
  
  var in = List[Arc[_, _, _]]()
  var out = List[Arc[_, _, _]]()
  def addIn(arc: Arc[_, _, _]) {
    in = arc :: in
  }
  def addOut(arc: Arc[_, _, _]) {
    out = arc :: out
  }
}
