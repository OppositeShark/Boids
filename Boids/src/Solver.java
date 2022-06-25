import java.util.ArrayList;
import java.lang.Math;

public class Solver {
	public void solve(double dt) {
		for(int i = 0; i<Entity.entities.size(); i++) {
			Entity.entities.get(i).run(dt);
		}
	}
}
