package src.piece;
import src.main.TYPE.Piece_types;
import java.util.ArrayList;

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

    @Override
    public boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board){
        if(isWithinBoardLimit(targetRow, targetCol))
        {
            //Queen: Upward and downward
            if((PrevRow == targetRow || PrevCol == targetCol) && !IsSameSquare(targetRow, targetCol)){
                   if(isValidSquare(targetRow, targetCol,board) && !PieceOnStraightLine(targetRow, targetCol,board)) 
                    return true;
                }
            //Queen: Diagonal
            if (Math.abs(targetRow - PrevRow) == Math.abs(targetCol - PrevCol) && !IsSameSquare(targetRow, targetCol))
              {
                  if(isValidSquare(targetRow, targetCol,board) && !PieceOnDiagonal(targetRow, targetCol,board)) 
                  return true;
              }                    
        }
        
        return false;
    }
}
