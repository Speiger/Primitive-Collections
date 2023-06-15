package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectBooleanPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectBooleanMutablePair<T> implements ObjectBooleanPair<T>
{
	protected T key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public ObjectBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectBooleanMutablePair(T key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectBooleanPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectBooleanPair<T> setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public ObjectBooleanPair<T> set(T key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectBooleanPair<T> shallowCopy() {
		return ObjectBooleanPair.mutable(key, value);
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