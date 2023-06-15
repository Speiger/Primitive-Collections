package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntLongPair;



/**
 * Mutable Pair Implementation that
 */
public class IntLongMutablePair implements IntLongPair
{
	protected int key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public IntLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntLongMutablePair(int key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntLongPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public IntLongPair set(int key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntLongPair shallowCopy() {
		return IntLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntLongPair) {
			IntLongPair entry = (IntLongPair)obj;
			return key == entry.getIntKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Long.toString(value);
	}
}