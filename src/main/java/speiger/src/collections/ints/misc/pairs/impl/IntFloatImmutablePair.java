package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class IntFloatImmutablePair implements IntFloatPair
{
	protected final int key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public IntFloatImmutablePair() {
		this(0, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntFloatImmutablePair(int key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntFloatPair setIntKey(int key) {
		return new IntFloatImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntFloatPair setFloatValue(float value) {
		return new IntFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public IntFloatPair set(int key, float value) {
		return new IntFloatImmutablePair(key, value);
	}
	
	@Override
	public IntFloatPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntFloatPair) {
			IntFloatPair entry = (IntFloatPair)obj;
			return key == entry.getIntKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Float.toString(value);
	}
}