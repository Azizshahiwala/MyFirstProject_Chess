package src.piece;
import java.util.ArrayList;
import src.main.TYPE.Piece_types;

public class Rook extends Piece {
 
    public Rook(String color, int row, int col) {
        super(color, row, col); 
        Ids = Piece_types.ROOK;
        if ("White".equals(color)) {
            img = getImage("/PIECES/white-rook");
        } else {
            img = getImage("/PIECES/black-rook");
        }
    } 
   
    @Override
    public boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board) {
        if (isWithinBoardLimit(targetRow, targetCol)) {
            // Rook's movement restriction
            if ((PrevRow == targetRow || PrevCol == targetCol) && !IsSameSquare(targetRow, targetCol)) {
                if (isValidSquare(targetRow, targetCol, board) && !PieceOnStraightLine(targetRow, targetCol, board)) {
                    return true;
                }
            }               
        }
        return false;
    }
}