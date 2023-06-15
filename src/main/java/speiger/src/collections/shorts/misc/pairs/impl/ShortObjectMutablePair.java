package speiger.src.collections.shorts.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.shorts.misc.pairs.ShortObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the keyType of elements maintained by this Collection
 */
public class ShortObjectMutablePair<V> implements ShortObjectPair<V>
{
	protected short key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public ShortObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortObjectMutablePair(short key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortObjectPair<V> setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ShortObjectPair<V> set(short key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortObjectPair<V> shallowCopy() {
		return ShortObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortObjectPair) {
			ShortObjectPair<V> entry = (ShortObjectPair<V>)obj;
			return key == entry.getShortKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Objects.toString(value);
	}
}