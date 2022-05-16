package speiger.src.collections.longs.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.longs.functions.consumer.LongShortConsumer;
import speiger.src.collections.longs.functions.function.Long2ShortFunction;
import speiger.src.collections.longs.functions.function.LongShortUnaryOperator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.maps.impl.customHash.Long2ShortLinkedOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.customHash.Long2ShortOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ShortLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ShortOpenHashMap;
import speiger.src.collections.longs.maps.impl.immutable.ImmutableLong2ShortOpenHashMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ShortAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ShortRBTreeMap;
import speiger.src.collections.longs.maps.impl.misc.Long2ShortArrayMap;
import speiger.src.collections.longs.maps.impl.concurrent.Long2ShortConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.collections.longs.utils.maps.Long2ShortMaps;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Long2ShortMap extends Map<Long, Short>, Long2ShortFunction
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
	public Long2ShortMap setDefaultReturnValue(short v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Long2ShortMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public short put(long key, short value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(long[] keys, short[] values) {
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
	public void putAll(long[] keys, short[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Long[] keys, Short[] values) {
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
	public void putAll(Long[] keys, Short[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public short putIfAbsent(long key, short value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Long2ShortMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public short addTo(long key, short value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Long2ShortMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public short subFrom(long key, short value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Long2ShortMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(long key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Long but just have to support equals with Long.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Long && containsKey(((Long)key).longValue());
	}
	
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
	public short remove(long key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Long but just have to support equals with Long.
	 */
	@Override
	public default Short remove(Object key) {
		return key instanceof Long ? Short.valueOf(remove(((Long)key).longValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(long key, short value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Long && value instanceof Short && remove(((Long)key).longValue(), ((Short)value).shortValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public short removeOrDefault(long key, short defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(long key, short oldValue, short newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public short replace(long key, short value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceShorts(Long2ShortMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceShorts(LongShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShort(long key, LongShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsent(long key, Long2ShortFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsent(long key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresent(long key, LongShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public short mergeShort(long key, short value, ShortShortUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllShort(Long2ShortMap m, ShortShortUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Short oldValue, Short newValue) {
		return replace(key.longValue(), oldValue.shortValue(), newValue.shortValue());
	}
	
	@Override
	@Deprecated
	public default Short replace(Long key, Short value) {
		return Short.valueOf(replace(key.longValue(), value.shortValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public short get(long key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public short getOrDefault(long key, short defaultValue);
	
	@Override
	@Deprecated
	public default Short get(Object key) {
		return Short.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Short getOrDefault(Object key, Short defaultValue) {
		Short value = Short.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceShorts(mappingFunction instanceof LongShortUnaryOperator ? (LongShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Short.valueOf(V)).shortValue());
	}
	
	@Override
	@Deprecated
	public default Short compute(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShort(key.longValue(), mappingFunction instanceof LongShortUnaryOperator ? (LongShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfAbsent(Long key, Function<? super Long, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfAbsent(key.longValue(), mappingFunction instanceof Long2ShortFunction ? (Long2ShortFunction)mappingFunction : K -> mappingFunction.apply(Long.valueOf(K)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfPresent(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfPresent(key.longValue(), mappingFunction instanceof LongShortUnaryOperator ? (LongShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short merge(Long key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(mergeShort(key.longValue(), value.shortValue(), mappingFunction instanceof ShortShortUnaryOperator ? (ShortShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(LongShortConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Short> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof LongShortConsumer ? (LongShortConsumer)action : (K, V) -> action.accept(Long.valueOf(K), Short.valueOf(V)));
	}
	
	@Override
	public LongSet keySet();
	@Override
	public ShortCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Long, Short>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> long2ShortEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Long2ShortMaps#synchronize
	 */
	public default Long2ShortMap synchronize() { return Long2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Long2ShortMaps#synchronize
	 */
	public default Long2ShortMap synchronize(Object mutex) { return Long2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Long2ShortMaps#unmodifiable
	 */
	public default Long2ShortMap unmodifiable() { return Long2ShortMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Short put(Long key, Short value) {
		return Short.valueOf(put(key.longValue(), value.shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Long key, Short value) {
		return Short.valueOf(put(key.longValue(), value.shortValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Long2ShortMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Long2ShortMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Long2ShortMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Long, Short>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public long getLongKey();
		public default Long getKey() { return Long.valueOf(getLongKey()); }
		
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
		 * @return a MapBuilder
		 */
		public BuilderCache start() {
			return new BuilderCache();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @return a MapBuilder
		 */
		public BuilderCache start(int size) {
			return new BuilderCache(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(long key, short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Long key, Short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2ShortOpenHashMap map() {
			return new Long2ShortOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2ShortOpenHashMap map(int size) {
			return new Long2ShortOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2ShortOpenHashMap map(long[] keys, short[] values) {
			return new Long2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortOpenHashMap map(Long[] keys, Short[] values) {
			return new Long2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2ShortOpenHashMap map(Long2ShortMap map) {
			return new Long2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortOpenHashMap map(Map<? extends Long, ? extends Short> map) {
			return new Long2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Long2ShortLinkedOpenHashMap linkedMap() {
			return new Long2ShortLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Long2ShortLinkedOpenHashMap linkedMap(int size) {
			return new Long2ShortLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Long2ShortLinkedOpenHashMap linkedMap(long[] keys, short[] values) {
			return new Long2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortLinkedOpenHashMap linkedMap(Long[] keys, Short[] values) {
			return new Long2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ShortLinkedOpenHashMap linkedMap(Long2ShortMap map) {
			return new Long2ShortLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2ShortOpenHashMap linkedMap(Map<? extends Long, ? extends Short> map) {
			return new ImmutableLong2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableLong2ShortOpenHashMap immutable(long[] keys, short[] values) {
			return new ImmutableLong2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableLong2ShortOpenHashMap immutable(Long[] keys, Short[] values) {
			return new ImmutableLong2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableLong2ShortOpenHashMap immutable(Long2ShortMap map) {
			return new ImmutableLong2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2ShortOpenHashMap immutable(Map<? extends Long, ? extends Short> map) {
			return new ImmutableLong2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Long2ShortOpenCustomHashMap customMap(LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Long2ShortOpenCustomHashMap customMap(int size, LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Long2ShortOpenCustomHashMap customMap(long[] keys, short[] values, LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortOpenCustomHashMap customMap(Long[] keys, Short[] values, LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ShortOpenCustomHashMap customMap(Long2ShortMap map, LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortOpenCustomHashMap customMap(Map<? extends Long, ? extends Short> map, LongStrategy strategy) {
			return new Long2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(int size, LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(long[] keys, short[] values, LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(Long[] keys, Short[] values, LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(Long2ShortMap map, LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(Map<? extends Long, ? extends Short> map, LongStrategy strategy) {
			return new Long2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2ShortArrayMap arrayMap() {
			return new Long2ShortArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2ShortArrayMap arrayMap(int size) {
			return new Long2ShortArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2ShortArrayMap arrayMap(long[] keys, short[] values) {
			return new Long2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortArrayMap arrayMap(Long[] keys, Short[] values) {
			return new Long2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2ShortArrayMap arrayMap(Long2ShortMap map) {
			return new Long2ShortArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortArrayMap arrayMap(Map<? extends Long, ? extends Short> map) {
			return new Long2ShortArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Long2ShortRBTreeMap rbTreeMap() {
			return new Long2ShortRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Long2ShortRBTreeMap rbTreeMap(LongComparator comp) {
			return new Long2ShortRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Long2ShortRBTreeMap rbTreeMap(long[] keys, short[] values, LongComparator comp) {
			return new Long2ShortRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortRBTreeMap rbTreeMap(Long[] keys, Short[] values, LongComparator comp) {
			return new Long2ShortRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Long2ShortRBTreeMap rbTreeMap(Long2ShortMap map, LongComparator comp) {
			return new Long2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortRBTreeMap rbTreeMap(Map<? extends Long, ? extends Short> map, LongComparator comp) {
			return new Long2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Long2ShortAVLTreeMap avlTreeMap() {
			return new Long2ShortAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Long2ShortAVLTreeMap avlTreeMap(LongComparator comp) {
			return new Long2ShortAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Long2ShortAVLTreeMap avlTreeMap(long[] keys, short[] values, LongComparator comp) {
			return new Long2ShortAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ShortAVLTreeMap avlTreeMap(Long[] keys, Short[] values, LongComparator comp) {
			return new Long2ShortAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Long2ShortAVLTreeMap avlTreeMap(Long2ShortMap map, LongComparator comp) {
			return new Long2ShortAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ShortAVLTreeMap avlTreeMap(Map<? extends Long, ? extends Short> map, LongComparator comp) {
			return new Long2ShortAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		long[] keys;
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
			keys = new long[initialSize];
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
		public BuilderCache put(long key, short value) {
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
		public BuilderCache put(Long key, Short value) {
			return put(key.longValue(), value.shortValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Long2ShortMap.Entry entry) {
			return put(entry.getLongKey(), entry.getShortValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Long2ShortMap map) {
			return putAll(Long2ShortMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Long, ? extends Short> map) {
			for(Map.Entry<? extends Long, ? extends Short> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Long2ShortMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Long2ShortMap.Entry>)c).size());
			
			for(Long2ShortMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Long2ShortMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Long2ShortOpenHashMap
		 */
		public Long2ShortOpenHashMap map() {
			return putElements(new Long2ShortOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Long2ShortLinkedOpenHashMap
		 */
		public Long2ShortLinkedOpenHashMap linkedMap() {
			return putElements(new Long2ShortLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableLong2ShortOpenHashMap
		 */
		public ImmutableLong2ShortOpenHashMap immutable() {
			return new ImmutableLong2ShortOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2ShortOpenCustomHashMap
		 */
		public Long2ShortOpenCustomHashMap customMap(LongStrategy strategy) {
			return putElements(new Long2ShortOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2ShortLinkedOpenCustomHashMap
		 */
		public Long2ShortLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return putElements(new Long2ShortLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Long2ShortConcurrentOpenHashMap
		 */
		public Long2ShortConcurrentOpenHashMap concurrentMap() {
			return putElements(new Long2ShortConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Long2ShortArrayMap
		 */
		public Long2ShortArrayMap arrayMap() {
			return new Long2ShortArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Long2ShortRBTreeMap
		 */
		public Long2ShortRBTreeMap rbTreeMap() {
			return putElements(new Long2ShortRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2ShortRBTreeMap
		 */
		public Long2ShortRBTreeMap rbTreeMap(LongComparator comp) {
			return putElements(new Long2ShortRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Long2ShortAVLTreeMap
		 */
		public Long2ShortAVLTreeMap avlTreeMap() {
			return putElements(new Long2ShortAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2ShortAVLTreeMap
		 */
		public Long2ShortAVLTreeMap avlTreeMap(LongComparator comp) {
			return putElements(new Long2ShortAVLTreeMap(comp));
		}
	}
}