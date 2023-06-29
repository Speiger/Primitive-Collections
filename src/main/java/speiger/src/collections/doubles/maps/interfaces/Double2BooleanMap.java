package speiger.src.collections.doubles.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.DoublePredicate;


import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.doubles.functions.consumer.DoubleBooleanConsumer;
import speiger.src.collections.doubles.functions.function.DoubleBooleanUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.maps.impl.customHash.Double2BooleanLinkedOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.customHash.Double2BooleanOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2BooleanLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2BooleanOpenHashMap;
import speiger.src.collections.doubles.maps.impl.immutable.ImmutableDouble2BooleanOpenHashMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2BooleanAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2BooleanRBTreeMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2BooleanArrayMap;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.collections.doubles.utils.maps.Double2BooleanMaps;
import speiger.src.collections.doubles.sets.DoubleSet;
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
public interface Double2BooleanMap extends Map<Double, Boolean>, DoublePredicate
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
	public Double2BooleanMap setDefaultReturnValue(boolean v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Double2BooleanMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public boolean put(double key, boolean value);
	
	/**
	 * A Helper method that allows to put int a Double2BooleanMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default boolean put(Entry entry) {
		return put(entry.getDoubleKey(), entry.getBooleanValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Boolean put(Map.Entry<Double, Boolean> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(double[] keys, boolean[] values) {
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
	public void putAll(double[] keys, boolean[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Double[] keys, Boolean[] values) {
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
	public void putAll(Double[] keys, Boolean[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public boolean putIfAbsent(double key, boolean value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Double2BooleanMap m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Double2BooleanMap m);
	
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
	public boolean remove(double key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default Boolean remove(Object key) {
		return key instanceof Double ? Boolean.valueOf(remove(((Double)key).doubleValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(double key, boolean value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Double && value instanceof Boolean && remove(((Double)key).doubleValue(), ((Boolean)value).booleanValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public boolean removeOrDefault(double key, boolean defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(double key, boolean oldValue, boolean newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public boolean replace(double key, boolean value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBooleans(Double2BooleanMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBooleans(DoubleBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBoolean(double key, DoubleBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsent(double key, DoublePredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsent(double key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresent(double key, DoubleBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBooleanNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsentNonDefault(double key, DoublePredicate mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsentNonDefault(double key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresentNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction);
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
	public boolean mergeBoolean(double key, boolean value, BooleanBooleanUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllBoolean(Double2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Boolean oldValue, Boolean newValue) {
		return replace(key.doubleValue(), oldValue.booleanValue(), newValue.booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Double key, Boolean value) {
		return Boolean.valueOf(replace(key.doubleValue(), value.booleanValue()));
	}
	
	@Override
	public default boolean test(double key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public boolean get(double key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public boolean getOrDefault(double key, boolean defaultValue);
	
	@Override
	@Deprecated
	public default Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = Boolean.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBooleans(mappingFunction instanceof DoubleBooleanUnaryOperator ? (DoubleBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Boolean.valueOf(V)).booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean compute(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBoolean(key.doubleValue(), mappingFunction instanceof DoubleBooleanUnaryOperator ? (DoubleBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Double key, Function<? super Double, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfAbsent(key.doubleValue(), mappingFunction instanceof DoublePredicate ? (DoublePredicate)mappingFunction : K -> mappingFunction.apply(Double.valueOf(K)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfPresent(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfPresent(key.doubleValue(), mappingFunction instanceof DoubleBooleanUnaryOperator ? (DoubleBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean merge(Double key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(mergeBoolean(key.doubleValue(), value.booleanValue(), mappingFunction instanceof BooleanBooleanUnaryOperator ? (BooleanBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(DoubleBooleanConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Boolean> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof DoubleBooleanConsumer ? (DoubleBooleanConsumer)action : (K, V) -> action.accept(Double.valueOf(K), Boolean.valueOf(V)));
	}
	
	@Override
	public DoubleSet keySet();
	@Override
	public BooleanCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Double, Boolean>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> double2BooleanEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Double2BooleanMaps#synchronize
	 */
	public default Double2BooleanMap synchronize() { return Double2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Double2BooleanMaps#synchronize
	 */
	public default Double2BooleanMap synchronize(Object mutex) { return Double2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Double2BooleanMaps#unmodifiable
	 */
	public default Double2BooleanMap unmodifiable() { return Double2BooleanMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Boolean put(Double key, Boolean value) {
		return Boolean.valueOf(put(key.doubleValue(), value.booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Double key, Boolean value) {
		return Boolean.valueOf(put(key.doubleValue(), value.booleanValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Double2BooleanMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Double2BooleanMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Double2BooleanMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Double, Boolean>
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
		public BuilderCache put(double key, boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Double key, Boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2BooleanOpenHashMap map() {
			return new Double2BooleanOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2BooleanOpenHashMap map(int size) {
			return new Double2BooleanOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2BooleanOpenHashMap map(double[] keys, boolean[] values) {
			return new Double2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2BooleanOpenHashMap map(Double[] keys, Boolean[] values) {
			return new Double2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2BooleanOpenHashMap map(Double2BooleanMap map) {
			return new Double2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanOpenHashMap map(Map<? extends Double, ? extends Boolean> map) {
			return new Double2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Double2BooleanLinkedOpenHashMap linkedMap() {
			return new Double2BooleanLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Double2BooleanLinkedOpenHashMap linkedMap(int size) {
			return new Double2BooleanLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Double2BooleanLinkedOpenHashMap linkedMap(double[] keys, boolean[] values) {
			return new Double2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2BooleanLinkedOpenHashMap linkedMap(Double[] keys, Boolean[] values) {
			return new Double2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2BooleanLinkedOpenHashMap linkedMap(Double2BooleanMap map) {
			return new Double2BooleanLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2BooleanOpenHashMap linkedMap(Map<? extends Double, ? extends Boolean> map) {
			return new ImmutableDouble2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableDouble2BooleanOpenHashMap immutable(double[] keys, boolean[] values) {
			return new ImmutableDouble2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableDouble2BooleanOpenHashMap immutable(Double[] keys, Boolean[] values) {
			return new ImmutableDouble2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableDouble2BooleanOpenHashMap immutable(Double2BooleanMap map) {
			return new ImmutableDouble2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2BooleanOpenHashMap immutable(Map<? extends Double, ? extends Boolean> map) {
			return new ImmutableDouble2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Double2BooleanOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Double2BooleanOpenCustomHashMap customMap(int size, DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Double2BooleanOpenCustomHashMap customMap(double[] keys, boolean[] values, DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(keys, values, strategy);
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
		public Double2BooleanOpenCustomHashMap customMap(Double[] keys, Boolean[] values, DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Double2BooleanOpenCustomHashMap customMap(Double2BooleanMap map, DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanOpenCustomHashMap customMap(Map<? extends Double, ? extends Boolean> map, DoubleStrategy strategy) {
			return new Double2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(int size, DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(double[] keys, boolean[] values, DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(Double[] keys, Boolean[] values, DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(Double2BooleanMap map, DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(Map<? extends Double, ? extends Boolean> map, DoubleStrategy strategy) {
			return new Double2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2BooleanArrayMap arrayMap() {
			return new Double2BooleanArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2BooleanArrayMap arrayMap(int size) {
			return new Double2BooleanArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2BooleanArrayMap arrayMap(double[] keys, boolean[] values) {
			return new Double2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2BooleanArrayMap arrayMap(Double[] keys, Boolean[] values) {
			return new Double2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2BooleanArrayMap arrayMap(Double2BooleanMap map) {
			return new Double2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanArrayMap arrayMap(Map<? extends Double, ? extends Boolean> map) {
			return new Double2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Double2BooleanRBTreeMap rbTreeMap() {
			return new Double2BooleanRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Double2BooleanRBTreeMap rbTreeMap(DoubleComparator comp) {
			return new Double2BooleanRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Double2BooleanRBTreeMap rbTreeMap(double[] keys, boolean[] values, DoubleComparator comp) {
			return new Double2BooleanRBTreeMap(keys, values, comp);
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
		public Double2BooleanRBTreeMap rbTreeMap(Double[] keys, Boolean[] values, DoubleComparator comp) {
			return new Double2BooleanRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Double2BooleanRBTreeMap rbTreeMap(Double2BooleanMap map, DoubleComparator comp) {
			return new Double2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanRBTreeMap rbTreeMap(Map<? extends Double, ? extends Boolean> map, DoubleComparator comp) {
			return new Double2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Double2BooleanAVLTreeMap avlTreeMap() {
			return new Double2BooleanAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Double2BooleanAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return new Double2BooleanAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Double2BooleanAVLTreeMap avlTreeMap(double[] keys, boolean[] values, DoubleComparator comp) {
			return new Double2BooleanAVLTreeMap(keys, values, comp);
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
		public Double2BooleanAVLTreeMap avlTreeMap(Double[] keys, Boolean[] values, DoubleComparator comp) {
			return new Double2BooleanAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Double2BooleanAVLTreeMap avlTreeMap(Double2BooleanMap map, DoubleComparator comp) {
			return new Double2BooleanAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2BooleanAVLTreeMap avlTreeMap(Map<? extends Double, ? extends Boolean> map, DoubleComparator comp) {
			return new Double2BooleanAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		double[] keys;
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
			keys = new double[initialSize];
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
		public BuilderCache put(double key, boolean value) {
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
		public BuilderCache put(Double key, Boolean value) {
			return put(key.doubleValue(), value.booleanValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Double2BooleanMap.Entry entry) {
			return put(entry.getDoubleKey(), entry.getBooleanValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Double2BooleanMap map) {
			return putAll(Double2BooleanMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Double, ? extends Boolean> map) {
			for(Map.Entry<? extends Double, ? extends Boolean> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Double2BooleanMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Double2BooleanMap.Entry>)c).size());
			
			for(Double2BooleanMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Double2BooleanMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Double2BooleanOpenHashMap
		 */
		public Double2BooleanOpenHashMap map() {
			return putElements(new Double2BooleanOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Double2BooleanLinkedOpenHashMap
		 */
		public Double2BooleanLinkedOpenHashMap linkedMap() {
			return putElements(new Double2BooleanLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableDouble2BooleanOpenHashMap
		 */
		public ImmutableDouble2BooleanOpenHashMap immutable() {
			return new ImmutableDouble2BooleanOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2BooleanOpenCustomHashMap
		 */
		public Double2BooleanOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return putElements(new Double2BooleanOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2BooleanLinkedOpenCustomHashMap
		 */
		public Double2BooleanLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return putElements(new Double2BooleanLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Double2BooleanConcurrentOpenHashMap
		 */
		public Double2BooleanConcurrentOpenHashMap concurrentMap() {
			return putElements(new Double2BooleanConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Double2BooleanArrayMap
		 */
		public Double2BooleanArrayMap arrayMap() {
			return new Double2BooleanArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Double2BooleanRBTreeMap
		 */
		public Double2BooleanRBTreeMap rbTreeMap() {
			return putElements(new Double2BooleanRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2BooleanRBTreeMap
		 */
		public Double2BooleanRBTreeMap rbTreeMap(DoubleComparator comp) {
			return putElements(new Double2BooleanRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Double2BooleanAVLTreeMap
		 */
		public Double2BooleanAVLTreeMap avlTreeMap() {
			return putElements(new Double2BooleanAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2BooleanAVLTreeMap
		 */
		public Double2BooleanAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return putElements(new Double2BooleanAVLTreeMap(comp));
		}
	}
}