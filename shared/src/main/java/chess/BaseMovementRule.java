package chess;

import java.util.Collection;

public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition nextPosition = new ChessPosition(position.getRow() + rowInc, position.getColumn() + colInc);

        while (nextPosition.getRow() < 9 && nextPosition.getRow() > 0 &&
                nextPosition.getColumn() < 9 && nextPosition.getColumn() > 0) {

            if (board.getPiece(nextPosition) != null) {
                if (board.getPiece(nextPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                    break;
                } else {
                    moves.add(new ChessMove(position, nextPosition, null));
                    break;
                }
            }

            moves.add(new ChessMove(position, nextPosition, null));

            if (!allowDistance) {
                break;
            }

            nextPosition = new ChessPosition(nextPosition.getRow() + rowInc, nextPosition.getColumn() + colInc);
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
