package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class ByteBooleanMutablePair implements ByteBooleanPair
{
	protected byte key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public ByteBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteBooleanMutablePair(byte key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteBooleanPair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public ByteBooleanPair set(byte key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteBooleanPair shallowCopy() {
		return ByteBooleanPair.mutable(key, value);
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