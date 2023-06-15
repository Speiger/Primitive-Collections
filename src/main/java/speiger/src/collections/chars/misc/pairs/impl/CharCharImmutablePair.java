package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharCharPair;

/**
 * Mutable Pair Implementation that
 */
public class CharCharImmutablePair implements CharCharPair
{
	protected final char key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public CharCharImmutablePair() {
		this((char)0, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharCharImmutablePair(char key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharCharPair setCharKey(char key) {
		return new CharCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharCharPair setCharValue(char value) {
		return new CharCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public CharCharPair set(char key, char value) {
		return new CharCharImmutablePair(key, value);
	}
	
	@Override
	public CharCharPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharCharPair) {
			CharCharPair entry = (CharCharPair)obj;
			return key == entry.getCharKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Character.toString(value);
	}
}