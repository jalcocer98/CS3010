
public class gaussian {
	// Forward Elimination
	public static void FwdElimination(float[][] coeff, float[] vector) {
		int n = coeff.length;
		for (int k=0; k<(n-1); k++) {
			for(int i=k+1; i<n; i++) {
				float mult = coeff[i][k] / coeff[k][k];
				
				for(int j=k+1; j<n; j++) {
					coeff[i][j] = coeff[i][j] - mult * coeff[k][j];
				}
				
			}
		}
	}
	public static void BackSubst(float[][] coeff, float[] vector, float[] sol) {
		int n = coeff.length
		sol[n] = vector[n] / coeff[n][n];
		
	}
}
