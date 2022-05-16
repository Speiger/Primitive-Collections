package speiger.src.collections.ints.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.ints.misc.pairs.IntObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class IntObjectMutablePair<V> implements IntObjectPair<V>
{
	protected int key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public IntObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntObjectMutablePair(int key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntObjectPair<V> setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public IntObjectPair<V> set(int key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntObjectPair<V> shallowCopy() {
		return IntObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntObjectPair) {
			IntObjectPair<V> entry = (IntObjectPair<V>)obj;
			return key == entry.getIntKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Objects.toString(value);
	}
}