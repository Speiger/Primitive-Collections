package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortIntPair;

/**
 * Mutable Pair Implementation that
 */
public class ShortIntImmutablePair implements ShortIntPair
{
	protected final short key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public ShortIntImmutablePair() {
		this((short)0, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortIntImmutablePair(short key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortIntPair setShortKey(short key) {
		return new ShortIntImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortIntPair setIntValue(int value) {
		return new ShortIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ShortIntPair set(short key, int value) {
		return new ShortIntImmutablePair(key, value);
	}
	
	@Override
	public ShortIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortIntPair) {
			ShortIntPair entry = (ShortIntPair)obj;
			return key == entry.getShortKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Integer.toString(value);
	}
}