package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatCharPair;



/**
 * Mutable Pair Implementation that
 */
public class FloatCharMutablePair implements FloatCharPair
{
	protected float key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public FloatCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatCharMutablePair(float key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatCharPair setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public FloatCharPair set(float key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatCharPair shallowCopy() {
		return FloatCharPair.mutable(key, value);
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