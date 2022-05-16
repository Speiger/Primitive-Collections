package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortIntPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortIntMutablePair implements ShortIntPair
{
	protected short key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public ShortIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortIntMutablePair(short key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortIntPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ShortIntPair set(short key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortIntPair shallowCopy() {
		return ShortIntPair.mutable(key, value);
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