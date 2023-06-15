package speiger.src.collections.chars.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.chars.functions.consumer.CharShortConsumer;
import speiger.src.collections.chars.functions.function.Char2ShortFunction;
import speiger.src.collections.chars.functions.function.CharShortUnaryOperator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.maps.impl.customHash.Char2ShortLinkedOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.customHash.Char2ShortOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ShortLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ShortOpenHashMap;
import speiger.src.collections.chars.maps.impl.immutable.ImmutableChar2ShortOpenHashMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ShortAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ShortRBTreeMap;
import speiger.src.collections.chars.maps.impl.misc.Char2ShortArrayMap;
import speiger.src.collections.chars.maps.impl.concurrent.Char2ShortConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.collections.chars.utils.maps.Char2ShortMaps;
import speiger.src.collections.chars.sets.CharSet;
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
public interface Char2ShortMap extends Map<Character, Short>, Char2ShortFunction
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
	public Char2ShortMap setDefaultReturnValue(short v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Char2ShortMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public short put(char key, short value);
	
	/**
	 * A Helper method that allows to put int a Char2ShortMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default short put(Entry entry) {
		return put(entry.getCharKey(), entry.getShortValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Short put(Map.Entry<Character, Short> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(char[] keys, short[] values) {
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
	public void putAll(char[] keys, short[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Character[] keys, Short[] values) {
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
	public void putAll(Character[] keys, Short[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public short putIfAbsent(char key, short value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Char2ShortMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public short addTo(char key, short value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Char2ShortMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public short subFrom(char key, short value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Char2ShortMap m);
	
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
	public short remove(char key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Character but just have to support equals with Character.
	 */
	@Override
	public default Short remove(Object key) {
		return key instanceof Character ? Short.valueOf(remove(((Character)key).charValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(char key, short value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Character && value instanceof Short && remove(((Character)key).charValue(), ((Short)value).shortValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public short removeOrDefault(char key, short defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(char key, short oldValue, short newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public short replace(char key, short value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceShorts(Char2ShortMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceShorts(CharShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShort(char key, CharShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsent(char key, ShortSupplier valueProvider);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction);
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
	public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Short oldValue, Short newValue) {
		return replace(key.charValue(), oldValue.shortValue(), newValue.shortValue());
	}
	
	@Override
	@Deprecated
	public default Short replace(Character key, Short value) {
		return Short.valueOf(replace(key.charValue(), value.shortValue()));
	}
	
	@Override
	public default short applyAsShort(char key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public short get(char key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public short getOrDefault(char key, short defaultValue);
	
	@Override
	@Deprecated
	public default Short get(Object key) {
		return Short.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Short getOrDefault(Object key, Short defaultValue) {
		Short value = Short.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceShorts(mappingFunction instanceof CharShortUnaryOperator ? (CharShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Short.valueOf(V)).shortValue());
	}
	
	@Override
	@Deprecated
	public default Short compute(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShort(key.charValue(), mappingFunction instanceof CharShortUnaryOperator ? (CharShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfAbsent(Character key, Function<? super Character, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfAbsent(key.charValue(), mappingFunction instanceof Char2ShortFunction ? (Char2ShortFunction)mappingFunction : K -> mappingFunction.apply(Character.valueOf(K)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short computeIfPresent(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(computeShortIfPresent(key.charValue(), mappingFunction instanceof CharShortUnaryOperator ? (CharShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short merge(Character key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Short.valueOf(mergeShort(key.charValue(), value.shortValue(), mappingFunction instanceof ShortShortUnaryOperator ? (ShortShortUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Short.valueOf(V)).shortValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(CharShortConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Short> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof CharShortConsumer ? (CharShortConsumer)action : (K, V) -> action.accept(Character.valueOf(K), Short.valueOf(V)));
	}
	
	@Override
	public CharSet keySet();
	@Override
	public ShortCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Character, Short>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> char2ShortEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Char2ShortMaps#synchronize
	 */
	public default Char2ShortMap synchronize() { return Char2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Char2ShortMaps#synchronize
	 */
	public default Char2ShortMap synchronize(Object mutex) { return Char2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Char2ShortMaps#unmodifiable
	 */
	public default Char2ShortMap unmodifiable() { return Char2ShortMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Short put(Character key, Short value) {
		return Short.valueOf(put(key.charValue(), value.shortValue()));
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Character key, Short value) {
		return Short.valueOf(put(key.charValue(), value.shortValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Char2ShortMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Char2ShortMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Char2ShortMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Character, Short>
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
		public BuilderCache put(char key, short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Character key, Short value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2ShortOpenHashMap map() {
			return new Char2ShortOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2ShortOpenHashMap map(int size) {
			return new Char2ShortOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2ShortOpenHashMap map(char[] keys, short[] values) {
			return new Char2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2ShortOpenHashMap map(Character[] keys, Short[] values) {
			return new Char2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2ShortOpenHashMap map(Char2ShortMap map) {
			return new Char2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortOpenHashMap map(Map<? extends Character, ? extends Short> map) {
			return new Char2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Char2ShortLinkedOpenHashMap linkedMap() {
			return new Char2ShortLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Char2ShortLinkedOpenHashMap linkedMap(int size) {
			return new Char2ShortLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Char2ShortLinkedOpenHashMap linkedMap(char[] keys, short[] values) {
			return new Char2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2ShortLinkedOpenHashMap linkedMap(Character[] keys, Short[] values) {
			return new Char2ShortLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2ShortLinkedOpenHashMap linkedMap(Char2ShortMap map) {
			return new Char2ShortLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2ShortOpenHashMap linkedMap(Map<? extends Character, ? extends Short> map) {
			return new ImmutableChar2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableChar2ShortOpenHashMap immutable(char[] keys, short[] values) {
			return new ImmutableChar2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableChar2ShortOpenHashMap immutable(Character[] keys, Short[] values) {
			return new ImmutableChar2ShortOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableChar2ShortOpenHashMap immutable(Char2ShortMap map) {
			return new ImmutableChar2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2ShortOpenHashMap immutable(Map<? extends Character, ? extends Short> map) {
			return new ImmutableChar2ShortOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Char2ShortOpenCustomHashMap customMap(CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Char2ShortOpenCustomHashMap customMap(int size, CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Char2ShortOpenCustomHashMap customMap(char[] keys, short[] values, CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(keys, values, strategy);
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
		public Char2ShortOpenCustomHashMap customMap(Character[] keys, Short[] values, CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Char2ShortOpenCustomHashMap customMap(Char2ShortMap map, CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortOpenCustomHashMap customMap(Map<? extends Character, ? extends Short> map, CharStrategy strategy) {
			return new Char2ShortOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(int size, CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(char[] keys, short[] values, CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(Character[] keys, Short[] values, CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(Char2ShortMap map, CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(Map<? extends Character, ? extends Short> map, CharStrategy strategy) {
			return new Char2ShortLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2ShortArrayMap arrayMap() {
			return new Char2ShortArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2ShortArrayMap arrayMap(int size) {
			return new Char2ShortArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2ShortArrayMap arrayMap(char[] keys, short[] values) {
			return new Char2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2ShortArrayMap arrayMap(Character[] keys, Short[] values) {
			return new Char2ShortArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2ShortArrayMap arrayMap(Char2ShortMap map) {
			return new Char2ShortArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortArrayMap arrayMap(Map<? extends Character, ? extends Short> map) {
			return new Char2ShortArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Char2ShortRBTreeMap rbTreeMap() {
			return new Char2ShortRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Char2ShortRBTreeMap rbTreeMap(CharComparator comp) {
			return new Char2ShortRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Char2ShortRBTreeMap rbTreeMap(char[] keys, short[] values, CharComparator comp) {
			return new Char2ShortRBTreeMap(keys, values, comp);
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
		public Char2ShortRBTreeMap rbTreeMap(Character[] keys, Short[] values, CharComparator comp) {
			return new Char2ShortRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Char2ShortRBTreeMap rbTreeMap(Char2ShortMap map, CharComparator comp) {
			return new Char2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortRBTreeMap rbTreeMap(Map<? extends Character, ? extends Short> map, CharComparator comp) {
			return new Char2ShortRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Char2ShortAVLTreeMap avlTreeMap() {
			return new Char2ShortAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Char2ShortAVLTreeMap avlTreeMap(CharComparator comp) {
			return new Char2ShortAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Char2ShortAVLTreeMap avlTreeMap(char[] keys, short[] values, CharComparator comp) {
			return new Char2ShortAVLTreeMap(keys, values, comp);
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
		public Char2ShortAVLTreeMap avlTreeMap(Character[] keys, Short[] values, CharComparator comp) {
			return new Char2ShortAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Char2ShortAVLTreeMap avlTreeMap(Char2ShortMap map, CharComparator comp) {
			return new Char2ShortAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2ShortAVLTreeMap avlTreeMap(Map<? extends Character, ? extends Short> map, CharComparator comp) {
			return new Char2ShortAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		char[] keys;
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
			keys = new char[initialSize];
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
		public BuilderCache put(char key, short value) {
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
		public BuilderCache put(Character key, Short value) {
			return put(key.charValue(), value.shortValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Char2ShortMap.Entry entry) {
			return put(entry.getCharKey(), entry.getShortValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Char2ShortMap map) {
			return putAll(Char2ShortMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Character, ? extends Short> map) {
			for(Map.Entry<? extends Character, ? extends Short> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Char2ShortMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Char2ShortMap.Entry>)c).size());
			
			for(Char2ShortMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Char2ShortMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Char2ShortOpenHashMap
		 */
		public Char2ShortOpenHashMap map() {
			return putElements(new Char2ShortOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Char2ShortLinkedOpenHashMap
		 */
		public Char2ShortLinkedOpenHashMap linkedMap() {
			return putElements(new Char2ShortLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableChar2ShortOpenHashMap
		 */
		public ImmutableChar2ShortOpenHashMap immutable() {
			return new ImmutableChar2ShortOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2ShortOpenCustomHashMap
		 */
		public Char2ShortOpenCustomHashMap customMap(CharStrategy strategy) {
			return putElements(new Char2ShortOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2ShortLinkedOpenCustomHashMap
		 */
		public Char2ShortLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return putElements(new Char2ShortLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Char2ShortConcurrentOpenHashMap
		 */
		public Char2ShortConcurrentOpenHashMap concurrentMap() {
			return putElements(new Char2ShortConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Char2ShortArrayMap
		 */
		public Char2ShortArrayMap arrayMap() {
			return new Char2ShortArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Char2ShortRBTreeMap
		 */
		public Char2ShortRBTreeMap rbTreeMap() {
			return putElements(new Char2ShortRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2ShortRBTreeMap
		 */
		public Char2ShortRBTreeMap rbTreeMap(CharComparator comp) {
			return putElements(new Char2ShortRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Char2ShortAVLTreeMap
		 */
		public Char2ShortAVLTreeMap avlTreeMap() {
			return putElements(new Char2ShortAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2ShortAVLTreeMap
		 */
		public Char2ShortAVLTreeMap avlTreeMap(CharComparator comp) {
			return putElements(new Char2ShortAVLTreeMap(comp));
		}
	}
}