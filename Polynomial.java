public class Polynomial {
    double[] coefficients;
    public Polynomial() {
    	coefficients = new double[]{0};
    }
    public Polynomial(double arr[]) {
        coefficients = arr.clone();
    }
    public Polynomial add(Polynomial p) {
    	Polynomial newP;
    	if (this.coefficients.length > p.coefficients.length) {
    		newP = new Polynomial(this.coefficients);
    		for (int i = 0; i < p.coefficients.length; i++)
    			newP.coefficients[i] += p.coefficients[i];
    	}
    	else {
    		newP = new Polynomial(p.coefficients);
    		for (int i = 0; i < this.coefficients.length; i++)
    			newP.coefficients[i] += this.coefficients[i];
    	}
    	return newP;
    }
    public double evaluate(double n) {
    	double result = 0;
    	for (int i = 0; i < coefficients.length; i++) {
    		result += coefficients[i]*Math.pow(n, i);
    	}
    	return result;
    }
    public boolean hasRoot(double n) {
    	return this.evaluate(n) == 0;
    }
}