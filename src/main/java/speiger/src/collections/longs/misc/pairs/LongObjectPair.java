package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongObjectImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongObjectMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface LongObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongObjectPair<?> EMPTY = new LongObjectImmutablePair<>();
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> LongObjectPair<V> of() {
		return (LongObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> LongObjectPair<V> ofKey(long key) {
		return new LongObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> LongObjectPair<V> ofValue(V value) {
		return new LongObjectImmutablePair<>(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> LongObjectPair<V> of(long key, V value) {
		return new LongObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> LongObjectPair<V> of(LongObjectPair<V> pair) {
		return new LongObjectImmutablePair<>(pair.getLongKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> LongObjectPair<V> mutable() {
		return new LongObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> LongObjectPair<V> mutableKey(long key) {
		return new LongObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> LongObjectPair<V> mutableValue(V value) {
		return new LongObjectMutablePair<>(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> LongObjectPair<V> mutable(long key, V value) {
		return new LongObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> LongObjectPair<V> mutable(LongObjectPair<V> pair) {
		return new LongObjectMutablePair<>(pair.getLongKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongObjectPair<V> setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongObjectPair<V> setValue(V value);
	
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
	public LongObjectPair<V> set(long key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongObjectPair<V> shallowCopy();
}