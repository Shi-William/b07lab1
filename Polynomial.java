import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.Arrays;
import java.io.IOException;

public class Polynomial {
    double[] coefficients;
    int[] exponents;
    public Polynomial() {
    	coefficients = new double[]{};
    	exponents = new int[]{};
    }
    public Polynomial(double[] coe, int[] exp) {
        coefficients = coe.clone();
        exponents = exp.clone();
    }
    public Polynomial(File f) {
    	try (BufferedReader input = new BufferedReader(new FileReader(f))) {
        	String line = input.readLine();
        	String[] terms = line.split("[+-]");
        	String[] symbols = line.split("");
        	int index = 0;
        	String[] signs;
        	if (line.charAt(0) != '+' && line.charAt(0) != '-') {
        		signs = new String[terms.length+1];
        		signs[0] = "+";
        		index = 1;
        		for (int i = 0; i < symbols.length; i++) {
        			if (symbols[i].equals("+") || symbols[i].equals("-")) {
        				signs[index] = symbols[i];
        				index++;
        			}
        		}
        	}
        	else {
        		signs = new String[terms.length];
        		for (int i = 0; i < symbols.length; i++) {
        			if (symbols[i].equals("+") || symbols[i].equals("-")) {
        				signs[index] = symbols[i];
        				index++;
        			}
        		}
            }
        	coefficients = new double[terms.length];
        	exponents = new int[terms.length];
        	for (int i = 0; i < terms.length; i++) {
        		if (!terms[i].contains("x")) {
        			if (signs[i].equals("+"))
        				coefficients[i] = Double.parseDouble(terms[i]);
        			else
        				coefficients[i] = -Double.parseDouble(terms[i]);
        			exponents[i] = 0;
        		}
        		else {
        			for (int j = 0; j < terms[i].length(); j++) {
        				if (terms[i].charAt(j) == 'x') {
        					index = j;
        					break;
        				}
        			}
        			if (index == 0)
        				coefficients[i] = 1;
        			else {
	        			if (signs[i].equals("+"))
	        				coefficients[i] = Double.parseDouble(terms[i].substring(0, index));
	        			else
	        				coefficients[i] = -Double.parseDouble(terms[i].substring(0, index));
        			}
        			if (index == terms[i].length()-1)
        				exponents[i] = 1;
        			else
        				exponents[i] = Integer.parseInt(terms[i].substring(index+1, terms[i].length()));
        		}
        	}
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public Polynomial add(Polynomial p) {
    	int newLength = exponents.length + p.exponents.length;
    	for (int i = 0; i < exponents.length; i++) {
    		for (int j = 0; j < p.exponents.length; j++) {
    			if (exponents[i] == p.exponents[j]) {
    				newLength--;
    				if (coefficients[i] + p.coefficients[j] == 0)
    					newLength--;
    			}
    		}
    	}
    	double[] newCoe = new double[newLength];
    	int[] newExp = new int[newLength];
    	int index = 0;
    	boolean add = true;
    	for (int i = 0; i < exponents.length; i++) {
    		add = true;
    		for (int j = 0; j < p.exponents.length; j++) {
    			if (exponents[i] == p.exponents[j] && coefficients[i] + p.coefficients[j] == 0)
    				add = false;
    		}
    		if (add) {
    			newExp[index] = exponents[i];
    			index++;
    		}
    	}
    	for (int j = 0; j < p.exponents.length; j++) {
    		add = true;
    		for (int i = 0; i<exponents.length;i++) {
    			if (exponents[i] == p.exponents[j])
    				add = false;
    		}
    		if (add) {
    			newExp[index] = p.exponents[j];
    			index++;
    		}
    	}
    	Arrays.sort(newExp);
    	index = 0;
    	while (newCoe[newLength-1] == 0) {
    		for (int i = 0; i<newLength; i++) {
    			for (int j = 0; j<exponents.length; j++) {
    				if (exponents[j] == newExp[i])
    					newCoe[i] += coefficients[j];
    			}
    			for (int j = 0; j<p.exponents.length; j++) {
    				if (p.exponents[j] == newExp[i])
    					newCoe[i] += p.coefficients[j];
    			}
    		}
    		index++;
    	}
    	return new Polynomial(newCoe, newExp); 
    }
    public double evaluate(double n) {
    	double result = 0;
    	for (int i = 0; i < coefficients.length; i++) {
    		result += coefficients[i]*Math.pow(n, exponents[i]);
    	}
    	return result;
    }
    public boolean hasRoot(double n) {
    	return this.evaluate(n) == 0;
    }
    public Polynomial multiply(Polynomial p) {
    	int maxExp = 0;
    	for (int i = 0; i < exponents.length; i++) {
    		for (int j = 0; j < p.exponents.length; j++) {
    			if (exponents[i]+p.exponents[j] > maxExp)
    				maxExp = exponents[i]+p.exponents[j];
    		}
    	}
    	double[] allCoe = new double[maxExp+1];
    	for (int i = 0; i < exponents.length; i++) {
    		for (int j = 0; j < p.exponents.length; j++) {
    			allCoe[exponents[i]+p.exponents[j]] += coefficients[i]*p.coefficients[j];
    		}
    	}
    	int newLength = allCoe.length;
    	for (int i = 0; i < allCoe.length; i++) {
    		if (allCoe[i] == 0)
    			newLength--;
    	}
    	double[] newCoe = new double[newLength];
    	int[] newExp = new int[newLength];
    	int index = 0;
    	for (int i = 0; i < allCoe.length; i++) {
    		if (allCoe[i] != 0) {
    			newCoe[index] = allCoe[i];
    			newExp[index] = i;
    			index++;
    		}
    	}
    	return new Polynomial(newCoe, newExp);
    }
    public void saveToFile(String fileName) {
    	try (FileWriter output = new FileWriter(fileName)) {
            for (int i = 0; i < coefficients.length; i++) {
            	if (coefficients[i] < 0) {
            		if (coefficients[i] == -1 && exponents[i] != 0)
            			output.write("-");
            		else {
            			if (coefficients[i] == (int)coefficients[i])
            				output.write(Integer.toString((int)coefficients[i]));
            			else
            				output.write(Double.toString(coefficients[i]));
            		}
            	}
            	else {
            		if (i != 0)
            			output.write("+");
            		if (coefficients[i] != 1 || exponents[i] == 0) {
            			if (coefficients[i] == (int)coefficients[i])
                			output.write(Integer.toString((int)coefficients[i]));
                		else
                			output.write(Double.toString(coefficients[i]));
            		}
            	}
            	if (exponents[i] != 0) {
            		output.write("x");
            		if (exponents[i] != 1)
            			output.write(Integer.toString(exponents[i]));
            	}
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}