import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.lang.Math;

public class Main{
	final static int WIDTH = 1150;
	final static int HEIGHT = 650;
	static JFrame window;
	static Screen screen;
	static Listener listener;
	static Solver solver;
	
	static long startTime;
	static long lastFrame;
	final static int DIVISIONS = 1;
	static Random random = new Random();
	
	public static void main(String[] args) {
		startTime = System.currentTimeMillis();
		lastFrame = startTime;
		
		window = new JFrame("Boids");
        window.setSize(WIDTH, HEIGHT);
        window.setLocation(0,0);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.BLACK);
        
        solver = new Solver();
        screen = new Screen();
        window.setContentPane(screen);
        
        listener = new Listener(screen);
		screen.addKeyListener(listener);
		screen.setDoubleBuffered(true);
		
        window.setVisible(true);
        restart();
	}
	public static void restart() {
		Entity.clear();
		for(int i = 0; i < 2; i++) {
			new Big();
		}
		for(int i = 0; i < 7; i++) {
			new Predator();
		}
		for(int i = 0; i < 450; i++) {
			new Boid();
		}
		for(int i = 0; i < 30; i++) {
			new Prey();
		}
	}
	public static void run() {
		double dt = (double)(System.currentTimeMillis()-lastFrame)/1000;
		for(int i = 0; i<DIVISIONS; i++) {
			solver.solve(dt/DIVISIONS);
		}
		screen.repaint();
		lastFrame = System.currentTimeMillis();
	}
	
}