package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharLongPair;

/**
 * Mutable Pair Implementation that
 */
public class CharLongImmutablePair implements CharLongPair
{
	protected final char key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public CharLongImmutablePair() {
		this((char)0, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharLongImmutablePair(char key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharLongPair setCharKey(char key) {
		return new CharLongImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharLongPair setLongValue(long value) {
		return new CharLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public CharLongPair set(char key, long value) {
		return new CharLongImmutablePair(key, value);
	}
	
	@Override
	public CharLongPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharLongPair) {
			CharLongPair entry = (CharLongPair)obj;
			return key == entry.getCharKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Long.toString(value);
	}
}