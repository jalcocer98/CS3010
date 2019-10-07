import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PolynomialRoots {
	public static void main(String[] args) {
		
	}
	private static float secant(float[] coeff, Object function, float a, float b, int maxIter, float eps) {
		float fa = evaluateFunction(coeff, a);
		float fb = evaluateFunction(coeff, b);
		
		if(Math.abs(fa) > Math.abs(fb)) {
			float temp = a;
			a = b;
			b = temp;
			
			temp = fa;
			fa = fb;
			fb = temp;
		}
		
		for(int i=0; i<maxIter; i++) {
			if(Math.abs(fa) > Math.abs(fb)) {
				float temp = a;
				a = b;
				b = temp;
				temp = fa;
				fa = fb;
				fb = temp;
			}
			float delta = (b-a)/(fb-fa);
			b = a;
			fb = fa;
			delta = delta*fa;
			
			if(Math.abs(delta) < eps) {
				System.out.println("Algorithm has converged after " + i + "iterations!");
				return a;
			}
			a = a - delta;
			fa = evaluateFunction(coeff, a);
		}
		System.out.println("Maximum number of iterations reached!");
		return a;
	}
	private static void newton(Object function, Object derivative, float x, int maxIter, float eps, float delta) {
		
	}
	private static void bisection(Object function, float a, float b, int maxIter, float eps) {
		
	}
	private 
	private static float evaluateFunction(float[] coeff, float x) {
		int degree = coeff.length-1;
		float result = 0;
		
		for(int i=0; i<(degree-1); i++) {
			result += coeff[i] * Math.pow(x, degree);
			degree--;
		}
		result += coeff[coeff.length-1];
		return result;
	}
	//add exception handling for improper file format
	private static float[] readFile(String fileName) {
		float[] coeff = null;
		try {
			Scanner input = new Scanner(new File(fileName));
			int n = input.nextInt();
			coeff = new float[n+1];
			
			for(int i=0; i<coeff.length; i++) {
				coeff[i] = input.nextFloat();
			}
			input.close();
		}catch(FileNotFoundException e) {
			System.out.println("Specified file not found. Please restart the program and try again");
			System.exit(0);
		}
		return coeff;
	}
}
