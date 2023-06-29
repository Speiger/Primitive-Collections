package speiger.src.collections.objects.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Comparator;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.ToByteFunction;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.maps.impl.customHash.Object2ByteLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2ByteOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ByteLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ByteOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2ByteOpenHashMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ByteAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ByteRBTreeMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ByteArrayMap;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ByteConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2ByteMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.objects.utils.maps.Object2ByteMaps;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface Object2ByteMap<T> extends Map<T, Byte>, ToByteFunction<T>
{
	/**
	 * Helper Class that allows to create Maps without requiring a to type out the entire implementation or know it.
	 * @return a MapBuilder
	 */
	public static MapBuilder builder() {
		return MapBuilder.INSTANCE;
	}
	
	/**
	 * Method to see what the default return value is.
	 * @return default return value
	 */
	public byte getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Object2ByteMap<T> setDefaultReturnValue(byte v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Object2ByteMap<T> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public byte put(T key, byte value);
	
	/**
	 * A Helper method that allows to put int a Object2ByteMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default byte put(Entry<T> entry) {
		return put(entry.getKey(), entry.getByteValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Byte put(Map.Entry<T, Byte> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, byte[] values) {
		if(keys.length != values.length) throw new IllegalStateException("Array sizes do not match");
		putAll(keys, values, 0, keys.length);
	}
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @param offset where the to start in the array
	 * @param size how many elements should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not within the range
	 */
	public void putAll(T[] keys, byte[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, Byte[] values) {
		if(keys.length != values.length) throw new IllegalStateException("Array sizes do not match");
		putAll(keys, values, 0, keys.length);
	}
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @param offset where the to start in the array
	 * @param size how many elements should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not within the range
	 */
	public void putAll(T[] keys, Byte[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public byte putIfAbsent(T key, byte value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Object2ByteMap<T> m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public byte addTo(T key, byte value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Object2ByteMap<T> m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public byte subFrom(T key, byte value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Object2ByteMap<T> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(byte value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Byte && containsValue(((Byte)value).byteValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public byte rem(T key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be T but just have to support equals with T.
	 */
	@Override
	public default Byte remove(Object key) {
		return Byte.valueOf(rem((T)key));
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(T key, byte value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return value instanceof Byte && remove((T)key, ((Byte)value).byteValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public byte remOrDefault(T key, byte defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(T key, byte oldValue, byte newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public byte replace(T key, byte value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBytes(Object2ByteMap<T> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBytes(ObjectByteUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsent(T key, ToByteFunction<T> mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsent(T key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByteNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsentNonDefault(T key, ToByteFunction<T> mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsentNonDefault(T key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresentNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllByte(Object2ByteMap<T> m, ByteByteUnaryOperator mappingFunction);
	
	@Override
	public default boolean replace(T key, Byte oldValue, Byte newValue) {
		return replace(key, oldValue.byteValue(), newValue.byteValue());
	}
	
	@Override
	public default Byte replace(T key, Byte value) {
		return Byte.valueOf(replace(key, value.byteValue()));
	}
	
	@Override
	public default byte applyAsByte(T key) {
		return getByte(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public byte getByte(T key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public byte getOrDefault(T key, byte defaultValue);
	
	@Override
	public default Byte get(Object key) {
		return Byte.valueOf(getByte((T)key));
	}
	
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		Byte value = Byte.valueOf(getByte((T)key));
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public default void replaceAll(BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBytes(mappingFunction instanceof ObjectByteUnaryOperator ? (ObjectByteUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Byte.valueOf(V)).byteValue());
	}
	
	@Override
	public default Byte compute(T key, BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByte(key, mappingFunction instanceof ObjectByteUnaryOperator ? (ObjectByteUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	public default Byte computeIfAbsent(T key, Function<? super T, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfAbsent(key, mappingFunction instanceof ToByteFunction ? (ToByteFunction<T>)mappingFunction : K -> mappingFunction.apply(K).byteValue()));
	}
	
	@Override
	public default Byte computeIfPresent(T key, BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfPresent(key, mappingFunction instanceof ObjectByteUnaryOperator ? (ObjectByteUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	public default Byte merge(T key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(mergeByte(key, value.byteValue(), mappingFunction instanceof ByteByteUnaryOperator ? (ByteByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ObjectByteConsumer<T> action);
	
	@Override
	public default void forEach(BiConsumer<? super T, ? super Byte> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ObjectByteConsumer ? (ObjectByteConsumer<T>)action : (K, V) -> action.accept(K, Byte.valueOf(V)));
	}
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public ByteCollection values();
	@Override
	public ObjectSet<Map.Entry<T, Byte>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<T>> object2ByteEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteMap<T> synchronize() { return Object2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteMap<T> synchronize(Object mutex) { return Object2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Object2ByteMaps#unmodifiable
	 */
	public default Object2ByteMap<T> unmodifiable() { return Object2ByteMaps.unmodifiable(this); }
	
	@Override
	public default Byte put(T key, Byte value) {
		return Byte.valueOf(put(key, value.byteValue()));
	}
	
	@Override
	public default Byte putIfAbsent(T key, Byte value) {
		return Byte.valueOf(put(key, value.byteValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public interface FastEntrySet<T> extends ObjectSet<Object2ByteMap.Entry<T>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Object2ByteMap.Entry<T>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Object2ByteMap.Entry<T>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public interface Entry<T> extends Map.Entry<T, Byte>
	{
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public byte getByteValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public byte setValue(byte value);
		@Override
		public default Byte getValue() { return Byte.valueOf(getByteValue()); }
		@Override
		public default Byte setValue(Byte value) { return Byte.valueOf(setValue(value.byteValue())); }
	}
	
	/**
	 * Helper class that reduces the method spam of the Map Class.
	 */
	public static final class MapBuilder
	{
		static final MapBuilder INSTANCE = new MapBuilder();
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start() {
			return new BuilderCache<T>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start(int size) {
			return new BuilderCache<T>(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, byte value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, Byte value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2ByteOpenHashMap<T> map() {
			return new Object2ByteOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2ByteOpenHashMap<T> map(int size) {
			return new Object2ByteOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2ByteOpenHashMap<T> map(T[] keys, byte[] values) {
			return new Object2ByteOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteOpenHashMap<T> map(T[] keys, Byte[] values) {
			return new Object2ByteOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteOpenHashMap<T> map(Object2ByteMap<T> map) {
			return new Object2ByteOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteOpenHashMap<T> map(Map<? extends T, ? extends Byte> map) {
			return new Object2ByteOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <T> Object2ByteLinkedOpenHashMap<T> linkedMap() {
			return new Object2ByteLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ByteLinkedOpenHashMap<T> linkedMap(int size) {
			return new Object2ByteLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2ByteLinkedOpenHashMap<T> linkedMap(T[] keys, byte[] values) {
			return new Object2ByteLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteLinkedOpenHashMap<T> linkedMap(T[] keys, Byte[] values) {
			return new Object2ByteLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteLinkedOpenHashMap<T> linkedMap(Object2ByteMap<T> map) {
			return new Object2ByteLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2ByteOpenHashMap<T> linkedMap(Map<? extends T, ? extends Byte> map) {
			return new ImmutableObject2ByteOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <T> ImmutableObject2ByteOpenHashMap<T> immutable(T[] keys, byte[] values) {
			return new ImmutableObject2ByteOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> ImmutableObject2ByteOpenHashMap<T> immutable(T[] keys, Byte[] values) {
			return new ImmutableObject2ByteOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <T> ImmutableObject2ByteOpenHashMap<T> immutable(Object2ByteMap<T> map) {
			return new ImmutableObject2ByteOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2ByteOpenHashMap<T> immutable(Map<? extends T, ? extends Byte> map) {
			return new ImmutableObject2ByteOpenHashMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a Empty EnumMap
		 */
		public <T extends Enum<T>> Enum2ByteMap<T> enumMap(Class<T> keyType) {
			 return new Enum2ByteMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the keyType of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> Enum2ByteMap<T> enumMap(T[] keys, Byte[] values) {
			return new Enum2ByteMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the keyType of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> Enum2ByteMap<T> enumMap(T[] keys, byte[] values) {
			return new Enum2ByteMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a EnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> Enum2ByteMap<T> enumMap(Map<? extends T, ? extends Byte> map) {
			return new Enum2ByteMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a EnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> Enum2ByteMap<T> enumMap(Object2ByteMap<T> map) {
			return new Enum2ByteMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a Empty LinkedEnumMap
		 */
		public <T extends Enum<T>> LinkedEnum2ByteMap<T> linkedEnumMap(Class<T> keyType) {
			 return new LinkedEnum2ByteMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the keyType of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2ByteMap<T> linkedEnumMap(T[] keys, Byte[] values) {
			return new LinkedEnum2ByteMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the keyType of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> LinkedEnum2ByteMap<T> linkedEnumMap(T[] keys, byte[] values) {
			return new LinkedEnum2ByteMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the keyType of elements maintained by this Collection
		 * @return a LinkedEnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2ByteMap<T> linkedEnumMap(Map<? extends T, ? extends Byte> map) {
			return new LinkedEnum2ByteMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a LinkedEnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> LinkedEnum2ByteMap<T> linkedEnumMap(Object2ByteMap<T> map) {
			return new LinkedEnum2ByteMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(int size, ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(T[] keys, byte[] values, ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(T[] keys, Byte[] values, ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(Object2ByteMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteOpenCustomHashMap<T> customMap(Map<? extends T, ? extends Byte> map, ObjectStrategy<T> strategy) {
			return new Object2ByteOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(int size, ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, byte[] values, ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, Byte[] values, ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(Object2ByteMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(Map<? extends T, ? extends Byte> map, ObjectStrategy<T> strategy) {
			return new Object2ByteLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2ByteArrayMap<T> arrayMap() {
			return new Object2ByteArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2ByteArrayMap<T> arrayMap(int size) {
			return new Object2ByteArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2ByteArrayMap<T> arrayMap(T[] keys, byte[] values) {
			return new Object2ByteArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteArrayMap<T> arrayMap(T[] keys, Byte[] values) {
			return new Object2ByteArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteArrayMap<T> arrayMap(Object2ByteMap<T> map) {
			return new Object2ByteArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteArrayMap<T> arrayMap(Map<? extends T, ? extends Byte> map) {
			return new Object2ByteArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap() {
			return new Object2ByteRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return new Object2ByteRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap(T[] keys, byte[] values, Comparator<T> comp) {
			return new Object2ByteRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap(T[] keys, Byte[] values, Comparator<T> comp) {
			return new Object2ByteRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap(Object2ByteMap<T> map, Comparator<T> comp) {
			return new Object2ByteRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteRBTreeMap<T> rbTreeMap(Map<? extends T, ? extends Byte> map, Comparator<T> comp) {
			return new Object2ByteRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap() {
			return new Object2ByteAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return new Object2ByteAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap(T[] keys, byte[] values, Comparator<T> comp) {
			return new Object2ByteAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap(T[] keys, Byte[] values, Comparator<T> comp) {
			return new Object2ByteAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap(Object2ByteMap<T> map, Comparator<T> comp) {
			return new Object2ByteAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ByteAVLTreeMap<T> avlTreeMap(Map<? extends T, ? extends Byte> map, Comparator<T> comp) {
			return new Object2ByteAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BuilderCache<T>
	{
		T[] keys;
		byte[] values;
		int size;
		
		/**
		 * Default Constructor
		 */
		public BuilderCache() {
			this(HashUtil.DEFAULT_MIN_CAPACITY);
		}
		
		/**
		 * Constructor providing a Minimum Capcity
		 * @param initialSize the requested start capacity
		 */
		public BuilderCache(int initialSize) {
			if(initialSize < 0) throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			keys = (T[])new Object[initialSize];
			values = new byte[initialSize];
		}
		
		private void ensureSize(int newSize) {
			if(keys.length >= newSize) return;
			newSize = (int)Math.max(Math.min((long)keys.length + (keys.length >> 1), SanityChecks.MAX_ARRAY_SIZE), newSize);
			keys = Arrays.copyOf(keys, newSize);
			values = Arrays.copyOf(values, newSize);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return self
		 */
		public BuilderCache<T> put(T key, byte value) {
			ensureSize(size+1);
			keys[size] = key;
			values[size] = value;
			size++;
			return this;
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return self
		 */
		public BuilderCache<T> put(T key, Byte value) {
			return put(key, value.byteValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<T> put(Object2ByteMap.Entry<T> entry) {
			return put(entry.getKey(), entry.getByteValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Object2ByteMap<T> map) {
			return putAll(Object2ByteMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Map<? extends T, ? extends Byte> map) {
			for(Map.Entry<? extends T, ? extends Byte> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(ObjectIterable<Object2ByteMap.Entry<T>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Object2ByteMap.Entry<T>>)c).size());
			
			for(Object2ByteMap.Entry<T> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Object2ByteMap<T>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Object2ByteOpenHashMap
		 */
		public Object2ByteOpenHashMap<T> map() {
			return putElements(new Object2ByteOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Object2ByteLinkedOpenHashMap
		 */
		public Object2ByteLinkedOpenHashMap<T> linkedMap() {
			return putElements(new Object2ByteLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableObject2ByteOpenHashMap
		 */
		public ImmutableObject2ByteOpenHashMap<T> immutable() {
			return new ImmutableObject2ByteOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2ByteOpenCustomHashMap
		 */
		public Object2ByteOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2ByteOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2ByteLinkedOpenCustomHashMap
		 */
		public Object2ByteLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2ByteLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Object2ByteConcurrentOpenHashMap
		 */
		public Object2ByteConcurrentOpenHashMap<T> concurrentMap() {
			return putElements(new Object2ByteConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Object2ByteArrayMap
		 */
		public Object2ByteArrayMap<T> arrayMap() {
			return new Object2ByteArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Object2ByteRBTreeMap
		 */
		public Object2ByteRBTreeMap<T> rbTreeMap() {
			return putElements(new Object2ByteRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2ByteRBTreeMap
		 */
		public Object2ByteRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return putElements(new Object2ByteRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Object2ByteAVLTreeMap
		 */
		public Object2ByteAVLTreeMap<T> avlTreeMap() {
			return putElements(new Object2ByteAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2ByteAVLTreeMap
		 */
		public Object2ByteAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return putElements(new Object2ByteAVLTreeMap<>(comp));
		}
	}
}