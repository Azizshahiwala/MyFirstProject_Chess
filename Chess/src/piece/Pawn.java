package src.piece;
import java.util.ArrayList;
import src.main.TYPE.Piece_types;

public class Pawn extends Piece {
    
    public Pawn(String color, int row, int col) {
        super(color, row, col); 
        Ids = Piece_types.PAWN;
        if ("White".equals(color)) {
            img = getImage("/PIECES/white-pawn");
        } else {
            img = getImage("/PIECES/black-pawn");
        }
    }
    
    @Override
    public boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board) {
        if (isWithinBoardLimit(targetRow, targetCol) && !IsSameSquare(targetRow, targetCol)) {   
            int moveVal = this.PieceColor.equals("White") ? -1 : 1;

            PieceHit = isColliding(targetRow, targetCol, board);

            // 1 sq movement
            if (targetCol == PrevCol && targetRow == PrevRow + moveVal && PieceHit == null) {
                return true;
            }
            
            // 2 sq movement
            if (targetCol == PrevCol && targetRow == PrevRow + (moveVal * 2) && PieceHit == null && !Moved && !PieceOnStraightLine(targetRow, targetCol, board)) {
                return true; // twoStepped is set in updatePosition() now
            }  
            
            // Standard Capture
            if (Math.abs(targetCol - PrevCol) == 1 && targetRow == PrevRow + moveVal && PieceHit != null && !PieceHit.PieceColor.equals(this.PieceColor)) {
                return true;
            }

            // En Passant
            if (Math.abs(targetCol - PrevCol) == 1 && targetRow == PrevRow + moveVal && PieceHit == null) {
                for (Piece piece : board) {
                    if (piece != this &&
                        piece.Ids == Piece_types.PAWN &&
                        !piece.PieceColor.equals(this.PieceColor) &&
                        piece.col == targetCol &&
                        piece.row == PrevRow &&
                        piece.twoStepped) {
                        this.PieceHit = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}