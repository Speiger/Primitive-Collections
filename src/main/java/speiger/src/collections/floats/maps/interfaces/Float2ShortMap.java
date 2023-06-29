package speiger.src.collections.floats.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.floats.functions.consumer.FloatShortConsumer;
import speiger.src.collections.floats.functions.function.Float2ShortFunction;
import speiger.src.collections.floats.functions.function.FloatShortUnaryOperator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.impl.customHash.Float2ShortLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2ShortOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ShortLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ShortOpenHashMap;
import speiger.src.collections.floats.maps.impl.immutable.ImmutableFloat2ShortOpenHashMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ShortAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ShortRBTreeMap;
import speiger.src.collections.floats.maps.impl.misc.Float2ShortArrayMap;
import speiger.src.collections.floats.maps.impl.concurrent.Float2ShortConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.floats.utils.maps.Float2ShortMaps;
import speiger.src.collections.floats.sets.FloatSet;
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
public interface Float2ShortMap extends Map<Float, Short>, Float2ShortFunction
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
	public Float2ShortMap setDefaultReturnValue(short v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Float2ShortMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public short put(float key, short value);
	
	/**
	 * A Helper method that allows to put int a Float2ShortMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default short put(Entry entry) {
		return put(entry.getFloatKey(), entry.getShortValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Short put(Map.Entry<Float, Short> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(float[] keys, short[] values) {
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
	public void putAll(float[] keys, short[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Float[] keys, Short[] values) {
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
	public void putAll(Float[] keys, Short[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public short putIfAbsent(float key, short value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Float2ShortMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public short addTo(float key, short value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Float2ShortMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public short subFrom(float key, short value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Float2ShortMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(float key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Float && containsKey(((Float)key).floatValue());
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
	public short remove(float key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default Short remove(Object key) {
		return key instanceof Float ? Short.valueOf(remove(((Float)key).floatValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(float key, short value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Float && value instanceof Short && remove(((Float)key).floatValue(), ((Short)value).shortValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public short removeOrDefault(float key, short defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(float key, short oldValue, short newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public short replace(float key, short value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceShorts(Float2ShortMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceShorts(FloatShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShort(float key, FloatShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsent(float key, Float2ShortFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsent(float key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresent(float key, FloatShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShortNonDefault(float key, FloatShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsentNonDefault(float key, Float2ShortFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsentNonDefault(float key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresentNonDefault(float key, FloatShortUnaryOperator mappingFunction);
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
	public short mergeShort(float key, short value, ShortShortUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllShort(Float2ShortMap m, ShortShortUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Short oldValue, Short newValue) {
		return replace(key.floatValue(), oldValue.shortValue(), newValue.shortValue());
	}
	
	@Override
	@Deprecated
	public default Short replace(Float key, Short value) {
		return Short.valueOf(replace(key.floatValue(), value.shortValue()));
	}
	
	@Override
	public default short applyAsShort(float key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public short get(float key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public short getOrDefault(float key, short defaultValue);
	
	@Override
	@Deprecated
	public default Short get(Object key) {
		return Short.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Short getOrDefault(Object key, Short defaultValue) {
		Short value = Short.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceShorts(mappingFunction instanceof FloatShortUnaryOperator ? (FloatShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Short.valueOf(V)).shortValue());
	}
	
	@Override
	@Deprecated
	public default Short compute(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShort(key.floatValue(), mappingFunction instanceof FloatShortUnaryOperator ? (FloatShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfAbsent(Float key, Function<? super Float, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfAbsent(key.floatValue(), mappingFunction instanceof Float2ShortFunction ? (Float2ShortFunction)mappingFunction : K -> mappingFunction.apply(Float.valueOf(K)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfPresent(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfPresent(key.floatValue(), mappingFunction instanceof FloatShortUnaryOperator ? (FloatShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short merge(Float key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(mergeShort(key.floatValue(), value.shortValue(), mappingFunction instanceof ShortShortUnaryOperator ? (ShortShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(FloatShortConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Short> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof FloatShortConsumer ? (FloatShortConsumer)action : (K, V) -> action.accept(Float.valueOf(K), Short.valueOf(V)));
	}
	
	@Override
	public FloatSet keySet();
	@Override
	public ShortCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Float, Short>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> float2ShortEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Float2ShortMaps#synchronize
	 */
	public default Float2ShortMap synchronize() { return Float2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Float2ShortMaps#synchronize
	 */
	public default Float2ShortMap synchronize(Object mutex) { return Float2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Float2ShortMaps#unmodifiable
	 */
	public default Float2ShortMap unmodifiable() { return Float2ShortMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Short put(Float key, Short value) {
		return Short.valueOf(put(key.floatValue(), value.shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Float key, Short value) {
		return Short.valueOf(put(key.floatValue(), value.shortValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Float2ShortMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Float2ShortMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Float2ShortMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Float, Short>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public float getFloatKey();
		public default Float getKey() { return Float.valueOf(getFloatKey()); }
		
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
		public BuilderCache put(float key, short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Float key, Short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2ShortOpenHashMap map() {
			return new Float2ShortOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2ShortOpenHashMap map(int size) {
			return new Float2ShortOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2ShortOpenHashMap map(float[] keys, short[] values) {
			return new Float2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2ShortOpenHashMap map(Float[] keys, Short[] values) {
			return new Float2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2ShortOpenHashMap map(Float2ShortMap map) {
			return new Float2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortOpenHashMap map(Map<? extends Float, ? extends Short> map) {
			return new Float2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Float2ShortLinkedOpenHashMap linkedMap() {
			return new Float2ShortLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Float2ShortLinkedOpenHashMap linkedMap(int size) {
			return new Float2ShortLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Float2ShortLinkedOpenHashMap linkedMap(float[] keys, short[] values) {
			return new Float2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2ShortLinkedOpenHashMap linkedMap(Float[] keys, Short[] values) {
			return new Float2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2ShortLinkedOpenHashMap linkedMap(Float2ShortMap map) {
			return new Float2ShortLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2ShortOpenHashMap linkedMap(Map<? extends Float, ? extends Short> map) {
			return new ImmutableFloat2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableFloat2ShortOpenHashMap immutable(float[] keys, short[] values) {
			return new ImmutableFloat2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableFloat2ShortOpenHashMap immutable(Float[] keys, Short[] values) {
			return new ImmutableFloat2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableFloat2ShortOpenHashMap immutable(Float2ShortMap map) {
			return new ImmutableFloat2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2ShortOpenHashMap immutable(Map<? extends Float, ? extends Short> map) {
			return new ImmutableFloat2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Float2ShortOpenCustomHashMap customMap(FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Float2ShortOpenCustomHashMap customMap(int size, FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Float2ShortOpenCustomHashMap customMap(float[] keys, short[] values, FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(keys, values, strategy);
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
		public Float2ShortOpenCustomHashMap customMap(Float[] keys, Short[] values, FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Float2ShortOpenCustomHashMap customMap(Float2ShortMap map, FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortOpenCustomHashMap customMap(Map<? extends Float, ? extends Short> map, FloatStrategy strategy) {
			return new Float2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(int size, FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(float[] keys, short[] values, FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(Float[] keys, Short[] values, FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(Float2ShortMap map, FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(Map<? extends Float, ? extends Short> map, FloatStrategy strategy) {
			return new Float2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2ShortArrayMap arrayMap() {
			return new Float2ShortArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2ShortArrayMap arrayMap(int size) {
			return new Float2ShortArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2ShortArrayMap arrayMap(float[] keys, short[] values) {
			return new Float2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2ShortArrayMap arrayMap(Float[] keys, Short[] values) {
			return new Float2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2ShortArrayMap arrayMap(Float2ShortMap map) {
			return new Float2ShortArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortArrayMap arrayMap(Map<? extends Float, ? extends Short> map) {
			return new Float2ShortArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Float2ShortRBTreeMap rbTreeMap() {
			return new Float2ShortRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Float2ShortRBTreeMap rbTreeMap(FloatComparator comp) {
			return new Float2ShortRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Float2ShortRBTreeMap rbTreeMap(float[] keys, short[] values, FloatComparator comp) {
			return new Float2ShortRBTreeMap(keys, values, comp);
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
		public Float2ShortRBTreeMap rbTreeMap(Float[] keys, Short[] values, FloatComparator comp) {
			return new Float2ShortRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Float2ShortRBTreeMap rbTreeMap(Float2ShortMap map, FloatComparator comp) {
			return new Float2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortRBTreeMap rbTreeMap(Map<? extends Float, ? extends Short> map, FloatComparator comp) {
			return new Float2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Float2ShortAVLTreeMap avlTreeMap() {
			return new Float2ShortAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Float2ShortAVLTreeMap avlTreeMap(FloatComparator comp) {
			return new Float2ShortAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Float2ShortAVLTreeMap avlTreeMap(float[] keys, short[] values, FloatComparator comp) {
			return new Float2ShortAVLTreeMap(keys, values, comp);
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
		public Float2ShortAVLTreeMap avlTreeMap(Float[] keys, Short[] values, FloatComparator comp) {
			return new Float2ShortAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Float2ShortAVLTreeMap avlTreeMap(Float2ShortMap map, FloatComparator comp) {
			return new Float2ShortAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2ShortAVLTreeMap avlTreeMap(Map<? extends Float, ? extends Short> map, FloatComparator comp) {
			return new Float2ShortAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		float[] keys;
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
			if(initialSize < 0) throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			keys = new float[initialSize];
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
		public BuilderCache put(float key, short value) {
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
		public BuilderCache put(Float key, Short value) {
			return put(key.floatValue(), value.shortValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Float2ShortMap.Entry entry) {
			return put(entry.getFloatKey(), entry.getShortValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Float2ShortMap map) {
			return putAll(Float2ShortMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Float, ? extends Short> map) {
			for(Map.Entry<? extends Float, ? extends Short> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Float2ShortMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Float2ShortMap.Entry>)c).size());
			
			for(Float2ShortMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Float2ShortMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Float2ShortOpenHashMap
		 */
		public Float2ShortOpenHashMap map() {
			return putElements(new Float2ShortOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Float2ShortLinkedOpenHashMap
		 */
		public Float2ShortLinkedOpenHashMap linkedMap() {
			return putElements(new Float2ShortLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableFloat2ShortOpenHashMap
		 */
		public ImmutableFloat2ShortOpenHashMap immutable() {
			return new ImmutableFloat2ShortOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2ShortOpenCustomHashMap
		 */
		public Float2ShortOpenCustomHashMap customMap(FloatStrategy strategy) {
			return putElements(new Float2ShortOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2ShortLinkedOpenCustomHashMap
		 */
		public Float2ShortLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return putElements(new Float2ShortLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Float2ShortConcurrentOpenHashMap
		 */
		public Float2ShortConcurrentOpenHashMap concurrentMap() {
			return putElements(new Float2ShortConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Float2ShortArrayMap
		 */
		public Float2ShortArrayMap arrayMap() {
			return new Float2ShortArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Float2ShortRBTreeMap
		 */
		public Float2ShortRBTreeMap rbTreeMap() {
			return putElements(new Float2ShortRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2ShortRBTreeMap
		 */
		public Float2ShortRBTreeMap rbTreeMap(FloatComparator comp) {
			return putElements(new Float2ShortRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Float2ShortAVLTreeMap
		 */
		public Float2ShortAVLTreeMap avlTreeMap() {
			return putElements(new Float2ShortAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2ShortAVLTreeMap
		 */
		public Float2ShortAVLTreeMap avlTreeMap(FloatComparator comp) {
			return putElements(new Float2ShortAVLTreeMap(comp));
		}
	}
}