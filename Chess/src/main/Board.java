package src.main;

import java.awt.Graphics2D;
import java.awt.Color;
public class Board {
    //chess board has 8x8 blocks
    final int COL = 8;
    final int ROW = 8;
    //each block should consist of 100 x 100 pixels. Making total of 800x800 pixels
    public static final int Full_block = 100;
    public static final int Half_block = Full_block/2;

    //Logic for interchanging squares
    public void draw(Graphics2D g2){
        
        for(int row=0; row<ROW; row++)
        {
            for(int col=0; col<COL; col++)
            {
                if((row+col) % 2 == 0)
                {
                    // White (or use another color)
                    g2.setColor(new Color(255,255,255));      
                }
                else
                {
                    g2.setColor(new Color(118, 150, 86));               
                }

                g2.fillRect(row*Full_block, col*Full_block, Full_block, Full_block);
            }
        }
    }
}
