package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortLongPair;

/**
 * Mutable Pair Implementation that
 */
public class ShortLongImmutablePair implements ShortLongPair
{
	protected final short key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public ShortLongImmutablePair() {
		this((short)0, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortLongImmutablePair(short key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortLongPair setShortKey(short key) {
		return new ShortLongImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortLongPair setLongValue(long value) {
		return new ShortLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ShortLongPair set(short key, long value) {
		return new ShortLongImmutablePair(key, value);
	}
	
	@Override
	public ShortLongPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortLongPair) {
			ShortLongPair entry = (ShortLongPair)obj;
			return key == entry.getShortKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Long.toString(value);
	}
}