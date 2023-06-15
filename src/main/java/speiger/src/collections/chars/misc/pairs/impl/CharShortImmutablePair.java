package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharShortPair;

/**
 * Mutable Pair Implementation that
 */
public class CharShortImmutablePair implements CharShortPair
{
	protected final char key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public CharShortImmutablePair() {
		this((char)0, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharShortImmutablePair(char key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharShortPair setCharKey(char key) {
		return new CharShortImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharShortPair setShortValue(short value) {
		return new CharShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public CharShortPair set(char key, short value) {
		return new CharShortImmutablePair(key, value);
	}
	
	@Override
	public CharShortPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharShortPair) {
			CharShortPair entry = (CharShortPair)obj;
			return key == entry.getCharKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Short.toString(value);
	}
}