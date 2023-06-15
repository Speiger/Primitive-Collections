package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectShortPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectShortImmutablePair<T> implements ObjectShortPair<T>
{
	protected final T key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public ObjectShortImmutablePair() {
		this(null, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectShortImmutablePair(T key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectShortPair<T> setKey(T key) {
		return new ObjectShortImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectShortPair<T> setShortValue(short value) {
		return new ObjectShortImmutablePair<>(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ObjectShortPair<T> set(T key, short value) {
		return new ObjectShortImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectShortPair<T> shallowCopy() {
		return this;
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