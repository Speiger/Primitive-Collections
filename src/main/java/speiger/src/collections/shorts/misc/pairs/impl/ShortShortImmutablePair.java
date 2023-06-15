package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortShortPair;

/**
 * Mutable Pair Implementation that
 */
public class ShortShortImmutablePair implements ShortShortPair
{
	protected final short key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public ShortShortImmutablePair() {
		this((short)0, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortShortImmutablePair(short key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortShortPair setShortKey(short key) {
		return new ShortShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortShortPair setShortValue(short value) {
		return new ShortShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ShortShortPair set(short key, short value) {
		return new ShortShortImmutablePair(key, value);
	}
	
	@Override
	public ShortShortPair shallowCopy() {
		return this;
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