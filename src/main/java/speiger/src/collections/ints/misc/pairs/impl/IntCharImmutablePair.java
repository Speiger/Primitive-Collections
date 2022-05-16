package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntCharPair;

/**
 * Mutable Pair Implementation that
 */
public class IntCharImmutablePair implements IntCharPair
{
	protected final int key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public IntCharImmutablePair() {
		this(0, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntCharImmutablePair(int key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntCharPair setIntKey(int key) {
		return new IntCharImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntCharPair setCharValue(char value) {
		return new IntCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public IntCharPair set(int key, char value) {
		return new IntCharImmutablePair(key, value);
	}
	
	@Override
	public IntCharPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntCharPair) {
			IntCharPair entry = (IntCharPair)obj;
			return key == entry.getIntKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Character.toString(value);
	}
}