package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntFloatPair;



/**
 * Mutable Pair Implementation that
 */
public class IntFloatMutablePair implements IntFloatPair
{
	protected int key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public IntFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntFloatMutablePair(int key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntFloatPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntFloatPair setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public IntFloatPair set(int key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntFloatPair shallowCopy() {
		return IntFloatPair.mutable(key, value);
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