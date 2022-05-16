package speiger.src.collections.booleans.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.booleans.misc.pairs.BooleanObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class BooleanObjectMutablePair<V> implements BooleanObjectPair<V>
{
	protected boolean key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public BooleanObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanObjectMutablePair(boolean key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanObjectPair<V> setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public BooleanObjectPair<V> set(boolean key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanObjectPair<V> shallowCopy() {
		return BooleanObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanObjectPair) {
			BooleanObjectPair<V> entry = (BooleanObjectPair<V>)obj;
			return key == entry.getBooleanKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Objects.toString(value);
	}
}