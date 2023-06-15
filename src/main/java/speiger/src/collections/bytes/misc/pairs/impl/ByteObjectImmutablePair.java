package speiger.src.collections.bytes.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.bytes.misc.pairs.ByteObjectPair;

/**
 * Mutable Pair Implementation that
 * @param <V> the keyType of elements maintained by this Collection
 */
public class ByteObjectImmutablePair<V> implements ByteObjectPair<V>
{
	protected final byte key;
	protected final V value;
	
	/**
	 * Default Constructor
	 */
	public ByteObjectImmutablePair() {
		this((byte)0, null);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteObjectImmutablePair(byte key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteObjectPair<V> setByteKey(byte key) {
		return new ByteObjectImmutablePair<>(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteObjectPair<V> setValue(V value) {
		return new ByteObjectImmutablePair<>(key, value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public ByteObjectPair<V> set(byte key, V value) {
		return new ByteObjectImmutablePair<>(key, value);
	}
	
	@Override
	public ByteObjectPair<V> shallowCopy() {
		return this;
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