package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 * @param <V> the keyType of elements maintained by this Collection
 */
public class ObjectObjectImmutablePair<T, V> implements ObjectObjectPair<T, V>
{
	protected final T key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public ObjectObjectImmutablePair() {
		this(null, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectObjectImmutablePair(T key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectObjectPair<T, V> setKey(T key) {
		return new ObjectObjectImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectObjectPair<T, V> setValue(V value) {
		return new ObjectObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ObjectObjectPair<T, V> set(T key, V value) {
		return new ObjectObjectImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectObjectPair<T, V> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectObjectPair) {
			ObjectObjectPair<T, V> entry = (ObjectObjectPair<T, V>)obj;
			return Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Objects.toString(value);
	}
}