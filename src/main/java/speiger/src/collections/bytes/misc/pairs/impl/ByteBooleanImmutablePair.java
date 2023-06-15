package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteBooleanImmutablePair implements ByteBooleanPair
{
	protected final byte key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public ByteBooleanImmutablePair() {
		this((byte)0, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteBooleanImmutablePair(byte key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteBooleanPair setByteKey(byte key) {
		return new ByteBooleanImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteBooleanPair setBooleanValue(boolean value) {
		return new ByteBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public ByteBooleanPair set(byte key, boolean value) {
		return new ByteBooleanImmutablePair(key, value);
	}
	
	@Override
	public ByteBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteBooleanPair) {
			ByteBooleanPair entry = (ByteBooleanPair)obj;
			return key == entry.getByteKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Boolean.toString(value);
	}
}