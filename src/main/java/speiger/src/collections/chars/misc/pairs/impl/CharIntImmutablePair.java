package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharIntPair;

/**
 * Mutable Pair Implementation that
 */
public class CharIntImmutablePair implements CharIntPair
{
	protected final char key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public CharIntImmutablePair() {
		this((char)0, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharIntImmutablePair(char key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharIntPair setCharKey(char key) {
		return new CharIntImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharIntPair setIntValue(int value) {
		return new CharIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public CharIntPair set(char key, int value) {
		return new CharIntImmutablePair(key, value);
	}
	
	@Override
	public CharIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharIntPair) {
			CharIntPair entry = (CharIntPair)obj;
			return key == entry.getCharKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Integer.toString(value);
	}
}