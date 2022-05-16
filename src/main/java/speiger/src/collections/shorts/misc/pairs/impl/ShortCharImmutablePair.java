package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortCharPair;

/**
 * Mutable Pair Implementation that
 */
public class ShortCharImmutablePair implements ShortCharPair
{
	protected final short key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public ShortCharImmutablePair() {
		this((short)0, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortCharImmutablePair(short key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortCharPair setShortKey(short key) {
		return new ShortCharImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortCharPair setCharValue(char value) {
		return new ShortCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ShortCharPair set(short key, char value) {
		return new ShortCharImmutablePair(key, value);
	}
	
	@Override
	public ShortCharPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortCharPair) {
			ShortCharPair entry = (ShortCharPair)obj;
			return key == entry.getShortKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Character.toString(value);
	}
}