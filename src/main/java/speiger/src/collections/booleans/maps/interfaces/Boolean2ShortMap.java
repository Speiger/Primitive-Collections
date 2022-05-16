package speiger.src.collections.booleans.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.booleans.functions.consumer.BooleanShortConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2ShortFunction;
import speiger.src.collections.booleans.functions.function.BooleanShortUnaryOperator;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Boolean2ShortMap extends Map<Boolean, Short>, Boolean2ShortFunction
{
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
	public Boolean2ShortMap setDefaultReturnValue(short v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Boolean2ShortMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public short put(boolean key, short value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(boolean[] keys, short[] values) {
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
	public void putAll(boolean[] keys, short[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Boolean[] keys, Short[] values) {
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
	public void putAll(Boolean[] keys, Short[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public short putIfAbsent(boolean key, short value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Boolean2ShortMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public short addTo(boolean key, short value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Boolean2ShortMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public short subFrom(boolean key, short value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Boolean2ShortMap m);
	
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
	public short remove(boolean key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Boolean but just have to support equals with Boolean.
	 */
	@Override
	public default Short remove(Object key) {
		return key instanceof Boolean ? Short.valueOf(remove(((Boolean)key).booleanValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(boolean key, short value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Boolean && value instanceof Short && remove(((Boolean)key).booleanValue(), ((Short)value).shortValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public short removeOrDefault(boolean key, short defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(boolean key, short oldValue, short newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public short replace(boolean key, short value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceShorts(Boolean2ShortMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceShorts(BooleanShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShort(boolean key, BooleanShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsent(boolean key, Boolean2ShortFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsent(boolean key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresent(boolean key, BooleanShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public short mergeShort(boolean key, short value, ShortShortUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllShort(Boolean2ShortMap m, ShortShortUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Boolean key, Short oldValue, Short newValue) {
		return replace(key.booleanValue(), oldValue.shortValue(), newValue.shortValue());
	}
	
	@Override
	@Deprecated
	public default Short replace(Boolean key, Short value) {
		return Short.valueOf(replace(key.booleanValue(), value.shortValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public short get(boolean key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public short getOrDefault(boolean key, short defaultValue);
	
	@Override
	@Deprecated
	public default Short get(Object key) {
		return Short.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Short getOrDefault(Object key, Short defaultValue) {
		Short value = Short.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Boolean, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceShorts(mappingFunction instanceof BooleanShortUnaryOperator ? (BooleanShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Short.valueOf(V)).shortValue());
	}
	
	@Override
	@Deprecated
	public default Short compute(Boolean key, BiFunction<? super Boolean, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShort(key.booleanValue(), mappingFunction instanceof BooleanShortUnaryOperator ? (BooleanShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfAbsent(Boolean key, Function<? super Boolean, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfAbsent(key.booleanValue(), mappingFunction instanceof Boolean2ShortFunction ? (Boolean2ShortFunction)mappingFunction : K -> mappingFunction.apply(Boolean.valueOf(K)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfPresent(Boolean key, BiFunction<? super Boolean, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfPresent(key.booleanValue(), mappingFunction instanceof BooleanShortUnaryOperator ? (BooleanShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short merge(Boolean key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(mergeShort(key.booleanValue(), value.shortValue(), mappingFunction instanceof ShortShortUnaryOperator ? (ShortShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(BooleanShortConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Boolean, ? super Short> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof BooleanShortConsumer ? (BooleanShortConsumer)action : (K, V) -> action.accept(Boolean.valueOf(K), Short.valueOf(V)));
	}
	
	@Override
	public BooleanSet keySet();
	@Override
	public ShortCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Boolean, Short>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> boolean2ShortEntrySet();
	
	@Override
	@Deprecated
	public default Short put(Boolean key, Short value) {
		return Short.valueOf(put(key.booleanValue(), value.shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Boolean key, Short value) {
		return Short.valueOf(put(key.booleanValue(), value.shortValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Boolean2ShortMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Boolean2ShortMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Boolean2ShortMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Boolean, Short>
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
	
}