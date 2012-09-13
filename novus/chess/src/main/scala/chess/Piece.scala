package chess

abstract class Piece(n:String, c:Color, p:Position) extends Moveable {
  val name = n
  val color = c
  var position = p

}