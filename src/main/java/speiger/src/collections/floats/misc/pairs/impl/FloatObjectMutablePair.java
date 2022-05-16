package speiger.src.collections.floats.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.floats.misc.pairs.FloatObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class FloatObjectMutablePair<V> implements FloatObjectPair<V>
{
	protected float key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public FloatObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatObjectMutablePair(float key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatObjectPair<V> setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public FloatObjectPair<V> set(float key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatObjectPair<V> shallowCopy() {
		return FloatObjectPair.mutable(key, value);
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