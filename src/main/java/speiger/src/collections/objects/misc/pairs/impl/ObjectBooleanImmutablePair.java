package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectBooleanPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectBooleanImmutablePair<T> implements ObjectBooleanPair<T>
{
	protected final T key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public ObjectBooleanImmutablePair() {
		this(null, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectBooleanImmutablePair(T key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectBooleanPair<T> setKey(T key) {
		return new ObjectBooleanImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectBooleanPair<T> setBooleanValue(boolean value) {
		return new ObjectBooleanImmutablePair<>(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public ObjectBooleanPair<T> set(T key, boolean value) {
		return new ObjectBooleanImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectBooleanPair<T> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectBooleanPair) {
			ObjectBooleanPair<T> entry = (ObjectBooleanPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Boolean.toString(value);
	}
}