package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class ByteDoubleMutablePair implements ByteDoublePair
{
	protected byte key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public ByteDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteDoubleMutablePair(byte key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteDoublePair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public ByteDoublePair set(byte key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteDoublePair shallowCopy() {
		return ByteDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteDoublePair) {
			ByteDoublePair entry = (ByteDoublePair)obj;
			return key == entry.getByteKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Double.toString(value);
	}
}