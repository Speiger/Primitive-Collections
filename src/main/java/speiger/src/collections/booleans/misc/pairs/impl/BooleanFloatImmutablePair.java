package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanFloatImmutablePair implements BooleanFloatPair
{
	protected final boolean key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public BooleanFloatImmutablePair() {
		this(false, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanFloatImmutablePair(boolean key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanFloatPair setBooleanKey(boolean key) {
		return new BooleanFloatImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanFloatPair setFloatValue(float value) {
		return new BooleanFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public BooleanFloatPair set(boolean key, float value) {
		return new BooleanFloatImmutablePair(key, value);
	}
	
	@Override
	public BooleanFloatPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanFloatPair) {
			BooleanFloatPair entry = (BooleanFloatPair)obj;
			return key == entry.getBooleanKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Float.toString(value);
	}
}