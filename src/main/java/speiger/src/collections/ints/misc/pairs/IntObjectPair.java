package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntObjectImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntObjectMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the type of elements maintained by this Collection
 */
public interface IntObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntObjectPair<?> EMPTY = new IntObjectImmutablePair<>();
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> IntObjectPair<V> of() {
		return (IntObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> IntObjectPair<V> ofKey(int key) {
		return new IntObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> IntObjectPair<V> ofValue(V value) {
		return new IntObjectImmutablePair<>(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> IntObjectPair<V> of(int key, V value) {
		return new IntObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> IntObjectPair<V> of(IntObjectPair<V> pair) {
		return new IntObjectImmutablePair<>(pair.getIntKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> IntObjectPair<V> mutable() {
		return new IntObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> IntObjectPair<V> mutableKey(int key) {
		return new IntObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> IntObjectPair<V> mutableValue(V value) {
		return new IntObjectMutablePair<>(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> IntObjectPair<V> mutable(int key, V value) {
		return new IntObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> IntObjectPair<V> mutable(IntObjectPair<V> pair) {
		return new IntObjectMutablePair<>(pair.getIntKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntObjectPair<V> setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntObjectPair<V> setValue(V value);
	
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
	public IntObjectPair<V> set(int key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntObjectPair<V> shallowCopy();
}