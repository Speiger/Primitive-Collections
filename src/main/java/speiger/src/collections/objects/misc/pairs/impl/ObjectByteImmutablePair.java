package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectBytePair;

/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectByteImmutablePair<T> implements ObjectBytePair<T>
{
	protected final T key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public ObjectByteImmutablePair() {
		this(null, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectByteImmutablePair(T key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectBytePair<T> setKey(T key) {
		return new ObjectByteImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectBytePair<T> setByteValue(byte value) {
		return new ObjectByteImmutablePair<>(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ObjectBytePair<T> set(T key, byte value) {
		return new ObjectByteImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectBytePair<T> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectBytePair) {
			ObjectBytePair<T> entry = (ObjectBytePair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Byte.toString(value);
	}
}