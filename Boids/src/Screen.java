import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class Screen extends JPanel{
	public Screen() {
		super();
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setPaint(new Color(0,0,0,20));
		g2d.fillRect(0,0,this.getWidth(),this.getHeight());
		for(int i = 0; i<Entity.entities.size(); i++) {
			Entity.entities.get(i).draw(g2d);
		}
		g2d.dispose();
	}
}