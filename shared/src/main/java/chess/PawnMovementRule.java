package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        int rowInc;
        if (board.getPiece(position).getTeamColor() == BLACK) {
            rowInc = -1;
        } else {
            rowInc = 1;
        }

        ChessPosition advanceOne = new ChessPosition(position.getRow() + rowInc, position.getColumn());
        if (board.getPiece(advanceOne).getTeamColor() != board.getPiece(position).getTeamColor()) {
            calculateMoves(board, position, rowInc, 0, moves, false);
        }

        return moves;
    }
}
