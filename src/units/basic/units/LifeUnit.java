package basic.units;

import java.util.Random;
import org.jlayer.annotations.*;

@LayerUnit
public class LifeUnit {
	@LayerField public int state;
	@LayerField public LifeUnit[] vector;
	
	@LayerMethod public void initState(Random r) { state = r.nextInt(2); }
	
	private int next;
	@LayerMethod public void nextState(){
		int sum = 0;
		for (int i = 0; i < vector.length; i++) { sum += vector[i].state; }
		if (sum < 2 || sum > 3) {
			next = 0;
		} else if (sum == 3	) {
			next = 1;
		} else {
			next = state;
		}
	}
	
	@LayerMethod public void updateState() { state = next; }
}
