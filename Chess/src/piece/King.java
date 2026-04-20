package src.piece;
import java.util.ArrayList;
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
    public boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board){
        if(isWithinBoardLimit(targetRow, targetCol))
        {
            //King's movement restriction.
            if(Math.abs(targetRow - PrevRow) + Math.abs(targetCol - PrevCol) == 1 ||
                Math.abs(targetRow - PrevRow) * Math.abs(targetCol - PrevCol) == 1)
                {
                    if(isValidSquare(targetRow, targetCol, board)) 
                    return true;
                }
           
           
            if(Moved == false) {
                // Kingside castling: King moves 2 squares RIGHT
                if (targetCol == PrevCol + 2 && targetRow == PrevRow) {
                    for (Piece piece : board) {
                        // Look for unmoved rook on the kingside (Col 7)
                        if (piece.Ids == Piece_types.ROOK && piece.col == 7 && piece.row == PrevRow && !piece.Moved && piece.PieceColor.equals(this.PieceColor)) {
                            // Verify squares between King and Rook are completely empty
                            Piece square1 = isColliding(PrevRow, PrevCol + 1, board);
                            Piece square2 = isColliding(PrevRow, PrevCol + 2, board);
                            
                            if (square1 == null && square2 == null) {
                                GamePanel.Castling = piece;
                                return true;
                            }
                        }
                    }
                }
                
                // Queenside castling: King moves 2 squares LEFT
                if (targetCol == PrevCol - 2 && targetRow == PrevRow) {        
                    for (Piece piece : board) {
                        // Look for unmoved rook on the queenside (Col 0)
                        if (piece.Ids == Piece_types.ROOK && piece.col == 0 && piece.row == PrevRow && !piece.Moved && piece.PieceColor.equals(this.PieceColor)) {
                            // Verify squares between King and Rook are completely empty
                            Piece square1 = isColliding(PrevRow, PrevCol - 1, board);
                            Piece square2 = isColliding(PrevRow, PrevCol - 2, board);
                            Piece square3 = isColliding(PrevRow, PrevCol - 3, board);
                            
                            if (square1 == null && square2 == null && square3 == null) {
                                GamePanel.Castling = piece;
                                return true;
                            }}}}}}
        return false;
    }
}