package speiger.src.collections.booleans.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.booleans.functions.consumer.BooleanLongConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2LongFunction;
import speiger.src.collections.booleans.functions.function.BooleanLongUnaryOperator;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Boolean2LongMap extends Map<Boolean, Long>, Boolean2LongFunction
{
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
	public Boolean2LongMap setDefaultReturnValue(long v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Boolean2LongMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public long put(boolean key, long value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(boolean[] keys, long[] values) {
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
	public void putAll(boolean[] keys, long[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Boolean[] keys, Long[] values) {
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
	public void putAll(Boolean[] keys, Long[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public long putIfAbsent(boolean key, long value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Boolean2LongMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public long addTo(boolean key, long value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Boolean2LongMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public long subFrom(boolean key, long value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Boolean2LongMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(boolean key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Boolean but just have to support equals with Boolean.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Boolean && containsKey(((Boolean)key).booleanValue());
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
	public long remove(boolean key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Boolean but just have to support equals with Boolean.
	 */
	@Override
	public default Long remove(Object key) {
		return key instanceof Boolean ? Long.valueOf(remove(((Boolean)key).booleanValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(boolean key, long value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Boolean && value instanceof Long && remove(((Boolean)key).booleanValue(), ((Long)value).longValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public long removeOrDefault(boolean key, long defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(boolean key, long oldValue, long newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public long replace(boolean key, long value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceLongs(Boolean2LongMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceLongs(BooleanLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public long computeLong(boolean key, BooleanLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public long computeLongIfAbsent(boolean key, Boolean2LongFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public long supplyLongIfAbsent(boolean key, LongSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public long computeLongIfPresent(boolean key, BooleanLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public long mergeLong(boolean key, long value, LongLongUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllLong(Boolean2LongMap m, LongLongUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Boolean key, Long oldValue, Long newValue) {
		return replace(key.booleanValue(), oldValue.longValue(), newValue.longValue());
	}
	
	@Override
	@Deprecated
	public default Long replace(Boolean key, Long value) {
		return Long.valueOf(replace(key.booleanValue(), value.longValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public long get(boolean key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public long getOrDefault(boolean key, long defaultValue);
	
	@Override
	@Deprecated
	public default Long get(Object key) {
		return Long.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Long getOrDefault(Object key, Long defaultValue) {
		Long value = Long.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Boolean, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceLongs(mappingFunction instanceof BooleanLongUnaryOperator ? (BooleanLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Long.valueOf(V)).longValue());
	}
	
	@Override
	@Deprecated
	public default Long compute(Boolean key, BiFunction<? super Boolean, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLong(key.booleanValue(), mappingFunction instanceof BooleanLongUnaryOperator ? (BooleanLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfAbsent(Boolean key, Function<? super Boolean, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfAbsent(key.booleanValue(), mappingFunction instanceof Boolean2LongFunction ? (Boolean2LongFunction)mappingFunction : K -> mappingFunction.apply(Boolean.valueOf(K)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfPresent(Boolean key, BiFunction<? super Boolean, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfPresent(key.booleanValue(), mappingFunction instanceof BooleanLongUnaryOperator ? (BooleanLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long merge(Boolean key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(mergeLong(key.booleanValue(), value.longValue(), mappingFunction instanceof LongLongUnaryOperator ? (LongLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(BooleanLongConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Boolean, ? super Long> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof BooleanLongConsumer ? (BooleanLongConsumer)action : (K, V) -> action.accept(Boolean.valueOf(K), Long.valueOf(V)));
	}
	
	@Override
	public BooleanSet keySet();
	@Override
	public LongCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Boolean, Long>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> boolean2LongEntrySet();
	
	@Override
	@Deprecated
	public default Long put(Boolean key, Long value) {
		return Long.valueOf(put(key.booleanValue(), value.longValue()));
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Boolean key, Long value) {
		return Long.valueOf(put(key.booleanValue(), value.longValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Boolean2LongMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Boolean2LongMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Boolean2LongMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Boolean, Long>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public boolean getBooleanKey();
		public default Boolean getKey() { return Boolean.valueOf(getBooleanKey()); }
		
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
	
}