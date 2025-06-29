package src.piece;
import src.main.TYPE.Piece_types;
public class Rook extends Piece{
  public boolean Moved = false;
 
   public Rook(String color, int row, int col)
    {
       super(color, row, col); 
      Ids = Piece_types.ROOK;
       if("White".equals(color))
       {
        img = getImage("/PIECES/white-rook");
       }
       else
       {
        img = getImage("/PIECES/black-rook");
       }
    } 
   
    public boolean CanMove(int targetRow, int targetCol){

      if(isWithinBoardLimit(targetRow, targetCol))
      {
          //Rook's movement restriction.
          if((PrevRow == targetRow || PrevCol == targetCol) && !IsSameSquare(targetRow, targetCol)){
                 if(isValidSquare(targetRow, targetCol) && !PieceOnStraightLine(targetRow, targetCol)) 
                  return true;
              }               
      }
      
      return false;
  }
  
}
