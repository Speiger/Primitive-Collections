package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectFloatPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectFloatMutablePair<T> implements ObjectFloatPair<T>
{
	protected T key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public ObjectFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectFloatMutablePair(T key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectFloatPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectFloatPair<T> setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public ObjectFloatPair<T> set(T key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectFloatPair<T> shallowCopy() {
		return ObjectFloatPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectFloatPair) {
			ObjectFloatPair<T> entry = (ObjectFloatPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Float.toString(value);
	}
}