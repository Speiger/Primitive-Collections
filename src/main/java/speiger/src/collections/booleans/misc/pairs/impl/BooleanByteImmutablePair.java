package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanBytePair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanByteImmutablePair implements BooleanBytePair
{
	protected final boolean key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public BooleanByteImmutablePair() {
		this(false, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanByteImmutablePair(boolean key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanBytePair setBooleanKey(boolean key) {
		return new BooleanByteImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanBytePair setByteValue(byte value) {
		return new BooleanByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public BooleanBytePair set(boolean key, byte value) {
		return new BooleanByteImmutablePair(key, value);
	}
	
	@Override
	public BooleanBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanBytePair) {
			BooleanBytePair entry = (BooleanBytePair)obj;
			return key == entry.getBooleanKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Byte.toString(value);
	}
}