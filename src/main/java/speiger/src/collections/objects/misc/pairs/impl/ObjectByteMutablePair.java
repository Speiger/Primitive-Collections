package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectBytePair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectByteMutablePair<T> implements ObjectBytePair<T>
{
	protected T key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public ObjectByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectByteMutablePair(T key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectBytePair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectBytePair<T> setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ObjectBytePair<T> set(T key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectBytePair<T> shallowCopy() {
		return ObjectBytePair.mutable(key, value);
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