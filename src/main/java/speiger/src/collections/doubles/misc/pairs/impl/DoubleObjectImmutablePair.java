package speiger.src.collections.doubles.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.doubles.misc.pairs.DoubleObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <V> the keyType of elements maintained by this Collection
 */
public class DoubleObjectImmutablePair<V> implements DoubleObjectPair<V>
{
	protected final double key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public DoubleObjectImmutablePair() {
		this(0D, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleObjectImmutablePair(double key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleObjectPair<V> setDoubleKey(double key) {
		return new DoubleObjectImmutablePair<>(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleObjectPair<V> setValue(V value) {
		return new DoubleObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public DoubleObjectPair<V> set(double key, V value) {
		return new DoubleObjectImmutablePair<>(key, value);
	}
	
	@Override
	public DoubleObjectPair<V> shallowCopy() {
		return this;
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