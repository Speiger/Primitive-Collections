package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanLongPair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanLongImmutablePair implements BooleanLongPair
{
	protected final boolean key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public BooleanLongImmutablePair() {
		this(false, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanLongImmutablePair(boolean key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanLongPair setBooleanKey(boolean key) {
		return new BooleanLongImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanLongPair setLongValue(long value) {
		return new BooleanLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public BooleanLongPair set(boolean key, long value) {
		return new BooleanLongImmutablePair(key, value);
	}
	
	@Override
	public BooleanLongPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanLongPair) {
			BooleanLongPair entry = (BooleanLongPair)obj;
			return key == entry.getBooleanKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Long.toString(value);
	}
}