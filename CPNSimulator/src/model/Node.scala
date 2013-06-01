package model

trait Node {
  
  def name: String

  override def toString = name
}