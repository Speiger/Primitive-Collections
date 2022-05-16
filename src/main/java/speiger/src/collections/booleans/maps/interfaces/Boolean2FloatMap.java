package speiger.src.collections.booleans.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.booleans.functions.consumer.BooleanFloatConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2FloatFunction;
import speiger.src.collections.booleans.functions.function.BooleanFloatUnaryOperator;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Boolean2FloatMap extends Map<Boolean, Float>, Boolean2FloatFunction
{
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
	public Boolean2FloatMap setDefaultReturnValue(float v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Boolean2FloatMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public float put(boolean key, float value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(boolean[] keys, float[] values) {
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
	public void putAll(boolean[] keys, float[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Boolean[] keys, Float[] values) {
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
	public void putAll(Boolean[] keys, Float[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public float putIfAbsent(boolean key, float value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Boolean2FloatMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public float addTo(boolean key, float value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Boolean2FloatMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public float subFrom(boolean key, float value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Boolean2FloatMap m);
	
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
	public float remove(boolean key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Boolean but just have to support equals with Boolean.
	 */
	@Override
	public default Float remove(Object key) {
		return key instanceof Boolean ? Float.valueOf(remove(((Boolean)key).booleanValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(boolean key, float value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Boolean && value instanceof Float && remove(((Boolean)key).booleanValue(), ((Float)value).floatValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public float removeOrDefault(boolean key, float defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(boolean key, float oldValue, float newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public float replace(boolean key, float value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceFloats(Boolean2FloatMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceFloats(BooleanFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloat(boolean key, BooleanFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsent(boolean key, Boolean2FloatFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsent(boolean key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresent(boolean key, BooleanFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public float mergeFloat(boolean key, float value, FloatFloatUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllFloat(Boolean2FloatMap m, FloatFloatUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Boolean key, Float oldValue, Float newValue) {
		return replace(key.booleanValue(), oldValue.floatValue(), newValue.floatValue());
	}
	
	@Override
	@Deprecated
	public default Float replace(Boolean key, Float value) {
		return Float.valueOf(replace(key.booleanValue(), value.floatValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public float get(boolean key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public float getOrDefault(boolean key, float defaultValue);
	
	@Override
	@Deprecated
	public default Float get(Object key) {
		return Float.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Float getOrDefault(Object key, Float defaultValue) {
		Float value = Float.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Boolean, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceFloats(mappingFunction instanceof BooleanFloatUnaryOperator ? (BooleanFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Float.valueOf(V)).floatValue());
	}
	
	@Override
	@Deprecated
	public default Float compute(Boolean key, BiFunction<? super Boolean, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloat(key.booleanValue(), mappingFunction instanceof BooleanFloatUnaryOperator ? (BooleanFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfAbsent(Boolean key, Function<? super Boolean, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfAbsent(key.booleanValue(), mappingFunction instanceof Boolean2FloatFunction ? (Boolean2FloatFunction)mappingFunction : K -> mappingFunction.apply(Boolean.valueOf(K)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfPresent(Boolean key, BiFunction<? super Boolean, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfPresent(key.booleanValue(), mappingFunction instanceof BooleanFloatUnaryOperator ? (BooleanFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float merge(Boolean key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(mergeFloat(key.booleanValue(), value.floatValue(), mappingFunction instanceof FloatFloatUnaryOperator ? (FloatFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(BooleanFloatConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Boolean, ? super Float> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof BooleanFloatConsumer ? (BooleanFloatConsumer)action : (K, V) -> action.accept(Boolean.valueOf(K), Float.valueOf(V)));
	}
	
	@Override
	public BooleanSet keySet();
	@Override
	public FloatCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Boolean, Float>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> boolean2FloatEntrySet();
	
	@Override
	@Deprecated
	public default Float put(Boolean key, Float value) {
		return Float.valueOf(put(key.booleanValue(), value.floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Boolean key, Float value) {
		return Float.valueOf(put(key.booleanValue(), value.floatValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Boolean2FloatMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Boolean2FloatMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Boolean2FloatMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Boolean, Float>
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
	
}