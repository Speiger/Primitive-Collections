package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectCharImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectCharPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectCharPair<?> EMPTY = new ObjectCharImmutablePair<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectCharPair<T> of() {
		return (ObjectCharPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectCharPair<T> ofKey(T key) {
		return new ObjectCharImmutablePair<>(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectCharPair<T> ofValue(char value) {
		return new ObjectCharImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectCharPair<T> of(T key, char value) {
		return new ObjectCharImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectCharPair<T> of(ObjectCharPair<T> pair) {
		return new ObjectCharImmutablePair<>(pair.getKey(), pair.getCharValue());
	}
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectCharPair<T> mutable() {
		return new ObjectCharMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectCharPair<T> mutableKey(T key) {
		return new ObjectCharMutablePair<>(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectCharPair<T> mutableValue(char value) {
		return new ObjectCharMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectCharPair<T> mutable(T key, char value) {
		return new ObjectCharMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectCharPair<T> mutable(ObjectCharPair<T> pair) {
		return new ObjectCharMutablePair<>(pair.getKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectCharPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectCharPair<T> setCharValue(char value);
	
	/**
	 * @return the Value of the Pair
	 */
	public char getCharValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectCharPair<T> set(T key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectCharPair<T> shallowCopy();
}