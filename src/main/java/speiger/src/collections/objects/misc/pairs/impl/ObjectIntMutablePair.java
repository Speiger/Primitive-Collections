package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectIntPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectIntMutablePair<T> implements ObjectIntPair<T>
{
	protected T key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public ObjectIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectIntMutablePair(T key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectIntPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectIntPair<T> setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ObjectIntPair<T> set(T key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectIntPair<T> shallowCopy() {
		return ObjectIntPair.mutable(key, value);
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