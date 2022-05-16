package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharBytePair;



/**
 * Mutable Pair Implementation that
 */
public class CharByteMutablePair implements CharBytePair
{
	protected char key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public CharByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharByteMutablePair(char key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharBytePair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharBytePair setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public CharBytePair set(char key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharBytePair shallowCopy() {
		return CharBytePair.mutable(key, value);
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