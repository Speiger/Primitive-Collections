package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class CharBooleanImmutablePair implements CharBooleanPair
{
	protected final char key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public CharBooleanImmutablePair() {
		this((char)0, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharBooleanImmutablePair(char key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharBooleanPair setCharKey(char key) {
		return new CharBooleanImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharBooleanPair setBooleanValue(boolean value) {
		return new CharBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public CharBooleanPair set(char key, boolean value) {
		return new CharBooleanImmutablePair(key, value);
	}
	
	@Override
	public CharBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharBooleanPair) {
			CharBooleanPair entry = (CharBooleanPair)obj;
			return key == entry.getCharKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Boolean.toString(value);
	}
}