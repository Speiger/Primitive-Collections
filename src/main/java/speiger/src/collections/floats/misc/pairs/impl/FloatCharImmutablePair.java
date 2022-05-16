package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatCharPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatCharImmutablePair implements FloatCharPair
{
	protected final float key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public FloatCharImmutablePair() {
		this(0F, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatCharImmutablePair(float key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatCharPair setFloatKey(float key) {
		return new FloatCharImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatCharPair setCharValue(char value) {
		return new FloatCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public FloatCharPair set(float key, char value) {
		return new FloatCharImmutablePair(key, value);
	}
	
	@Override
	public FloatCharPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatCharPair) {
			FloatCharPair entry = (FloatCharPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Character.toString(value);
	}
}