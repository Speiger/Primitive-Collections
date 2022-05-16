package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectCharPair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectCharMutablePair<T> implements ObjectCharPair<T>
{
	protected T key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public ObjectCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectCharMutablePair(T key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectCharPair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectCharPair<T> setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ObjectCharPair<T> set(T key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectCharPair<T> shallowCopy() {
		return ObjectCharPair.mutable(key, value);
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