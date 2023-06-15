package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongFloatPair;



/**
 * Mutable Pair Implementation that
 */
public class LongFloatMutablePair implements LongFloatPair
{
	protected long key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public LongFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongFloatMutablePair(long key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongFloatPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongFloatPair setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public LongFloatPair set(long key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongFloatPair shallowCopy() {
		return LongFloatPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongFloatPair) {
			LongFloatPair entry = (LongFloatPair)obj;
			return key == entry.getLongKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Float.toString(value);
	}
}