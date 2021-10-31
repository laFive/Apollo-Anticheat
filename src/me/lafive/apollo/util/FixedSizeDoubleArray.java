package me.lafive.apollo.util;

import java.util.ArrayList;

public class FixedSizeDoubleArray {
	
	private ArrayList<Double> array;
	private int size;
	
	public FixedSizeDoubleArray(int size) {
		
		this.size = size;
		this.array = new ArrayList<Double>();
		
	}
	
	public void add(double d) {
		if (array.size() + 1 > size) {
			array.remove(0);
		}
		array.add(d);
	}
	
	public void clear() {
		array.clear();
	}
	
	public int getSize() {
		return array.size();
	}
	
	public void remove(double d) {
		array.remove((Object)d);
	}
	
	public double getAverage() {
		
		double total = 0;
		for (Double d : array) {
			total += d;
		}
		return total / array.size();
		
	}
	
	public double getVariance() {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        final double average;

        for (final Number number : array) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        for (final Number number : array) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }
	
	public double getStandardDeviation() {
		return MathHelper.sqrt(getVariance());
	}

}
