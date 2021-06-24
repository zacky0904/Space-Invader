package enemy;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EnemySystem {
	
	public static void gernerateEnemy1(int count, float target) {
		float random = (float) Math.random()*20-10;
		Vector2f heading = new Vector2f((float)Math.random(), (float)Math.random());
		heading.normalise();
		for(int i=0;i<count;i++) {
			new Enemy1(new Vector3f( random + i*2.2f, 0 , -200 - i*5), new Vector3f( 0, 0 , target),-10);
		}
	}
	
	public static void gernerateEnemy1Reverse(int count, float target) {
		float random = (float) Math.random()*10-10;
		Vector2f heading = new Vector2f((float)Math.random(), (float)Math.random());
		heading.normalise();
		for(int i=0;i<count;i++) {
			new Enemy2(new Vector3f( random + i*2.2f, 0 , -(200+count*5) + i*5), new Vector3f( 0, 0 , target),10, heading);
		}
	}
	
	

}
