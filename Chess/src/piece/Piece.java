package src.piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList; // Replaces the GamePanel dependency
import javax.imageio.ImageIO;
import src.main.Board;
import src.main.TYPE.Piece_types;

// Making the class abstract ensures we only ever create specific pieces (Pawn, Rook, etc.)
public abstract class Piece {
    public Piece_types Ids;
    public BufferedImage img;
    public int x, y;
    public int row, col, PrevRow, PrevCol;
    public String PieceColor = "White";
    public Piece PieceHit;
    public boolean Moved = false, twoStepped = false;
    public boolean captured = false;

    // 1. STANDARD CONSTRUCTOR
    public Piece(String color, int row, int col) {       
        this.PieceColor = color;
        this.row = row;
        this.col = col;
        this.PrevRow = row;
        this.PrevCol = col;
        
        // Corrected mapping: Col represents X (horizontal), Row represents Y (vertical)
        this.x = getX(col);
        this.y = getY(row);
    }

    // 2. COPY CONSTRUCTOR (The Deep Copy Fix)
    // This allows us to clone pieces for move simulation without altering the real game
    public Piece(Piece source) {
        this.Ids = source.Ids;
        this.img = source.img; 
        this.x = source.x;
        this.y = source.y;
        this.row = source.row;
        this.col = source.col;
        this.PrevRow = source.PrevRow;
        this.PrevCol = source.PrevCol;
        this.PieceColor = source.PieceColor;
        this.Moved = source.Moved;
        this.twoStepped = source.twoStepped;
        this.captured = source.captured;
        this.PieceHit = null; // Reset collisions for the clone
    }

    // 3. CORRECTED COORDINATE MATH
    public int getX(int col) {
        return col * Board.Full_block;
    }

    public int getY(int row) {
        return row * Board.Full_block;
    }

    public int getCol(int x) {
        return (x + Board.Half_block) / Board.Full_block;
    }

    public int getRow(int y) {
        return (y + Board.Half_block) / Board.Full_block;
    }

    public BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, Board.Full_block, Board.Full_block, null);
    }

    public void resetPosition() {
        col = PrevCol;
        row = PrevRow;
        x = getX(col); 
        y = getY(row);
    }

    public void updatePosition() {
        if (Ids == Piece_types.PAWN && Math.abs(row - PrevRow) == 2) {
            twoStepped = true;
        }
        x = getX(col); 
        y = getY(row);

        PrevRow = getRow(y);
        PrevCol = getCol(x);   
        Moved = true;   
    }

    // 4. DECOUPLING: Passing the board state directly into the collision methods
    public Piece isColliding(int targetRow, int targetCol, ArrayList<Piece> board) {
        for (Piece p : board) {
            if (p.row == targetRow && p.col == targetCol && p != this && !p.captured) {
                return p;
            }
        }
        return null;
    }

    public boolean isValidSquare(int targetRow, int targetCol, ArrayList<Piece> board) {
        PieceHit = isColliding(targetRow, targetCol, board);

        if (PieceHit == null) {
            return true; // Square is vacant
        } else {
            if (!PieceHit.PieceColor.equals(this.PieceColor)) {
                return true; // Square has an enemy piece
            } else {
                PieceHit = null;
                return false; // Square blocked by friendly piece
            }
        }
    }

    public boolean isWithinBoardLimit(int targetRow, int targetCol) {
        return (targetRow >= 0 && targetRow <= 7) && (targetCol >= 0 && targetCol <= 7);
    }

    public boolean IsSameSquare(int targetRow, int targetCol) {
        return PrevRow == targetRow && PrevCol == targetCol;
    }

    // 5. OPTIMIZED RAYCASTING: No more nested board loops!
    public boolean PieceOnStraightLine(int targetRow, int targetCol, ArrayList<Piece> board) {
        // Determine the direction of movement (-1, 0, or 1)
        int rowDirection = Integer.compare(targetRow, PrevRow);
        int colDirection = Integer.compare(targetCol, PrevCol);

        int currentRow = PrevRow + rowDirection;
        int currentCol = PrevCol + colDirection;

        // Step through the squares one by one until we reach the target
        while (currentRow != targetRow || currentCol != targetCol) {
            Piece collision = isColliding(currentRow, currentCol, board);
            if (collision != null) {
                PieceHit = collision;
                return true; // Path is blocked
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        return false;
    }

    public boolean PieceOnDiagonal(int targetRow, int targetCol, ArrayList<Piece> board) {
        int rowDirection = Integer.compare(targetRow, PrevRow);
        int colDirection = Integer.compare(targetCol, PrevCol);

        int currentRow = PrevRow + rowDirection;
        int currentCol = PrevCol + colDirection;

        while (currentRow != targetRow && currentCol != targetCol) {
            Piece collision = isColliding(currentRow, currentCol, board);
            if (collision != null) {
                PieceHit = collision;
                return true; // Path is blocked
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        return false;
    }

    // 6. ABSTRACT SIGNATURE
    // Forcing all child classes to implement this, passing the board in.
    public abstract boolean CanMove(int targetRow, int targetCol, ArrayList<Piece> board);
}