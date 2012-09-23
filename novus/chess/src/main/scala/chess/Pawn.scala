package chess

case class Pawn(var p: Position, c: Color) extends Piece("Pawn", c, p) {

  private def neverMoved: Boolean = (position.rank.value, color) match {
    case (2, white) => true
    case (7, black) => true
    case _ => false
  }

  private def onePositionForward(p: Position) = if (color == white) p+* else p-*
 

  private def twoPositionForward(p: Position) = onePositionForward(p) match {
    case Some(x) => onePositionForward(x)
    case None => None
  }

  private def killPosition2(board: Board, p: Position) = (onePositionForward(p), board.isPositionOccupied(onePositionForward(p).get.*-, Color.other(color))) match {
    case (Some(x), true) => x*-
    case _ => None
  }

  private def killPosition1(board: Board, p: Position) = (onePositionForward(p), board.isPositionOccupied(onePositionForward(p).get.*+, Color.other(color))) match {
    case (Some(x), true) => x*+
    case _ => None
  }

  private def killPositions(board: Board, p: Position) = List(killPosition1(board, p), killPosition2(board,p)).filter { X => !X.isEmpty }.map { Y => Y.get }


  override def possiblePositions(board: Board) : List[Position] = (neverMoved, onePositionForward(position)) match {
    case (false, Some(forwardPos)) if board.isPositionEmpty(forwardPos)  => List(forwardPos) ::: killPositions(board, position) 
    case (true, Some(forwardPos))  if board.isPositionEmpty(forwardPos)  => List(forwardPos) ::: killPositions(board, position) ::: (if (board.isPositionEmpty(onePositionForward(forwardPos) )) List(onePositionForward(forwardPos).get) else Nil)
    case _ => killPositions(board, position)
  }
}