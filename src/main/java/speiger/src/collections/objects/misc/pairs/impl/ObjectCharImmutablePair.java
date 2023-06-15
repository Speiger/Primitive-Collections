package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectCharPair;

/**
 * Mutable Pair Implementation that
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectCharImmutablePair<T> implements ObjectCharPair<T>
{
	protected final T key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public ObjectCharImmutablePair() {
		this(null, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectCharImmutablePair(T key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectCharPair<T> setKey(T key) {
		return new ObjectCharImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectCharPair<T> setCharValue(char value) {
		return new ObjectCharImmutablePair<>(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ObjectCharPair<T> set(T key, char value) {
		return new ObjectCharImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectCharPair<T> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectCharPair) {
			ObjectCharPair<T> entry = (ObjectCharPair<T>)obj;
			return Objects.equals(key, entry.getKey()) && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Character.toString(value);
	}
}