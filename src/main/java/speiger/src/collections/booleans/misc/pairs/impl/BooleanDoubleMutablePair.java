package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class BooleanDoubleMutablePair implements BooleanDoublePair
{
	protected boolean key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public BooleanDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanDoubleMutablePair(boolean key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanDoublePair setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public BooleanDoublePair set(boolean key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanDoublePair shallowCopy() {
		return BooleanDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanDoublePair) {
			BooleanDoublePair entry = (BooleanDoublePair)obj;
			return key == entry.getBooleanKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Double.toString(value);
	}
}