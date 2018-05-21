package srfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SRFont extends JPanel{
    BufferedImage image = new BufferedImage(64, 512, BufferedImage.TYPE_INT_ARGB);
    JFileChooser fileChooser = new JFileChooser();
    JLabel label = new JLabel("Font size:");
    JTextField spw = new JTextField();
    Font curFont = null;
    FontMetrics fm;
    File header;
    int rows, columns;
    final String chars = " !\"#$%£\'()´+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€�‚ƒ„…†‡ˆ‰Š‹Œ�Ž��‘’“”•–—˜™š›œ�žŸ";
    final Metric[] metrics;
    boolean valid;
    String savePath = "";

    public SRFont(){
        rows = 32;
        columns = 4;
        metrics = new Metric[rows*columns];
        valid = false;
    }
	
    public void draw(){
        for(int i = 0; i < 64; i++){
            for(int j = 0; j < 512; j++){
                image.setRGB(i, j, Color.black.getRGB());
            }
        }
        Graphics g = image.getGraphics();
        g.drawImage(image,0,0,Color.black,null);
        if(curFont != null){
            g.setColor(Color.white);
            g.setFont(curFont);
            fm = g.getFontMetrics(curFont);
            int counter = 0;
            for(int j = 1; j < rows+1; j++){
                for(int i = 0; i < columns; i++){
                    char c = chars.charAt(counter);
                    String s = ""+c;
                    int pr = i*16;
                    int pc = j*16;
                    int w = fm.charWidth(c);
                    //System.out.println(c + ": " + w);
                    metrics[counter] = new Metric(c, pr, pc-16, w);
                    g.drawString(s, pr, pc-3);
                    counter++;
                }
            }
        }
    }
    
    public Font loadFont(File f) throws IOException, FontFormatException{
        return loadFont(f, 16.0f);
    }
    
    public Font loadFont(File fil, double size) throws IOException, FontFormatException{
        FileInputStream fis;
        Font fon = null;
        try{
            fis = new FileInputStream(fil);
            fon = Font.createFont(Font.TRUETYPE_FONT, fis);
            fis.close();
            fon = fon.deriveFont(16.0f);
        }
        catch(IOException e){

        }
        return fon;
    }
    
    public void updateFont(int size){
        if(curFont != null) this.curFont = curFont.deriveFont(Font.PLAIN, size);
    }
    
    void loadTtf(){
        FileFilter filter = new FileNameExtensionFilter("TrueTypeFont", new String[]{"ttf"});
        fileChooser.setFileFilter(filter);
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                curFont = loadFont(fileChooser.getSelectedFile());
                repaint();
            }
            catch (IOException | FontFormatException ex) {
            }
        }
    }
    
    void savePng(){
        if(curFont != null){
            FileFilter filter = new FileNameExtensionFilter("PNG Image", new String[]{"png"});
            fileChooser.setFileFilter(filter);
            if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                draw();
                File outputfile;
                if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".png"))
                    outputfile = new File(fileChooser.getSelectedFile() + ".png");
                else outputfile = fileChooser.getSelectedFile();
                String fname = outputfile.getName();
                int i = fname.lastIndexOf('.');
                if (i <= 0) fname += ".png";
                try {
                    outputfile.renameTo(new File(fname));
                    ImageIO.write(image, "png", outputfile);
                }
                catch(IOException ex){}
            }
        }
    }
    
    void saveMetrics(){
        if(curFont != null){
            FileFilter filter = new FileNameExtensionFilter("C header file", new String[]{"h"});
            fileChooser.setFileFilter(filter);
            boolean foundLine = false;
            header = new File("uvcoord_font_16x16.h");

            if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    File outputfile;
                    if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".h"))
                        outputfile = new File(fileChooser.getSelectedFile() + ".h");
                    else outputfile = fileChooser.getSelectedFile();
                    outputfile.createNewFile();
                    String fname = outputfile.getName();
                    int i = fname.lastIndexOf('.');
                    if (i <= 0) fname += ".h";
                    BufferedReader br = new BufferedReader(new FileReader(header));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
                    bw.flush();
                    while(foundLine != true){
                        String line = br.readLine();
                        String[] words = line.split(" ");
                        for(String word : words) if(word.compareTo("extern") == 0) foundLine = true;
                        bw.write(line+"\n");
                    }
                    for(i = 0; i < rows*columns; i++){
                        bw.write(String.format("\t%s, TEXT_SY, /* %c */\n", metrics[i].toString(), metrics[i].getChar()));
                    }
                    bw.write("};\n#endif");
                    br.close();
                    bw.close();
                } 

                catch (IOException ex) {
                    Logger.getLogger(SRFont.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
    }
}
