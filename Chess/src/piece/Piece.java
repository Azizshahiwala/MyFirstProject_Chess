package src.piece;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.main.Board;
import src.main.GamePanel;
import src.main.TYPE.Piece_types;

public class Piece{
    public Piece_types Ids;
    public BufferedImage img;
    public int x,y;
    public int row,col,PrevRow,PrevCol;
    public String PieceColor="White";
    public String CurrentColor = PieceColor;
    public Piece PieceHit;
    public boolean Moved = false,twoStepped;
    public boolean captured = false;
    
    public Piece(String Piececolor, int row, int col)
    {       
        this.PieceColor = Piececolor;
        this.row = row;
        this.col = col;
        x = GetX(row);
        y = GetY(col);
        PrevRow = row;
        PrevCol = col;
    }
    public int GetX(int row)
    {
        return row*Board.Full_block;
    }
    public int GetY(int col)
    {
        return col*Board.Full_block;
    }
    public int getCol(int y)
    {
        return (y + Board.Half_block)/Board.Full_block;
    }
    public int getRow(int x)
    {
        return (x + Board.Half_block)/Board.Full_block;
    }
    public BufferedImage getImage(String path)
    {
        BufferedImage img = null;
        try{
            img = ImageIO.read(getClass().getResourceAsStream(path+".png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return img;
    }
    public void CanCastle(){

        return;
    }
    public void draw(Graphics2D g2)
    {
        g2.drawImage(img, x, y,Board.Full_block, Board.Full_block, null);
    }
    public void resetPosition()
    {
        col = PrevCol;
        row = PrevRow;
        x = GetX(row); 
y = GetY(col);
    }

    public void updatePosition(){
        if(Ids == Piece_types.PAWN)
        {
            if(Math.abs(row-PrevRow) == 2)
            twoStepped = true;
        }
    x = GetX(row); 
    y = GetY(col);

        //Previous row and col because the move has confirmed.
        PrevRow = getRow(x);
        PrevCol = getCol(y);   
        
        Moved = true;   
    }
    public Piece isColliding(int targetRow, int targetCol)
    {
        // Check if there's any piece colliding or not.
        for(Piece e: GamePanel.Piece)
        {
            if(e.row == targetRow && e.col == targetCol && e != this){
                return e;
            }
        }
        return null;
    }
    public boolean isValidSquare(int targetRow, int targetCol)
    {
        PieceHit = isColliding(targetRow,targetCol);

        if(PieceHit == null){//Checks if square is vacant
            return true;
        }
        else
        {
            if(PieceHit.PieceColor != this.PieceColor){
                return true;
            }
            else
            {
                PieceHit = null;
            }
        }
        return false;
    }
    public boolean isWithinBoardLimit(int targetRow, int targetCol)
    {
        if((targetRow >=0 && targetRow <=7) && (targetCol >= 0 && targetCol <= 7))
        {
         return true;   
        }
        return false;
    }
    public int findIndex()
    {
        for(int i=0; i<GamePanel.Piece.size(); i++)
        {
            if(GamePanel.Piece.get(i) == this) return i;
        }
        return 0;
    }   
    public boolean IsSameSquare(int targetRow, int targetCol)
    {
        if(PrevRow == targetRow && PrevCol == targetCol) return true;
        return false;
    }
    public boolean PieceOnStraightLine(int targetRow, int targetCol)
    {
        //Check left
        for(int Left = PrevCol-1; Left > targetCol; Left--)
        {
            for(Piece e : GamePanel.Piece)
            {
                if(e.col == Left && e.row == targetRow)// e.row == targetRow because we're checking for right / left side
                {
                    PieceHit = e;
                    return true;
                } 
            }
        }
        //Check right
        for(int Right = PrevCol+1; Right < targetCol; Right++)
        {
            for(Piece e : GamePanel.Piece)
            {
                if(e.col == Right && e.row == targetRow)// e.row == targetRow because we're checking for right / left side
                {
                    PieceHit = e;
                    return true;
                } 
            }
        }
        //Check up
        for(int Up = PrevRow-1; Up > targetRow; Up--)
        {
            for(Piece e : GamePanel.Piece)
            {
                if(e.col == targetCol && e.row == Up)// e.col == targetCol because we're checking for upper / down
                {
                    PieceHit = e;
                    return true;
                } 
            }
        }
        //Check down
        for(int Down = PrevRow+1; Down < targetRow; Down++)
        {
            for(Piece e : GamePanel.Piece)
            {
                if(e.col == targetCol && e.row == Down)// e.col == targetCol because we're checking for down / upper
                {
                    PieceHit = e;
                    return true;
                } 
            }
        }
        return false;
    }
    public boolean PieceOnDiagonal(int targetRow, int targetCol)
    {
        
        //Check Up right / left
       if(targetRow < PrevRow)
       {
        for(int p = PrevCol-1 ; p > targetCol; p--)
        {
            for(Piece e: GamePanel.Piece)
            {
                int df = Math.abs(p-PrevCol);
                
                if(e.col == p && e.row == PrevRow - df)
                {
                    PieceHit = e;
                    return true;
                }
            }
        }

        for(int p = PrevCol+1 ; p < targetCol; p++)
        {
            for(Piece e: GamePanel.Piece)
            {
                int df = Math.abs(p-PrevCol);
                
                if(e.col == p && e.row == PrevRow - df)
                {
                    PieceHit = e;
                    return true;
                }
            }
        }
       }
        //Check Down right / left
       if(targetRow > PrevRow)
       {
        for(int p = PrevCol-1 ; p > targetCol; p--)
        {
            for(Piece e: GamePanel.Piece)
            {
                int df = Math.abs(p-PrevCol);
                
                if(e.col == p && e.row == PrevRow + df)
                {
                    PieceHit = e;
                    return true;
                }
            }
        }

        for(int p = PrevCol+1 ; p < targetCol; p++)
        {
            for(Piece e: GamePanel.Piece)
            {
                int df = Math.abs(p-PrevCol);
                
                if(e.col == p && e.row == PrevRow + df)
                {
                    PieceHit = e;
                    return true;
                }
            }
        }
       }

        
        return false;
    }
    //we're going to override this later.
    public boolean CanMove(int targetRow, int targetCol){
           System.out.println("Base Piece CanMove called. This should be overridden.");
        return false;
    }
    
}
