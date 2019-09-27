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
		sol[n] = constants[n] / coeff[n][n];
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
	public static void SPPFwdElimination(float[][] coeff, float[] constants, int[] indices) {
		int n = coeff.length;
		float[] scaling = new float[coeff.length];
		float sum;
		float smax = 0;
		for(int i=0; i<n; i++) {
			sum = 0;
			for(int j=0; j<n; j++) {
			//smax = max(smax, | coeff[i][j])
			}
			scaling[i] = smax;
		}
		for(int k=0; k<(n-1); k++) {
			float rmax = 0;
			int maxInd = k;
			
			for(int i=k; i<n; i++) {
				//r = |coeff[indices[i]][k] / scaling[ind[i]]
				int r = 0;
				if(r>rmax) {
					rmax = r;
					maxInd = i;
				}	
			}
			swap(indices[maxInd], indices[k]);
			float mult;
			for(int i=k+1; i<n; i++) {
				mult = coeff[indices[i]][k] / coeff[indices[k]][k];
				
				for(int j=k+1; j<n; j++) {
					coeff[indices[i]][j] = coeff[indices[i]][j] - mult * coeff[indices[k]][j];
				}
				constants[indices[i]] = constants[indices[i]] - mult * constants[indices[k]];
			}
		}
		
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
	private static void swap(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	
	private static void readFile(String fileName, float[][] coeff, float[] constants) {
		try {
			Scanner input = new Scanner(new File(fileName));
			int n = input.nextInt();
			coeff = new float[n][n];
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
		}catch(FileNotFoundException e) {
			System.out.println("Specified file not found. Please restart the program and try again");
			System.exit(0);
		}
	}
	private static void writeFile(String fileName, float[] sol) {
		try {
			FileWriter out = new FileWriter(fileName + ".sol");
			for(int i=0; i<sol.length; i++) {
				out.write(sol[i] + " ");
			}
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
		
		float[][] coeff = null;
		float[] constants = null;
		readFile(fileName, coeff, constants);
		
		float[] solution;
		if(runSPP) {
			int indices[] = null;
			SPPFwdElimination(coeff, constants, indices);
			solution = SPPBackSubst(coeff, constants, indices);
		}
		else {
			FwdElimination(coeff, constants);
			solution = BackSubst(coeff, constants);
		}
		writeFile(fileName, solution);
	}
}
