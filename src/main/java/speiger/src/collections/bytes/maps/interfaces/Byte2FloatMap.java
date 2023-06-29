package speiger.src.collections.bytes.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.bytes.functions.consumer.ByteFloatConsumer;
import speiger.src.collections.bytes.functions.function.Byte2FloatFunction;
import speiger.src.collections.bytes.functions.function.ByteFloatUnaryOperator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2FloatLinkedOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2FloatOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2FloatLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2FloatOpenHashMap;
import speiger.src.collections.bytes.maps.impl.immutable.ImmutableByte2FloatOpenHashMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2FloatAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2FloatRBTreeMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2FloatArrayMap;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2FloatConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.bytes.utils.maps.Byte2FloatMaps;
import speiger.src.collections.bytes.sets.ByteSet;
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
public interface Byte2FloatMap extends Map<Byte, Float>, Byte2FloatFunction
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
	public Byte2FloatMap setDefaultReturnValue(float v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Byte2FloatMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public float put(byte key, float value);
	
	/**
	 * A Helper method that allows to put int a Byte2FloatMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default float put(Entry entry) {
		return put(entry.getByteKey(), entry.getFloatValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Float put(Map.Entry<Byte, Float> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(byte[] keys, float[] values) {
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
	public void putAll(byte[] keys, float[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Byte[] keys, Float[] values) {
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
	public void putAll(Byte[] keys, Float[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public float putIfAbsent(byte key, float value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Byte2FloatMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public float addTo(byte key, float value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Byte2FloatMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public float subFrom(byte key, float value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Byte2FloatMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(byte key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Byte but just have to support equals with Byte.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Byte && containsKey(((Byte)key).byteValue());
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
	public float remove(byte key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Byte but just have to support equals with Byte.
	 */
	@Override
	public default Float remove(Object key) {
		return key instanceof Byte ? Float.valueOf(remove(((Byte)key).byteValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(byte key, float value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Byte && value instanceof Float && remove(((Byte)key).byteValue(), ((Float)value).floatValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public float removeOrDefault(byte key, float defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(byte key, float oldValue, float newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public float replace(byte key, float value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceFloats(Byte2FloatMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceFloats(ByteFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloat(byte key, ByteFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsent(byte key, Byte2FloatFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsent(byte key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresent(byte key, ByteFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloatNonDefault(byte key, ByteFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsentNonDefault(byte key, Byte2FloatFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsentNonDefault(byte key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresentNonDefault(byte key, ByteFloatUnaryOperator mappingFunction);
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
	public float mergeFloat(byte key, float value, FloatFloatUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllFloat(Byte2FloatMap m, FloatFloatUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Float oldValue, Float newValue) {
		return replace(key.byteValue(), oldValue.floatValue(), newValue.floatValue());
	}
	
	@Override
	@Deprecated
	public default Float replace(Byte key, Float value) {
		return Float.valueOf(replace(key.byteValue(), value.floatValue()));
	}
	
	@Override
	public default float applyAsFloat(byte key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public float get(byte key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public float getOrDefault(byte key, float defaultValue);
	
	@Override
	@Deprecated
	public default Float get(Object key) {
		return Float.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Float getOrDefault(Object key, Float defaultValue) {
		Float value = Float.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceFloats(mappingFunction instanceof ByteFloatUnaryOperator ? (ByteFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Float.valueOf(V)).floatValue());
	}
	
	@Override
	@Deprecated
	public default Float compute(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloat(key.byteValue(), mappingFunction instanceof ByteFloatUnaryOperator ? (ByteFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfAbsent(Byte key, Function<? super Byte, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfAbsent(key.byteValue(), mappingFunction instanceof Byte2FloatFunction ? (Byte2FloatFunction)mappingFunction : K -> mappingFunction.apply(Byte.valueOf(K)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfPresent(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfPresent(key.byteValue(), mappingFunction instanceof ByteFloatUnaryOperator ? (ByteFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float merge(Byte key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(mergeFloat(key.byteValue(), value.floatValue(), mappingFunction instanceof FloatFloatUnaryOperator ? (FloatFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ByteFloatConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Float> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ByteFloatConsumer ? (ByteFloatConsumer)action : (K, V) -> action.accept(Byte.valueOf(K), Float.valueOf(V)));
	}
	
	@Override
	public ByteSet keySet();
	@Override
	public FloatCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Byte, Float>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> byte2FloatEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Byte2FloatMaps#synchronize
	 */
	public default Byte2FloatMap synchronize() { return Byte2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Byte2FloatMaps#synchronize
	 */
	public default Byte2FloatMap synchronize(Object mutex) { return Byte2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Byte2FloatMaps#unmodifiable
	 */
	public default Byte2FloatMap unmodifiable() { return Byte2FloatMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Float put(Byte key, Float value) {
		return Float.valueOf(put(key.byteValue(), value.floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Byte key, Float value) {
		return Float.valueOf(put(key.byteValue(), value.floatValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Byte2FloatMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Byte2FloatMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Byte2FloatMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Byte, Float>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public byte getByteKey();
		public default Byte getKey() { return Byte.valueOf(getByteKey()); }
		
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
		public BuilderCache put(byte key, float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Byte key, Float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2FloatOpenHashMap map() {
			return new Byte2FloatOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2FloatOpenHashMap map(int size) {
			return new Byte2FloatOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2FloatOpenHashMap map(byte[] keys, float[] values) {
			return new Byte2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2FloatOpenHashMap map(Byte[] keys, Float[] values) {
			return new Byte2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2FloatOpenHashMap map(Byte2FloatMap map) {
			return new Byte2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatOpenHashMap map(Map<? extends Byte, ? extends Float> map) {
			return new Byte2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Byte2FloatLinkedOpenHashMap linkedMap() {
			return new Byte2FloatLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2FloatLinkedOpenHashMap linkedMap(int size) {
			return new Byte2FloatLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Byte2FloatLinkedOpenHashMap linkedMap(byte[] keys, float[] values) {
			return new Byte2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2FloatLinkedOpenHashMap linkedMap(Byte[] keys, Float[] values) {
			return new Byte2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2FloatLinkedOpenHashMap linkedMap(Byte2FloatMap map) {
			return new Byte2FloatLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2FloatOpenHashMap linkedMap(Map<? extends Byte, ? extends Float> map) {
			return new ImmutableByte2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableByte2FloatOpenHashMap immutable(byte[] keys, float[] values) {
			return new ImmutableByte2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableByte2FloatOpenHashMap immutable(Byte[] keys, Float[] values) {
			return new ImmutableByte2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableByte2FloatOpenHashMap immutable(Byte2FloatMap map) {
			return new ImmutableByte2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2FloatOpenHashMap immutable(Map<? extends Byte, ? extends Float> map) {
			return new ImmutableByte2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Byte2FloatOpenCustomHashMap customMap(ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Byte2FloatOpenCustomHashMap customMap(int size, ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Byte2FloatOpenCustomHashMap customMap(byte[] keys, float[] values, ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(keys, values, strategy);
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
		public Byte2FloatOpenCustomHashMap customMap(Byte[] keys, Float[] values, ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2FloatOpenCustomHashMap customMap(Byte2FloatMap map, ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatOpenCustomHashMap customMap(Map<? extends Byte, ? extends Float> map, ByteStrategy strategy) {
			return new Byte2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(int size, ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(byte[] keys, float[] values, ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(Byte[] keys, Float[] values, ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(Byte2FloatMap map, ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(Map<? extends Byte, ? extends Float> map, ByteStrategy strategy) {
			return new Byte2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2FloatArrayMap arrayMap() {
			return new Byte2FloatArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2FloatArrayMap arrayMap(int size) {
			return new Byte2FloatArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2FloatArrayMap arrayMap(byte[] keys, float[] values) {
			return new Byte2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2FloatArrayMap arrayMap(Byte[] keys, Float[] values) {
			return new Byte2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2FloatArrayMap arrayMap(Byte2FloatMap map) {
			return new Byte2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatArrayMap arrayMap(Map<? extends Byte, ? extends Float> map) {
			return new Byte2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Byte2FloatRBTreeMap rbTreeMap() {
			return new Byte2FloatRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Byte2FloatRBTreeMap rbTreeMap(ByteComparator comp) {
			return new Byte2FloatRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Byte2FloatRBTreeMap rbTreeMap(byte[] keys, float[] values, ByteComparator comp) {
			return new Byte2FloatRBTreeMap(keys, values, comp);
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
		public Byte2FloatRBTreeMap rbTreeMap(Byte[] keys, Float[] values, ByteComparator comp) {
			return new Byte2FloatRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Byte2FloatRBTreeMap rbTreeMap(Byte2FloatMap map, ByteComparator comp) {
			return new Byte2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatRBTreeMap rbTreeMap(Map<? extends Byte, ? extends Float> map, ByteComparator comp) {
			return new Byte2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Byte2FloatAVLTreeMap avlTreeMap() {
			return new Byte2FloatAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Byte2FloatAVLTreeMap avlTreeMap(ByteComparator comp) {
			return new Byte2FloatAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Byte2FloatAVLTreeMap avlTreeMap(byte[] keys, float[] values, ByteComparator comp) {
			return new Byte2FloatAVLTreeMap(keys, values, comp);
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
		public Byte2FloatAVLTreeMap avlTreeMap(Byte[] keys, Float[] values, ByteComparator comp) {
			return new Byte2FloatAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Byte2FloatAVLTreeMap avlTreeMap(Byte2FloatMap map, ByteComparator comp) {
			return new Byte2FloatAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2FloatAVLTreeMap avlTreeMap(Map<? extends Byte, ? extends Float> map, ByteComparator comp) {
			return new Byte2FloatAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		byte[] keys;
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
			keys = new byte[initialSize];
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
		public BuilderCache put(byte key, float value) {
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
		public BuilderCache put(Byte key, Float value) {
			return put(key.byteValue(), value.floatValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Byte2FloatMap.Entry entry) {
			return put(entry.getByteKey(), entry.getFloatValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Byte2FloatMap map) {
			return putAll(Byte2FloatMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Byte, ? extends Float> map) {
			for(Map.Entry<? extends Byte, ? extends Float> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Byte2FloatMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Byte2FloatMap.Entry>)c).size());
			
			for(Byte2FloatMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Byte2FloatMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Byte2FloatOpenHashMap
		 */
		public Byte2FloatOpenHashMap map() {
			return putElements(new Byte2FloatOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Byte2FloatLinkedOpenHashMap
		 */
		public Byte2FloatLinkedOpenHashMap linkedMap() {
			return putElements(new Byte2FloatLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableByte2FloatOpenHashMap
		 */
		public ImmutableByte2FloatOpenHashMap immutable() {
			return new ImmutableByte2FloatOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2FloatOpenCustomHashMap
		 */
		public Byte2FloatOpenCustomHashMap customMap(ByteStrategy strategy) {
			return putElements(new Byte2FloatOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2FloatLinkedOpenCustomHashMap
		 */
		public Byte2FloatLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return putElements(new Byte2FloatLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Byte2FloatConcurrentOpenHashMap
		 */
		public Byte2FloatConcurrentOpenHashMap concurrentMap() {
			return putElements(new Byte2FloatConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Byte2FloatArrayMap
		 */
		public Byte2FloatArrayMap arrayMap() {
			return new Byte2FloatArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Byte2FloatRBTreeMap
		 */
		public Byte2FloatRBTreeMap rbTreeMap() {
			return putElements(new Byte2FloatRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2FloatRBTreeMap
		 */
		public Byte2FloatRBTreeMap rbTreeMap(ByteComparator comp) {
			return putElements(new Byte2FloatRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Byte2FloatAVLTreeMap
		 */
		public Byte2FloatAVLTreeMap avlTreeMap() {
			return putElements(new Byte2FloatAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2FloatAVLTreeMap
		 */
		public Byte2FloatAVLTreeMap avlTreeMap(ByteComparator comp) {
			return putElements(new Byte2FloatAVLTreeMap(comp));
		}
	}
}