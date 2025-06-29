package src.piece;
import src.main.TYPE.Piece_types;
public class Queen extends Piece{
   
    public Queen(String color, int row, int col)
    {
       super(color, row, col); 
        Ids = Piece_types.QUEEN;
       if("White".equals(color))
       {
        img = getImage("/PIECES/white-queen");
       }
       else
       {
        img = getImage("/PIECES/black-queen");
       }
    } 
    public boolean CanMove(int targetRow, int targetCol){

        if(isWithinBoardLimit(targetRow, targetCol))
        {
            //Queen: Upward and downward
            if((PrevRow == targetRow || PrevCol == targetCol) && !IsSameSquare(targetRow, targetCol)){
                   if(isValidSquare(targetRow, targetCol) && !PieceOnStraightLine(targetRow, targetCol)) 
                    return true;
                }
            //Queen: Diagonal
            if (Math.abs(targetRow - PrevRow) == Math.abs(targetCol - PrevCol) && !IsSameSquare(targetRow, targetCol))
              {
                  if(isValidSquare(targetRow, targetCol) && !PieceOnDiagonal(targetRow, targetCol)) 
                  return true;
              }                    
        }
        
        return false;
    }
}
