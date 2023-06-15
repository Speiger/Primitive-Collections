package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 * @param <V> the keyType of elements maintained by this Collection
 */
public class ObjectObjectMutablePair<T, V> implements ObjectObjectPair<T, V>
{
	protected T key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public ObjectObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectObjectMutablePair(T key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectObjectPair<T, V> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectObjectPair<T, V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ObjectObjectPair<T, V> set(T key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectObjectPair<T, V> shallowCopy() {
		return ObjectObjectPair.mutable(key, value);
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