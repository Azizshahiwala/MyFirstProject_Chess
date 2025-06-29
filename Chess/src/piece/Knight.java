package src.piece;
import src.main.TYPE.Piece_types;
public class Knight extends Piece{
    
    public Knight(String color, int row, int col)
    {
       super(color, row, col); 
        Ids = Piece_types.KNIGHT;
       if("White".equals(color))
       img = getImage("/PIECES/white-knight");
       
       else
       img = getImage("/PIECES/black-knight");
       
    }
    public boolean CanMove(int targetRow, int targetCol){

        if(isWithinBoardLimit(targetRow, targetCol))
        {
            //Knight's movement restriction (1:2 or 2:1).
            if (Math.abs(targetRow - PrevRow) * Math.abs(targetCol - PrevCol) == 2)
                {
                    if(isValidSquare(targetRow, targetCol)) 
                    return true;
                }              
        }
        return false;
    }
}
