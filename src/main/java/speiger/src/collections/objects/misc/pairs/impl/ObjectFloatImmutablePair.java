package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectFloatPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectFloatImmutablePair<T> implements ObjectFloatPair<T>
{
	protected final T key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public ObjectFloatImmutablePair() {
		this(null, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectFloatImmutablePair(T key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectFloatPair<T> setKey(T key) {
		return new ObjectFloatImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectFloatPair<T> setFloatValue(float value) {
		return new ObjectFloatImmutablePair<>(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public ObjectFloatPair<T> set(T key, float value) {
		return new ObjectFloatImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectFloatPair<T> shallowCopy() {
		return this;
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