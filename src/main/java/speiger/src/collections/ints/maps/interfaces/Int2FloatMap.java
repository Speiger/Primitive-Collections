package speiger.src.collections.ints.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.function.Int2FloatFunction;
import speiger.src.collections.ints.functions.function.IntFloatUnaryOperator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.maps.impl.customHash.Int2FloatLinkedOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.customHash.Int2FloatOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2FloatLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2FloatOpenHashMap;
import speiger.src.collections.ints.maps.impl.immutable.ImmutableInt2FloatOpenHashMap;
import speiger.src.collections.ints.maps.impl.tree.Int2FloatAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2FloatRBTreeMap;
import speiger.src.collections.ints.maps.impl.misc.Int2FloatArrayMap;
import speiger.src.collections.ints.maps.impl.concurrent.Int2FloatConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.collections.ints.utils.maps.Int2FloatMaps;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Int2FloatMap extends Map<Integer, Float>, Int2FloatFunction
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
	public float getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Int2FloatMap setDefaultReturnValue(float v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Int2FloatMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public float put(int key, float value);
	
	/**
	 * A Helper method that allows to put int a Int2FloatMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default float put(Entry entry) {
		return put(entry.getIntKey(), entry.getFloatValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Float put(Map.Entry<Integer, Float> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(int[] keys, float[] values) {
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
	public void putAll(int[] keys, float[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Integer[] keys, Float[] values) {
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
	public void putAll(Integer[] keys, Float[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public float putIfAbsent(int key, float value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Int2FloatMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public float addTo(int key, float value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Int2FloatMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public float subFrom(int key, float value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Int2FloatMap m);
	
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
	public boolean containsValue(float value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Float && containsValue(((Float)value).floatValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public float remove(int key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Integer but just have to support equals with Integer.
	 */
	@Override
	public default Float remove(Object key) {
		return key instanceof Integer ? Float.valueOf(remove(((Integer)key).intValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(int key, float value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Integer && value instanceof Float && remove(((Integer)key).intValue(), ((Float)value).floatValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public float removeOrDefault(int key, float defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(int key, float oldValue, float newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public float replace(int key, float value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceFloats(Int2FloatMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceFloats(IntFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloat(int key, IntFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction);
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
	public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Float oldValue, Float newValue) {
		return replace(key.intValue(), oldValue.floatValue(), newValue.floatValue());
	}
	
	@Override
	@Deprecated
	public default Float replace(Integer key, Float value) {
		return Float.valueOf(replace(key.intValue(), value.floatValue()));
	}
	
	@Override
	public default float applyAsFloat(int key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public float get(int key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public float getOrDefault(int key, float defaultValue);
	
	@Override
	@Deprecated
	public default Float get(Object key) {
		return Float.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Float getOrDefault(Object key, Float defaultValue) {
		Float value = Float.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceFloats(mappingFunction instanceof IntFloatUnaryOperator ? (IntFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Float.valueOf(V)).floatValue());
	}
	
	@Override
	@Deprecated
	public default Float compute(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloat(key.intValue(), mappingFunction instanceof IntFloatUnaryOperator ? (IntFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfAbsent(Integer key, Function<? super Integer, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfAbsent(key.intValue(), mappingFunction instanceof Int2FloatFunction ? (Int2FloatFunction)mappingFunction : K -> mappingFunction.apply(Integer.valueOf(K)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfPresent(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfPresent(key.intValue(), mappingFunction instanceof IntFloatUnaryOperator ? (IntFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float merge(Integer key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(mergeFloat(key.intValue(), value.floatValue(), mappingFunction instanceof FloatFloatUnaryOperator ? (FloatFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(IntFloatConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Float> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof IntFloatConsumer ? (IntFloatConsumer)action : (K, V) -> action.accept(Integer.valueOf(K), Float.valueOf(V)));
	}
	
	@Override
	public IntSet keySet();
	@Override
	public FloatCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Integer, Float>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> int2FloatEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Int2FloatMaps#synchronize
	 */
	public default Int2FloatMap synchronize() { return Int2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Int2FloatMaps#synchronize
	 */
	public default Int2FloatMap synchronize(Object mutex) { return Int2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Int2FloatMaps#unmodifiable
	 */
	public default Int2FloatMap unmodifiable() { return Int2FloatMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Float put(Integer key, Float value) {
		return Float.valueOf(put(key.intValue(), value.floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Integer key, Float value) {
		return Float.valueOf(put(key.intValue(), value.floatValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Int2FloatMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Int2FloatMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Int2FloatMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Integer, Float>
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
		public float getFloatValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public float setValue(float value);
		@Override
		public default Float getValue() { return Float.valueOf(getFloatValue()); }
		@Override
		public default Float setValue(Float value) { return Float.valueOf(setValue(value.floatValue())); }
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
		public BuilderCache put(int key, float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Integer key, Float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2FloatOpenHashMap map() {
			return new Int2FloatOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2FloatOpenHashMap map(int size) {
			return new Int2FloatOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2FloatOpenHashMap map(int[] keys, float[] values) {
			return new Int2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2FloatOpenHashMap map(Integer[] keys, Float[] values) {
			return new Int2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2FloatOpenHashMap map(Int2FloatMap map) {
			return new Int2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatOpenHashMap map(Map<? extends Integer, ? extends Float> map) {
			return new Int2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Int2FloatLinkedOpenHashMap linkedMap() {
			return new Int2FloatLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Int2FloatLinkedOpenHashMap linkedMap(int size) {
			return new Int2FloatLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Int2FloatLinkedOpenHashMap linkedMap(int[] keys, float[] values) {
			return new Int2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2FloatLinkedOpenHashMap linkedMap(Integer[] keys, Float[] values) {
			return new Int2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2FloatLinkedOpenHashMap linkedMap(Int2FloatMap map) {
			return new Int2FloatLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2FloatOpenHashMap linkedMap(Map<? extends Integer, ? extends Float> map) {
			return new ImmutableInt2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableInt2FloatOpenHashMap immutable(int[] keys, float[] values) {
			return new ImmutableInt2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableInt2FloatOpenHashMap immutable(Integer[] keys, Float[] values) {
			return new ImmutableInt2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableInt2FloatOpenHashMap immutable(Int2FloatMap map) {
			return new ImmutableInt2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2FloatOpenHashMap immutable(Map<? extends Integer, ? extends Float> map) {
			return new ImmutableInt2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Int2FloatOpenCustomHashMap customMap(IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Int2FloatOpenCustomHashMap customMap(int size, IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Int2FloatOpenCustomHashMap customMap(int[] keys, float[] values, IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(keys, values, strategy);
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
		public Int2FloatOpenCustomHashMap customMap(Integer[] keys, Float[] values, IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Int2FloatOpenCustomHashMap customMap(Int2FloatMap map, IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatOpenCustomHashMap customMap(Map<? extends Integer, ? extends Float> map, IntStrategy strategy) {
			return new Int2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(int size, IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(int[] keys, float[] values, IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(Integer[] keys, Float[] values, IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(Int2FloatMap map, IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(Map<? extends Integer, ? extends Float> map, IntStrategy strategy) {
			return new Int2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2FloatArrayMap arrayMap() {
			return new Int2FloatArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2FloatArrayMap arrayMap(int size) {
			return new Int2FloatArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2FloatArrayMap arrayMap(int[] keys, float[] values) {
			return new Int2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2FloatArrayMap arrayMap(Integer[] keys, Float[] values) {
			return new Int2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2FloatArrayMap arrayMap(Int2FloatMap map) {
			return new Int2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatArrayMap arrayMap(Map<? extends Integer, ? extends Float> map) {
			return new Int2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Int2FloatRBTreeMap rbTreeMap() {
			return new Int2FloatRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Int2FloatRBTreeMap rbTreeMap(IntComparator comp) {
			return new Int2FloatRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Int2FloatRBTreeMap rbTreeMap(int[] keys, float[] values, IntComparator comp) {
			return new Int2FloatRBTreeMap(keys, values, comp);
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
		public Int2FloatRBTreeMap rbTreeMap(Integer[] keys, Float[] values, IntComparator comp) {
			return new Int2FloatRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Int2FloatRBTreeMap rbTreeMap(Int2FloatMap map, IntComparator comp) {
			return new Int2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatRBTreeMap rbTreeMap(Map<? extends Integer, ? extends Float> map, IntComparator comp) {
			return new Int2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Int2FloatAVLTreeMap avlTreeMap() {
			return new Int2FloatAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Int2FloatAVLTreeMap avlTreeMap(IntComparator comp) {
			return new Int2FloatAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Int2FloatAVLTreeMap avlTreeMap(int[] keys, float[] values, IntComparator comp) {
			return new Int2FloatAVLTreeMap(keys, values, comp);
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
		public Int2FloatAVLTreeMap avlTreeMap(Integer[] keys, Float[] values, IntComparator comp) {
			return new Int2FloatAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Int2FloatAVLTreeMap avlTreeMap(Int2FloatMap map, IntComparator comp) {
			return new Int2FloatAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2FloatAVLTreeMap avlTreeMap(Map<? extends Integer, ? extends Float> map, IntComparator comp) {
			return new Int2FloatAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		int[] keys;
		float[] values;
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
			keys = new int[initialSize];
			values = new float[initialSize];
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
		public BuilderCache put(int key, float value) {
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
		public BuilderCache put(Integer key, Float value) {
			return put(key.intValue(), value.floatValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Int2FloatMap.Entry entry) {
			return put(entry.getIntKey(), entry.getFloatValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Int2FloatMap map) {
			return putAll(Int2FloatMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Integer, ? extends Float> map) {
			for(Map.Entry<? extends Integer, ? extends Float> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Int2FloatMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Int2FloatMap.Entry>)c).size());
			
			for(Int2FloatMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Int2FloatMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Int2FloatOpenHashMap
		 */
		public Int2FloatOpenHashMap map() {
			return putElements(new Int2FloatOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Int2FloatLinkedOpenHashMap
		 */
		public Int2FloatLinkedOpenHashMap linkedMap() {
			return putElements(new Int2FloatLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableInt2FloatOpenHashMap
		 */
		public ImmutableInt2FloatOpenHashMap immutable() {
			return new ImmutableInt2FloatOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2FloatOpenCustomHashMap
		 */
		public Int2FloatOpenCustomHashMap customMap(IntStrategy strategy) {
			return putElements(new Int2FloatOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2FloatLinkedOpenCustomHashMap
		 */
		public Int2FloatLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return putElements(new Int2FloatLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Int2FloatConcurrentOpenHashMap
		 */
		public Int2FloatConcurrentOpenHashMap concurrentMap() {
			return putElements(new Int2FloatConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Int2FloatArrayMap
		 */
		public Int2FloatArrayMap arrayMap() {
			return new Int2FloatArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Int2FloatRBTreeMap
		 */
		public Int2FloatRBTreeMap rbTreeMap() {
			return putElements(new Int2FloatRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2FloatRBTreeMap
		 */
		public Int2FloatRBTreeMap rbTreeMap(IntComparator comp) {
			return putElements(new Int2FloatRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Int2FloatAVLTreeMap
		 */
		public Int2FloatAVLTreeMap avlTreeMap() {
			return putElements(new Int2FloatAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2FloatAVLTreeMap
		 */
		public Int2FloatAVLTreeMap avlTreeMap(IntComparator comp) {
			return putElements(new Int2FloatAVLTreeMap(comp));
		}
	}
}