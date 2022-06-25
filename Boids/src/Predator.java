import java.awt.Color;
import java.awt.Graphics2D;

public class Predator extends Boid{
	final static String[] names = {
			"Default",
			"Prey",
			"Predator",
			"Prey2"
	};
	final static double[] separation = {
			-10.0,
			-20.0,
			30.0,
			9999.0};
	final static double[] alignment = {
			0.0,
			0.0,
			-1.0,
			1.0};
	final static double[] cohesion = {
			10.0,
			10.0,
			-5.0,
			-9999.0};
	final static double mouseFollow = 1.0;

	final static double maxSpeed = 50;
	final static double acceleration = 600;
	final static double constAcceleration = 300;
	final static double resistance = -3.0;
	
	final static double senseRange = 150;
	final static double senseAng = Math.PI*(3.0/6.0);
	final static double avoidDist = 40;
	final static int maxSee = 10;
	
	public Predator() {
		super();
		this.setSpecies(2);
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
		
		copy.scale(1.5, 1.5);
		copy.setPaint(new Color(255, 255, 255));
		copy.fillPolygon(new int[]{-5, 5, -5, 0}, new int[]{-5, 0, 5, 0}, 4);
		copy.fillPolygon(new int[]{-8, 8, -8, 0}, new int[]{-3, 0, 3, 0}, 4);
		copy.setPaint(new Color(255, 0, 0));
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
		//Turn back if outside
		if(xy.getX() > Main.WIDTH-30 || xy.getX() < 30 || xy.getY() > Main.HEIGHT-30 || xy.getY() < 30) {
			this.accelerate(new Vector((double)Main.WIDTH/2, (double)Main.HEIGHT/2).sub(xy).clamp(acceleration), dt);
			return;
		}
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
			//Only sense BIG
			Boid boid;
			if(i > 5) {
				boid = boids.get((int)(r.nextDouble()*boids.size()));
			}
			else {
				boid = boids.get(i);
			}
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
				if((boidSpecies == 1 || boidSpecies == 0)&& dist < 25) {
					boid.reset();
				}
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
		target.addToThis(Listener.getMouseXY().sub(xy).mult(this.mouseFollow));
		this.accelerate(target.clamp(this.acceleration), dt);
	}
}
