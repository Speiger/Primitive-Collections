package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class ShortDoubleImmutablePair implements ShortDoublePair
{
	protected final short key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public ShortDoubleImmutablePair() {
		this((short)0, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortDoubleImmutablePair(short key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortDoublePair setShortKey(short key) {
		return new ShortDoubleImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortDoublePair setDoubleValue(double value) {
		return new ShortDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public ShortDoublePair set(short key, double value) {
		return new ShortDoubleImmutablePair(key, value);
	}
	
	@Override
	public ShortDoublePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortDoublePair) {
			ShortDoublePair entry = (ShortDoublePair)obj;
			return key == entry.getShortKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Double.toString(value);
	}
}