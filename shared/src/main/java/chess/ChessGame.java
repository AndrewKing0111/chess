package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor turn = WHITE;
    private ChessPosition whiteKingPos = new ChessPosition(1, 5);
    private ChessPosition blackKingPos = new ChessPosition(8, 5);

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, startPosition);

        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = gameBoard;
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(startPosition));
            if (!isInCheck(gameBoard.getPiece(startPosition).getTeamColor())) {
                validMoves.add(move);
            }
            gameBoard = tempBoard;
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Move is empty.");
        } else if (turn != gameBoard.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Not your turn.");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            gameBoard.removePiece(move.getStartPosition());
        } else {
            throw new InvalidMoveException("Invalid move.");
        }

        if (turn == WHITE) {
            turn = BLACK;
        } else {
            turn = WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                if (gameBoard.getPiece(new ChessPosition(row, col)) != null) {
                    if (gameBoard.getPiece(new ChessPosition(row, col)).getPieceType() == KING) {
                        if (gameBoard.getPiece(new ChessPosition(row, col)).getTeamColor() == WHITE) {
                            whiteKingPos = new ChessPosition(row, col);
                        } else {
                            blackKingPos = new ChessPosition(row, col);
                        }
                    }
                }
            }
        }


        Rules rules = new Rules();
        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            Collection<ChessMove> checkMoves;
            if (teamColor == WHITE) {
                checkMoves = rules.pieceRule(pieceType).moves(gameBoard, whiteKingPos);
            } else {
                checkMoves = rules.pieceRule(pieceType).moves(gameBoard, blackKingPos);
            }

            for (ChessMove move : checkMoves) {
                if (gameBoard.getPiece(move.getEndPosition()) != null) {
                    if ((gameBoard.getPiece(move.getEndPosition()).getPieceType() == pieceType) &&
                            (gameBoard.getPiece(move.getEndPosition()).getTeamColor() != teamColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setGameBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getGameBoard(), chessGame.getGameBoard()) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameBoard(), turn);
    }
}
