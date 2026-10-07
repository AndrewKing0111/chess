package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

public class PawnMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        int rowInc;
        int startRow;

        if (board.getPiece(position).getTeamColor() == WHITE) {
            rowInc = 1;
            startRow = 2;
        } else {
            rowInc = -1;
            startRow = 7;
        }

        if (board.getPiece(new ChessPosition(currentRow + rowInc, currentCol)) == null) {
            calculateMoves(board, position, rowInc, 0, moves, false);

            if (currentRow == startRow && board.getPiece(new ChessPosition(currentRow + rowInc * 2, currentCol)) == null) {
                calculateMoves(board, position, rowInc * 2, 0, moves, false);
            }
        }
        if (currentCol < 8) {
            if (board.getPiece(new ChessPosition(currentRow + rowInc, currentCol + 1)) != null) {
                calculateMoves(board, position, rowInc, 1, moves, false);
            }
        }
        if (currentCol > 1) {
            if (board.getPiece(new ChessPosition(currentRow + rowInc, currentCol - 1)) != null) {
                calculateMoves(board, position, rowInc, -1, moves, false);
            }
        }

        var movesToRemove = new HashSet<ChessMove>();
        for (ChessMove move : moves) {
            if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
                movesToRemove.add(move);
            }
        }

        for (ChessMove move : movesToRemove) {
            moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), QUEEN));
            moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), BISHOP));
            moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), KNIGHT));
            moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ROOK));
            moves.remove(move);
        }

        return moves;
    }
}
