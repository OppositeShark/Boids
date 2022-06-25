import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.Math;


public class Boid extends Entity{
	final static String[] names = {
			"Default",
			"Prey",
			"Predator",
			"BIG"
	};
	final static double[] separation = {
			35.0, 
			1.0,
			9999.0,
			1.0};
	final static double[] alignment = {
			2.5, 
			-0.5,
			1.0,
			1.0};
	final static double[] cohesion = {
			2.0,
			-1.0,
			-9999.0,
			0.3};
	final static double mouseFollow = 0.0;

	final static double maxSpeed = 100;
	final static double acceleration = 700;
	final static double constAcceleration = 100;
	final static double resistance = -3.0;
	
	final static double senseRange = 70;
	final static double senseAng = Math.PI*(6.0/6.0);
	final static double avoidDist = 30;
	final static int maxSee = 20;
	
	static Random r = new Random();
	public static ArrayList<Boid> boids = new ArrayList<Boid>();
	
	static int currid = 0;
	private int species;
	private int id;
	
	public Boid() {
		super(new Vector(r.nextDouble()*Main.WIDTH,r.nextDouble()*Main.HEIGHT),
				Vector.getUnitVec(r.nextDouble()*2.0*Math.PI).mult(maxSpeed));
		boids.add(this);
		this.id = currid;
		currid += 1;
		this.species = 0;
	}
	public static void add(Boid b) {
		boids.add(b);
	}
	public static void remove(Entity b) {
		boids.remove(b);
	}
	public static void clear() {
		boids.clear();
	}
	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D copy = (Graphics2D)g2d.create();
		copy.translate(this.getX(),this.getY());
		double ang = this.getV().getAng();
		copy.rotate(ang);
		/*
		BufferedImage img = ImageIO.read("C:\\Users\\degog\\OneDrive\\Pictures\\Images\\Bear.png");
		copy.drawImage(img, 0, 0);
		*/
		
		copy.scale(0.5, 0.5);
		copy.setPaint(new Color(255, 255, 255));
		copy.fillPolygon(new int[]{-5, 5, -5, 0}, new int[]{-5, 0, 5, 0}, 4);
		copy.fillPolygon(new int[]{-8, 8, -8, 0}, new int[]{-3, 0, 3, 0}, 4);
		copy.setPaint(new Color(0, 0, 255));
		copy.fillPolygon(new int[]{-6, 4, -6, -1}, new int[]{-5, 0, 5, 0}, 4);
		copy.fillPolygon(new int[]{-9, 6, -9, -1}, new int[]{-3, 0, 3, 0}, 4);
	    copy.dispose();
	    
	}
	@Override
	public void run(double dt) {
		Vector xy = this.getXY();
		Vector v = this.getV();
		double speed = v.getMagnitude();
		//Move
		this.move(dt);
		//Cap Speed
		if(speed > maxSpeed) {
			this.accelerate(v.mult((1-(maxSpeed/speed))*resistance), dt);
		}
		//Accelerate
		else {
			this.accelerate(v.getUnitVec().mult(constAcceleration), dt);
		}
		/*
		//Loop around
		if(this.getX() > (double)Main.WIDTH + 30.0) {
			this.setX(-30.0);
		}
		if(this.getX() < -30.0) {
			this.setX((double)Main.WIDTH + 30.0);
		}
		if(this.getY() > (double)Main.HEIGHT + 30.0) {
			this.setY(-30.0);
		}
		if(this.getY() < -30.0) {
			this.setY((double)Main.HEIGHT + 30.0);
		}
		*/
		//Sensing
		Vector target = new Vector(0.0, 0.0);
		Vector center = new Vector(0.0, 0.0);
		Vector avgAngles = new Vector(0.0, 0.0);
		Vector antiCenter = new Vector(0.0, 0.0);
		int numBoidsSeen = 0;
		int numBoidsFar = 0;
		int numBoidsClose = 0;
		for(int i = 0; i<boids.size(); i++) {
			if(numBoidsSeen >= maxSee) {
				break;
			}
			Boid boid = boids.get(i);
			//Don't see itself
			if(boid.getID() == this.getID()) {
				continue;
			}
			//If its in range
			Vector relXY = boid.getXY().sub(xy);
			double dist = relXY.getMagnitude();
			if(dist < senseRange && Math.acos(relXY.dot(v)/dist/speed) < senseAng) {
				//Get species of boid
				int boidSpecies = boid.getSpecies();
				numBoidsSeen += 1;
				//Avg Velocities
				avgAngles.addToThis(boid.getV().mult(alignment[boidSpecies]));
				//Away from close
				if(dist < avoidDist && dist != 0) {
					antiCenter.addToThis(relXY.mult(-1.0*separation[boidSpecies]));
					numBoidsClose += 1;
				}
				//Avg position
				else {
					center.addToThis(relXY.mult(cohesion[boidSpecies]));
					numBoidsFar += 1;
				}
			}
		}
		avgAngles.subToThis(v);
		if(numBoidsFar != 0) {
			target.addToThis(center.mult(1.0/numBoidsFar));
		}
		if(numBoidsSeen != 0) {
			target.addToThis(avgAngles.mult(1.0/numBoidsSeen));
		}
		if(numBoidsClose != 0) {
			target.addToThis(antiCenter.mult(1.0/numBoidsClose));
		}
		if(this.mouseFollow != 0) {
			target.addToThis(Listener.getMouseXY().sub(xy).mult(this.mouseFollow));
		}
		//Center
		target.addToThis(new Vector((double)Main.WIDTH/2, (double)Main.HEIGHT/2).sub(xy).mult(0.9));	
		this.accelerate(target.clamp(this.acceleration), dt);
	}
	@Override
	public void reset() {
		this.setV(new Vector(0,0));
		this.setXY(Vector.getUnitVec(r.nextDouble()*2.0*Math.PI)
				.mult(Math.sqrt(Main.HEIGHT*Main.HEIGHT + Main.WIDTH*Main.WIDTH)/2.0)
				.add(new Vector((double)Main.WIDTH/2.0, (double)Main.HEIGHT/2.0)));
	}
	public int getID() {
		return this.id;
	}
	public int getSpecies() {
		return this.species;
	}
	public void setSpecies(int s) {
		this.species = s;
	}
}
