package speiger.src.collections.doubles.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.doubles.misc.pairs.DoubleObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the keyType of elements maintained by this Collection
 */
public class DoubleObjectMutablePair<V> implements DoubleObjectPair<V>
{
	protected double key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public DoubleObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleObjectMutablePair(double key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleObjectPair<V> setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public DoubleObjectPair<V> set(double key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleObjectPair<V> shallowCopy() {
		return DoubleObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleObjectPair) {
			DoubleObjectPair<V> entry = (DoubleObjectPair<V>)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Objects.toString(value);
	}
}