package chess

import scala.swing._
import scala.swing.event._


case class ChessBoardSquare(parent : ChessBoardPanel, position: Position, squareColor: Color, private[chess] var piece: Piece = null) extends GridPanel(1,1) {
  background = getAwtSquareColor
  private val button = new Button {
    background = getAwtSquareColor
    text = getButtonText
    foreground = getAwtPieceColor
  }

  private def isMoveablePiece() = piece != null && piece.color == parent.game.turn
  private def debugClick() = println("Clicked " + piece + " @ " + position + " on " + squareColor + " square")

  private def movePiece(selPieceSquare:ChessBoardSquare,selPiece:Piece, newPos:Position) = {
    selPiece.position = position
    parent.place(selPiece)
    selPieceSquare.piece = null
    selPieceSquare.refresh
  }

  listenTo(button)
  reactions += {
    case ButtonClicked(b) => {
      if (isMoveablePiece()) {
        val possiblePositions = piece.possiblePositions(parent.game.board)
        parent.unselectAll()
        select
        possiblePositions.foreach { X => parent.squares.filter { Y => Y.position == X }.foreach { sq => sq.select }} 
      } else if (selected) {
        val selPieceSquare = parent.getSourceSquare()
        val selPiece = selPieceSquare.piece
        val oldPos = selPiece.position
        if (parent.game.move(selPiece, position).isCheck) {
          parent.game.move(selPiece, oldPos)
          parent.unselectAll()
        } else {
          movePiece(selPieceSquare, selPiece, position)
          parent.unselectAll()
          GameGui.nextTurn
        }
      } else {
        debugClick()
      }
    }
  }
  contents += button

  private def getButtonText = if (piece == null) "" else piece.name

  private[chess] var selected = false
  private def refresh = {
    button.background = getAwtSquareColor
    button.text = getButtonText
    button.foreground = getAwtPieceColor
  }

  private[chess] def unselect = {
    border = Swing.EmptyBorder(0)
    selected = false;
  }

  private def select = {
    border = Swing.LineBorder(java.awt.Color.yellow)
    selected = true
  }

  private def getAwtPieceColor = if (piece == null) getAwtSquareColor else if (piece.color == white) java.awt.Color.white else java.awt.Color.black
             
  private def getAwtSquareColor = if (squareColor == white) java.awt.Color.lightGray else java.awt.Color.darkGray

  private def removeExistingPiece(p: Piece) {
    val newPieces = parent.game.board.pieces.filter(X => X != p)
    parent.game.board.pieces = newPieces
  }

  private[chess] def setPiece(newPiece: Piece) = {
    removeExistingPiece(piece)
    piece = newPiece
    button.text = getButtonText
    button.foreground = getAwtPieceColor
  }

  private def getButton = button
}
