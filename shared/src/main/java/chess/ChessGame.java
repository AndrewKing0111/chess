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
    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

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
        Collection<ChessMove> legalMoves = new HashSet<>();
        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = new ChessBoard(gameBoard);
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(startPosition));
            gameBoard.removePiece(move.getStartPosition());
            if (!isInCheck(gameBoard.getPiece(move.getEndPosition()).getTeamColor())) {
                legalMoves.add(move);
            }
            gameBoard = new ChessBoard(tempBoard);
        }
        return legalMoves;
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
        } else if (gameBoard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Nothing to move.");
        } else if (turn != gameBoard.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Not your turn.");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            ChessPiece pieceToMove;
            if (move.getPromotionPiece() != null) {
                pieceToMove = new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
            } else {
                pieceToMove = gameBoard.getPiece(move.getStartPosition());
            }

            gameBoard.addPiece(move.getEndPosition(), pieceToMove);
            gameBoard.removePiece(move.getStartPosition());

            if (turn == WHITE) {
                turn = BLACK;
            } else {
                turn = WHITE;
            }
        } else {
            throw new InvalidMoveException("Invalid move.");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        getKingPosition(teamColor);

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
        getKingPosition(teamColor);

        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessMove> possibleMoves;
        Rules rules = new Rules();
        if (teamColor == WHITE) {
            possibleMoves = rules.pieceRule(KING).moves(gameBoard, whiteKingPos);
        } else {
            possibleMoves = rules.pieceRule(KING).moves(gameBoard, blackKingPos);
        }

        Collection<ChessMove> movesToRemove = new HashSet<>();
        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = new ChessBoard(gameBoard);
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            gameBoard.removePiece(move.getStartPosition());
            if (isInCheck(teamColor)) {
                movesToRemove.add(move);
            }
            gameBoard = new ChessBoard(tempBoard);
        }

        for (ChessMove move : movesToRemove) {
            possibleMoves.remove(move);
        }

        if (!possibleMoves.isEmpty()) {
            return false;
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(position);
                if ((piece == null) || (piece.getTeamColor() != teamColor) || (piece.getPieceType() == KING)) {
                    continue;
                }

                Collection<ChessMove> checkMoves = piece.pieceMoves(gameBoard, position);
                Collection<ChessMove> outOfCheckMoves = new HashSet<>();
                for (ChessMove move : checkMoves) {
                    ChessBoard tempBoard = new ChessBoard(gameBoard);
                    gameBoard.addPiece(move.getEndPosition(), piece);
                    gameBoard.removePiece(move.getStartPosition());
                    if (!isInCheck(turn)) {
                        outOfCheckMoves.add(move);
                    }
                    gameBoard = new ChessBoard(tempBoard);
                }
                if (!outOfCheckMoves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
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

    private void getKingPosition(ChessGame.TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (gameBoard.getPiece(position) != null) {
                    if (gameBoard.getPiece(position).getPieceType() == KING) {
                        if (gameBoard.getPiece(position).getTeamColor() == teamColor) {
                            if (teamColor == WHITE) {
                                whiteKingPos = position;
                            } else {
                                blackKingPos = position;
                            }
                            return;
                        }
                    }
                }
            }
        }
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
