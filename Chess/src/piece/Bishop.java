package src.piece;
import src.main.TYPE.Piece_types;
import java.util.ArrayList;
public class Bishop extends Piece{
   
 public Bishop(String color, int row, int col)
    {
       super(color, row, col);
       Ids = Piece_types.BISHOP; 
       if("White".equals(color))
       {
        img = getImage("/PIECES/white-bishop");
       }
       else
       {
        img = getImage("/PIECES/black-bishop");
       }
    }    
    public boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board){

      if(isWithinBoardLimit(targetRow, targetCol) && !IsSameSquare(targetRow, targetCol))
      {
          //Bishop's movement is 1:1
          if (Math.abs(targetRow - PrevRow) == Math.abs(targetCol - PrevCol) )
              {
                  if(isValidSquare(targetRow, targetCol,board) && !PieceOnDiagonal(targetRow, targetCol,board)) 
                  return true;
              }              
      }
      return false;
  }
}
