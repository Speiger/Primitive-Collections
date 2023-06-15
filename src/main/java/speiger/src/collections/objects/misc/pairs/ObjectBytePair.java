package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectByteImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectByteMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectBytePair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectBytePair<?> EMPTY = new ObjectByteImmutablePair<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectBytePair<T> of() {
		return (ObjectBytePair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectBytePair<T> ofKey(T key) {
		return new ObjectByteImmutablePair<>(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectBytePair<T> ofValue(byte value) {
		return new ObjectByteImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectBytePair<T> of(T key, byte value) {
		return new ObjectByteImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectBytePair<T> of(ObjectBytePair<T> pair) {
		return new ObjectByteImmutablePair<>(pair.getKey(), pair.getByteValue());
	}
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectBytePair<T> mutable() {
		return new ObjectByteMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectBytePair<T> mutableKey(T key) {
		return new ObjectByteMutablePair<>(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectBytePair<T> mutableValue(byte value) {
		return new ObjectByteMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectBytePair<T> mutable(T key, byte value) {
		return new ObjectByteMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectBytePair<T> mutable(ObjectBytePair<T> pair) {
		return new ObjectByteMutablePair<>(pair.getKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectBytePair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectBytePair<T> setByteValue(byte value);
	
	/**
	 * @return the Value of the Pair
	 */
	public byte getByteValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectBytePair<T> set(T key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectBytePair<T> shallowCopy();
}