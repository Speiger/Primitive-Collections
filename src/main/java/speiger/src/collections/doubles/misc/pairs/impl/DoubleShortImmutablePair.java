package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleShortPair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleShortImmutablePair implements DoubleShortPair
{
	protected final double key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public DoubleShortImmutablePair() {
		this(0D, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleShortImmutablePair(double key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleShortPair setDoubleKey(double key) {
		return new DoubleShortImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleShortPair setShortValue(short value) {
		return new DoubleShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public DoubleShortPair set(double key, short value) {
		return new DoubleShortImmutablePair(key, value);
	}
	
	@Override
	public DoubleShortPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleShortPair) {
			DoubleShortPair entry = (DoubleShortPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Short.toString(value);
	}
}