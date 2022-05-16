package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortFloatPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortFloatMutablePair implements ShortFloatPair
{
	protected short key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public ShortFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortFloatMutablePair(short key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortFloatPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortFloatPair setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public ShortFloatPair set(short key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortFloatPair shallowCopy() {
		return ShortFloatPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortFloatPair) {
			ShortFloatPair entry = (ShortFloatPair)obj;
			return key == entry.getShortKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Float.toString(value);
	}
}