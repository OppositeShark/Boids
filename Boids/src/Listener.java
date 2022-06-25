import javax.swing.*;
import java.awt.event.*;
import java.awt.Point;
import java.awt.MouseInfo;

public class Listener implements KeyListener, ActionListener{
	public final int FPS = 100;
	private final Timer timer = new Timer(1000/FPS, this);
	private boolean[] keysPressed = new boolean[2048];
	
	private static Vector xy = new Vector(0,0);
	private static Screen screen;
	public Listener(Screen s) {
		screen = s;
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		screen.requestFocus();
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		xy.setX((double)mouse.getX());
		xy.setY((double)mouse.getY());
		Main.run();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed[e.getKeyCode()] = false;
	}
	public static Vector getMouseXY() {
		return xy.clone();
	}
}
