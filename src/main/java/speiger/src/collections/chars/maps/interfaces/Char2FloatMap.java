package speiger.src.collections.chars.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.chars.functions.consumer.CharFloatConsumer;
import speiger.src.collections.chars.functions.function.Char2FloatFunction;
import speiger.src.collections.chars.functions.function.CharFloatUnaryOperator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.maps.impl.customHash.Char2FloatLinkedOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.customHash.Char2FloatOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2FloatLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2FloatOpenHashMap;
import speiger.src.collections.chars.maps.impl.immutable.ImmutableChar2FloatOpenHashMap;
import speiger.src.collections.chars.maps.impl.tree.Char2FloatAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2FloatRBTreeMap;
import speiger.src.collections.chars.maps.impl.misc.Char2FloatArrayMap;
import speiger.src.collections.chars.maps.impl.concurrent.Char2FloatConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.collections.chars.utils.maps.Char2FloatMaps;
import speiger.src.collections.chars.sets.CharSet;
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
public interface Char2FloatMap extends Map<Character, Float>, Char2FloatFunction
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
	public Char2FloatMap setDefaultReturnValue(float v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Char2FloatMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public float put(char key, float value);
	
	/**
	 * A Helper method that allows to put int a Char2FloatMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default float put(Entry entry) {
		return put(entry.getCharKey(), entry.getFloatValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Float put(Map.Entry<Character, Float> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(char[] keys, float[] values) {
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
	public void putAll(char[] keys, float[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Character[] keys, Float[] values) {
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
	public void putAll(Character[] keys, Float[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public float putIfAbsent(char key, float value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Char2FloatMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public float addTo(char key, float value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Char2FloatMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public float subFrom(char key, float value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Char2FloatMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(char key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Character but just have to support equals with Character.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Character && containsKey(((Character)key).charValue());
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
	public float remove(char key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Character but just have to support equals with Character.
	 */
	@Override
	public default Float remove(Object key) {
		return key instanceof Character ? Float.valueOf(remove(((Character)key).charValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(char key, float value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Character && value instanceof Float && remove(((Character)key).charValue(), ((Float)value).floatValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public float removeOrDefault(char key, float defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(char key, float oldValue, float newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public float replace(char key, float value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceFloats(Char2FloatMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceFloats(CharFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloat(char key, CharFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public float computeFloatNonDefault(char key, CharFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsent(char key, Char2FloatFunction mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public float computeFloatIfAbsentNonDefault(char key, Char2FloatFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsent(char key, FloatSupplier valueProvider);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public float supplyFloatIfAbsentNonDefault(char key, FloatSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresent(char key, CharFloatUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public float computeFloatIfPresentNonDefault(char key, CharFloatUnaryOperator mappingFunction);
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
	public float mergeFloat(char key, float value, FloatFloatUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllFloat(Char2FloatMap m, FloatFloatUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Float oldValue, Float newValue) {
		return replace(key.charValue(), oldValue.floatValue(), newValue.floatValue());
	}
	
	@Override
	@Deprecated
	public default Float replace(Character key, Float value) {
		return Float.valueOf(replace(key.charValue(), value.floatValue()));
	}
	
	@Override
	public default float applyAsFloat(char key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public float get(char key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public float getOrDefault(char key, float defaultValue);
	
	@Override
	@Deprecated
	public default Float get(Object key) {
		return Float.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Float getOrDefault(Object key, Float defaultValue) {
		Float value = Float.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceFloats(mappingFunction instanceof CharFloatUnaryOperator ? (CharFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Float.valueOf(V)).floatValue());
	}
	
	@Override
	@Deprecated
	public default Float compute(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloat(key.charValue(), mappingFunction instanceof CharFloatUnaryOperator ? (CharFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfAbsent(Character key, Function<? super Character, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfAbsent(key.charValue(), mappingFunction instanceof Char2FloatFunction ? (Char2FloatFunction)mappingFunction : K -> mappingFunction.apply(Character.valueOf(K)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float computeIfPresent(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(computeFloatIfPresent(key.charValue(), mappingFunction instanceof CharFloatUnaryOperator ? (CharFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float merge(Character key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Float.valueOf(mergeFloat(key.charValue(), value.floatValue(), mappingFunction instanceof FloatFloatUnaryOperator ? (FloatFloatUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), Float.valueOf(V)).floatValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(CharFloatConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Float> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof CharFloatConsumer ? (CharFloatConsumer)action : (K, V) -> action.accept(Character.valueOf(K), Float.valueOf(V)));
	}
	
	@Override
	public CharSet keySet();
	@Override
	public FloatCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Character, Float>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> char2FloatEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Char2FloatMaps#synchronize
	 */
	public default Char2FloatMap synchronize() { return Char2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Char2FloatMaps#synchronize
	 */
	public default Char2FloatMap synchronize(Object mutex) { return Char2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Char2FloatMaps#unmodifiable
	 */
	public default Char2FloatMap unmodifiable() { return Char2FloatMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Float put(Character key, Float value) {
		return Float.valueOf(put(key.charValue(), value.floatValue()));
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Character key, Float value) {
		return Float.valueOf(put(key.charValue(), value.floatValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Char2FloatMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Char2FloatMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Char2FloatMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Character, Float>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public char getCharKey();
		public default Character getKey() { return Character.valueOf(getCharKey()); }
		
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
		public BuilderCache put(char key, float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Character key, Float value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2FloatOpenHashMap map() {
			return new Char2FloatOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2FloatOpenHashMap map(int size) {
			return new Char2FloatOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2FloatOpenHashMap map(char[] keys, float[] values) {
			return new Char2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2FloatOpenHashMap map(Character[] keys, Float[] values) {
			return new Char2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2FloatOpenHashMap map(Char2FloatMap map) {
			return new Char2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatOpenHashMap map(Map<? extends Character, ? extends Float> map) {
			return new Char2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Char2FloatLinkedOpenHashMap linkedMap() {
			return new Char2FloatLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Char2FloatLinkedOpenHashMap linkedMap(int size) {
			return new Char2FloatLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Char2FloatLinkedOpenHashMap linkedMap(char[] keys, float[] values) {
			return new Char2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2FloatLinkedOpenHashMap linkedMap(Character[] keys, Float[] values) {
			return new Char2FloatLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2FloatLinkedOpenHashMap linkedMap(Char2FloatMap map) {
			return new Char2FloatLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2FloatOpenHashMap linkedMap(Map<? extends Character, ? extends Float> map) {
			return new ImmutableChar2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableChar2FloatOpenHashMap immutable(char[] keys, float[] values) {
			return new ImmutableChar2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableChar2FloatOpenHashMap immutable(Character[] keys, Float[] values) {
			return new ImmutableChar2FloatOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableChar2FloatOpenHashMap immutable(Char2FloatMap map) {
			return new ImmutableChar2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2FloatOpenHashMap immutable(Map<? extends Character, ? extends Float> map) {
			return new ImmutableChar2FloatOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Char2FloatOpenCustomHashMap customMap(CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Char2FloatOpenCustomHashMap customMap(int size, CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Char2FloatOpenCustomHashMap customMap(char[] keys, float[] values, CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(keys, values, strategy);
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
		public Char2FloatOpenCustomHashMap customMap(Character[] keys, Float[] values, CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Char2FloatOpenCustomHashMap customMap(Char2FloatMap map, CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatOpenCustomHashMap customMap(Map<? extends Character, ? extends Float> map, CharStrategy strategy) {
			return new Char2FloatOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(int size, CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(char[] keys, float[] values, CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(Character[] keys, Float[] values, CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(Char2FloatMap map, CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(Map<? extends Character, ? extends Float> map, CharStrategy strategy) {
			return new Char2FloatLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2FloatArrayMap arrayMap() {
			return new Char2FloatArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2FloatArrayMap arrayMap(int size) {
			return new Char2FloatArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2FloatArrayMap arrayMap(char[] keys, float[] values) {
			return new Char2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2FloatArrayMap arrayMap(Character[] keys, Float[] values) {
			return new Char2FloatArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2FloatArrayMap arrayMap(Char2FloatMap map) {
			return new Char2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatArrayMap arrayMap(Map<? extends Character, ? extends Float> map) {
			return new Char2FloatArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Char2FloatRBTreeMap rbTreeMap() {
			return new Char2FloatRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Char2FloatRBTreeMap rbTreeMap(CharComparator comp) {
			return new Char2FloatRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Char2FloatRBTreeMap rbTreeMap(char[] keys, float[] values, CharComparator comp) {
			return new Char2FloatRBTreeMap(keys, values, comp);
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
		public Char2FloatRBTreeMap rbTreeMap(Character[] keys, Float[] values, CharComparator comp) {
			return new Char2FloatRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Char2FloatRBTreeMap rbTreeMap(Char2FloatMap map, CharComparator comp) {
			return new Char2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatRBTreeMap rbTreeMap(Map<? extends Character, ? extends Float> map, CharComparator comp) {
			return new Char2FloatRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Char2FloatAVLTreeMap avlTreeMap() {
			return new Char2FloatAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Char2FloatAVLTreeMap avlTreeMap(CharComparator comp) {
			return new Char2FloatAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Char2FloatAVLTreeMap avlTreeMap(char[] keys, float[] values, CharComparator comp) {
			return new Char2FloatAVLTreeMap(keys, values, comp);
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
		public Char2FloatAVLTreeMap avlTreeMap(Character[] keys, Float[] values, CharComparator comp) {
			return new Char2FloatAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Char2FloatAVLTreeMap avlTreeMap(Char2FloatMap map, CharComparator comp) {
			return new Char2FloatAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2FloatAVLTreeMap avlTreeMap(Map<? extends Character, ? extends Float> map, CharComparator comp) {
			return new Char2FloatAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		char[] keys;
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
			keys = new char[initialSize];
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
		public BuilderCache put(char key, float value) {
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
		public BuilderCache put(Character key, Float value) {
			return put(key.charValue(), value.floatValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Char2FloatMap.Entry entry) {
			return put(entry.getCharKey(), entry.getFloatValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Char2FloatMap map) {
			return putAll(Char2FloatMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Character, ? extends Float> map) {
			for(Map.Entry<? extends Character, ? extends Float> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Char2FloatMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Char2FloatMap.Entry>)c).size());
			
			for(Char2FloatMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Char2FloatMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Char2FloatOpenHashMap
		 */
		public Char2FloatOpenHashMap map() {
			return putElements(new Char2FloatOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Char2FloatLinkedOpenHashMap
		 */
		public Char2FloatLinkedOpenHashMap linkedMap() {
			return putElements(new Char2FloatLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableChar2FloatOpenHashMap
		 */
		public ImmutableChar2FloatOpenHashMap immutable() {
			return new ImmutableChar2FloatOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2FloatOpenCustomHashMap
		 */
		public Char2FloatOpenCustomHashMap customMap(CharStrategy strategy) {
			return putElements(new Char2FloatOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2FloatLinkedOpenCustomHashMap
		 */
		public Char2FloatLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return putElements(new Char2FloatLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Char2FloatConcurrentOpenHashMap
		 */
		public Char2FloatConcurrentOpenHashMap concurrentMap() {
			return putElements(new Char2FloatConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Char2FloatArrayMap
		 */
		public Char2FloatArrayMap arrayMap() {
			return new Char2FloatArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Char2FloatRBTreeMap
		 */
		public Char2FloatRBTreeMap rbTreeMap() {
			return putElements(new Char2FloatRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2FloatRBTreeMap
		 */
		public Char2FloatRBTreeMap rbTreeMap(CharComparator comp) {
			return putElements(new Char2FloatRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Char2FloatAVLTreeMap
		 */
		public Char2FloatAVLTreeMap avlTreeMap() {
			return putElements(new Char2FloatAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2FloatAVLTreeMap
		 */
		public Char2FloatAVLTreeMap avlTreeMap(CharComparator comp) {
			return putElements(new Char2FloatAVLTreeMap(comp));
		}
	}
}