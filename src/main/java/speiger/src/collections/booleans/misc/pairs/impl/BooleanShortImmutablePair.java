package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanShortPair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanShortImmutablePair implements BooleanShortPair
{
	protected final boolean key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public BooleanShortImmutablePair() {
		this(false, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanShortImmutablePair(boolean key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanShortPair setBooleanKey(boolean key) {
		return new BooleanShortImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanShortPair setShortValue(short value) {
		return new BooleanShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public BooleanShortPair set(boolean key, short value) {
		return new BooleanShortImmutablePair(key, value);
	}
	
	@Override
	public BooleanShortPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanShortPair) {
			BooleanShortPair entry = (BooleanShortPair)obj;
			return key == entry.getBooleanKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Short.toString(value);
	}
}