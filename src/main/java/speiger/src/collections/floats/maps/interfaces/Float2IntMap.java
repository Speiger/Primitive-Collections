package speiger.src.collections.floats.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.floats.functions.consumer.FloatIntConsumer;
import speiger.src.collections.floats.functions.function.Float2IntFunction;
import speiger.src.collections.floats.functions.function.FloatIntUnaryOperator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.impl.customHash.Float2IntLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2IntOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2IntLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2IntOpenHashMap;
import speiger.src.collections.floats.maps.impl.immutable.ImmutableFloat2IntOpenHashMap;
import speiger.src.collections.floats.maps.impl.tree.Float2IntAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2IntRBTreeMap;
import speiger.src.collections.floats.maps.impl.misc.Float2IntArrayMap;
import speiger.src.collections.floats.maps.impl.concurrent.Float2IntConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.floats.utils.maps.Float2IntMaps;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Float2IntMap extends Map<Float, Integer>, Float2IntFunction
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
	public int getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Float2IntMap setDefaultReturnValue(int v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Float2IntMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public int put(float key, int value);
	
	/**
	 * A Helper method that allows to put int a Float2IntMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default int put(Entry entry) {
		return put(entry.getFloatKey(), entry.getIntValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Integer put(Map.Entry<Float, Integer> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(float[] keys, int[] values) {
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
	public void putAll(float[] keys, int[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Float[] keys, Integer[] values) {
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
	public void putAll(Float[] keys, Integer[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public int putIfAbsent(float key, int value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Float2IntMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public int addTo(float key, int value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Float2IntMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public int subFrom(float key, int value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Float2IntMap m);
	
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
	public boolean containsValue(int value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Integer && containsValue(((Integer)value).intValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public int remove(float key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default Integer remove(Object key) {
		return key instanceof Float ? Integer.valueOf(remove(((Float)key).floatValue())) : Integer.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(float key, int value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Float && value instanceof Integer && remove(((Float)key).floatValue(), ((Integer)value).intValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public int removeOrDefault(float key, int defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(float key, int oldValue, int newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public int replace(float key, int value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceInts(Float2IntMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceInts(FloatIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public int computeInt(float key, FloatIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public int computeIntIfAbsent(float key, Float2IntFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public int supplyIntIfAbsent(float key, IntSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public int computeIntIfPresent(float key, FloatIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public int computeIntNonDefault(float key, FloatIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public int computeIntIfAbsentNonDefault(float key, Float2IntFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public int supplyIntIfAbsentNonDefault(float key, IntSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public int computeIntIfPresentNonDefault(float key, FloatIntUnaryOperator mappingFunction);
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
	public int mergeInt(float key, int value, IntIntUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllInt(Float2IntMap m, IntIntUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Integer oldValue, Integer newValue) {
		return replace(key.floatValue(), oldValue.intValue(), newValue.intValue());
	}
	
	@Override
	@Deprecated
	public default Integer replace(Float key, Integer value) {
		return Integer.valueOf(replace(key.floatValue(), value.intValue()));
	}
	
	@Override
	public default int applyAsInt(float key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public int get(float key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public int getOrDefault(float key, int defaultValue);
	
	@Override
	@Deprecated
	public default Integer get(Object key) {
		return Integer.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		Integer value = Integer.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceInts(mappingFunction instanceof FloatIntUnaryOperator ? (FloatIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Integer.valueOf(V)).intValue());
	}
	
	@Override
	@Deprecated
	public default Integer compute(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeInt(key.floatValue(), mappingFunction instanceof FloatIntUnaryOperator ? (FloatIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer computeIfAbsent(Float key, Function<? super Float, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfAbsent(key.floatValue(), mappingFunction instanceof Float2IntFunction ? (Float2IntFunction)mappingFunction : K -> mappingFunction.apply(Float.valueOf(K)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer computeIfPresent(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfPresent(key.floatValue(), mappingFunction instanceof FloatIntUnaryOperator ? (FloatIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer merge(Float key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(mergeInt(key.floatValue(), value.intValue(), mappingFunction instanceof IntIntUnaryOperator ? (IntIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(FloatIntConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Integer> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof FloatIntConsumer ? (FloatIntConsumer)action : (K, V) -> action.accept(Float.valueOf(K), Integer.valueOf(V)));
	}
	
	@Override
	public FloatSet keySet();
	@Override
	public IntCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Float, Integer>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> float2IntEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Float2IntMaps#synchronize
	 */
	public default Float2IntMap synchronize() { return Float2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Float2IntMaps#synchronize
	 */
	public default Float2IntMap synchronize(Object mutex) { return Float2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Float2IntMaps#unmodifiable
	 */
	public default Float2IntMap unmodifiable() { return Float2IntMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Integer put(Float key, Integer value) {
		return Integer.valueOf(put(key.floatValue(), value.intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Float key, Integer value) {
		return Integer.valueOf(put(key.floatValue(), value.intValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Float2IntMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Float2IntMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Float2IntMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Float, Integer>
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
		public int getIntValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public int setValue(int value);
		@Override
		public default Integer getValue() { return Integer.valueOf(getIntValue()); }
		@Override
		public default Integer setValue(Integer value) { return Integer.valueOf(setValue(value.intValue())); }
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
		public BuilderCache put(float key, int value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Float key, Integer value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2IntOpenHashMap map() {
			return new Float2IntOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2IntOpenHashMap map(int size) {
			return new Float2IntOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2IntOpenHashMap map(float[] keys, int[] values) {
			return new Float2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2IntOpenHashMap map(Float[] keys, Integer[] values) {
			return new Float2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2IntOpenHashMap map(Float2IntMap map) {
			return new Float2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntOpenHashMap map(Map<? extends Float, ? extends Integer> map) {
			return new Float2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Float2IntLinkedOpenHashMap linkedMap() {
			return new Float2IntLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Float2IntLinkedOpenHashMap linkedMap(int size) {
			return new Float2IntLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Float2IntLinkedOpenHashMap linkedMap(float[] keys, int[] values) {
			return new Float2IntLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2IntLinkedOpenHashMap linkedMap(Float[] keys, Integer[] values) {
			return new Float2IntLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2IntLinkedOpenHashMap linkedMap(Float2IntMap map) {
			return new Float2IntLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2IntOpenHashMap linkedMap(Map<? extends Float, ? extends Integer> map) {
			return new ImmutableFloat2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableFloat2IntOpenHashMap immutable(float[] keys, int[] values) {
			return new ImmutableFloat2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableFloat2IntOpenHashMap immutable(Float[] keys, Integer[] values) {
			return new ImmutableFloat2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableFloat2IntOpenHashMap immutable(Float2IntMap map) {
			return new ImmutableFloat2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2IntOpenHashMap immutable(Map<? extends Float, ? extends Integer> map) {
			return new ImmutableFloat2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Float2IntOpenCustomHashMap customMap(FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Float2IntOpenCustomHashMap customMap(int size, FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Float2IntOpenCustomHashMap customMap(float[] keys, int[] values, FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(keys, values, strategy);
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
		public Float2IntOpenCustomHashMap customMap(Float[] keys, Integer[] values, FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Float2IntOpenCustomHashMap customMap(Float2IntMap map, FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntOpenCustomHashMap customMap(Map<? extends Float, ? extends Integer> map, FloatStrategy strategy) {
			return new Float2IntOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(int size, FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(float[] keys, int[] values, FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(Float[] keys, Integer[] values, FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(Float2IntMap map, FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(Map<? extends Float, ? extends Integer> map, FloatStrategy strategy) {
			return new Float2IntLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2IntArrayMap arrayMap() {
			return new Float2IntArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2IntArrayMap arrayMap(int size) {
			return new Float2IntArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2IntArrayMap arrayMap(float[] keys, int[] values) {
			return new Float2IntArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2IntArrayMap arrayMap(Float[] keys, Integer[] values) {
			return new Float2IntArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2IntArrayMap arrayMap(Float2IntMap map) {
			return new Float2IntArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntArrayMap arrayMap(Map<? extends Float, ? extends Integer> map) {
			return new Float2IntArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Float2IntRBTreeMap rbTreeMap() {
			return new Float2IntRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Float2IntRBTreeMap rbTreeMap(FloatComparator comp) {
			return new Float2IntRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Float2IntRBTreeMap rbTreeMap(float[] keys, int[] values, FloatComparator comp) {
			return new Float2IntRBTreeMap(keys, values, comp);
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
		public Float2IntRBTreeMap rbTreeMap(Float[] keys, Integer[] values, FloatComparator comp) {
			return new Float2IntRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Float2IntRBTreeMap rbTreeMap(Float2IntMap map, FloatComparator comp) {
			return new Float2IntRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntRBTreeMap rbTreeMap(Map<? extends Float, ? extends Integer> map, FloatComparator comp) {
			return new Float2IntRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Float2IntAVLTreeMap avlTreeMap() {
			return new Float2IntAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Float2IntAVLTreeMap avlTreeMap(FloatComparator comp) {
			return new Float2IntAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Float2IntAVLTreeMap avlTreeMap(float[] keys, int[] values, FloatComparator comp) {
			return new Float2IntAVLTreeMap(keys, values, comp);
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
		public Float2IntAVLTreeMap avlTreeMap(Float[] keys, Integer[] values, FloatComparator comp) {
			return new Float2IntAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Float2IntAVLTreeMap avlTreeMap(Float2IntMap map, FloatComparator comp) {
			return new Float2IntAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2IntAVLTreeMap avlTreeMap(Map<? extends Float, ? extends Integer> map, FloatComparator comp) {
			return new Float2IntAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		float[] keys;
		int[] values;
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
			values = new int[initialSize];
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
		public BuilderCache put(float key, int value) {
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
		public BuilderCache put(Float key, Integer value) {
			return put(key.floatValue(), value.intValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Float2IntMap.Entry entry) {
			return put(entry.getFloatKey(), entry.getIntValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Float2IntMap map) {
			return putAll(Float2IntMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Float, ? extends Integer> map) {
			for(Map.Entry<? extends Float, ? extends Integer> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Float2IntMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Float2IntMap.Entry>)c).size());
			
			for(Float2IntMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Float2IntMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Float2IntOpenHashMap
		 */
		public Float2IntOpenHashMap map() {
			return putElements(new Float2IntOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Float2IntLinkedOpenHashMap
		 */
		public Float2IntLinkedOpenHashMap linkedMap() {
			return putElements(new Float2IntLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableFloat2IntOpenHashMap
		 */
		public ImmutableFloat2IntOpenHashMap immutable() {
			return new ImmutableFloat2IntOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2IntOpenCustomHashMap
		 */
		public Float2IntOpenCustomHashMap customMap(FloatStrategy strategy) {
			return putElements(new Float2IntOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2IntLinkedOpenCustomHashMap
		 */
		public Float2IntLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return putElements(new Float2IntLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Float2IntConcurrentOpenHashMap
		 */
		public Float2IntConcurrentOpenHashMap concurrentMap() {
			return putElements(new Float2IntConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Float2IntArrayMap
		 */
		public Float2IntArrayMap arrayMap() {
			return new Float2IntArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Float2IntRBTreeMap
		 */
		public Float2IntRBTreeMap rbTreeMap() {
			return putElements(new Float2IntRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2IntRBTreeMap
		 */
		public Float2IntRBTreeMap rbTreeMap(FloatComparator comp) {
			return putElements(new Float2IntRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Float2IntAVLTreeMap
		 */
		public Float2IntAVLTreeMap avlTreeMap() {
			return putElements(new Float2IntAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2IntAVLTreeMap
		 */
		public Float2IntAVLTreeMap avlTreeMap(FloatComparator comp) {
			return putElements(new Float2IntAVLTreeMap(comp));
		}
	}
}