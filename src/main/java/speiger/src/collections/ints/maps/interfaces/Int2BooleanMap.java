package speiger.src.collections.ints.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntBooleanUnaryOperator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.maps.impl.customHash.Int2BooleanLinkedOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.customHash.Int2BooleanOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2BooleanLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2BooleanOpenHashMap;
import speiger.src.collections.ints.maps.impl.immutable.ImmutableInt2BooleanOpenHashMap;
import speiger.src.collections.ints.maps.impl.tree.Int2BooleanAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2BooleanRBTreeMap;
import speiger.src.collections.ints.maps.impl.misc.Int2BooleanArrayMap;
import speiger.src.collections.ints.maps.impl.concurrent.Int2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.collections.ints.utils.maps.Int2BooleanMaps;
import speiger.src.collections.ints.sets.IntSet;
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
public interface Int2BooleanMap extends Map<Integer, Boolean>, Int2BooleanFunction
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
	public Int2BooleanMap setDefaultReturnValue(boolean v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Int2BooleanMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public boolean put(int key, boolean value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(int[] keys, boolean[] values) {
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
	public void putAll(int[] keys, boolean[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Integer[] keys, Boolean[] values) {
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
	public void putAll(Integer[] keys, Boolean[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public boolean putIfAbsent(int key, boolean value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Int2BooleanMap m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Int2BooleanMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(int key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Integer but just have to support equals with Integer.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Integer && containsKey(((Integer)key).intValue());
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
	public boolean remove(int key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Integer but just have to support equals with Integer.
	 */
	@Override
	public default Boolean remove(Object key) {
		return key instanceof Integer ? Boolean.valueOf(remove(((Integer)key).intValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(int key, boolean value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Integer && value instanceof Boolean && remove(((Integer)key).intValue(), ((Boolean)value).booleanValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public boolean removeOrDefault(int key, boolean defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(int key, boolean oldValue, boolean newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public boolean replace(int key, boolean value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBooleans(Int2BooleanMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBooleans(IntBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBoolean(int key, IntBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsent(int key, Int2BooleanFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsent(int key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresent(int key, IntBooleanUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public boolean mergeBoolean(int key, boolean value, BooleanBooleanUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllBoolean(Int2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Boolean oldValue, Boolean newValue) {
		return replace(key.intValue(), oldValue.booleanValue(), newValue.booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Integer key, Boolean value) {
		return Boolean.valueOf(replace(key.intValue(), value.booleanValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public boolean get(int key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public boolean getOrDefault(int key, boolean defaultValue);
	
	@Override
	@Deprecated
	public default Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = Boolean.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBooleans(mappingFunction instanceof IntBooleanUnaryOperator ? (IntBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Boolean.valueOf(V)).booleanValue());
	}
	
	@Override
	@Deprecated
	public default Boolean compute(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBoolean(key.intValue(), mappingFunction instanceof IntBooleanUnaryOperator ? (IntBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Integer key, Function<? super Integer, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfAbsent(key.intValue(), mappingFunction instanceof Int2BooleanFunction ? (Int2BooleanFunction)mappingFunction : K -> mappingFunction.apply(Integer.valueOf(K)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean computeIfPresent(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfPresent(key.intValue(), mappingFunction instanceof IntBooleanUnaryOperator ? (IntBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean merge(Integer key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(mergeBoolean(key.intValue(), value.booleanValue(), mappingFunction instanceof BooleanBooleanUnaryOperator ? (BooleanBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(IntBooleanConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Boolean> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof IntBooleanConsumer ? (IntBooleanConsumer)action : (K, V) -> action.accept(Integer.valueOf(K), Boolean.valueOf(V)));
	}
	
	@Override
	public IntSet keySet();
	@Override
	public BooleanCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Integer, Boolean>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> int2BooleanEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Int2BooleanMaps#synchronize
	 */
	public default Int2BooleanMap synchronize() { return Int2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Int2BooleanMaps#synchronize
	 */
	public default Int2BooleanMap synchronize(Object mutex) { return Int2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Int2BooleanMaps#unmodifiable
	 */
	public default Int2BooleanMap unmodifiable() { return Int2BooleanMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Boolean put(Integer key, Boolean value) {
		return Boolean.valueOf(put(key.intValue(), value.booleanValue()));
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Integer key, Boolean value) {
		return Boolean.valueOf(put(key.intValue(), value.booleanValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Int2BooleanMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Int2BooleanMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Int2BooleanMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Integer, Boolean>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public int getIntKey();
		public default Integer getKey() { return Integer.valueOf(getIntKey()); }
		
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
		public BuilderCache put(int key, boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Integer key, Boolean value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2BooleanOpenHashMap map() {
			return new Int2BooleanOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2BooleanOpenHashMap map(int size) {
			return new Int2BooleanOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2BooleanOpenHashMap map(int[] keys, boolean[] values) {
			return new Int2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2BooleanOpenHashMap map(Integer[] keys, Boolean[] values) {
			return new Int2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2BooleanOpenHashMap map(Int2BooleanMap map) {
			return new Int2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanOpenHashMap map(Map<? extends Integer, ? extends Boolean> map) {
			return new Int2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Int2BooleanLinkedOpenHashMap linkedMap() {
			return new Int2BooleanLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Int2BooleanLinkedOpenHashMap linkedMap(int size) {
			return new Int2BooleanLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Int2BooleanLinkedOpenHashMap linkedMap(int[] keys, boolean[] values) {
			return new Int2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2BooleanLinkedOpenHashMap linkedMap(Integer[] keys, Boolean[] values) {
			return new Int2BooleanLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2BooleanLinkedOpenHashMap linkedMap(Int2BooleanMap map) {
			return new Int2BooleanLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2BooleanOpenHashMap linkedMap(Map<? extends Integer, ? extends Boolean> map) {
			return new ImmutableInt2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableInt2BooleanOpenHashMap immutable(int[] keys, boolean[] values) {
			return new ImmutableInt2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableInt2BooleanOpenHashMap immutable(Integer[] keys, Boolean[] values) {
			return new ImmutableInt2BooleanOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableInt2BooleanOpenHashMap immutable(Int2BooleanMap map) {
			return new ImmutableInt2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2BooleanOpenHashMap immutable(Map<? extends Integer, ? extends Boolean> map) {
			return new ImmutableInt2BooleanOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Int2BooleanOpenCustomHashMap customMap(IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Int2BooleanOpenCustomHashMap customMap(int size, IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Int2BooleanOpenCustomHashMap customMap(int[] keys, boolean[] values, IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(keys, values, strategy);
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
		public Int2BooleanOpenCustomHashMap customMap(Integer[] keys, Boolean[] values, IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Int2BooleanOpenCustomHashMap customMap(Int2BooleanMap map, IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanOpenCustomHashMap customMap(Map<? extends Integer, ? extends Boolean> map, IntStrategy strategy) {
			return new Int2BooleanOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(int size, IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(int[] keys, boolean[] values, IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(Integer[] keys, Boolean[] values, IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(Int2BooleanMap map, IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(Map<? extends Integer, ? extends Boolean> map, IntStrategy strategy) {
			return new Int2BooleanLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2BooleanArrayMap arrayMap() {
			return new Int2BooleanArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2BooleanArrayMap arrayMap(int size) {
			return new Int2BooleanArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2BooleanArrayMap arrayMap(int[] keys, boolean[] values) {
			return new Int2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2BooleanArrayMap arrayMap(Integer[] keys, Boolean[] values) {
			return new Int2BooleanArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2BooleanArrayMap arrayMap(Int2BooleanMap map) {
			return new Int2BooleanArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanArrayMap arrayMap(Map<? extends Integer, ? extends Boolean> map) {
			return new Int2BooleanArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Int2BooleanRBTreeMap rbTreeMap() {
			return new Int2BooleanRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Int2BooleanRBTreeMap rbTreeMap(IntComparator comp) {
			return new Int2BooleanRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Int2BooleanRBTreeMap rbTreeMap(int[] keys, boolean[] values, IntComparator comp) {
			return new Int2BooleanRBTreeMap(keys, values, comp);
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
		public Int2BooleanRBTreeMap rbTreeMap(Integer[] keys, Boolean[] values, IntComparator comp) {
			return new Int2BooleanRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Int2BooleanRBTreeMap rbTreeMap(Int2BooleanMap map, IntComparator comp) {
			return new Int2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanRBTreeMap rbTreeMap(Map<? extends Integer, ? extends Boolean> map, IntComparator comp) {
			return new Int2BooleanRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Int2BooleanAVLTreeMap avlTreeMap() {
			return new Int2BooleanAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Int2BooleanAVLTreeMap avlTreeMap(IntComparator comp) {
			return new Int2BooleanAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Int2BooleanAVLTreeMap avlTreeMap(int[] keys, boolean[] values, IntComparator comp) {
			return new Int2BooleanAVLTreeMap(keys, values, comp);
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
		public Int2BooleanAVLTreeMap avlTreeMap(Integer[] keys, Boolean[] values, IntComparator comp) {
			return new Int2BooleanAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Int2BooleanAVLTreeMap avlTreeMap(Int2BooleanMap map, IntComparator comp) {
			return new Int2BooleanAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2BooleanAVLTreeMap avlTreeMap(Map<? extends Integer, ? extends Boolean> map, IntComparator comp) {
			return new Int2BooleanAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		int[] keys;
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
			keys = new int[initialSize];
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
		public BuilderCache put(int key, boolean value) {
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
		public BuilderCache put(Integer key, Boolean value) {
			return put(key.intValue(), value.booleanValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Int2BooleanMap.Entry entry) {
			return put(entry.getIntKey(), entry.getBooleanValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Int2BooleanMap map) {
			return putAll(Int2BooleanMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Integer, ? extends Boolean> map) {
			for(Map.Entry<? extends Integer, ? extends Boolean> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Int2BooleanMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Int2BooleanMap.Entry>)c).size());
			
			for(Int2BooleanMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Int2BooleanMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Int2BooleanOpenHashMap
		 */
		public Int2BooleanOpenHashMap map() {
			return putElements(new Int2BooleanOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Int2BooleanLinkedOpenHashMap
		 */
		public Int2BooleanLinkedOpenHashMap linkedMap() {
			return putElements(new Int2BooleanLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableInt2BooleanOpenHashMap
		 */
		public ImmutableInt2BooleanOpenHashMap immutable() {
			return new ImmutableInt2BooleanOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2BooleanOpenCustomHashMap
		 */
		public Int2BooleanOpenCustomHashMap customMap(IntStrategy strategy) {
			return putElements(new Int2BooleanOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2BooleanLinkedOpenCustomHashMap
		 */
		public Int2BooleanLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return putElements(new Int2BooleanLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Int2BooleanConcurrentOpenHashMap
		 */
		public Int2BooleanConcurrentOpenHashMap concurrentMap() {
			return putElements(new Int2BooleanConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Int2BooleanArrayMap
		 */
		public Int2BooleanArrayMap arrayMap() {
			return new Int2BooleanArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Int2BooleanRBTreeMap
		 */
		public Int2BooleanRBTreeMap rbTreeMap() {
			return putElements(new Int2BooleanRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2BooleanRBTreeMap
		 */
		public Int2BooleanRBTreeMap rbTreeMap(IntComparator comp) {
			return putElements(new Int2BooleanRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Int2BooleanAVLTreeMap
		 */
		public Int2BooleanAVLTreeMap avlTreeMap() {
			return putElements(new Int2BooleanAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2BooleanAVLTreeMap
		 */
		public Int2BooleanAVLTreeMap avlTreeMap(IntComparator comp) {
			return putElements(new Int2BooleanAVLTreeMap(comp));
		}
	}
}