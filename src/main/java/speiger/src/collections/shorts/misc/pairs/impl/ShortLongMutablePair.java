package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortLongPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortLongMutablePair implements ShortLongPair
{
	protected short key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public ShortLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortLongMutablePair(short key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortLongPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ShortLongPair set(short key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortLongPair shallowCopy() {
		return ShortLongPair.mutable(key, value);
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