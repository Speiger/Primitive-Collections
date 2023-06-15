package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectLongPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectLongMutablePair<T> implements ObjectLongPair<T>
{
	protected T key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public ObjectLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectLongMutablePair(T key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectLongPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectLongPair<T> setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ObjectLongPair<T> set(T key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectLongPair<T> shallowCopy() {
		return ObjectLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectLongPair) {
			ObjectLongPair<T> entry = (ObjectLongPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Long.toString(value);
	}
}