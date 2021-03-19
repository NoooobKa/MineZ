package com.NBK.MineZ.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total;

    public RandomCollection() {
        this.random = new Random();
        this.total = 0;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }
    
    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
    
    public NavigableMap<Double, E> getMap() {
    	return map;
    }
    
    public double getProcentOfElement(E element) {
    	int mapSize = map.size();
    	if (mapSize == 0 || element == null)return -1;
    	Object[] keySet = map.keySet().toArray();
    	if (mapSize == 1)return (double) keySet[0];
    	int elementIndex = ArrayUtils.indexOf(map.values().toArray(), element);
    	return elementIndex == 0 ? (double)keySet[0] : elementIndex == (mapSize - 1) ? total - (double)keySet[elementIndex - 1] : (double)keySet[elementIndex] - (double)keySet[elementIndex - 1];
    }
    
    public double getTotal() {
    	return total;
    }
    
    
}
