package srfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrawText extends JPanel implements ActionListener{
    static final long serialVersionUID = 1L;
    Graphics g;
    BufferedImage image = new BufferedImage(64, 512, BufferedImage.TYPE_INT_ARGB);
    //Container cont = new Container();
    GroupLayout gl = new GroupLayout(this);
    JButton openBtn = new JButton("Choose Font");
    JButton saveBtn = new JButton("Save PNG");
    JButton saveMetricsBtn = new JButton("Save Metrics");
    JFileChooser fileChooser = new JFileChooser();
    JTextField spw = new JTextField();
    Font curFont = null;
    FontMetrics fm;
    File header;
    int rows, columns;
    final String chars = " !\"#$%£\'()´+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€�‚ƒ„…†‡ˆ‰Š‹Œ�Ž��‘’“”•–—˜™š›œ�žŸ";
    final int[] metrics;
    boolean valid;
    String savePath = "";

    public DrawText(){
        rows = 32;
        columns = 4;
        metrics = new int[rows*columns];
        valid = false;
        for(int i = 0; i < 64; i++){
            for(int j = 0; j < 512; j++){
                image.setRGB(i, j, Color.black.getRGB());
            }
        }
        init();
    }
    
    private void init(){
        openBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        saveMetricsBtn.addActionListener(this);
        
        setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hg = gl.createSequentialGroup();
        hg.addGroup(gl.createParallelGroup().
            addComponent(openBtn));
        hg.addGroup(gl.createParallelGroup().
            addComponent(saveBtn));
        hg.addGroup(gl.createParallelGroup().
            addComponent(saveMetricsBtn));

        GroupLayout.SequentialGroup vg = gl.createSequentialGroup();
        vg.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(openBtn).addComponent(saveBtn).addComponent(saveMetricsBtn));

        gl.setHorizontalGroup(hg);
        gl.setVerticalGroup(vg);
    }
    
    @Override
    public void paint (Graphics g){
        super.paint(g);
        if(curFont != null){
            draw(g);
        }
    }
	
    public void draw(Graphics g){
        g.drawImage(image,0,0,Color.black,null);
        g.setColor(Color.white);
        g.setFont(curFont);
        fm = g.getFontMetrics(curFont);
        System.out.println(g.getFont());
        int counter = 0;
        for(int j = 1; j < rows+1; j++){
            for(int i = 0; i < columns; i++){
                char c = chars.charAt(counter);
                metrics[counter] = fm.charWidth(c)+1;
                String s = ""+c;
                int pr = i*16;
                int pc = j*16 -3;
                g.drawString(s, pr, pc);
                counter++;
            }
        }
    }

    public Font loadFont(File fil) throws IOException, FontFormatException{
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
        FileFilter filter = new FileNameExtensionFilter("PNG Image", new String[]{"png"});
        fileChooser.setFileFilter(filter);
        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            draw(image.getGraphics());
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
    
    void saveMetrics(){
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
                    System.out.println(line);
                    bw.write(line+"\n");
                }
                br.close();
                bw.close();
            } 

            catch (IOException ex) {
                System.out.println(new File(".").getAbsolutePath());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == openBtn){
            loadTtf();
        }
        else if(e.getSource() == saveBtn && curFont != null){
            savePng();
        }
        else if(e.getSource() == saveMetricsBtn && curFont != null){
            saveMetrics();
        }
    }
}
