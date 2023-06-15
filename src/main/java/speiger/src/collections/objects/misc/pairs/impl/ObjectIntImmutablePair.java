package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectIntPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectIntImmutablePair<T> implements ObjectIntPair<T>
{
	protected final T key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public ObjectIntImmutablePair() {
		this(null, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectIntImmutablePair(T key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectIntPair<T> setKey(T key) {
		return new ObjectIntImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectIntPair<T> setIntValue(int value) {
		return new ObjectIntImmutablePair<>(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ObjectIntPair<T> set(T key, int value) {
		return new ObjectIntImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectIntPair<T> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectIntPair) {
			ObjectIntPair<T> entry = (ObjectIntPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Integer.toString(value);
	}
}