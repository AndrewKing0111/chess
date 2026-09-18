package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        ArrayList<ChessPiece.PieceType> pieces = new ArrayList<>(List.of(ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK));
        for (int col=1; col<=8; col++) {
            addPiece(new ChessPosition(1,col), new ChessPiece(WHITE, pieces.get(col-1)));
        }

        for (int col=1; col<=8; col++) {
            addPiece(new ChessPosition(2,col), new ChessPiece(WHITE, PAWN));
        }

        for (int col=1; col<=8; col++) {
            addPiece(new ChessPosition(8,col), new ChessPiece(BLACK, pieces.get(col-1)));
        }

        for (int col=1; col<=8; col++) {
            addPiece(new ChessPosition(7,col), new ChessPiece(BLACK, PAWN));
        }
    }

    @Override
    public String toString() {
        String boardString = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    boardString += "|";
                    boardString = boardString.concat(board[i][j].toString());
                } else {
                    boardString = boardString.concat("| ");
                }
            }
            boardString = boardString.concat("|\n");
        }
        return boardString;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
