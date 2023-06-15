package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleObjectImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleObjectMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface DoubleObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleObjectPair<?> EMPTY = new DoubleObjectImmutablePair<>();
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> DoubleObjectPair<V> of() {
		return (DoubleObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> DoubleObjectPair<V> ofKey(double key) {
		return new DoubleObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> DoubleObjectPair<V> ofValue(V value) {
		return new DoubleObjectImmutablePair<>(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> DoubleObjectPair<V> of(double key, V value) {
		return new DoubleObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> DoubleObjectPair<V> of(DoubleObjectPair<V> pair) {
		return new DoubleObjectImmutablePair<>(pair.getDoubleKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> DoubleObjectPair<V> mutable() {
		return new DoubleObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> DoubleObjectPair<V> mutableKey(double key) {
		return new DoubleObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> DoubleObjectPair<V> mutableValue(V value) {
		return new DoubleObjectMutablePair<>(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> DoubleObjectPair<V> mutable(double key, V value) {
		return new DoubleObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> DoubleObjectPair<V> mutable(DoubleObjectPair<V> pair) {
		return new DoubleObjectMutablePair<>(pair.getDoubleKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleObjectPair<V> setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleObjectPair<V> setValue(V value);
	
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
	public DoubleObjectPair<V> set(double key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleObjectPair<V> shallowCopy();
}