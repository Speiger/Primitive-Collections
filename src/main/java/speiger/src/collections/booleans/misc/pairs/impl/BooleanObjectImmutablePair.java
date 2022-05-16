package speiger.src.collections.booleans.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.booleans.misc.pairs.BooleanObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class BooleanObjectImmutablePair<V> implements BooleanObjectPair<V>
{
	protected final boolean key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public BooleanObjectImmutablePair() {
		this(false, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanObjectImmutablePair(boolean key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanObjectPair<V> setBooleanKey(boolean key) {
		return new BooleanObjectImmutablePair<>(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanObjectPair<V> setValue(V value) {
		return new BooleanObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public BooleanObjectPair<V> set(boolean key, V value) {
		return new BooleanObjectImmutablePair<>(key, value);
	}
	
	@Override
	public BooleanObjectPair<V> shallowCopy() {
		return this;
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