package speiger.src.collections.bytes.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.bytes.misc.pairs.ByteObjectPair;



/**
 * Mutable Pair Implementation that
 * @param <V> the type of elements maintained by this Collection
 */
public class ByteObjectMutablePair<V> implements ByteObjectPair<V>
{
	protected byte key;
	protected V value;
	
	/**
	 * Default Constructor
	 */
	public ByteObjectMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteObjectMutablePair(byte key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteObjectPair<V> setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteObjectPair<V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ByteObjectPair<V> set(byte key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteObjectPair<V> shallowCopy() {
		return ByteObjectPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteObjectPair) {
			ByteObjectPair<V> entry = (ByteObjectPair<V>)obj;
			return key == entry.getByteKey() && Objects.equals(value, entry.getValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Objects.toString(value);
	}
}