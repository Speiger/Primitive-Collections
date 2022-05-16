package speiger.src.collections.shorts.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.shorts.misc.pairs.ShortObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class ShortObjectImmutablePair<V> implements ShortObjectPair<V>
{
	protected final short key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public ShortObjectImmutablePair() {
		this((short)0, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortObjectImmutablePair(short key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortObjectPair<V> setShortKey(short key) {
		return new ShortObjectImmutablePair<>(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortObjectPair<V> setValue(V value) {
		return new ShortObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ShortObjectPair<V> set(short key, V value) {
		return new ShortObjectImmutablePair<>(key, value);
	}
	
	@Override
	public ShortObjectPair<V> shallowCopy() {
		return this;
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