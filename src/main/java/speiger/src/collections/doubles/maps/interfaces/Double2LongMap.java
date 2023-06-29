package speiger.src.collections.doubles.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.doubles.functions.consumer.DoubleLongConsumer;
import speiger.src.collections.doubles.functions.function.Double2LongFunction;
import speiger.src.collections.doubles.functions.function.DoubleLongUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.maps.impl.customHash.Double2LongLinkedOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.customHash.Double2LongOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2LongLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2LongOpenHashMap;
import speiger.src.collections.doubles.maps.impl.immutable.ImmutableDouble2LongOpenHashMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2LongAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2LongRBTreeMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2LongArrayMap;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2LongConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.collections.doubles.utils.maps.Double2LongMaps;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Double2LongMap extends Map<Double, Long>, Double2LongFunction
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
	public long getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Double2LongMap setDefaultReturnValue(long v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Double2LongMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public long put(double key, long value);
	
	/**
	 * A Helper method that allows to put int a Double2LongMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default long put(Entry entry) {
		return put(entry.getDoubleKey(), entry.getLongValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Long put(Map.Entry<Double, Long> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(double[] keys, long[] values) {
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
	public void putAll(double[] keys, long[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Double[] keys, Long[] values) {
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
	public void putAll(Double[] keys, Long[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public long putIfAbsent(double key, long value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Double2LongMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public long addTo(double key, long value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Double2LongMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public long subFrom(double key, long value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Double2LongMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(double key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Double && containsKey(((Double)key).doubleValue());
	}
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(long value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Long && containsValue(((Long)value).longValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public long remove(double key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default Long remove(Object key) {
		return key instanceof Double ? Long.valueOf(remove(((Double)key).doubleValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(double key, long value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Double && value instanceof Long && remove(((Double)key).doubleValue(), ((Long)value).longValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public long removeOrDefault(double key, long defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(double key, long oldValue, long newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public long replace(double key, long value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceLongs(Double2LongMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceLongs(DoubleLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public long computeLong(double key, DoubleLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public long supplyLongIfAbsent(double key, LongSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public long computeLongNonDefault(double key, DoubleLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public long computeLongIfAbsentNonDefault(double key, Double2LongFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public long supplyLongIfAbsentNonDefault(double key, LongSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public long computeLongIfPresentNonDefault(double key, DoubleLongUnaryOperator mappingFunction);
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
	public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Long oldValue, Long newValue) {
		return replace(key.doubleValue(), oldValue.longValue(), newValue.longValue());
	}
	
	@Override
	@Deprecated
	public default Long replace(Double key, Long value) {
		return Long.valueOf(replace(key.doubleValue(), value.longValue()));
	}
	
	@Override
	public default long applyAsLong(double key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public long get(double key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public long getOrDefault(double key, long defaultValue);
	
	@Override
	@Deprecated
	public default Long get(Object key) {
		return Long.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Long getOrDefault(Object key, Long defaultValue) {
		Long value = Long.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceLongs(mappingFunction instanceof DoubleLongUnaryOperator ? (DoubleLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Long.valueOf(V)).longValue());
	}
	
	@Override
	@Deprecated
	public default Long compute(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLong(key.doubleValue(), mappingFunction instanceof DoubleLongUnaryOperator ? (DoubleLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfAbsent(Double key, Function<? super Double, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfAbsent(key.doubleValue(), mappingFunction instanceof Double2LongFunction ? (Double2LongFunction)mappingFunction : K -> mappingFunction.apply(Double.valueOf(K)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfPresent(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfPresent(key.doubleValue(), mappingFunction instanceof DoubleLongUnaryOperator ? (DoubleLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long merge(Double key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(mergeLong(key.doubleValue(), value.longValue(), mappingFunction instanceof LongLongUnaryOperator ? (LongLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(DoubleLongConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Long> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof DoubleLongConsumer ? (DoubleLongConsumer)action : (K, V) -> action.accept(Double.valueOf(K), Long.valueOf(V)));
	}
	
	@Override
	public DoubleSet keySet();
	@Override
	public LongCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Double, Long>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> double2LongEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Double2LongMaps#synchronize
	 */
	public default Double2LongMap synchronize() { return Double2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Double2LongMaps#synchronize
	 */
	public default Double2LongMap synchronize(Object mutex) { return Double2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Double2LongMaps#unmodifiable
	 */
	public default Double2LongMap unmodifiable() { return Double2LongMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Long put(Double key, Long value) {
		return Long.valueOf(put(key.doubleValue(), value.longValue()));
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Double key, Long value) {
		return Long.valueOf(put(key.doubleValue(), value.longValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Double2LongMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Double2LongMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Double2LongMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Double, Long>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public double getDoubleKey();
		public default Double getKey() { return Double.valueOf(getDoubleKey()); }
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public long getLongValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public long setValue(long value);
		@Override
		public default Long getValue() { return Long.valueOf(getLongValue()); }
		@Override
		public default Long setValue(Long value) { return Long.valueOf(setValue(value.longValue())); }
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
		public BuilderCache put(double key, long value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Double key, Long value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2LongOpenHashMap map() {
			return new Double2LongOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2LongOpenHashMap map(int size) {
			return new Double2LongOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2LongOpenHashMap map(double[] keys, long[] values) {
			return new Double2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2LongOpenHashMap map(Double[] keys, Long[] values) {
			return new Double2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2LongOpenHashMap map(Double2LongMap map) {
			return new Double2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongOpenHashMap map(Map<? extends Double, ? extends Long> map) {
			return new Double2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Double2LongLinkedOpenHashMap linkedMap() {
			return new Double2LongLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Double2LongLinkedOpenHashMap linkedMap(int size) {
			return new Double2LongLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Double2LongLinkedOpenHashMap linkedMap(double[] keys, long[] values) {
			return new Double2LongLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2LongLinkedOpenHashMap linkedMap(Double[] keys, Long[] values) {
			return new Double2LongLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2LongLinkedOpenHashMap linkedMap(Double2LongMap map) {
			return new Double2LongLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2LongOpenHashMap linkedMap(Map<? extends Double, ? extends Long> map) {
			return new ImmutableDouble2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableDouble2LongOpenHashMap immutable(double[] keys, long[] values) {
			return new ImmutableDouble2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableDouble2LongOpenHashMap immutable(Double[] keys, Long[] values) {
			return new ImmutableDouble2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableDouble2LongOpenHashMap immutable(Double2LongMap map) {
			return new ImmutableDouble2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2LongOpenHashMap immutable(Map<? extends Double, ? extends Long> map) {
			return new ImmutableDouble2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Double2LongOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Double2LongOpenCustomHashMap customMap(int size, DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Double2LongOpenCustomHashMap customMap(double[] keys, long[] values, DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(keys, values, strategy);
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
		public Double2LongOpenCustomHashMap customMap(Double[] keys, Long[] values, DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Double2LongOpenCustomHashMap customMap(Double2LongMap map, DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongOpenCustomHashMap customMap(Map<? extends Double, ? extends Long> map, DoubleStrategy strategy) {
			return new Double2LongOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(int size, DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(double[] keys, long[] values, DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(Double[] keys, Long[] values, DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(Double2LongMap map, DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(Map<? extends Double, ? extends Long> map, DoubleStrategy strategy) {
			return new Double2LongLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2LongArrayMap arrayMap() {
			return new Double2LongArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2LongArrayMap arrayMap(int size) {
			return new Double2LongArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2LongArrayMap arrayMap(double[] keys, long[] values) {
			return new Double2LongArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2LongArrayMap arrayMap(Double[] keys, Long[] values) {
			return new Double2LongArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2LongArrayMap arrayMap(Double2LongMap map) {
			return new Double2LongArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongArrayMap arrayMap(Map<? extends Double, ? extends Long> map) {
			return new Double2LongArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Double2LongRBTreeMap rbTreeMap() {
			return new Double2LongRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Double2LongRBTreeMap rbTreeMap(DoubleComparator comp) {
			return new Double2LongRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Double2LongRBTreeMap rbTreeMap(double[] keys, long[] values, DoubleComparator comp) {
			return new Double2LongRBTreeMap(keys, values, comp);
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
		public Double2LongRBTreeMap rbTreeMap(Double[] keys, Long[] values, DoubleComparator comp) {
			return new Double2LongRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Double2LongRBTreeMap rbTreeMap(Double2LongMap map, DoubleComparator comp) {
			return new Double2LongRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongRBTreeMap rbTreeMap(Map<? extends Double, ? extends Long> map, DoubleComparator comp) {
			return new Double2LongRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Double2LongAVLTreeMap avlTreeMap() {
			return new Double2LongAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Double2LongAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return new Double2LongAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Double2LongAVLTreeMap avlTreeMap(double[] keys, long[] values, DoubleComparator comp) {
			return new Double2LongAVLTreeMap(keys, values, comp);
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
		public Double2LongAVLTreeMap avlTreeMap(Double[] keys, Long[] values, DoubleComparator comp) {
			return new Double2LongAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Double2LongAVLTreeMap avlTreeMap(Double2LongMap map, DoubleComparator comp) {
			return new Double2LongAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2LongAVLTreeMap avlTreeMap(Map<? extends Double, ? extends Long> map, DoubleComparator comp) {
			return new Double2LongAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		double[] keys;
		long[] values;
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
			keys = new double[initialSize];
			values = new long[initialSize];
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
		public BuilderCache put(double key, long value) {
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
		public BuilderCache put(Double key, Long value) {
			return put(key.doubleValue(), value.longValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Double2LongMap.Entry entry) {
			return put(entry.getDoubleKey(), entry.getLongValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Double2LongMap map) {
			return putAll(Double2LongMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Double, ? extends Long> map) {
			for(Map.Entry<? extends Double, ? extends Long> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Double2LongMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Double2LongMap.Entry>)c).size());
			
			for(Double2LongMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Double2LongMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Double2LongOpenHashMap
		 */
		public Double2LongOpenHashMap map() {
			return putElements(new Double2LongOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Double2LongLinkedOpenHashMap
		 */
		public Double2LongLinkedOpenHashMap linkedMap() {
			return putElements(new Double2LongLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableDouble2LongOpenHashMap
		 */
		public ImmutableDouble2LongOpenHashMap immutable() {
			return new ImmutableDouble2LongOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2LongOpenCustomHashMap
		 */
		public Double2LongOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return putElements(new Double2LongOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2LongLinkedOpenCustomHashMap
		 */
		public Double2LongLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return putElements(new Double2LongLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Double2LongConcurrentOpenHashMap
		 */
		public Double2LongConcurrentOpenHashMap concurrentMap() {
			return putElements(new Double2LongConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Double2LongArrayMap
		 */
		public Double2LongArrayMap arrayMap() {
			return new Double2LongArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Double2LongRBTreeMap
		 */
		public Double2LongRBTreeMap rbTreeMap() {
			return putElements(new Double2LongRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2LongRBTreeMap
		 */
		public Double2LongRBTreeMap rbTreeMap(DoubleComparator comp) {
			return putElements(new Double2LongRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Double2LongAVLTreeMap
		 */
		public Double2LongAVLTreeMap avlTreeMap() {
			return putElements(new Double2LongAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2LongAVLTreeMap
		 */
		public Double2LongAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return putElements(new Double2LongAVLTreeMap(comp));
		}
	}
}