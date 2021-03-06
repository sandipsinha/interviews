package chess

import scala.swing._
import scala.swing.event._

case class ChessBoardPanel(game: Game) extends GridPanel(10,10) {
  var squares: List[ChessBoardSquare] = List[ChessBoardSquare]()
  val allFiles = List[File](A, B, C, D, E, F, G, H)
  val allRanks = List[Rank](eight, seven, six, five, four, three, two, one)

  private[chess] def unselectAll() = squares.foreach { square => square.unselect }

  private[chess] def getSourceSquare() = squares.filter { Y => Y.selected == true && Y.piece != null && Y.piece.color == game.turn }.head

  private def printFileLabels() = {
    contents += new Label("")
    for (f <- allFiles) {
      contents += new Label(f.name)
    }
    contents += new Label("")
  }
  printFileLabels()
  
  for (r <- allRanks) {
    var color:Color = if (r.value % 2 == 0) black else white
    contents += new Label(r.value.toString)
    for (f <- allFiles) {
      color = Color.other(color)
      val square = ChessBoardSquare(this, Position(f,r.value), color)
      contents += square
      squares = square :: squares
    }
    contents += new Label(r.value.toString)
  }
  
  printFileLabels()
  game.board.pieces.foreach (piece => place(piece) )

  def remove(pos: Position) {
    game.board.pieces = game.board.pieces.filter { X => X.position != pos }
  }
  def place(piece: Piece) = {
    squares.find { X => X.position == piece.position } match {
      case Some(square) => square.setPiece(piece)
      case _ => println("Warning could not place piece " + piece.name + " on position " + piece.position)
    }
  }
}