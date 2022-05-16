package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class LongBooleanMutablePair implements LongBooleanPair
{
	protected long key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public LongBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongBooleanMutablePair(long key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongBooleanPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public LongBooleanPair set(long key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongBooleanPair shallowCopy() {
		return LongBooleanPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongBooleanPair) {
			LongBooleanPair entry = (LongBooleanPair)obj;
			return key == entry.getLongKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Boolean.toString(value);
	}
}