package speiger.src.collections.floats.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.floats.misc.pairs.FloatObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class FloatObjectImmutablePair<V> implements FloatObjectPair<V>
{
	protected final float key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public FloatObjectImmutablePair() {
		this(0F, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatObjectImmutablePair(float key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatObjectPair<V> setFloatKey(float key) {
		return new FloatObjectImmutablePair<>(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatObjectPair<V> setValue(V value) {
		return new FloatObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public FloatObjectPair<V> set(float key, V value) {
		return new FloatObjectImmutablePair<>(key, value);
	}
	
	@Override
	public FloatObjectPair<V> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatObjectPair) {
			FloatObjectPair<V> entry = (FloatObjectPair<V>)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Objects.toString(value);
	}
}