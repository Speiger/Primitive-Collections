package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class LongFloatImmutablePair implements LongFloatPair
{
	protected final long key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public LongFloatImmutablePair() {
		this(0L, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongFloatImmutablePair(long key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongFloatPair setLongKey(long key) {
		return new LongFloatImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongFloatPair setFloatValue(float value) {
		return new LongFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public LongFloatPair set(long key, float value) {
		return new LongFloatImmutablePair(key, value);
	}
	
	@Override
	public LongFloatPair shallowCopy() {
		return this;
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