package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortObjectImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortObjectMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the type of elements maintained by this Collection
 */
public interface ShortObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortObjectPair<?> EMPTY = new ShortObjectImmutablePair<>();
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> ShortObjectPair<V> of() {
		return (ShortObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> ShortObjectPair<V> ofKey(short key) {
		return new ShortObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> ShortObjectPair<V> ofValue(V value) {
		return new ShortObjectImmutablePair<>((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> ShortObjectPair<V> of(short key, V value) {
		return new ShortObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> ShortObjectPair<V> of(ShortObjectPair<V> pair) {
		return new ShortObjectImmutablePair<>(pair.getShortKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> ShortObjectPair<V> mutable() {
		return new ShortObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> ShortObjectPair<V> mutableKey(short key) {
		return new ShortObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> ShortObjectPair<V> mutableValue(V value) {
		return new ShortObjectMutablePair<>((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> ShortObjectPair<V> mutable(short key, V value) {
		return new ShortObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> ShortObjectPair<V> mutable(ShortObjectPair<V> pair) {
		return new ShortObjectMutablePair<>(pair.getShortKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortObjectPair<V> setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortObjectPair<V> setValue(V value);
	
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
	public ShortObjectPair<V> set(short key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortObjectPair<V> shallowCopy();
}