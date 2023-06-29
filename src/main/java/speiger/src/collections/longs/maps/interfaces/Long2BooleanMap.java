package speiger.src.collections.longs.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongPredicate;


import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.longs.functions.consumer.LongBooleanConsumer;
import speiger.src.collections.longs.functions.function.LongBooleanUnaryOperator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.maps.impl.customHash.Long2BooleanLinkedOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.customHash.Long2BooleanOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2BooleanLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2BooleanOpenHashMap;
import speiger.src.collections.longs.maps.impl.immutable.ImmutableLong2BooleanOpenHashMap;
import speiger.src.collections.longs.maps.impl.tree.Long2BooleanAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2BooleanRBTreeMap;
import speiger.src.collections.longs.maps.impl.misc.Long2BooleanArrayMap;
import speiger.src.collections.longs.maps.impl.concurrent.Long2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.collections.longs.utils.maps.Long2BooleanMaps;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Long2BooleanMap extends Map<Long, Boolean>, LongPredicate
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
	public boolean getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Long2BooleanMap setDefaultReturnValue(boolean v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Long2BooleanMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public boolean put(long key, boolean value);
	
	/**
	 * A Helper method that allows to put int a Long2BooleanMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default boolean put(Entry entry) {
		return put(entry.getLongKey(), entry.getBooleanValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Boolean put(Map.Entry<Long, Boolean> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(long[] keys, boolean[] values) {
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
	public void putAll(long[] keys, boolean[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Long[] keys, Boolean[] values) {
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
	public void putAll(Long[] keys, Boolean[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public boolean putIfAbsent(long key, boolean value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Long2BooleanMap m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Long2BooleanMap m);
	
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
	public boolean containsValue(boolean value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Boolean && containsValue(((Boolean)value).booleanValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public boolean remove(long key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Long but just have to support equals with Long.
	 */
	@Override
	public default Boolean remove(Object key) {
		return key instanceof Long ? Boolean.valueOf(remove(((Long)key).longValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(long key, boolean value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Long && value instanceof Boolean && remove(((Long)key).longValue(), ((Boolean)value).booleanValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public boolean removeOrDefault(long key, boolean defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(long key, boolean oldValue, boolean newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public boolean replace(long key, boolean value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBooleans(Long2BooleanMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBooleans(LongBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBoolean(long key, LongBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsent(long key, LongPredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsent(long key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresent(long key, LongBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBooleanNonDefault(long key, LongBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsentNonDefault(long key, LongPredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsentNonDefault(long key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresentNonDefault(long key, LongBooleanUnaryOperator mappingFunction);
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
	public boolean mergeBoolean(long key, boolean value, BooleanBooleanUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllBoolean(Long2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Boolean oldValue, Boolean newValue) {
		return replace(key.longValue(), oldValue.booleanValue(), newValue.booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Long key, Boolean value) {
		return Boolean.valueOf(replace(key.longValue(), value.booleanValue()));
	}
	
	@Override
	public default boolean test(long key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public boolean get(long key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public boolean getOrDefault(long key, boolean defaultValue);
	
	@Override
	@Deprecated
	public default Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = Boolean.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBooleans(mappingFunction instanceof LongBooleanUnaryOperator ? (LongBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Boolean.valueOf(V)).booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean compute(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBoolean(key.longValue(), mappingFunction instanceof LongBooleanUnaryOperator ? (LongBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Long key, Function<? super Long, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfAbsent(key.longValue(), mappingFunction instanceof LongPredicate ? (LongPredicate)mappingFunction : K -> mappingFunction.apply(Long.valueOf(K)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfPresent(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfPresent(key.longValue(), mappingFunction instanceof LongBooleanUnaryOperator ? (LongBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean merge(Long key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(mergeBoolean(key.longValue(), value.booleanValue(), mappingFunction instanceof BooleanBooleanUnaryOperator ? (BooleanBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(LongBooleanConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Boolean> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof LongBooleanConsumer ? (LongBooleanConsumer)action : (K, V) -> action.accept(Long.valueOf(K), Boolean.valueOf(V)));
	}
	
	@Override
	public LongSet keySet();
	@Override
	public BooleanCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Long, Boolean>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> long2BooleanEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Long2BooleanMaps#synchronize
	 */
	public default Long2BooleanMap synchronize() { return Long2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Long2BooleanMaps#synchronize
	 */
	public default Long2BooleanMap synchronize(Object mutex) { return Long2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Long2BooleanMaps#unmodifiable
	 */
	public default Long2BooleanMap unmodifiable() { return Long2BooleanMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Boolean put(Long key, Boolean value) {
		return Boolean.valueOf(put(key.longValue(), value.booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Long key, Boolean value) {
		return Boolean.valueOf(put(key.longValue(), value.booleanValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Long2BooleanMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Long2BooleanMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Long2BooleanMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Long, Boolean>
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
		public boolean getBooleanValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public boolean setValue(boolean value);
		@Override
		public default Boolean getValue() { return Boolean.valueOf(getBooleanValue()); }
		@Override
		public default Boolean setValue(Boolean value) { return Boolean.valueOf(setValue(value.booleanValue())); }
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
		public BuilderCache put(long key, boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Long key, Boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2BooleanOpenHashMap map() {
			return new Long2BooleanOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2BooleanOpenHashMap map(int size) {
			return new Long2BooleanOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2BooleanOpenHashMap map(long[] keys, boolean[] values) {
			return new Long2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2BooleanOpenHashMap map(Long[] keys, Boolean[] values) {
			return new Long2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2BooleanOpenHashMap map(Long2BooleanMap map) {
			return new Long2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanOpenHashMap map(Map<? extends Long, ? extends Boolean> map) {
			return new Long2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Long2BooleanLinkedOpenHashMap linkedMap() {
			return new Long2BooleanLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Long2BooleanLinkedOpenHashMap linkedMap(int size) {
			return new Long2BooleanLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Long2BooleanLinkedOpenHashMap linkedMap(long[] keys, boolean[] values) {
			return new Long2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2BooleanLinkedOpenHashMap linkedMap(Long[] keys, Boolean[] values) {
			return new Long2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2BooleanLinkedOpenHashMap linkedMap(Long2BooleanMap map) {
			return new Long2BooleanLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2BooleanOpenHashMap linkedMap(Map<? extends Long, ? extends Boolean> map) {
			return new ImmutableLong2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableLong2BooleanOpenHashMap immutable(long[] keys, boolean[] values) {
			return new ImmutableLong2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableLong2BooleanOpenHashMap immutable(Long[] keys, Boolean[] values) {
			return new ImmutableLong2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableLong2BooleanOpenHashMap immutable(Long2BooleanMap map) {
			return new ImmutableLong2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2BooleanOpenHashMap immutable(Map<? extends Long, ? extends Boolean> map) {
			return new ImmutableLong2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Long2BooleanOpenCustomHashMap customMap(LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Long2BooleanOpenCustomHashMap customMap(int size, LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Long2BooleanOpenCustomHashMap customMap(long[] keys, boolean[] values, LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(keys, values, strategy);
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
		public Long2BooleanOpenCustomHashMap customMap(Long[] keys, Boolean[] values, LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Long2BooleanOpenCustomHashMap customMap(Long2BooleanMap map, LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanOpenCustomHashMap customMap(Map<? extends Long, ? extends Boolean> map, LongStrategy strategy) {
			return new Long2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(int size, LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(long[] keys, boolean[] values, LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(Long[] keys, Boolean[] values, LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(Long2BooleanMap map, LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(Map<? extends Long, ? extends Boolean> map, LongStrategy strategy) {
			return new Long2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2BooleanArrayMap arrayMap() {
			return new Long2BooleanArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2BooleanArrayMap arrayMap(int size) {
			return new Long2BooleanArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2BooleanArrayMap arrayMap(long[] keys, boolean[] values) {
			return new Long2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2BooleanArrayMap arrayMap(Long[] keys, Boolean[] values) {
			return new Long2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2BooleanArrayMap arrayMap(Long2BooleanMap map) {
			return new Long2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanArrayMap arrayMap(Map<? extends Long, ? extends Boolean> map) {
			return new Long2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Long2BooleanRBTreeMap rbTreeMap() {
			return new Long2BooleanRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Long2BooleanRBTreeMap rbTreeMap(LongComparator comp) {
			return new Long2BooleanRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Long2BooleanRBTreeMap rbTreeMap(long[] keys, boolean[] values, LongComparator comp) {
			return new Long2BooleanRBTreeMap(keys, values, comp);
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
		public Long2BooleanRBTreeMap rbTreeMap(Long[] keys, Boolean[] values, LongComparator comp) {
			return new Long2BooleanRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Long2BooleanRBTreeMap rbTreeMap(Long2BooleanMap map, LongComparator comp) {
			return new Long2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanRBTreeMap rbTreeMap(Map<? extends Long, ? extends Boolean> map, LongComparator comp) {
			return new Long2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Long2BooleanAVLTreeMap avlTreeMap() {
			return new Long2BooleanAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Long2BooleanAVLTreeMap avlTreeMap(LongComparator comp) {
			return new Long2BooleanAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Long2BooleanAVLTreeMap avlTreeMap(long[] keys, boolean[] values, LongComparator comp) {
			return new Long2BooleanAVLTreeMap(keys, values, comp);
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
		public Long2BooleanAVLTreeMap avlTreeMap(Long[] keys, Boolean[] values, LongComparator comp) {
			return new Long2BooleanAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Long2BooleanAVLTreeMap avlTreeMap(Long2BooleanMap map, LongComparator comp) {
			return new Long2BooleanAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2BooleanAVLTreeMap avlTreeMap(Map<? extends Long, ? extends Boolean> map, LongComparator comp) {
			return new Long2BooleanAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		long[] keys;
		boolean[] values;
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
			keys = new long[initialSize];
			values = new boolean[initialSize];
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
		public BuilderCache put(long key, boolean value) {
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
		public BuilderCache put(Long key, Boolean value) {
			return put(key.longValue(), value.booleanValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Long2BooleanMap.Entry entry) {
			return put(entry.getLongKey(), entry.getBooleanValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Long2BooleanMap map) {
			return putAll(Long2BooleanMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Long, ? extends Boolean> map) {
			for(Map.Entry<? extends Long, ? extends Boolean> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Long2BooleanMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Long2BooleanMap.Entry>)c).size());
			
			for(Long2BooleanMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Long2BooleanMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Long2BooleanOpenHashMap
		 */
		public Long2BooleanOpenHashMap map() {
			return putElements(new Long2BooleanOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Long2BooleanLinkedOpenHashMap
		 */
		public Long2BooleanLinkedOpenHashMap linkedMap() {
			return putElements(new Long2BooleanLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableLong2BooleanOpenHashMap
		 */
		public ImmutableLong2BooleanOpenHashMap immutable() {
			return new ImmutableLong2BooleanOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2BooleanOpenCustomHashMap
		 */
		public Long2BooleanOpenCustomHashMap customMap(LongStrategy strategy) {
			return putElements(new Long2BooleanOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2BooleanLinkedOpenCustomHashMap
		 */
		public Long2BooleanLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return putElements(new Long2BooleanLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Long2BooleanConcurrentOpenHashMap
		 */
		public Long2BooleanConcurrentOpenHashMap concurrentMap() {
			return putElements(new Long2BooleanConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Long2BooleanArrayMap
		 */
		public Long2BooleanArrayMap arrayMap() {
			return new Long2BooleanArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Long2BooleanRBTreeMap
		 */
		public Long2BooleanRBTreeMap rbTreeMap() {
			return putElements(new Long2BooleanRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2BooleanRBTreeMap
		 */
		public Long2BooleanRBTreeMap rbTreeMap(LongComparator comp) {
			return putElements(new Long2BooleanRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Long2BooleanAVLTreeMap
		 */
		public Long2BooleanAVLTreeMap avlTreeMap() {
			return putElements(new Long2BooleanAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2BooleanAVLTreeMap
		 */
		public Long2BooleanAVLTreeMap avlTreeMap(LongComparator comp) {
			return putElements(new Long2BooleanAVLTreeMap(comp));
		}
	}
}