package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, position, 0, 1, moves, false);
        calculateMoves(board, position, 0, -1, moves, false);
        calculateMoves(board, position, 1, 0, moves, false);
        calculateMoves(board, position, -1, 0, moves, false);
        calculateMoves(board, position, -1, -1, moves, false);
        calculateMoves(board, position, -1, 1, moves, false);
        calculateMoves(board, position, 1, -1, moves, false);
        calculateMoves(board, position, 1, 1, moves, false);
        return moves;
    }
}
