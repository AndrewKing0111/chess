package chess;

import java.util.Collection;

public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition newPosition = new ChessPosition(position.getRow()+rowInc, position.getColumn()+colInc);

        while (newPosition.getRow() + rowInc <= 9 && newPosition.getRow() + rowInc >= 0 &&
                newPosition.getColumn() + colInc <= 9 && newPosition.getColumn() + colInc >= 0) {

            if (board.getPiece(newPosition) instanceof ChessPiece) {
                if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                    break;
                }
                else {
                    moves.add(new ChessMove(position, newPosition, null));
                    break;
                }
            }
            else {
                moves.add(new ChessMove(position, newPosition, null));
            }

            newPosition = new ChessPosition(newPosition.getRow()+rowInc, newPosition.getColumn()+colInc);
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
