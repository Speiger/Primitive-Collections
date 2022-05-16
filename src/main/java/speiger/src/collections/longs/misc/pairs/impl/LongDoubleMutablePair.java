package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class LongDoubleMutablePair implements LongDoublePair
{
	protected long key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public LongDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongDoubleMutablePair(long key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongDoublePair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public LongDoublePair set(long key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongDoublePair shallowCopy() {
		return LongDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongDoublePair) {
			LongDoublePair entry = (LongDoublePair)obj;
			return key == entry.getLongKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Double.toString(value);
	}
}