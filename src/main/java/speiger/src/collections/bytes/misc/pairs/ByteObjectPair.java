package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteObjectImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteObjectMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface ByteObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteObjectPair<?> EMPTY = new ByteObjectImmutablePair<>();
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> ByteObjectPair<V> of() {
		return (ByteObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> ByteObjectPair<V> ofKey(byte key) {
		return new ByteObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> ByteObjectPair<V> ofValue(V value) {
		return new ByteObjectImmutablePair<>((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> ByteObjectPair<V> of(byte key, V value) {
		return new ByteObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> ByteObjectPair<V> of(ByteObjectPair<V> pair) {
		return new ByteObjectImmutablePair<>(pair.getByteKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> ByteObjectPair<V> mutable() {
		return new ByteObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> ByteObjectPair<V> mutableKey(byte key) {
		return new ByteObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> ByteObjectPair<V> mutableValue(V value) {
		return new ByteObjectMutablePair<>((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> ByteObjectPair<V> mutable(byte key, V value) {
		return new ByteObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> ByteObjectPair<V> mutable(ByteObjectPair<V> pair) {
		return new ByteObjectMutablePair<>(pair.getByteKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteObjectPair<V> setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteObjectPair<V> setValue(V value);
	
	/**
	 * @return the Value of the Pair
	 */
	public V getValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ByteObjectPair<V> set(byte key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteObjectPair<V> shallowCopy();
}