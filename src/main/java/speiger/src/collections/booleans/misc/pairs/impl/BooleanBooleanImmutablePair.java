package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanBooleanImmutablePair implements BooleanBooleanPair
{
	protected final boolean key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public BooleanBooleanImmutablePair() {
		this(false, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanBooleanImmutablePair(boolean key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanBooleanPair setBooleanKey(boolean key) {
		return new BooleanBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanBooleanPair setBooleanValue(boolean value) {
		return new BooleanBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public BooleanBooleanPair set(boolean key, boolean value) {
		return new BooleanBooleanImmutablePair(key, value);
	}
	
	@Override
	public BooleanBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanBooleanPair) {
			BooleanBooleanPair entry = (BooleanBooleanPair)obj;
			return key == entry.getBooleanKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Boolean.toString(value);
	}
}