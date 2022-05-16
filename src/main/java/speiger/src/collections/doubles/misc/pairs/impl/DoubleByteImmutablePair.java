package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleBytePair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleByteImmutablePair implements DoubleBytePair
{
	protected final double key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public DoubleByteImmutablePair() {
		this(0D, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleByteImmutablePair(double key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleBytePair setDoubleKey(double key) {
		return new DoubleByteImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleBytePair setByteValue(byte value) {
		return new DoubleByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public DoubleBytePair set(double key, byte value) {
		return new DoubleByteImmutablePair(key, value);
	}
	
	@Override
	public DoubleBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleBytePair) {
			DoubleBytePair entry = (DoubleBytePair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Byte.toString(value);
	}
}