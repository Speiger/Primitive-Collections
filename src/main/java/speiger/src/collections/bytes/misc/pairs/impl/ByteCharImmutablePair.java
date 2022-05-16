package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteCharPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteCharImmutablePair implements ByteCharPair
{
	protected final byte key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public ByteCharImmutablePair() {
		this((byte)0, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteCharImmutablePair(byte key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteCharPair setByteKey(byte key) {
		return new ByteCharImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteCharPair setCharValue(char value) {
		return new ByteCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ByteCharPair set(byte key, char value) {
		return new ByteCharImmutablePair(key, value);
	}
	
	@Override
	public ByteCharPair shallowCopy() {
		return this;
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