package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharBytePair;

/**
 * Mutable Pair Implementation that
 */
public class CharByteImmutablePair implements CharBytePair
{
	protected final char key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public CharByteImmutablePair() {
		this((char)0, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharByteImmutablePair(char key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharBytePair setCharKey(char key) {
		return new CharByteImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharBytePair setByteValue(byte value) {
		return new CharByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public CharBytePair set(char key, byte value) {
		return new CharByteImmutablePair(key, value);
	}
	
	@Override
	public CharBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharBytePair) {
			CharBytePair entry = (CharBytePair)obj;
			return key == entry.getCharKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Byte.toString(value);
	}
}