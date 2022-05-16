package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteCharPair;



/**
 * Mutable Pair Implementation that
 */
public class ByteCharMutablePair implements ByteCharPair
{
	protected byte key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public ByteCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteCharMutablePair(byte key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteCharPair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ByteCharPair set(byte key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteCharPair shallowCopy() {
		return ByteCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteCharPair) {
			ByteCharPair entry = (ByteCharPair)obj;
			return key == entry.getByteKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Character.toString(value);
	}
}