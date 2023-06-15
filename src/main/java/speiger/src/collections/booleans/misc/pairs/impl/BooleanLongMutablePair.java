package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanLongPair;



/**
 * Mutable Pair Implementation that
 */
public class BooleanLongMutablePair implements BooleanLongPair
{
	protected boolean key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public BooleanLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanLongMutablePair(boolean key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanLongPair setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public BooleanLongPair set(boolean key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanLongPair shallowCopy() {
		return BooleanLongPair.mutable(key, value);
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