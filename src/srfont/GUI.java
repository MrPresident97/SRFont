package srfont;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI implements ActionListener{
    public static JFrame frame = new JFrame();
    public static final Dimension DIM= new Dimension(600,600);
    public JTabbedPane tabs;
    public JPanel tabFont16;
    public JPanel btnPanel;
    public JPanel tabFont;
    public JButton b0;
    public JButton b1;
    public JButton b2;
    public JButton b3;
    public JSpinner sp;
    public GroupLayout g1;
    public BorderLayout g2;
    public JPanel statusBar;
    public JLabel status;
    public JLabel fsize;
    public SRFont srfont;
    public Preview preview;

    public void  initComp() {
        tabs = new JTabbedPane();
        tabFont = new JPanel();
        tabFont16 = new JPanel();
        btnPanel = new JPanel();
        statusBar = new JPanel();
        srfont = new SRFont();
        g1 = new GroupLayout(btnPanel);
        g2 = new BorderLayout();
        b0 = new JButton("Open TTF");
        b1 = new JButton("Save PNG");
        b2 = new JButton("Save Metrics");
        sp = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));

        status = new JLabel("");
        fsize = new JLabel("Font size:");
        preview = new Preview(srfont.image);

        frame.setTitle("SRFont 0.2");
        tabs.addTab("Font 16x16", tabFont16);
        btnPanel.add(b0);
        btnPanel.add(b1);
        btnPanel.add(b2);
        b0.addActionListener(this);
        b1.addActionListener(this);
        b2.addActionListener(this);
        sp.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                srfont.updateFont((int)sp.getValue());
                srfont.draw();
                preview.repaint();
            }
        });
        tabFont16.setLayout(g2);
        btnPanel.setLayout(g1);

        g1.setHorizontalGroup(
            g1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(g1.createSequentialGroup()
                .addContainerGap()
                .addGroup(g1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(b0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, g1.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fsize)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(sp))
                .addComponent(b1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
            .addContainerGap())
        );
        g1.setVerticalGroup(
            g1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(g1.createSequentialGroup()
                .addContainerGap()
                .addComponent(b0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(g1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, 28)
                    .addComponent(fsize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b2)
            .addContainerGap())
        );
        tabFont16.add(btnPanel, BorderLayout.EAST);
        tabFont16.add(statusBar, BorderLayout.SOUTH);
        statusBar.setLayout(new BorderLayout());
        statusBar.setBackground(Color.lightGray);
        status.setBorder(new EmptyBorder(2,6,2,2));
        statusBar.add(status);
        tabFont16.add(preview);
        ((DefaultEditor)sp.getEditor()).getTextField().setEditable(false);
    }

    public void paint(Graphics g) {
        srfont.draw();
    }

    public GUI() {
        initComp();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DIM);
        frame.setPreferredSize(DIM);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.add(tabs);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == b0) {
            srfont.loadTtf();
            srfont.draw();
            preview.repaint();
        }
        if(e.getSource() == b1) {
            srfont.savePng();
        }
        if(e.getSource() == b2) {
            srfont.saveMetrics();
        }
    }
}
