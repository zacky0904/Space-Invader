package test;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Test {

	public static void main(String[] args) {
		Vector3f vec = new Vector3f(0,1,0);
		
		
//		Matrix4f.translate(vec,result,result);
		for(int i=0;i<360;i+=36) {
			for(int j=0;j<=180;j+=36) {
				Matrix4f result = new Matrix4f();
				Matrix4f rotate = new Matrix4f();
				Matrix4f.translate(vec,result,result);
				Matrix4f.rotate((float) Math.toRadians(i),new Vector3f(0, 1, 0) , rotate,rotate);
				Matrix4f.rotate((float) Math.toRadians(j),new Vector3f(1, 0, 0) , rotate,rotate);
				Matrix4f.mul(rotate, result, result);
				Vector3f resultV = new Vector3f(result.m30,result.m31,result.m32);
				System.out.println(resultV.toString());
			}
			System.out.println("switch");
		}

		
		

	}

}
