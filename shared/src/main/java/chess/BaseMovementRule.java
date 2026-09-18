package chess;

import java.util.Collection;

import static chess.ChessPiece.PieceType.*;

public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition lastPosition = position;
        ChessPosition newPosition = new ChessPosition(position.getRow() + rowInc, position.getColumn() + colInc);

        while (lastPosition.getRow() + rowInc <= 8 && lastPosition.getRow() + rowInc >= 1 &&
                lastPosition.getColumn() + colInc <= 8 && lastPosition.getColumn() + colInc >= 1) {

            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                    break;
                } else {
                    calculateMovesHelper(board, position, newPosition, moves);
                    break;
                }
            }

            calculateMovesHelper(board, position, newPosition, moves);

            if (!allowDistance) {
                break;
            }

            lastPosition = newPosition;
            newPosition = new ChessPosition(newPosition.getRow() + rowInc, newPosition.getColumn() + colInc);
        }
    }

    private void calculateMovesHelper(ChessBoard board, ChessPosition position, ChessPosition newPosition, Collection<ChessMove> moves) {
        if (board.getPiece(position).getPieceType() == PAWN && (newPosition.getRow() == 1 || newPosition.getRow() == 8)) {
            moves.add(new ChessMove(position, newPosition, QUEEN));
            moves.add(new ChessMove(position, newPosition, BISHOP));
            moves.add(new ChessMove(position, newPosition, KNIGHT));
            moves.add(new ChessMove(position, newPosition, ROOK));
        } else {
            moves.add(new ChessMove(position, newPosition, null));
        }

    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
