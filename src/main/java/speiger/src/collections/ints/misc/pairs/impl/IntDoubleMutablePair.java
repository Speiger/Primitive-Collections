package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class IntDoubleMutablePair implements IntDoublePair
{
	protected int key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public IntDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntDoubleMutablePair(int key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntDoublePair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public IntDoublePair set(int key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntDoublePair shallowCopy() {
		return IntDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntDoublePair) {
			IntDoublePair entry = (IntDoublePair)obj;
			return key == entry.getIntKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Double.toString(value);
	}
}