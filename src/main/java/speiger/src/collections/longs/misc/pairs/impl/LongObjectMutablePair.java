package speiger.src.collections.longs.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.longs.misc.pairs.LongObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class LongObjectMutablePair<V> implements LongObjectPair<V>
{
	protected long key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public LongObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongObjectMutablePair(long key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongObjectPair<V> setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public LongObjectPair<V> set(long key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongObjectPair<V> shallowCopy() {
		return LongObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongObjectPair) {
			LongObjectPair<V> entry = (LongObjectPair<V>)obj;
			return key == entry.getLongKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Objects.toString(value);
	}
}