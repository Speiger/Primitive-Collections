package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleDoubleMutablePair implements DoubleDoublePair
{
	protected double key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public DoubleDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleDoubleMutablePair(double key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleDoublePair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public DoubleDoublePair set(double key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleDoublePair shallowCopy() {
		return DoubleDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleDoublePair) {
			DoubleDoublePair entry = (DoubleDoublePair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Double.toString(value);
	}
}