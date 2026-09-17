package chess;

import java.util.HashMap;
import static chess.ChessPiece.PieceType.*;

public class Rules {
    private final HashMap<ChessPiece.PieceType, MovementRule> rules = new HashMap<>();

    public Rules() {
        rules.put(ROOK, new RookMovementRule());
        rules.put(BISHOP, new BishopMovementRule());
        rules.put(QUEEN, new QueenMovementRule());
    }

    public MovementRule pieceRule(ChessPiece.PieceType pieceType) {
        return rules.get(pieceType);
    }
}
