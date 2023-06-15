package speiger.src.collections.chars.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.chars.misc.pairs.CharObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the keyType of elements maintained by this Collection
 */
public class CharObjectMutablePair<V> implements CharObjectPair<V>
{
	protected char key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public CharObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharObjectMutablePair(char key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharObjectPair<V> setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public CharObjectPair<V> set(char key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharObjectPair<V> shallowCopy() {
		return CharObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharObjectPair) {
			CharObjectPair<V> entry = (CharObjectPair<V>)obj;
			return key == entry.getCharKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Objects.toString(value);
	}
}