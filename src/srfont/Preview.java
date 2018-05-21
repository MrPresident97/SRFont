package srfont;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Preview extends Canvas {
	private BufferedImage image;
	public Preview(BufferedImage img) {
		image = img;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image, 4, 4, this);
	}
}
