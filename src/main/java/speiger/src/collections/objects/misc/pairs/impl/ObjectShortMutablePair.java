package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectShortPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectShortMutablePair<T> implements ObjectShortPair<T>
{
	protected T key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public ObjectShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectShortMutablePair(T key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectShortPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectShortPair<T> setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ObjectShortPair<T> set(T key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectShortPair<T> shallowCopy() {
		return ObjectShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectShortPair) {
			ObjectShortPair<T> entry = (ObjectShortPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Short.toString(value);
	}
}