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

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.Object2ShortFunction;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.maps.impl.customHash.Object2ShortLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2ShortOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ShortLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ShortOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2ShortOpenHashMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ShortAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ShortRBTreeMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ShortArrayMap;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ShortConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2ShortMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.objects.utils.maps.Object2ShortMaps;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2ShortMap<T> extends Map<T, Short>, Object2ShortFunction<T>
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
	public short getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Object2ShortMap<T> setDefaultReturnValue(short v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Object2ShortMap<T> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public short put(T key, short value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, short[] values) {
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
	public void putAll(T[] keys, short[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, Short[] values) {
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
	public void putAll(T[] keys, Short[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public short putIfAbsent(T key, short value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Object2ShortMap<T> m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public short addTo(T key, short value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Object2ShortMap<T> m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public short subFrom(T key, short value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Object2ShortMap<T> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(short value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Short && containsValue(((Short)value).shortValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public short rem(T key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be T but just have to support equals with T.
	 */
	@Override
	public default Short remove(Object key) {
		return Short.valueOf(rem((T)key));
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(T key, short value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return value instanceof Short && remove((T)key, ((Short)value).shortValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public short remOrDefault(T key, short defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(T key, short oldValue, short newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public short replace(T key, short value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceShorts(Object2ShortMap<T> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceShorts(ObjectShortUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsent(T key, Object2ShortFunction<T> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsent(T key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction);
	
	@Override
	public default boolean replace(T key, Short oldValue, Short newValue) {
		return replace(key, oldValue.shortValue(), newValue.shortValue());
	}
	
	@Override
	public default Short replace(T key, Short value) {
		return Short.valueOf(replace(key, value.shortValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public short getShort(T key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public short getOrDefault(T key, short defaultValue);
	
	@Override
	public default Short get(Object key) {
		return Short.valueOf(getShort((T)key));
	}
	
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		Short value = Short.valueOf(getShort((T)key));
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public default void replaceAll(BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceShorts(mappingFunction instanceof ObjectShortUnaryOperator ? (ObjectShortUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Short.valueOf(V)).shortValue());
	}
	
	@Override
	public default Short compute(T key, BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShort(key, mappingFunction instanceof ObjectShortUnaryOperator ? (ObjectShortUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Short.valueOf(V)).shortValue()));
	}
	
	@Override
	public default Short computeIfAbsent(T key, Function<? super T, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfAbsent(key, mappingFunction instanceof Object2ShortFunction ? (Object2ShortFunction<T>)mappingFunction : K -> mappingFunction.apply(K).shortValue()));
	}
	
	@Override
	public default Short computeIfPresent(T key, BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfPresent(key, mappingFunction instanceof ObjectShortUnaryOperator ? (ObjectShortUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Short.valueOf(V)).shortValue()));
	}
	
	@Override
	public default Short merge(T key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(mergeShort(key, value.shortValue(), mappingFunction instanceof ShortShortUnaryOperator ? (ShortShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ObjectShortConsumer<T> action);
	
	@Override
	public default void forEach(BiConsumer<? super T, ? super Short> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ObjectShortConsumer ? (ObjectShortConsumer<T>)action : (K, V) -> action.accept(K, Short.valueOf(V)));
	}
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public ShortCollection values();
	@Override
	public ObjectSet<Map.Entry<T, Short>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<T>> object2ShortEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Object2ShortMaps#synchronize
	 */
	public default Object2ShortMap<T> synchronize() { return Object2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Object2ShortMaps#synchronize
	 */
	public default Object2ShortMap<T> synchronize(Object mutex) { return Object2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Object2ShortMaps#unmodifiable
	 */
	public default Object2ShortMap<T> unmodifiable() { return Object2ShortMaps.unmodifiable(this); }
	
	@Override
	public default Short put(T key, Short value) {
		return Short.valueOf(put(key, value.shortValue()));
	}
	
	@Override
	public default Short putIfAbsent(T key, Short value) {
		return Short.valueOf(put(key, value.shortValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<T> extends ObjectSet<Object2ShortMap.Entry<T>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Object2ShortMap.Entry<T>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface Entry<T> extends Map.Entry<T, Short>
	{
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public short getShortValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public short setValue(short value);
		@Override
		public default Short getValue() { return Short.valueOf(getShortValue()); }
		@Override
		public default Short setValue(Short value) { return Short.valueOf(setValue(value.shortValue())); }
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
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start() {
			return new BuilderCache<T>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <T> the type of elements maintained by this Collection
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
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, short value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, Short value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2ShortOpenHashMap<T> map() {
			return new Object2ShortOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2ShortOpenHashMap<T> map(int size) {
			return new Object2ShortOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2ShortOpenHashMap<T> map(T[] keys, short[] values) {
			return new Object2ShortOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortOpenHashMap<T> map(T[] keys, Short[] values) {
			return new Object2ShortOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortOpenHashMap<T> map(Object2ShortMap<T> map) {
			return new Object2ShortOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortOpenHashMap<T> map(Map<? extends T, ? extends Short> map) {
			return new Object2ShortOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <T> Object2ShortLinkedOpenHashMap<T> linkedMap() {
			return new Object2ShortLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ShortLinkedOpenHashMap<T> linkedMap(int size) {
			return new Object2ShortLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2ShortLinkedOpenHashMap<T> linkedMap(T[] keys, short[] values) {
			return new Object2ShortLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortLinkedOpenHashMap<T> linkedMap(T[] keys, Short[] values) {
			return new Object2ShortLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortLinkedOpenHashMap<T> linkedMap(Object2ShortMap<T> map) {
			return new Object2ShortLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2ShortOpenHashMap<T> linkedMap(Map<? extends T, ? extends Short> map) {
			return new ImmutableObject2ShortOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <T> ImmutableObject2ShortOpenHashMap<T> immutable(T[] keys, short[] values) {
			return new ImmutableObject2ShortOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> ImmutableObject2ShortOpenHashMap<T> immutable(T[] keys, Short[] values) {
			return new ImmutableObject2ShortOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <T> ImmutableObject2ShortOpenHashMap<T> immutable(Object2ShortMap<T> map) {
			return new ImmutableObject2ShortOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2ShortOpenHashMap<T> immutable(Map<? extends T, ? extends Short> map) {
			return new ImmutableObject2ShortOpenHashMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty EnumMap
		 */
		public <T extends Enum<T>> Enum2ShortMap<T> enumMap(Class<T> keyType) {
			 return new Enum2ShortMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> Enum2ShortMap<T> enumMap(T[] keys, Short[] values) {
			return new Enum2ShortMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> Enum2ShortMap<T> enumMap(T[] keys, short[] values) {
			return new Enum2ShortMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a EnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> Enum2ShortMap<T> enumMap(Map<? extends T, ? extends Short> map) {
			return new Enum2ShortMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a EnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> Enum2ShortMap<T> enumMap(Object2ShortMap<T> map) {
			return new Enum2ShortMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty LinkedEnumMap
		 */
		public <T extends Enum<T>> LinkedEnum2ShortMap<T> linkedEnumMap(Class<T> keyType) {
			 return new LinkedEnum2ShortMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2ShortMap<T> linkedEnumMap(T[] keys, Short[] values) {
			return new LinkedEnum2ShortMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> LinkedEnum2ShortMap<T> linkedEnumMap(T[] keys, short[] values) {
			return new LinkedEnum2ShortMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a LinkedEnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2ShortMap<T> linkedEnumMap(Map<? extends T, ? extends Short> map) {
			return new LinkedEnum2ShortMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a LinkedEnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> LinkedEnum2ShortMap<T> linkedEnumMap(Object2ShortMap<T> map) {
			return new LinkedEnum2ShortMap<>(map);
		}
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(int size, ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(T[] keys, short[] values, ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(T[] keys, Short[] values, ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(Object2ShortMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortOpenCustomHashMap<T> customMap(Map<? extends T, ? extends Short> map, ObjectStrategy<T> strategy) {
			return new Object2ShortOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(int size, ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, short[] values, ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, Short[] values, ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(Object2ShortMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(Map<? extends T, ? extends Short> map, ObjectStrategy<T> strategy) {
			return new Object2ShortLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2ShortArrayMap<T> arrayMap() {
			return new Object2ShortArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2ShortArrayMap<T> arrayMap(int size) {
			return new Object2ShortArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2ShortArrayMap<T> arrayMap(T[] keys, short[] values) {
			return new Object2ShortArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortArrayMap<T> arrayMap(T[] keys, Short[] values) {
			return new Object2ShortArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortArrayMap<T> arrayMap(Object2ShortMap<T> map) {
			return new Object2ShortArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortArrayMap<T> arrayMap(Map<? extends T, ? extends Short> map) {
			return new Object2ShortArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap() {
			return new Object2ShortRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return new Object2ShortRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap(T[] keys, short[] values, Comparator<T> comp) {
			return new Object2ShortRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap(T[] keys, Short[] values, Comparator<T> comp) {
			return new Object2ShortRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap(Object2ShortMap<T> map, Comparator<T> comp) {
			return new Object2ShortRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortRBTreeMap<T> rbTreeMap(Map<? extends T, ? extends Short> map, Comparator<T> comp) {
			return new Object2ShortRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap() {
			return new Object2ShortAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return new Object2ShortAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap(T[] keys, short[] values, Comparator<T> comp) {
			return new Object2ShortAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap(T[] keys, Short[] values, Comparator<T> comp) {
			return new Object2ShortAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap(Object2ShortMap<T> map, Comparator<T> comp) {
			return new Object2ShortAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2ShortAVLTreeMap<T> avlTreeMap(Map<? extends T, ? extends Short> map, Comparator<T> comp) {
			return new Object2ShortAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<T>
	{
		T[] keys;
		short[] values;
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
			keys = (T[])new Object[initialSize];
			values = new short[initialSize];
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
		public BuilderCache<T> put(T key, short value) {
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
		public BuilderCache<T> put(T key, Short value) {
			return put(key, value.shortValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<T> put(Object2ShortMap.Entry<T> entry) {
			return put(entry.getKey(), entry.getShortValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Object2ShortMap<T> map) {
			return putAll(Object2ShortMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Map<? extends T, ? extends Short> map) {
			for(Map.Entry<? extends T, ? extends Short> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(ObjectIterable<Object2ShortMap.Entry<T>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Object2ShortMap.Entry<T>>)c).size());
			
			for(Object2ShortMap.Entry<T> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Object2ShortMap<T>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Object2ShortOpenHashMap
		 */
		public Object2ShortOpenHashMap<T> map() {
			return putElements(new Object2ShortOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Object2ShortLinkedOpenHashMap
		 */
		public Object2ShortLinkedOpenHashMap<T> linkedMap() {
			return putElements(new Object2ShortLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableObject2ShortOpenHashMap
		 */
		public ImmutableObject2ShortOpenHashMap<T> immutable() {
			return new ImmutableObject2ShortOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2ShortOpenCustomHashMap
		 */
		public Object2ShortOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2ShortOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2ShortLinkedOpenCustomHashMap
		 */
		public Object2ShortLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2ShortLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Object2ShortConcurrentOpenHashMap
		 */
		public Object2ShortConcurrentOpenHashMap<T> concurrentMap() {
			return putElements(new Object2ShortConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Object2ShortArrayMap
		 */
		public Object2ShortArrayMap<T> arrayMap() {
			return new Object2ShortArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Object2ShortRBTreeMap
		 */
		public Object2ShortRBTreeMap<T> rbTreeMap() {
			return putElements(new Object2ShortRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2ShortRBTreeMap
		 */
		public Object2ShortRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return putElements(new Object2ShortRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Object2ShortAVLTreeMap
		 */
		public Object2ShortAVLTreeMap<T> avlTreeMap() {
			return putElements(new Object2ShortAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2ShortAVLTreeMap
		 */
		public Object2ShortAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return putElements(new Object2ShortAVLTreeMap<>(comp));
		}
	}
}