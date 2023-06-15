package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class CharDoubleMutablePair implements CharDoublePair
{
	protected char key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public CharDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharDoubleMutablePair(char key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharDoublePair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public CharDoublePair set(char key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharDoublePair shallowCopy() {
		return CharDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharDoublePair) {
			CharDoublePair entry = (CharDoublePair)obj;
			return key == entry.getCharKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Double.toString(value);
	}
}