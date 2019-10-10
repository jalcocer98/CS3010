import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PolynomialRoots {
	public static void main(String[] args) {
		runAlgorithm(args);
	}
	private static float secant(float[] coeff, float a, float b, int maxIter, float eps) {
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
				System.out.println("Algorithm has converged after " + i + " iterations!");
				return a;
			}
			a = a - delta;
			fa = evaluateFunction(coeff, a);
		}
		System.out.println("Maximum number of iterations reached!");
		return a;
	}
	private static float newton(float[] coeff, float[] derivCoeff, float x, int maxIter, float eps, float delta) {
		float fx = evaluateFunction(coeff, x);
		for(int i=0; i<maxIter; i++) {
			float fd = evaluateFunction(derivCoeff, x);
			if(Math.abs(fd) < delta) {
				System.out.println("Small slope!");
				return x;
			}
			float d = fx / fd;
			x = x - d;
			fx = evaluateFunction(coeff, x);
			if(Math.abs(d) < eps ) {
				System.out.println("Algorithm has converged after "+ i + " iterations!");
				return x;
			}
		}
		System.out.println("Max iterations reached without convergence...");
		return x;
	}
	private static float bisection(float[] coeff, float a, float b, int maxIter, float eps) {
		float fa = evaluateFunction(coeff, a);
		float fb = evaluateFunction(coeff, b);
		if((fa*fb) >= 0) {
			System.out.println("Inadequate values for a and b");
			return (float) -1.0;
		}
		float c = 0;
		float error = b-a;
		for(int i=0; i<maxIter; i++) {
			error = error / 2;
			c = a + error;
			float fc = evaluateFunction(coeff, c);
			if(Math.abs(error) < eps || fc == 0) {
				System.out.println("Algorithm has converged after " + i + " iterations!");
				return c;
			}
			if((fa*fc) < 0) {
				b = c;
				fb = fc;
			}
			else {
				a = c;
				fa = fc;
			}
		}
		System.out.println("Max iterations reached without convergence...");
		return c;
	}
	private static float hybrid(float[] coeff, float a, float b, int maxIter, float eps, float delta) {
		float fa = evaluateFunction(coeff, a);
		float fb = evaluateFunction(coeff, b);
		if((fa*fb) >= 0) {
			System.out.println("Inadequate values for a and b");
			return (float) -1.0;
		}
		float c = b;
		float error = b-a;
		int counter = 0;
		while(evaluateFunction(coeff, c) > 0.01 && counter < maxIter) {//int i=0; i<maxIter; i++) {
			error = error / 2;
			c = a + error;
			float fc = evaluateFunction(coeff, c);
			if(Math.abs(error) < eps || fc == 0) {
				System.out.println("Algorithm has converged after " + counter + " iterations!");
				return c;
			}
			if((fa*fc) < 0) {
				b = c;
				fb = fc;
			}
			else {
				a = c;
				fa = fc;
			}
			counter++;
		}
		if(counter >= maxIter) {
			System.out.println("Max iterations reached without convergence...");
			return c;
		}
		else {
			float fx = evaluateFunction(coeff, c);
			float[] derivCoeff = derivePolynomial(coeff);
			for(int i=counter; i<maxIter; i++) {
				float fd = evaluateFunction(derivCoeff, c);
				if(Math.abs(fd) < delta) {
					System.out.println("Small slope!");
					return c;
				}
				float d = fx / fd;
				c = c - d;
				fx = evaluateFunction(coeff, c);
				if(Math.abs(d) < eps ) {
					System.out.println("Algorithm has converged after "+ i + " iterations!");
					return c;
				}
			}
			System.out.println("Max iterations reached without convergence...");
			return c;
		}
	}
	private static float[] derivePolynomial(float[] coeff) {
		float[] derivCoeff = new float[coeff.length-1];
		for(int i=0; i<derivCoeff.length; i++) {
			derivCoeff[i] = coeff[i+1] * (i+1);
		}
		return derivCoeff;
	}
	private static float evaluateFunction(float[] coeff, float x) {
		float result = 0;
		for(int i=0; i<coeff.length; i++) {
			result += coeff[i] * Math.pow(x, i);
		}
		return result;
	}
	//add exception handling for improper file format
	private static void runAlgorithm(String[] args) {
		if(args == null) {
			System.out.println("No parameteters detected. \nPlease restart the program and "
								+ "specify the appropriate parameters.");
			System.exit(0);
		}
		float initP = 0;
		int maxIter = 10000;
		
		switch(args[0]) {
			case "newt":
				if(args[0].length() < 3) {
					System.out.println("Not enough information provided. \nPlease restart the program and"
										+ " specify all the information needed.");
					System.exit(0);
				}
				if(args[0].length() > 5) {
					System.out.println("Too much information specfied for -newt mode.\nPlease restart the program"
										+ " with the appropriate amount of information.");
					System.exit(0);
				}
				if(args[0].length() == 3) {
					float x = Float.parseFloat(args[1]);
					float coeff[] = readFile(args[2]);
					float derivCoeff[] = derivePolynomial(coeff);
					newton(coeff, derivCoeff, x, maxIter, (float)0.001, (float)0.0001);
				}
				if(args[0].length() == 5) {
					if(!args[1].equals("-maxIt") || (args[1].equals("-maxIt") && args.length == 4)) {
						System.out.println("Formatting incorrect. \nPlease restart the program and format the"
											+ " information properly.");
						System.exit(0);
					}
					maxIter = Integer.parseInt(args[2]);
					float x = Float.parseFloat(args[3]);
					float coeff[] = readFile(args[4]);
					float derivCoeff[] = derivePolynomial(coeff);
					newton(coeff, derivCoeff, x, maxIter, (float)0.1, (float)0.1);
				}
				break;
			case "sec":
				if(args[0].length() < 4) {
					System.out.println("Not enough information provided.\nPlease restart the program and"
							+ " specify all the information needed.");
					System.exit(0);
				}
				if(args[0].length() > 6) {
					System.out.println("Too much information specfied for -newt mode.\nPlease restart the program"
										+ " with the appropriate amount of information.");
					System.exit(0);
				}
				if(args[0].length() == 4) {
					float a = Float.parseFloat(args[1]);
					float b = Float.parseFloat(args[2]);
					float coeff[] = readFile(args[3]);
					secant(coeff, a, b, maxIter, (float)0.001);
				}
				if(args[0].length() == 6) {
					if(!args[1].equals("-maxIt") || (args[1].equals("-maxIt") && args.length == 5)) {
						System.out.println("Formatting incorrect. \nPlease restart the program and format the"
											+ " information properly.");
						System.exit(0);
					}
					maxIter = Integer.parseInt(args[2]);
					float x = Float.parseFloat(args[3]);
					float coeff[] = readFile(args[4]);
				}
				break;
			default:
				if(args[0].length() < 3) {
					System.out.println("Not enough information provided.\nPlease restart the program and"
							+ "specify all the information needed.");
					System.exit(0);
				}
				if(args[0].length() > 5) {
					System.out.println("Too much information specfied for -newt mode.\nPlease restart the program"
										+ " with the appropriate amount of information.");
					System.exit(0);
				}
				if(args[0].length() == 3) {
					float x = Float.parseFloat(args[1]);
					float coeff[] = readFile(args[2]);
					float derivCoeff[] = derivePolynomial(coeff);
					newton(coeff, derivCoeff, x, maxIter, (float)0.001, (float)0.0001);
				}
				if(args[0].length() == 5) {
					if(!args[1].equals("-maxIt") || (args[1].equals("-maxIt") && args.length == 4)) {
						System.out.println("Formatting incorrect. \nPlease restart the program and format the"
											+ " information properly.");
						System.exit(0);
					}
					maxIter = Integer.parseInt(args[2]);
					float x = Float.parseFloat(args[3]);
					float coeff[] = readFile(args[4]);
					float derivCoeff[] = derivePolynomial(coeff);
					newton(coeff, derivCoeff, x, maxIter, (float)0.1, (float)0.1);
				}
				break;
				
		}
		
	}
	private static void writeFile(String fileName, float x) {
		try {
			int regexPoint = fileName.indexOf('.');
			String newFileName = fileName.substring(0, regexPoint);
			FileWriter out = new FileWriter(newFileName + ".sol");
			out.write((int)x);
			out.close();
		}catch (IOException e) {
			
		}
	}
	private static float[] readFile(String fileName) {
		float[] coeff = null;
		try {
			Scanner input = new Scanner(new File(fileName));
			int n = input.nextInt();
			coeff = new float[n+1];
			
			for(int i=(coeff.length-1); i>-1; i--) {
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
