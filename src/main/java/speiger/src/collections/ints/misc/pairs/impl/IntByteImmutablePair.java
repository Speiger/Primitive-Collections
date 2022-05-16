package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntBytePair;

/**
 * Mutable Pair Implementation that
 */
public class IntByteImmutablePair implements IntBytePair
{
	protected final int key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public IntByteImmutablePair() {
		this(0, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntByteImmutablePair(int key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntBytePair setIntKey(int key) {
		return new IntByteImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntBytePair setByteValue(byte value) {
		return new IntByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public IntBytePair set(int key, byte value) {
		return new IntByteImmutablePair(key, value);
	}
	
	@Override
	public IntBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntBytePair) {
			IntBytePair entry = (IntBytePair)obj;
			return key == entry.getIntKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Byte.toString(value);
	}
}