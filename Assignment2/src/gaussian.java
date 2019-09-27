import java.io.*;
import java.util.*;

public class gaussian {
	// Forward Elimination
	public static void FwdElimination(float[][] coeff, float[] constants) {
		int n = coeff.length;
		float mult;
		for (int k=0; k<(n-1); k++) {
			for(int i=k+1; i<n; i++) {
				mult = coeff[i][k] / coeff[k][k];
				
				for(int j=k+1; j<n; j++) {
					coeff[i][j] = coeff[i][j] - mult * coeff[k][j];
				}
				constants[i] = constants[i] - mult * constants[k];
			}
		}
	}
	public static float[] BackSubst(float[][] coeff, float[] constants) {
		int n = coeff.length;
		float[] sol = new float[n];
		sol[n-1] = constants[n-1] / coeff[n-1][n-1];
		float sum = 0;
		for(int i=n-2; i>-1; i--) {
			sum = constants[i];
			for(int j=i+1; j<n; j++) {
				sum = sum - coeff[i][j] * sol[j]; 
			}
			sol[i] = sum / coeff[i][i];
		}
		return sol;
	}
	public static int[] SPPFwdElimination(float[][] coeff, float[] constants) {
		int n = coeff.length;
		float[] scaling = new float[n];
		int[] indices = new int[n];
		for(int i=0; i<n; i++) {
			indices[i] = i;
		}
		float smax = 0;
		for(int i=0; i<n; i++) {
			smax = 0;
			for(int j=0; j<n; j++) {
				//smax = max(smax, | coeff[i][j])
				if(smax < Math.abs(coeff[i][j])) {
					smax = coeff[i][j];
				}
			}
			scaling[i] = smax;
		}
		for(int k=0; k<(n-1); k++) {
			float rmax = 0;
			int maxInd = k;
			for(int i=k; i<n; i++) {
				float r = Math.abs(coeff[indices[i]][k] / scaling[indices[i]]);
				if(r>rmax) {
					rmax = r;
					maxInd = i;
				}	
			}
			swap(indices, k, maxInd);
			float mult;
			for(int i=k+1; i<n; i++) {
				mult = coeff[indices[i]][k] / coeff[indices[k]][k];
				
				for(int j=k+1; j<n; j++) {
					coeff[indices[i]][j] = coeff[indices[i]][j] - mult * coeff[indices[k]][j];
				}
				constants[indices[i]] = constants[indices[i]] - mult * constants[indices[k]];
			}
		}
		return indices;
	}
	public static float[] SPPBackSubst(float[][] coeff, float[] constants, int[] indices) {
		int n = coeff.length;
		float[] sol = new float[n];
		sol[n] = constants[indices[n]] / coeff[indices[n]][n];
		float sum = 0;
		for(int i=n-2; i>-1; i--) {
			sum = constants[indices[i]];
			for(int j=i+1; j<n; i++) {
				sum = sum - coeff[indices[i]][i] * sol[j];
			}
			sol[i] = sum / coeff[indices[i]][i];
		}
		return sol;
	}
	private static void swap(int[] indices, int k, int maxInd) {
		int temp = indices[k];
		indices[k] = indices[maxInd];
		indices[maxInd] = temp;
	}
	
	private static Object[] readFile(String fileName) {
		Object[] equations = new Object[2];
		try {
			Scanner input = new Scanner(new File(fileName));
			int n = input.nextInt();
			float[][] coeff = new float[n][n];
			float[] constants = new float[n];
			
			while(input.hasNext()) {
				for(int i=0; i<n; i++) {
					for(int j=0; j<n; j++) {
						coeff[i][j] = input.nextFloat();
					}
				}
				for(int i=0; i<n; i++) {
					constants[i] = input.nextFloat();
				}
			}
			equations[0] = coeff;
			equations[1] = constants;
		}catch(FileNotFoundException e) {
			System.out.println("Specified file not found. Please restart the program and try again");
			System.exit(0);
		}
		return equations;
	}
	private static void writeFile(String fileName, float[] sol) {
		try {
			String[] fileString = fileName.split(".");
			System.out.println(fileName);
			System.out.println(fileString.length);
			FileWriter out = new FileWriter(fileName + ".sol");
			for(int i=0; i<sol.length; i++) {
				out.write(sol[i] + " ");
			}
			out.close();
		}catch (IOException e) {
			
		}
	}
	public static void main(String[] args) {
		boolean runSPP = false;
		String fileName;
		String modifier;
		if(args.length > 1) {
			modifier = args[0];
			if(modifier.equalsIgnoreCase("--spp")) {
				runSPP = true;
			}
			else {
				System.out.println("Unknown modifier specifed. Running default settings.");
			}
			fileName = args[1];
		}
		else {
			fileName = args[0];
		}
		Object[] equations = readFile(fileName);
		float[][] coeff = (float[][])equations[0];
		float[] constants  = (float[])equations[1];
		float[] solution;
		
		if(runSPP) {
			int[] indices = SPPFwdElimination(coeff, constants);
			solution = SPPBackSubst(coeff, constants, indices);
		}
		else {
			FwdElimination(coeff, constants);
			solution = BackSubst(coeff, constants);
		}
		writeFile(fileName, solution);
	}
}
