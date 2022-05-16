package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectLongPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectLongImmutablePair<T> implements ObjectLongPair<T>
{
	protected final T key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public ObjectLongImmutablePair() {
		this(null, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectLongImmutablePair(T key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectLongPair<T> setKey(T key) {
		return new ObjectLongImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectLongPair<T> setLongValue(long value) {
		return new ObjectLongImmutablePair<>(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ObjectLongPair<T> set(T key, long value) {
		return new ObjectLongImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectLongPair<T> shallowCopy() {
		return this;
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