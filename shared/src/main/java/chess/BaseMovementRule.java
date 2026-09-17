package chess;

import java.util.Collection;

public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc, Collection<ChessMove> moves, boolean allowDistance) {
        if (position.getRow() + rowInc > 8 && position.getRow() + rowInc < 1 &&
                position.getColumn() + colInc > 8 && position.getColumn() + colInc < 1) {
            return;
        }
        ChessPosition newPosition = new ChessPosition(position.getRow()+rowInc, position.getColumn()+colInc);

        if (board.getPiece(newPosition) instanceof ChessPiece) {
            if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                return;
            }
            else {
                moves.add(new ChessMove(position, newPosition, null));
                return;
            }
        }
        else {
            moves.add(new ChessMove(position, newPosition, null));
            return;
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
