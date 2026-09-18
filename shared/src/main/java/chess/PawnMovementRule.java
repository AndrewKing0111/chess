package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        int rowInc;
        int startRow;

        if (board.getPiece(position).getTeamColor() == BLACK) {
            rowInc = -1;
            startRow = 7;
        } else {
            rowInc = 1;
            startRow = 2;
        }

        if (board.getPiece(new ChessPosition(currentRow +rowInc, currentCol)) == null) {
            calculateMoves(board, position, rowInc, 0, moves, false);

            if (currentRow == startRow && board.getPiece(new ChessPosition(currentRow + rowInc * 2, currentCol)) == null) {
                calculateMoves(board, position, rowInc * 2, 0, moves, false);
            }
        }
        if (currentCol + 1 <= 8) {
            if (board.getPiece(new ChessPosition(currentRow + rowInc, currentCol + 1)) != null) {
                calculateMoves(board, position, rowInc, 1, moves, false);
            }
        }
        if (currentCol - 1 >= 1) {
            if (board.getPiece(new ChessPosition(currentRow + rowInc, currentCol - 1)) != null) {
                calculateMoves(board, position, rowInc, -1, moves, false);
            }
        }

        return moves;
    }
}
