package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static chess.ChessPiece.PieceType.KNIGHT;

public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition lastPosition = position;
        ChessPosition newPosition = new ChessPosition(position.getRow()+rowInc, position.getColumn()+colInc);

        while (lastPosition.getRow() + rowInc <= 8 && lastPosition.getRow() + rowInc >= 1 &&
                lastPosition.getColumn() + colInc <= 8 && lastPosition.getColumn() + colInc >= 1) {

            if (board.getPiece(newPosition) instanceof ChessPiece) {
                if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                    break;
                }
                else {
                    moves.add(new ChessMove(position, newPosition, null));
                    break;
                }
            }

            moves.add(new ChessMove(position, newPosition, null));

            if(!allowDistance) {
                break;
            }

            lastPosition = newPosition;
            newPosition = new ChessPosition(newPosition.getRow()+rowInc, newPosition.getColumn()+colInc);
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
