package srfont;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class SRFont {
    JFrame frame;
    DrawText t;
    static int width=800;
    static int height=600;

    public static void main(String[]args){
        Dimension dimensionScreen = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        DrawText t = new DrawText();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        int tmp0 = (int) ((dimensionScreen.getWidth() - width )/ 2);
        int tmp1 = (int) ((dimensionScreen.getHeight() - height) / 2); 
        //frame.setLocation(tmp0,tmp1);
        frame.add(t);
    }
}