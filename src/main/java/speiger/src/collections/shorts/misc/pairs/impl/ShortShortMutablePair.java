package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortShortPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortShortMutablePair implements ShortShortPair
{
	protected short key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public ShortShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortShortMutablePair(short key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortShortPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ShortShortPair set(short key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortShortPair shallowCopy() {
		return ShortShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortShortPair) {
			ShortShortPair entry = (ShortShortPair)obj;
			return key == entry.getShortKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Short.toString(value);
	}
}