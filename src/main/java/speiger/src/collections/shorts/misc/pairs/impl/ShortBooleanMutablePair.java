package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortBooleanMutablePair implements ShortBooleanPair
{
	protected short key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public ShortBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortBooleanMutablePair(short key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortBooleanPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public ShortBooleanPair set(short key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortBooleanPair shallowCopy() {
		return ShortBooleanPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortBooleanPair) {
			ShortBooleanPair entry = (ShortBooleanPair)obj;
			return key == entry.getShortKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Boolean.toString(value);
	}
}