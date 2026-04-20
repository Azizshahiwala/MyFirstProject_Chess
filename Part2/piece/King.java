package src.piece;
import src.main.GamePanel;
import src.main.TYPE.Piece_types;
public class King extends Piece{
  
    public King(String color, int row, int col)
    {
       super(color, row, col); 
       Ids = Piece_types.KING;
       if("White".equals(color))
       {
        img = getImage("/PIECES/white-king");
       }
       else
       {
        img = getImage("/PIECES/black-king");
       }
    }
    public boolean CanMove(int targetRow, int targetCol){
        if(isWithinBoardLimit(targetRow, targetCol))
        {
            //King's movement restriction.
            if(Math.abs(targetRow - PrevRow) + Math.abs(targetCol - PrevCol) == 1 ||
                Math.abs(targetRow - PrevRow) * Math.abs(targetCol - PrevCol) == 1)
                {
                    if(isValidSquare(targetRow, targetCol)) 
                    return true;
                }
           
           if(Moved == false){
             // Kingside castling: King moves 2 squares right (PrevRow + 2)
             if (targetRow == PrevRow + 2 && targetCol == PrevCol && !PieceOnStraightLine(targetRow, targetCol)) {
                for (Piece piece : GamePanel.Piece) {
                    // Look for unmoved rook on the kingside (at col 7 for king's side)
                    if (piece.Ids == Piece_types.ROOK && piece.col == PrevCol && piece.row == 7 && !piece.Moved && piece.PieceColor.equals(this.PieceColor)) {
                        GamePanel.Castling = piece;
                        return true;
                    }
                }
            }
            
            // Queenside castling: King moves 2 squares left (PrevRow - 2)
            if(targetRow == PrevRow-2 && targetCol == PrevCol && !PieceOnStraightLine(targetRow, targetCol))
            {        
                for(Piece piece : GamePanel.Piece){
                    // Look for unmoved rook on the queenside (at col 0 for queen's side)
                    if(piece.Ids == Piece_types.ROOK && piece.col == PrevCol && piece.row == 0 && !piece.Moved && piece.PieceColor.equals(this.PieceColor)){
                        // Check that squares between king and rook are empty
                        Piece Square1 = isColliding(PrevRow - 1, PrevCol);
                        Piece Square2 = isColliding(PrevRow - 2, PrevCol);
                        Piece Square3 = isColliding(PrevRow - 3, PrevCol);
                        
                        if(Square1 == null && Square2 == null && Square3 == null && !PieceOnStraightLine(PrevRow, targetCol))
                        {
                            GamePanel.Castling = piece;
                            return true;
                        }
                    }
                }
            }
        }
    }
        return false;
    }
}