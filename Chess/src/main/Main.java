package src.main;

import javax.swing.JFrame;

public class Main {
    
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess application demo");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);

        //Create a panel for chess
        GamePanel panel = new GamePanel();
        window.add(panel);
        window.pack();

        panel.launchGame();
    }
}
