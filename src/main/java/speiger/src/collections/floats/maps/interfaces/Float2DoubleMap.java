package speiger.src.collections.floats.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.floats.functions.consumer.FloatDoubleConsumer;
import speiger.src.collections.floats.functions.function.Float2DoubleFunction;
import speiger.src.collections.floats.functions.function.FloatDoubleUnaryOperator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.impl.customHash.Float2DoubleLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2DoubleOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2DoubleLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2DoubleOpenHashMap;
import speiger.src.collections.floats.maps.impl.immutable.ImmutableFloat2DoubleOpenHashMap;
import speiger.src.collections.floats.maps.impl.tree.Float2DoubleAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2DoubleRBTreeMap;
import speiger.src.collections.floats.maps.impl.misc.Float2DoubleArrayMap;
import speiger.src.collections.floats.maps.impl.concurrent.Float2DoubleConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.floats.utils.maps.Float2DoubleMaps;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Float2DoubleMap extends Map<Float, Double>, Float2DoubleFunction
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
	public double getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Float2DoubleMap setDefaultReturnValue(double v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Float2DoubleMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public double put(float key, double value);
	
	/**
	 * A Helper method that allows to put int a Float2DoubleMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default double put(Entry entry) {
		return put(entry.getFloatKey(), entry.getDoubleValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Double put(Map.Entry<Float, Double> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(float[] keys, double[] values) {
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
	public void putAll(float[] keys, double[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Float[] keys, Double[] values) {
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
	public void putAll(Float[] keys, Double[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public double putIfAbsent(float key, double value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Float2DoubleMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public double addTo(float key, double value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Float2DoubleMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public double subFrom(float key, double value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Float2DoubleMap m);
	
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
	public boolean containsValue(double value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Double && containsValue(((Double)value).doubleValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public double remove(float key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default Double remove(Object key) {
		return key instanceof Float ? Double.valueOf(remove(((Float)key).floatValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(float key, double value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Float && value instanceof Double && remove(((Float)key).floatValue(), ((Double)value).doubleValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public double removeOrDefault(float key, double defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(float key, double oldValue, double newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public double replace(float key, double value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceDoubles(Float2DoubleMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceDoubles(FloatDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public double computeDouble(float key, FloatDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public double computeDoubleIfAbsent(float key, Float2DoubleFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public double supplyDoubleIfAbsent(float key, DoubleSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public double computeDoubleIfPresent(float key, FloatDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public double computeDoubleNonDefault(float key, FloatDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public double computeDoubleIfAbsentNonDefault(float key, Float2DoubleFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public double supplyDoubleIfAbsentNonDefault(float key, DoubleSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public double computeDoubleIfPresentNonDefault(float key, FloatDoubleUnaryOperator mappingFunction);
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
	public double mergeDouble(float key, double value, DoubleDoubleUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllDouble(Float2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Double oldValue, Double newValue) {
		return replace(key.floatValue(), oldValue.doubleValue(), newValue.doubleValue());
	}
	
	@Override
	@Deprecated
	public default Double replace(Float key, Double value) {
		return Double.valueOf(replace(key.floatValue(), value.doubleValue()));
	}
	
	@Override
	public default double applyAsDouble(float key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public double get(float key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public double getOrDefault(float key, double defaultValue);
	
	@Override
	@Deprecated
	public default Double get(Object key) {
		return Double.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Double getOrDefault(Object key, Double defaultValue) {
		Double value = Double.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceDoubles(mappingFunction instanceof FloatDoubleUnaryOperator ? (FloatDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Double.valueOf(V)).doubleValue());
	}
	
	@Override
	@Deprecated
	public default Double compute(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDouble(key.floatValue(), mappingFunction instanceof FloatDoubleUnaryOperator ? (FloatDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double computeIfAbsent(Float key, Function<? super Float, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDoubleIfAbsent(key.floatValue(), mappingFunction instanceof Float2DoubleFunction ? (Float2DoubleFunction)mappingFunction : K -> mappingFunction.apply(Float.valueOf(K)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double computeIfPresent(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDoubleIfPresent(key.floatValue(), mappingFunction instanceof FloatDoubleUnaryOperator ? (FloatDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double merge(Float key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(mergeDouble(key.floatValue(), value.doubleValue(), mappingFunction instanceof DoubleDoubleUnaryOperator ? (DoubleDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(FloatDoubleConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Double> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof FloatDoubleConsumer ? (FloatDoubleConsumer)action : (K, V) -> action.accept(Float.valueOf(K), Double.valueOf(V)));
	}
	
	@Override
	public FloatSet keySet();
	@Override
	public DoubleCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Float, Double>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> float2DoubleEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Float2DoubleMaps#synchronize
	 */
	public default Float2DoubleMap synchronize() { return Float2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Float2DoubleMaps#synchronize
	 */
	public default Float2DoubleMap synchronize(Object mutex) { return Float2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Float2DoubleMaps#unmodifiable
	 */
	public default Float2DoubleMap unmodifiable() { return Float2DoubleMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Double put(Float key, Double value) {
		return Double.valueOf(put(key.floatValue(), value.doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Float key, Double value) {
		return Double.valueOf(put(key.floatValue(), value.doubleValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Float2DoubleMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Float2DoubleMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Float2DoubleMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Float, Double>
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
		public double getDoubleValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public double setValue(double value);
		@Override
		public default Double getValue() { return Double.valueOf(getDoubleValue()); }
		@Override
		public default Double setValue(Double value) { return Double.valueOf(setValue(value.doubleValue())); }
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
		public BuilderCache put(float key, double value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Float key, Double value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2DoubleOpenHashMap map() {
			return new Float2DoubleOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2DoubleOpenHashMap map(int size) {
			return new Float2DoubleOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2DoubleOpenHashMap map(float[] keys, double[] values) {
			return new Float2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2DoubleOpenHashMap map(Float[] keys, Double[] values) {
			return new Float2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2DoubleOpenHashMap map(Float2DoubleMap map) {
			return new Float2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleOpenHashMap map(Map<? extends Float, ? extends Double> map) {
			return new Float2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Float2DoubleLinkedOpenHashMap linkedMap() {
			return new Float2DoubleLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Float2DoubleLinkedOpenHashMap linkedMap(int size) {
			return new Float2DoubleLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Float2DoubleLinkedOpenHashMap linkedMap(float[] keys, double[] values) {
			return new Float2DoubleLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2DoubleLinkedOpenHashMap linkedMap(Float[] keys, Double[] values) {
			return new Float2DoubleLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2DoubleLinkedOpenHashMap linkedMap(Float2DoubleMap map) {
			return new Float2DoubleLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2DoubleOpenHashMap linkedMap(Map<? extends Float, ? extends Double> map) {
			return new ImmutableFloat2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableFloat2DoubleOpenHashMap immutable(float[] keys, double[] values) {
			return new ImmutableFloat2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableFloat2DoubleOpenHashMap immutable(Float[] keys, Double[] values) {
			return new ImmutableFloat2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableFloat2DoubleOpenHashMap immutable(Float2DoubleMap map) {
			return new ImmutableFloat2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableFloat2DoubleOpenHashMap immutable(Map<? extends Float, ? extends Double> map) {
			return new ImmutableFloat2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Float2DoubleOpenCustomHashMap customMap(FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Float2DoubleOpenCustomHashMap customMap(int size, FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Float2DoubleOpenCustomHashMap customMap(float[] keys, double[] values, FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(keys, values, strategy);
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
		public Float2DoubleOpenCustomHashMap customMap(Float[] keys, Double[] values, FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Float2DoubleOpenCustomHashMap customMap(Float2DoubleMap map, FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleOpenCustomHashMap customMap(Map<? extends Float, ? extends Double> map, FloatStrategy strategy) {
			return new Float2DoubleOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(int size, FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(float[] keys, double[] values, FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(Float[] keys, Double[] values, FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(Float2DoubleMap map, FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(Map<? extends Float, ? extends Double> map, FloatStrategy strategy) {
			return new Float2DoubleLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Float2DoubleArrayMap arrayMap() {
			return new Float2DoubleArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Float2DoubleArrayMap arrayMap(int size) {
			return new Float2DoubleArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Float2DoubleArrayMap arrayMap(float[] keys, double[] values) {
			return new Float2DoubleArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Float2DoubleArrayMap arrayMap(Float[] keys, Double[] values) {
			return new Float2DoubleArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Float2DoubleArrayMap arrayMap(Float2DoubleMap map) {
			return new Float2DoubleArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleArrayMap arrayMap(Map<? extends Float, ? extends Double> map) {
			return new Float2DoubleArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Float2DoubleRBTreeMap rbTreeMap() {
			return new Float2DoubleRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Float2DoubleRBTreeMap rbTreeMap(FloatComparator comp) {
			return new Float2DoubleRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Float2DoubleRBTreeMap rbTreeMap(float[] keys, double[] values, FloatComparator comp) {
			return new Float2DoubleRBTreeMap(keys, values, comp);
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
		public Float2DoubleRBTreeMap rbTreeMap(Float[] keys, Double[] values, FloatComparator comp) {
			return new Float2DoubleRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Float2DoubleRBTreeMap rbTreeMap(Float2DoubleMap map, FloatComparator comp) {
			return new Float2DoubleRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleRBTreeMap rbTreeMap(Map<? extends Float, ? extends Double> map, FloatComparator comp) {
			return new Float2DoubleRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Float2DoubleAVLTreeMap avlTreeMap() {
			return new Float2DoubleAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Float2DoubleAVLTreeMap avlTreeMap(FloatComparator comp) {
			return new Float2DoubleAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Float2DoubleAVLTreeMap avlTreeMap(float[] keys, double[] values, FloatComparator comp) {
			return new Float2DoubleAVLTreeMap(keys, values, comp);
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
		public Float2DoubleAVLTreeMap avlTreeMap(Float[] keys, Double[] values, FloatComparator comp) {
			return new Float2DoubleAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Float2DoubleAVLTreeMap avlTreeMap(Float2DoubleMap map, FloatComparator comp) {
			return new Float2DoubleAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Float2DoubleAVLTreeMap avlTreeMap(Map<? extends Float, ? extends Double> map, FloatComparator comp) {
			return new Float2DoubleAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		float[] keys;
		double[] values;
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
			values = new double[initialSize];
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
		public BuilderCache put(float key, double value) {
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
		public BuilderCache put(Float key, Double value) {
			return put(key.floatValue(), value.doubleValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Float2DoubleMap.Entry entry) {
			return put(entry.getFloatKey(), entry.getDoubleValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Float2DoubleMap map) {
			return putAll(Float2DoubleMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Float, ? extends Double> map) {
			for(Map.Entry<? extends Float, ? extends Double> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Float2DoubleMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Float2DoubleMap.Entry>)c).size());
			
			for(Float2DoubleMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Float2DoubleMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Float2DoubleOpenHashMap
		 */
		public Float2DoubleOpenHashMap map() {
			return putElements(new Float2DoubleOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Float2DoubleLinkedOpenHashMap
		 */
		public Float2DoubleLinkedOpenHashMap linkedMap() {
			return putElements(new Float2DoubleLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableFloat2DoubleOpenHashMap
		 */
		public ImmutableFloat2DoubleOpenHashMap immutable() {
			return new ImmutableFloat2DoubleOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2DoubleOpenCustomHashMap
		 */
		public Float2DoubleOpenCustomHashMap customMap(FloatStrategy strategy) {
			return putElements(new Float2DoubleOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2DoubleLinkedOpenCustomHashMap
		 */
		public Float2DoubleLinkedOpenCustomHashMap customLinkedMap(FloatStrategy strategy) {
			return putElements(new Float2DoubleLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Float2DoubleConcurrentOpenHashMap
		 */
		public Float2DoubleConcurrentOpenHashMap concurrentMap() {
			return putElements(new Float2DoubleConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Float2DoubleArrayMap
		 */
		public Float2DoubleArrayMap arrayMap() {
			return new Float2DoubleArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Float2DoubleRBTreeMap
		 */
		public Float2DoubleRBTreeMap rbTreeMap() {
			return putElements(new Float2DoubleRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2DoubleRBTreeMap
		 */
		public Float2DoubleRBTreeMap rbTreeMap(FloatComparator comp) {
			return putElements(new Float2DoubleRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Float2DoubleAVLTreeMap
		 */
		public Float2DoubleAVLTreeMap avlTreeMap() {
			return putElements(new Float2DoubleAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2DoubleAVLTreeMap
		 */
		public Float2DoubleAVLTreeMap avlTreeMap(FloatComparator comp) {
			return putElements(new Float2DoubleAVLTreeMap(comp));
		}
	}
}