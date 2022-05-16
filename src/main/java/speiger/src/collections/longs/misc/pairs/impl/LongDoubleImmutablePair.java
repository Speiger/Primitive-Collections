package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class LongDoubleImmutablePair implements LongDoublePair
{
	protected final long key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public LongDoubleImmutablePair() {
		this(0L, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongDoubleImmutablePair(long key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongDoublePair setLongKey(long key) {
		return new LongDoubleImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongDoublePair setDoubleValue(double value) {
		return new LongDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public LongDoublePair set(long key, double value) {
		return new LongDoubleImmutablePair(key, value);
	}
	
	@Override
	public LongDoublePair shallowCopy() {
		return this;
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