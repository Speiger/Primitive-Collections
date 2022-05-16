package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntBytePair;



/**
 * Mutable Pair Implementation that
 */
public class IntByteMutablePair implements IntBytePair
{
	protected int key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public IntByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntByteMutablePair(int key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntBytePair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntBytePair setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public IntBytePair set(int key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntBytePair shallowCopy() {
		return IntBytePair.mutable(key, value);
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