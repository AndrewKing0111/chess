package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Rules rules = new Rules();
        return rules.pieceRule(this.type).moves(board, myPosition);
    }

    public String toString() {
        switch (this.type) {
            case KING:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "k";
                }
                return "K";
            case QUEEN:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "q";
                }
                return "Q";
            case BISHOP:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "b";
                }
                return "B";
            case KNIGHT:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "n";
                }
                return "N";
            case ROOK:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "r";
                }
                return "R";
            case PAWN:
                if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                    return "p";
                }
                return "P";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
