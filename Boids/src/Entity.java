import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Comparator;

public class Entity {
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	private Vector xy;
	private Vector v;
	
	public Entity(Vector xy, Vector v) {
		this.xy = xy.clone();
		this.v = v.clone();
		entities.add(this);
	}
	public void move(double dt) {
		this.xy.addToThis(v.mult(dt));
	}
	public void shift(Vector v) {
		this.xy.addToThis(v);
	}
	public void turn(double ang, double dt) {
		this.v.rotate(ang*dt);
	}
	public void accelerate(Vector accel, double dt) {
		this.v.addToThis(accel.mult(dt));
	}
	public static void add(Entity e) {
		entities.add(e);
	}
	public static void remove(Entity e) {
		entities.remove(e);
	}
	public static void clear() {
		entities.clear();
	}
	public void draw(Graphics2D g2d) {
		Graphics2D copy = (Graphics2D)g2d.create();
		copy.translate(this.xy.getX(),this.xy.getY());
		copy.setPaint(Color.WHITE);
		copy.setStroke(new BasicStroke(3));
		copy.drawRect(-5, -5, 10, 10);
	    copy.dispose();
	}
	public void run(double dt) {
	}
	public void reset() {
		this.xy = new Vector(0,0);
		this.v = new Vector(0,0);
	}
	public void setX(double x) {
		this.xy.setX(x);
	}
	public void setY(double y) {
		this.xy.setY(y);
	}
	public void setXY(Vector xy) {
		this.xy = xy;
	}
	public void setV(Vector v) {
		this.v = v.clone();
	}
	public double getX() {
		return this.xy.getX();
	}
	public double getY() {
		return this.xy.getY();
	}
	public Vector getXY() {
		return this.xy.clone();
	}
	public Vector getV() {
		return this.v.clone();
	}
	public double getSpeed() {
		return this.v.getMagnitude();
	}
}