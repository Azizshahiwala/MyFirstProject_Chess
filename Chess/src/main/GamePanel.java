package src.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import src.main.TYPE.Piece_types;
import src.piece.Bishop;
import src.piece.King;
import src.piece.Knight;
import src.piece.Pawn;
import src.piece.Piece;
import src.piece.Queen;
import src.piece.Rook;

import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    public static final int panelWidth = 1100;
    public static final int panelHeight = 800;
    final int FPS = 60;
    Thread Screen;
    boolean ValidSquare = false,canMove = false, GAMEOVER=false;
   
    boolean Stalemate = false;
    public int Fifty_Fifty = 0;    
    public int StalemateCondition = -1;
    boolean promotion;
    
    Board board = new Board();
    //Class to track mouse events
    Events mouse = new Events();
    
    //Class to get active piece by player
    public Piece ActivePiece;
    public Piece Check;
    public static Piece Castling;
    static String PlayerColor = "White";
    String CurrentColor = PlayerColor;

    public static ArrayList<Piece> Piece= new ArrayList<>();
    public static ArrayList<Piece> BackupPiece= new ArrayList<>();
    public ArrayList<Piece> PromotionPieces = new ArrayList<>();
    
    //constructor to setup game panel size and color.
    public GamePanel()
    {      
        setForeground(Color.black);
        setBackground(Color.gray);
        setPreferredSize(new Dimension(panelWidth,panelHeight));

        setPieces();
        copyPieces(Piece, BackupPiece);

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }
    
    private void changeTurn()
    {
        if(CurrentColor.equals("White"))
        {
            Fifty_Fifty++;
            CurrentColor = "Black";
            for(Piece e: Piece)
            {
                if(e.PieceColor.equals("Black")){e.twoStepped = false;}
            }
        }
        else
        {        
            Fifty_Fifty++;
            CurrentColor = "White";
            for(Piece e: Piece)
            {
                if(e.PieceColor.equals("White")){e.twoStepped = false;}
            }     
        }
        ActivePiece = null;
    }
    
    //Piece variables for castling white:
    Piece whiteRook1 = new Rook("White", 0, 7)
    ,whiteKnight1 = new Knight("White", 1, 7)
    ,whiteBishop1 = new Bishop("White", 5, 7) 
    ,whiteQueen = new Queen("White", 3, 7)
    ,whiteKing = new King("White", 4, 7)
    ,whiteBishop2 = new Bishop("White", 2, 7)
    ,whiteKnight2 = new Knight("White", 6, 7)
    ,whiteRook2 = new Rook("White", 7, 7);

    //Piece variables for castling black:
    Piece blackRook1 = new Rook("Black", 0, 0)
    ,blackKnight1 = new Knight("Black", 1, 0)
    ,blackBishop1 = new Bishop("Black", 5, 0) 
    ,blackQueen = new Queen("Black", 3, 0)
    ,blackKing = new King("Black", 4, 0)
    ,blackBishop2 = new Bishop("Black", 2, 0)
    ,blackKnight2 = new Knight("Black", 6, 0)
    ,blackRook2 = new Rook("Black", 7, 0);

    public void setPieces()
    {
        //White team

        //Pawn structure
        Piece.add(new Pawn("White",0,6));
        Piece.add(new Pawn("White",1,6));
        Piece.add(new Pawn("White",2,6));
        Piece.add(new Pawn("White",3,6));
        Piece.add(new Pawn("White",4,6));
        Piece.add(new Pawn("White",5,6));
        Piece.add(new Pawn("White",6,6));
        Piece.add(new Pawn("White",7,6));

        //Rooks
        Piece.add(whiteRook1);
        Piece.add(whiteRook2);

        //Bishops
        Piece.add(whiteBishop1);
        Piece.add(whiteBishop2);

        //Knights
        Piece.add(whiteKnight1);
        Piece.add(whiteKnight2);

        //King
        Piece.add(whiteKing);

        //Queen
        Piece.add(whiteQueen);

        //Black team
        //Pawn structure
        Piece.add(new Pawn("Black",0,1));
        Piece.add(new Pawn("Black",1,1));
        Piece.add(new Pawn("Black",2,1));
        Piece.add(new Pawn("Black",3,1));
        Piece.add(new Pawn("Black",4,1));
        Piece.add(new Pawn("Black",5,1));
        Piece.add(new Pawn("Black",6,1));
        Piece.add(new Pawn("Black",7,1));

        //Rooks
        Piece.add(blackRook1);
        Piece.add(blackRook2);

        //Bishops
        Piece.add(blackBishop1);
        Piece.add(blackBishop2);

        //Knights
        Piece.add(blackKnight1);
        Piece.add(blackKnight2);

        //King
        Piece.add(blackKing);

        //Queen
        Piece.add(blackQueen);
     }

    //setup a seperate thread for game loop.
    public void run()
    {
        final double timePerFrame = 1000000000.0 / FPS; // in nanoseconds
        long lastTime = System.nanoTime();
        double delta = 0;

        while (Screen != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / timePerFrame;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            try {
                Thread.sleep(1); // Prevent CPU hogging
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //paintComponent method, whenever it needs to be re-painted. From JPanel
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D newG = (Graphics2D)(g);
        board.draw(newG);

        for(Piece e: Piece)
        {
            e.draw(newG);
        }

        if(ActivePiece != null)
        {
                if((isIllegal(ActivePiece) || OpponentCanCapture()))
                {
                newG.setColor(Color.red);
                newG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f)); 
                newG.fillRect(ActivePiece.row*Board.Full_block, ActivePiece.col*Board.Full_block, Board.Full_block,Board.Full_block); 
                newG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f)); 
                }
                else if(canMove)
                {
                newG.setColor(Color.gray);
                newG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f)); 
                newG.fillRect(ActivePiece.row*Board.Full_block, ActivePiece.col*Board.Full_block, Board.Full_block,Board.Full_block); 
                newG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f)); 
                }
        
            //At the end draw this piece.
            ActivePiece.draw(newG);
        }
        if(promotion)
        {
            g.setFont(new Font("Adobe",Font.PLAIN,40));
            g.setColor(Color.WHITE);
            g.drawString("Promote to:", 840,150);
            for(Piece e: PromotionPieces)
            {
                g.drawImage(e.img,e.GetX(e.row),e.GetY(e.col),Board.Full_block,Board.Full_block,null);
            }
        }

        if(GAMEOVER)
        {                   
            String PlayerWinColor = CurrentColor + " Team wins!";
            System.out.println(PlayerWinColor);
            g.setFont(new Font("Arial",Font.PLAIN,90));
            g.setColor(Color.green);
            g.drawString(PlayerWinColor, 200, 420);
        }
        else if(Stalemate && !isKingInCheck())
        {
            g.setFont(new Font("Arial",Font.PLAIN,70));
            g.setColor(Color.green);

            if(StalemateCondition == 1)
            g.drawString("Draw by insufficient matrial!", 200, 420); 
            else if(StalemateCondition == 2)
            g.drawString("Draw by No legal moves!", 200, 420);
            else if(StalemateCondition == 3)
            g.drawString("Draw by rule 50-50!", 200, 420);

             System.out.println("DRAW");
        }
        
    }
    private void CheckCastle()
{
    if (GamePanel.Castling != null) {
        if (ActivePiece.row == ActivePiece.PrevRow + 2) { // Kingside
            Castling.row = ActivePiece.PrevRow + 1; // Rook moves to the square next to the king
        } 
        else if (ActivePiece.row == ActivePiece.PrevRow - 2) { // Queenside
            Castling.row = ActivePiece.PrevRow - 1; // Rook moves to the square next to the king
        }

    Castling.x = Castling.GetX(Castling.row);
}
}
    private void update()
    {
        StalemateCondition = isStalemate();
            if(promotion)
            {
            PromotionProcess();
            //When promotion is true, we stop the game for a moment to choose from.
            }
            else if(GAMEOVER == false){
            //Check if mouse is pressed.
                if(mouse.Pressed){
            //If there's no active piece go here
                if(ActivePiece == null){
                //If piece is null, check if you can pick it up as acticeP
                for(Piece e : Piece)
                {
                    if((e.PieceColor).equals(CurrentColor) && (e.row == mouse.x/Board.Full_block && e.col == mouse.y/Board.Full_block))
                        ActivePiece = e;
                }
            }
            //If piece is active stimulate thinking process / Move which is not yet confirmed.
            else stimulate();            
        }    

            if(!mouse.Pressed){
        if(ActivePiece!= null){
                if(ValidSquare)
                {
                    //Update changes if player's mouse is released.
                    copyPieces(Piece, BackupPiece);
                    ActivePiece.updatePosition();
                    
                    //Update changes when mouse is released.
                    if(Castling!=null){
                        Castling.updatePosition();
                        Castling = null;          
                    }
                    copyPieces(Piece, BackupPiece);

                    if(ActivePiece.Ids == Piece_types.PAWN && (ActivePiece.row == 4 || ActivePiece.row == 3)) ActivePiece.twoStepped = true;
                    
                    if(isKingInCheck() && isCheckmatePossible()){                    
                        //Check about checkmate and checks
                        GAMEOVER = true;    
                    System.out.println("iskingincheck = "+isKingInCheck());
                    System.out.println("ischeckmatepossible = "+isCheckmatePossible());
                    }
                    else{
                        if(canPromote()) promotion = true;
                        else changeTurn();
                    }  
                    
                    if(StalemateCondition >= 1)
                    {
                        Stalemate = true;
                    }
                }        
                else
                {
                    //Update changes if player's mouse is released on incorrect piece.
                    copyPieces(BackupPiece, Piece);
                    ActivePiece.resetPosition();           
                    ActivePiece = null;
                }
             }
           }
        }       
    }
    private void PromotionProcess(){
        if(mouse.Pressed)
        {
            for(Piece e: PromotionPieces)
            {          
                if(e.row == mouse.x/Board.Full_block && e.col == mouse.y/Board.Full_block)
                {
                   switch (e.Ids) {
                    case QUEEN:
                    System.out.println("Queen selected");
                    Piece.add(new Queen(CurrentColor,ActivePiece.row, ActivePiece.col));
                    break;
                    case ROOK:
                    System.out.println("Rook selected");
                    Piece.add(new Rook(CurrentColor,ActivePiece.row, ActivePiece.col));
                    break;
                    case BISHOP:
                    System.out.println("Bishop selected");
                    Piece.add(new Bishop(CurrentColor,ActivePiece.row, ActivePiece.col));
                    break;
                    case KNIGHT:
                    System.out.println("Knight selected");
                    Piece.add(new Knight(CurrentColor,ActivePiece.row, ActivePiece.col));
                    break;
                    default:
                    System.out.println("No option selected");
                    break;
                   }

                   Piece.remove(ActivePiece.findIndex());
                   copyPieces(Piece,BackupPiece);
                   ActivePiece = null;
                   promotion = false;
                   changeTurn();
                   break;
                }
            }
                
        }
    }
 private boolean isCheckmatePossible() {
    System.out.println("Check1");
    Piece king = getking(true);  // Get the current player's king
    
     if (KingCanEscape(king)) 
        return false;
    else
    {
        int RowDiff = Math.abs(Check.row - king.row);
    int ColDiff = Math.abs(Check.col - king.col);
    // Direction of attack - blocking moves
    // this code already finds attacking path.
    if (ColDiff == 0) {  // Vertical attack
        if (Check.row < king.row) {
            for (int r = Check.row + 1; r < king.row; r++) {
                for (Piece p : Piece) {
                    if (p != king && p.PieceColor.equals(king.PieceColor) && p.CanMove(r, Check.col))
                        return false;
                }
            }
        } else {
            for (int r = Check.row - 1; r > king.row; r--) {
                for (Piece p : Piece) {
                    if (p != king && p.PieceColor.equals(king.PieceColor) && p.CanMove(r, Check.col))
                        return false;
                }
            }
        }
    } else if (RowDiff == 0) {  // Horizontal attack
        if (Check.col < king.col) {
            for (int c = Check.col + 1; c < king.col; c++) {
                for (Piece p : Piece) {
                    if (p != king && p.PieceColor.equals(king.PieceColor) && p.CanMove(king.row, c))
                        return false;
                }
            }
        } else {
            for (int c = Check.col - 1; c > king.col; c--) {
                for (Piece p : Piece) {
                    if (p != king && p.PieceColor.equals(king.PieceColor) && p.CanMove(king.row, c))
                        return false;
                }
            }
        }
    } else if (RowDiff == ColDiff) {  // Diagonal attack
        int rowStep = (Check.row < king.row) ? 1 : -1;
        int colStep = (Check.col < king.col) ? 1 : -1;

        int r = Check.row + rowStep;
        int c = Check.col + colStep;

        while (r != king.row && c != king.col) {
            for (Piece p : Piece) {
                if (p != king && p.PieceColor.equals(king.PieceColor) && p.CanMove(r, c)) {
                    return false;
                }
            }
            r += rowStep;
            c += colStep;
        }
    } 
    else{
        System.out.println("knight check");
    }
}
    // If no escape, no block, no capture => checkmate
    return  true;
}
    private int isStalemate()
    {
        int PieceCount = 0;
        for(Piece e: Piece)
        {
            if(e.Ids != Piece_types.KING)
            PieceCount++;
        }

        if(PieceCount == 0 && KingCanEscape(getking(true)) == false)
        {
            return 1; // 1 means Draw by insufficient material from both sides
        }
        else if(Fifty_Fifty >= 100)
        {
            return 3; // 50 - 50 move rule draw
        }

        return -1; // -1 means Game can be continued.
    }
    public Piece getAttackingPiece(Piece king) {
    for (Piece p : GamePanel.Piece) {
        if (!p.PieceColor.equals(king.PieceColor) && !p.captured) {
            if (p.CanMove(king.row, king.col)) {
                System.out.println("Attacking piece: " +p);
                return p;
            }
        }
    }
    return null;
    }
    //Check if king can move
    private boolean KingCanEscape(Piece king){
        //stimulate directions
        if(isValidMoveForKing(king,-1,-1)) return true;
        if(isValidMoveForKing(king,0,-1)) return true;
        if(isValidMoveForKing(king,1,-1)) return true;
        if(isValidMoveForKing(king,-1,0)) return true;
        if(isValidMoveForKing(king,1,0)) return true;
        if(isValidMoveForKing(king,-1,1)) return true;
        if(isValidMoveForKing(king,0,1)) return true;
        if(isValidMoveForKing(king,1,1)) return true;
        
        return false;
    }
    //we will check for hypothetical position if king can move in a square or not
    private boolean isValidMoveForKing(Piece king, int rowPlus, int colPlus){
        boolean isValidMove = false;
        king.row += rowPlus;
        king.col += colPlus;
        if(king.CanMove(king.row, king.col))
        {
            if(king.PieceHit!=null)
            {
                Piece.remove(king.PieceHit.findIndex());
            }

            if(isIllegal(king) == false)
            {
                isValidMove = true;
            }
        }
        //Reset if stimulation over.
        king.resetPosition();
        copyPieces(BackupPiece,Piece);

        return isValidMove;
    }
    public Piece getking(boolean OpponentsPiece){
        //This method returns Piece if the king is opponent's or not
        Piece king = null;
        for(Piece e: GamePanel.Piece){
            if(e == null) continue;
            if(e.Ids == Piece_types.KING)
            {
               if(OpponentsPiece)
               {
                if(!e.PieceColor.equals(CurrentColor)) king = e;
               } 

               else if (!OpponentsPiece)
               {
                if(e.PieceColor.equals(CurrentColor)) king = e;
               }
            }
        }
        return king;
    }
    public boolean isKingInCheck() {
    Piece king = getking(true);  // Get opponent king
    if (king == null) return false;

   if(ActivePiece.CanMove(king.row, king.col))
   {
    Check = ActivePiece;
    return true;
   }
   else{  
    Check = null;
   }
return false;
}
    //Check if king can move in occupied spaces
    protected boolean isIllegal(Piece King)
    {
        if(King.Ids == Piece_types.KING)
        {
            for(Piece e: Piece)
            {                       
                if(e != King && !e.PieceColor.equals(King.PieceColor) && e.CanMove(King.row, King.col)){
                    return true;
                }
            }
        }
        return false;
    }
    //Check if a piece is about to capture king
    private boolean OpponentCanCapture()
    {
    Piece king = getking(false);

    for(Piece piece: Piece)
    {
        if(!piece.PieceColor.equals(king.PieceColor) && piece.CanMove(king.row, king.col))
        {     
            return true;
        }
    } 
    return false;
}
    private void stimulate()
    {
        int OriginalRow = ActivePiece.row;
        int OriginalCol = ActivePiece.col;

        canMove= false;
        ValidSquare = false;

        //Fix the pieces first
        copyPieces(BackupPiece, Piece);

        if(Castling != null)
        {
            Castling.row = Castling.PrevRow;
            Castling.x = Castling.GetX(Castling.row);
            Castling = null;
        }
        
        //Make sure that mouse pointer is between the image, not at top left.
        ActivePiece.x = mouse.x - Board.Half_block;
        ActivePiece.y = mouse.y - Board.Half_block;

        ActivePiece.row = ActivePiece.getRow(ActivePiece.x);
        ActivePiece.col = ActivePiece.getCol(ActivePiece.y);

        ActivePiece = Piece.get(ActivePiece.findIndex());
                
        // Check if moving to a square occupied by a friendly piece
    for (Piece e : Piece) {
        if (e != ActivePiece && e.row == ActivePiece.row && e.col == ActivePiece.col) {
            if (e.PieceColor.equals(ActivePiece.PieceColor)) {
                // Friendly piece in target square — disallow move
                ValidSquare = false;
                canMove = false;
                ActivePiece.row = OriginalRow;
                ActivePiece.col = OriginalCol;
                return;
            }
        }
    }     
        // Basic move and legality check
    if(ActivePiece.CanMove(ActivePiece.row,ActivePiece.col)){
            if(isIllegal(ActivePiece) == false && OpponentCanCapture()==false){
            ValidSquare = true;
            canMove = true;
            }
        }     

        if(ActivePiece.PieceHit != null)
            Piece.remove(ActivePiece.PieceHit.findIndex());

        CheckCastle();

        if (ActivePiece instanceof Pawn && ActivePiece.PieceHit == null && Math.abs(ActivePiece.row - ActivePiece.PrevRow) == 1 && Math.abs(ActivePiece.col - ActivePiece.PrevCol) == 1) {
    //Find and remove captured pawn :)
            for (int i = 0; i < Piece.size(); i++) {    
        Piece p = Piece.get(i);
        if (p.Ids == Piece_types.PAWN && !p.PieceColor.equals(ActivePiece.PieceColor) && p.row == ActivePiece.row && p.col == ActivePiece.PrevCol && p.twoStepped) {
            Piece.remove(i);
            break;
            }
        } 
    }
}
   public boolean canPromote()
   {    if(ActivePiece.Ids == Piece_types.PAWN){
            if(CurrentColor.equals("White") && ActivePiece.col==0 || CurrentColor.equals("Black") && ActivePiece.col==7)
            {
            PromotionPieces.clear();
            PromotionPieces.add(new Queen(CurrentColor,9,2));
            PromotionPieces.add(new Rook(CurrentColor,9,3));
            PromotionPieces.add(new Bishop(CurrentColor,9,4));
            PromotionPieces.add(new Knight(CurrentColor,9,5));
            return true;
            }
        }
        
    return false;
   }
    public void copyPieces(ArrayList<Piece> Source, ArrayList<Piece> Target)
    {
            Target.clear();
            for(int i=0; i<Source.size(); i++)
            Target.add(Source.get(i));

    }
    //launches the game using a seperate thread.
    public void launchGame()
    {
        Screen = new Thread(this);
        Screen.start();
    }
}