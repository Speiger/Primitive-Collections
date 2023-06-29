package speiger.src.collections.floats.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.floats.functions.consumer.FloatBooleanConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatBooleanUnaryOperator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.impl.customHash.Float2BooleanLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2BooleanOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2BooleanLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2BooleanOpenHashMap;
import speiger.src.collections.floats.maps.impl.immutable.ImmutableFloat2BooleanOpenHashMap;
import speiger.src.collections.floats.maps.impl.tree.Float2BooleanAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2BooleanRBTreeMap;
import speiger.src.collections.floats.maps.impl.misc.Float2BooleanArrayMap;
import speiger.src.collections.floats.maps.impl.concurrent.Float2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.floats.utils.maps.Float2BooleanMaps;
import speiger.src.collections.floats.sets.FloatSet;
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
public interface Float2BooleanMap extends Map<Float, Boolean>, FloatPredicate
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
	public Float2BooleanMap setDefaultReturnValue(boolean v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Float2BooleanMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public boolean put(float key, boolean value);
	
	/**
	 * A Helper method that allows to put int a Float2BooleanMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default boolean put(Entry entry) {
		return put(entry.getFloatKey(), entry.getBooleanValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Boolean put(Map.Entry<Float, Boolean> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(float[] keys, boolean[] values) {
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
	public void putAll(float[] keys, boolean[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Float[] keys, Boolean[] values) {
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
	public void putAll(Float[] keys, Boolean[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public boolean putIfAbsent(float key, boolean value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Float2BooleanMap m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Float2BooleanMap m);
	
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
	public boolean remove(float key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default Boolean remove(Object key) {
		return key instanceof Float ? Boolean.valueOf(remove(((Float)key).floatValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(float key, boolean value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Float && value instanceof Boolean && remove(((Float)key).floatValue(), ((Boolean)value).booleanValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public boolean removeOrDefault(float key, boolean defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(float key, boolean oldValue, boolean newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public boolean replace(float key, boolean value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBooleans(Float2BooleanMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBooleans(FloatBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBoolean(float key, FloatBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsent(float key, FloatPredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsent(float key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresent(float key, FloatBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBooleanNonDefault(float key, FloatBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsentNonDefault(float key, FloatPredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsentNonDefault(float key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresentNonDefault(float key, FloatBooleanUnaryOperator mappingFunction);
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
	public boolean mergeBoolean(float key, boolean value, BooleanBooleanUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllBoolean(Float2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Boolean oldValue, Boolean newValue) {
		return replace(key.floatValue(), oldValue.booleanValue(), newValue.booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Float key, Boolean value) {
		return Boolean.valueOf(replace(key.floatValue(), value.booleanValue()));
	}
	
	@Override
	public default boolean test(float key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public boolean get(float key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public boolean getOrDefault(float key, boolean defaultValue);
	
	@Override
	@Deprecated
	public default Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = Boolean.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBooleans(mappingFunction instanceof FloatBooleanUnaryOperator ? (FloatBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Boolean.valueOf(V)).booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean compute(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBoolean(key.floatValue(), mappingFunction instanceof FloatBooleanUnaryOperator ? (FloatBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Float key, Function<? super Float, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfAbsent(key.floatValue(), mappingFunction instanceof FloatPredicate ? (FloatPredicate)mappingFunction : K -> mappingFunction.apply(Float.valueOf(K)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfPresent(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfPresent(key.floatValue(), mappingFunction instanceof FloatBooleanUnaryOperator ? (FloatBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean merge(Float key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(mergeBoolean(key.floatValue(), value.booleanValue(), mappingFunction instanceof BooleanBooleanUnaryOperator ? (BooleanBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(FloatBooleanConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Boolean> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof FloatBooleanConsumer ? (FloatBooleanConsumer)action : (K, V) -> action.accept(Float.valueOf(K), Boolean.valueOf(V)));
	}
	
	@Override
	public FloatSet keySet();
	@Override
	public BooleanCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Float, Boolean>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> float2BooleanEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Float2BooleanMaps#synchronize
	 */
	public default Float2BooleanMap synchronize() { return Float2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Float2BooleanMaps#synchronize
	 */
	public default Float2BooleanMap synchronize(Object mutex) { return Float2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Float2BooleanMaps#unmodifiable
	 */
	public default Float2BooleanMap unmodifiable() { return Float2BooleanMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Boolean put(Float key, Boolean value) {
		return Boolean.valueOf(put(key.floatValue(), value.booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Float key, Boolean value) {
		return Boolean.valueOf(put(key.floatValue(), value.booleanValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Float2BooleanMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Float2BooleanMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Float2BooleanMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Float, Boolean>
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
		public BuilderCache put(float key, boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Float key, Boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2BooleanOpenHashMap map() {
			return new Float2BooleanOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2BooleanOpenHashMap map(int size) {
			return new Float2BooleanOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2BooleanOpenHashMap map(float[] keys, boolean[] values) {
			return new Float2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2BooleanOpenHashMap map(Float[] keys, Boolean[] values) {
			return new Float2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2BooleanOpenHashMap map(Float2BooleanMap map) {
			return new Float2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanOpenHashMap map(Map<? extends Float, ? extends Boolean> map) {
			return new Float2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Float2BooleanLinkedOpenHashMap linkedMap() {
			return new Float2BooleanLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Float2BooleanLinkedOpenHashMap linkedMap(int size) {
			return new Float2BooleanLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Float2BooleanLinkedOpenHashMap linkedMap(float[] keys, boolean[] values) {
			return new Float2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2BooleanLinkedOpenHashMap linkedMap(Float[] keys, Boolean[] values) {
			return new Float2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2BooleanLinkedOpenHashMap linkedMap(Float2BooleanMap map) {
			return new Float2BooleanLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2BooleanOpenHashMap linkedMap(Map<? extends Float, ? extends Boolean> map) {
			return new ImmutableFloat2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableFloat2BooleanOpenHashMap immutable(float[] keys, boolean[] values) {
			return new ImmutableFloat2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableFloat2BooleanOpenHashMap immutable(Float[] keys, Boolean[] values) {
			return new ImmutableFloat2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableFloat2BooleanOpenHashMap immutable(Float2BooleanMap map) {
			return new ImmutableFloat2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2BooleanOpenHashMap immutable(Map<? extends Float, ? extends Boolean> map) {
			return new ImmutableFloat2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Float2BooleanOpenCustomHashMap customMap(FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Float2BooleanOpenCustomHashMap customMap(int size, FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Float2BooleanOpenCustomHashMap customMap(float[] keys, boolean[] values, FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(keys, values, strategy);
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
		public Float2BooleanOpenCustomHashMap customMap(Float[] keys, Boolean[] values, FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Float2BooleanOpenCustomHashMap customMap(Float2BooleanMap map, FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanOpenCustomHashMap customMap(Map<? extends Float, ? extends Boolean> map, FloatStrategy strategy) {
			return new Float2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(int size, FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(float[] keys, boolean[] values, FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(Float[] keys, Boolean[] values, FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(Float2BooleanMap map, FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(Map<? extends Float, ? extends Boolean> map, FloatStrategy strategy) {
			return new Float2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2BooleanArrayMap arrayMap() {
			return new Float2BooleanArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2BooleanArrayMap arrayMap(int size) {
			return new Float2BooleanArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2BooleanArrayMap arrayMap(float[] keys, boolean[] values) {
			return new Float2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2BooleanArrayMap arrayMap(Float[] keys, Boolean[] values) {
			return new Float2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2BooleanArrayMap arrayMap(Float2BooleanMap map) {
			return new Float2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanArrayMap arrayMap(Map<? extends Float, ? extends Boolean> map) {
			return new Float2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Float2BooleanRBTreeMap rbTreeMap() {
			return new Float2BooleanRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Float2BooleanRBTreeMap rbTreeMap(FloatComparator comp) {
			return new Float2BooleanRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Float2BooleanRBTreeMap rbTreeMap(float[] keys, boolean[] values, FloatComparator comp) {
			return new Float2BooleanRBTreeMap(keys, values, comp);
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
		public Float2BooleanRBTreeMap rbTreeMap(Float[] keys, Boolean[] values, FloatComparator comp) {
			return new Float2BooleanRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Float2BooleanRBTreeMap rbTreeMap(Float2BooleanMap map, FloatComparator comp) {
			return new Float2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanRBTreeMap rbTreeMap(Map<? extends Float, ? extends Boolean> map, FloatComparator comp) {
			return new Float2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Float2BooleanAVLTreeMap avlTreeMap() {
			return new Float2BooleanAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Float2BooleanAVLTreeMap avlTreeMap(FloatComparator comp) {
			return new Float2BooleanAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Float2BooleanAVLTreeMap avlTreeMap(float[] keys, boolean[] values, FloatComparator comp) {
			return new Float2BooleanAVLTreeMap(keys, values, comp);
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
		public Float2BooleanAVLTreeMap avlTreeMap(Float[] keys, Boolean[] values, FloatComparator comp) {
			return new Float2BooleanAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Float2BooleanAVLTreeMap avlTreeMap(Float2BooleanMap map, FloatComparator comp) {
			return new Float2BooleanAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2BooleanAVLTreeMap avlTreeMap(Map<? extends Float, ? extends Boolean> map, FloatComparator comp) {
			return new Float2BooleanAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		float[] keys;
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
			keys = new float[initialSize];
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
		public BuilderCache put(float key, boolean value) {
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
		public BuilderCache put(Float key, Boolean value) {
			return put(key.floatValue(), value.booleanValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Float2BooleanMap.Entry entry) {
			return put(entry.getFloatKey(), entry.getBooleanValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Float2BooleanMap map) {
			return putAll(Float2BooleanMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Float, ? extends Boolean> map) {
			for(Map.Entry<? extends Float, ? extends Boolean> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Float2BooleanMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Float2BooleanMap.Entry>)c).size());
			
			for(Float2BooleanMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Float2BooleanMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Float2BooleanOpenHashMap
		 */
		public Float2BooleanOpenHashMap map() {
			return putElements(new Float2BooleanOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Float2BooleanLinkedOpenHashMap
		 */
		public Float2BooleanLinkedOpenHashMap linkedMap() {
			return putElements(new Float2BooleanLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableFloat2BooleanOpenHashMap
		 */
		public ImmutableFloat2BooleanOpenHashMap immutable() {
			return new ImmutableFloat2BooleanOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2BooleanOpenCustomHashMap
		 */
		public Float2BooleanOpenCustomHashMap customMap(FloatStrategy strategy) {
			return putElements(new Float2BooleanOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2BooleanLinkedOpenCustomHashMap
		 */
		public Float2BooleanLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return putElements(new Float2BooleanLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Float2BooleanConcurrentOpenHashMap
		 */
		public Float2BooleanConcurrentOpenHashMap concurrentMap() {
			return putElements(new Float2BooleanConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Float2BooleanArrayMap
		 */
		public Float2BooleanArrayMap arrayMap() {
			return new Float2BooleanArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Float2BooleanRBTreeMap
		 */
		public Float2BooleanRBTreeMap rbTreeMap() {
			return putElements(new Float2BooleanRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2BooleanRBTreeMap
		 */
		public Float2BooleanRBTreeMap rbTreeMap(FloatComparator comp) {
			return putElements(new Float2BooleanRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Float2BooleanAVLTreeMap
		 */
		public Float2BooleanAVLTreeMap avlTreeMap() {
			return putElements(new Float2BooleanAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2BooleanAVLTreeMap
		 */
		public Float2BooleanAVLTreeMap avlTreeMap(FloatComparator comp) {
			return putElements(new Float2BooleanAVLTreeMap(comp));
		}
	}
}