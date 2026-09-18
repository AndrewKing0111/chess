package chess;

public class Test {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        System.out.println(board.toString());
    }
}
