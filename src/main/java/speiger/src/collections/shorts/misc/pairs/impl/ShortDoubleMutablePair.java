package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class ShortDoubleMutablePair implements ShortDoublePair
{
	protected short key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public ShortDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortDoubleMutablePair(short key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortDoublePair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public ShortDoublePair set(short key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortDoublePair shallowCopy() {
		return ShortDoublePair.mutable(key, value);
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