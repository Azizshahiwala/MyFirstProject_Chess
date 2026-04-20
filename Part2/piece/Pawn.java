package src.piece;

import src.main.GamePanel;
import src.main.TYPE.Piece_types;
public class Pawn extends Piece{
    public Pawn(String color, int row, int col)
    {
       super(color, row, col); 
       Ids = Piece_types.PAWN;
       if("White".equals(color))
       {
        img = getImage("/PIECES/white-pawn");
       }
       else
       {
        img = getImage("/PIECES/black-pawn");
       }
       
    }
    
    public boolean CanMove(int targetRow, int targetCol){

        if(isWithinBoardLimit(targetRow, targetCol) && !IsSameSquare(targetRow, targetCol))
        {   
            int moveVal;
            
                if(this.PieceColor.equals("White"))
                {
                    moveVal = -1;
                }
                else 
                {
                    moveVal = 1;
                }

                PieceHit = isColliding(targetRow, targetCol);

                //1 sq movement
                if (targetCol == PrevCol + moveVal && targetRow == PrevRow && PieceHit == null){
                    
                    return true;
                }
                //2 sq movement
                if (targetCol == PrevCol + moveVal * 2 && targetRow == PrevRow && PieceHit == null && !Moved && !PieceOnStraightLine(targetRow, targetCol))
                {
                    twoStepped = true;
                    return true;
                }  
                if (targetCol == PrevCol + moveVal && Math.abs(targetRow - PrevRow) == 1 && PieceHit != null && !PieceHit.PieceColor.equals(this.PieceColor)){
            return true;
            }

              if (targetCol == PrevCol + moveVal && Math.abs(targetRow - PrevRow) == 1 && PieceHit == null){
                
                    for (Piece piece : GamePanel.Piece) {
                        if (piece != this &&
                            piece.Ids == Piece_types.PAWN &&
                            !piece.PieceColor.equals(this.PieceColor) &&
                            piece.col == PrevCol &&
                            piece.row == targetRow &&
                            piece.twoStepped) {
                            this.PieceHit = piece;
                            return true;
                        }
                    }
                }
                 // it’s an enemy
        }
        return false;
    }
}