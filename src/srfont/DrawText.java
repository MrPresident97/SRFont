package srfont;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
public class DrawText extends JPanel implements ActionListener{
	static final long serialVersionUID = 1L;
	Graphics g;
	BufferedImage image = new BufferedImage(64, 512, BufferedImage.TYPE_INT_ARGB);
	//Container cont = new Container();
	GroupLayout gl = new GroupLayout(this);
	JButton openBtn = new JButton("Choose Font...");
	JButton saveBtn = new JButton("Save");
	JFileChooser fontChooser = new JFileChooser();
	Font fif = null;
	int rows, columns;
	final String chars = " !\"#$%£\'()´+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€�‚ƒ„…†‡ˆ‰Š‹Œ�Ž��‘’“”•–—˜™š›œ�žŸ";
	public DrawText(){
		rows = 32;
		columns = 4;
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 512; j++){
				image.setRGB(i, j, Color.black.getRGB());
			}
		}
                setLayout(gl);
                gl.setAutoCreateGaps(true);
                gl.setAutoCreateContainerGaps(true);
                
                GroupLayout.SequentialGroup hg = gl.createSequentialGroup();
                hg.addGroup(gl.createParallelGroup().
                    addComponent(openBtn));
                hg.addGroup(gl.createParallelGroup().
                    addComponent(saveBtn));
                
                GroupLayout.SequentialGroup vg = gl.createSequentialGroup();
                vg.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
                        addComponent(openBtn).addComponent(saveBtn));
                
                gl.setHorizontalGroup(hg);
                gl.setVerticalGroup(vg);
	}
	@Override
	public void paint (Graphics g){
            super.paint(g);
            if(fif != null){
                draw(image.getGraphics());
                draw(g);
                File outputfile = new File("/home/doom/Desktop/image.png");
                try {
                        ImageIO.write(image, "png", outputfile);
                }
                catch (IOException e) {
                }
            }
	}
	
    public void draw(Graphics g){
        g.drawImage(image,0,0,Color.black,null);
        g.setColor(Color.white);
        if(fif != null) g.setFont(fif.deriveFont(16.0f));
        System.out.println(g.getFont());
        int counter = 0;
        for(int j = 1; j < rows+1; j++){
            for(int i = 0; i < columns; i++){
                char c = chars.charAt(counter);
                int pr = i*16;
                int pc = j*16;
                g.drawString(""+c, pr, pc);
                counter++;
            }
        }

    }

    public Font loadFont(File fil) throws IOException, FontFormatException{
            FileInputStream fis = null;
            Font fon = null;
            try{
                    fis = new FileInputStream(fil);
                    fon = Font.createFont(Font.TRUETYPE_FONT, fis);

            }
            catch(IOException e){

            }
            fis.close();
            return fon;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == openBtn){
                FileFilter filter = new FileNameExtensionFilter("TrueTypeFont", new String[]{"ttf"});
                fontChooser.setFileFilter(filter);
            if(fontChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    fif = loadFont(fontChooser.getSelectedFile());
                    repaint();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                catch (FontFormatException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
	
	
	
}
