package speiger.src.collections.chars.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.chars.functions.consumer.CharIntConsumer;
import speiger.src.collections.chars.functions.function.Char2IntFunction;
import speiger.src.collections.chars.functions.function.CharIntUnaryOperator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.maps.impl.customHash.Char2IntLinkedOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.customHash.Char2IntOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2IntLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2IntOpenHashMap;
import speiger.src.collections.chars.maps.impl.immutable.ImmutableChar2IntOpenHashMap;
import speiger.src.collections.chars.maps.impl.tree.Char2IntAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2IntRBTreeMap;
import speiger.src.collections.chars.maps.impl.misc.Char2IntArrayMap;
import speiger.src.collections.chars.maps.impl.concurrent.Char2IntConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.collections.chars.utils.maps.Char2IntMaps;
import speiger.src.collections.chars.sets.CharSet;
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
public interface Char2IntMap extends Map<Character, Integer>, Char2IntFunction
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
	public Char2IntMap setDefaultReturnValue(int v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Char2IntMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public int put(char key, int value);
	
	/**
	 * A Helper method that allows to put int a Char2IntMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default int put(Entry entry) {
		return put(entry.getCharKey(), entry.getIntValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Integer put(Map.Entry<Character, Integer> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(char[] keys, int[] values) {
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
	public void putAll(char[] keys, int[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Character[] keys, Integer[] values) {
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
	public void putAll(Character[] keys, Integer[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public int putIfAbsent(char key, int value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Char2IntMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public int addTo(char key, int value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Char2IntMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public int subFrom(char key, int value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Char2IntMap m);
	
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
	public int remove(char key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Character but just have to support equals with Character.
	 */
	@Override
	public default Integer remove(Object key) {
		return key instanceof Character ? Integer.valueOf(remove(((Character)key).charValue())) : Integer.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(char key, int value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Character && value instanceof Integer && remove(((Character)key).charValue(), ((Integer)value).intValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public int removeOrDefault(char key, int defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(char key, int oldValue, int newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public int replace(char key, int value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceInts(Char2IntMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceInts(CharIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public int computeInt(char key, CharIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public int computeIntIfAbsent(char key, Char2IntFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public int supplyIntIfAbsent(char key, IntSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public int computeIntIfPresent(char key, CharIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public int computeIntNonDefault(char key, CharIntUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public int computeIntIfAbsentNonDefault(char key, Char2IntFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public int supplyIntIfAbsentNonDefault(char key, IntSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public int computeIntIfPresentNonDefault(char key, CharIntUnaryOperator mappingFunction);
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
	public int mergeInt(char key, int value, IntIntUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllInt(Char2IntMap m, IntIntUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Integer oldValue, Integer newValue) {
		return replace(key.charValue(), oldValue.intValue(), newValue.intValue());
	}
	
	@Override
	@Deprecated
	public default Integer replace(Character key, Integer value) {
		return Integer.valueOf(replace(key.charValue(), value.intValue()));
	}
	
	@Override
	public default int applyAsInt(char key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public int get(char key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public int getOrDefault(char key, int defaultValue);
	
	@Override
	@Deprecated
	public default Integer get(Object key) {
		return Integer.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		Integer value = Integer.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceInts(mappingFunction instanceof CharIntUnaryOperator ? (CharIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Integer.valueOf(V)).intValue());
	}
	
	@Override
	@Deprecated
	public default Integer compute(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeInt(key.charValue(), mappingFunction instanceof CharIntUnaryOperator ? (CharIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer computeIfAbsent(Character key, Function<? super Character, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfAbsent(key.charValue(), mappingFunction instanceof Char2IntFunction ? (Char2IntFunction)mappingFunction : K -> mappingFunction.apply(Character.valueOf(K)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer computeIfPresent(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfPresent(key.charValue(), mappingFunction instanceof CharIntUnaryOperator ? (CharIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer merge(Character key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(mergeInt(key.charValue(), value.intValue(), mappingFunction instanceof IntIntUnaryOperator ? (IntIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(CharIntConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Integer> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof CharIntConsumer ? (CharIntConsumer)action : (K, V) -> action.accept(Character.valueOf(K), Integer.valueOf(V)));
	}
	
	@Override
	public CharSet keySet();
	@Override
	public IntCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Character, Integer>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> char2IntEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Char2IntMaps#synchronize
	 */
	public default Char2IntMap synchronize() { return Char2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Char2IntMaps#synchronize
	 */
	public default Char2IntMap synchronize(Object mutex) { return Char2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Char2IntMaps#unmodifiable
	 */
	public default Char2IntMap unmodifiable() { return Char2IntMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Integer put(Character key, Integer value) {
		return Integer.valueOf(put(key.charValue(), value.intValue()));
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Character key, Integer value) {
		return Integer.valueOf(put(key.charValue(), value.intValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Char2IntMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Char2IntMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Char2IntMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Character, Integer>
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
		public BuilderCache put(char key, int value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Character key, Integer value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2IntOpenHashMap map() {
			return new Char2IntOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2IntOpenHashMap map(int size) {
			return new Char2IntOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2IntOpenHashMap map(char[] keys, int[] values) {
			return new Char2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2IntOpenHashMap map(Character[] keys, Integer[] values) {
			return new Char2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2IntOpenHashMap map(Char2IntMap map) {
			return new Char2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntOpenHashMap map(Map<? extends Character, ? extends Integer> map) {
			return new Char2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Char2IntLinkedOpenHashMap linkedMap() {
			return new Char2IntLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Char2IntLinkedOpenHashMap linkedMap(int size) {
			return new Char2IntLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Char2IntLinkedOpenHashMap linkedMap(char[] keys, int[] values) {
			return new Char2IntLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2IntLinkedOpenHashMap linkedMap(Character[] keys, Integer[] values) {
			return new Char2IntLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2IntLinkedOpenHashMap linkedMap(Char2IntMap map) {
			return new Char2IntLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2IntOpenHashMap linkedMap(Map<? extends Character, ? extends Integer> map) {
			return new ImmutableChar2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableChar2IntOpenHashMap immutable(char[] keys, int[] values) {
			return new ImmutableChar2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableChar2IntOpenHashMap immutable(Character[] keys, Integer[] values) {
			return new ImmutableChar2IntOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableChar2IntOpenHashMap immutable(Char2IntMap map) {
			return new ImmutableChar2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableChar2IntOpenHashMap immutable(Map<? extends Character, ? extends Integer> map) {
			return new ImmutableChar2IntOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Char2IntOpenCustomHashMap customMap(CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Char2IntOpenCustomHashMap customMap(int size, CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Char2IntOpenCustomHashMap customMap(char[] keys, int[] values, CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(keys, values, strategy);
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
		public Char2IntOpenCustomHashMap customMap(Character[] keys, Integer[] values, CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Char2IntOpenCustomHashMap customMap(Char2IntMap map, CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntOpenCustomHashMap customMap(Map<? extends Character, ? extends Integer> map, CharStrategy strategy) {
			return new Char2IntOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(int size, CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(char[] keys, int[] values, CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(Character[] keys, Integer[] values, CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(Char2IntMap map, CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(Map<? extends Character, ? extends Integer> map, CharStrategy strategy) {
			return new Char2IntLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Char2IntArrayMap arrayMap() {
			return new Char2IntArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Char2IntArrayMap arrayMap(int size) {
			return new Char2IntArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Char2IntArrayMap arrayMap(char[] keys, int[] values) {
			return new Char2IntArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Char2IntArrayMap arrayMap(Character[] keys, Integer[] values) {
			return new Char2IntArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Char2IntArrayMap arrayMap(Char2IntMap map) {
			return new Char2IntArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntArrayMap arrayMap(Map<? extends Character, ? extends Integer> map) {
			return new Char2IntArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Char2IntRBTreeMap rbTreeMap() {
			return new Char2IntRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Char2IntRBTreeMap rbTreeMap(CharComparator comp) {
			return new Char2IntRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Char2IntRBTreeMap rbTreeMap(char[] keys, int[] values, CharComparator comp) {
			return new Char2IntRBTreeMap(keys, values, comp);
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
		public Char2IntRBTreeMap rbTreeMap(Character[] keys, Integer[] values, CharComparator comp) {
			return new Char2IntRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Char2IntRBTreeMap rbTreeMap(Char2IntMap map, CharComparator comp) {
			return new Char2IntRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntRBTreeMap rbTreeMap(Map<? extends Character, ? extends Integer> map, CharComparator comp) {
			return new Char2IntRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Char2IntAVLTreeMap avlTreeMap() {
			return new Char2IntAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Char2IntAVLTreeMap avlTreeMap(CharComparator comp) {
			return new Char2IntAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Char2IntAVLTreeMap avlTreeMap(char[] keys, int[] values, CharComparator comp) {
			return new Char2IntAVLTreeMap(keys, values, comp);
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
		public Char2IntAVLTreeMap avlTreeMap(Character[] keys, Integer[] values, CharComparator comp) {
			return new Char2IntAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Char2IntAVLTreeMap avlTreeMap(Char2IntMap map, CharComparator comp) {
			return new Char2IntAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Char2IntAVLTreeMap avlTreeMap(Map<? extends Character, ? extends Integer> map, CharComparator comp) {
			return new Char2IntAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		char[] keys;
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
			keys = new char[initialSize];
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
		public BuilderCache put(char key, int value) {
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
		public BuilderCache put(Character key, Integer value) {
			return put(key.charValue(), value.intValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Char2IntMap.Entry entry) {
			return put(entry.getCharKey(), entry.getIntValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Char2IntMap map) {
			return putAll(Char2IntMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Character, ? extends Integer> map) {
			for(Map.Entry<? extends Character, ? extends Integer> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Char2IntMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Char2IntMap.Entry>)c).size());
			
			for(Char2IntMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Char2IntMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Char2IntOpenHashMap
		 */
		public Char2IntOpenHashMap map() {
			return putElements(new Char2IntOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Char2IntLinkedOpenHashMap
		 */
		public Char2IntLinkedOpenHashMap linkedMap() {
			return putElements(new Char2IntLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableChar2IntOpenHashMap
		 */
		public ImmutableChar2IntOpenHashMap immutable() {
			return new ImmutableChar2IntOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2IntOpenCustomHashMap
		 */
		public Char2IntOpenCustomHashMap customMap(CharStrategy strategy) {
			return putElements(new Char2IntOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Char2IntLinkedOpenCustomHashMap
		 */
		public Char2IntLinkedOpenCustomHashMap customLinkedMap(CharStrategy strategy) {
			return putElements(new Char2IntLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Char2IntConcurrentOpenHashMap
		 */
		public Char2IntConcurrentOpenHashMap concurrentMap() {
			return putElements(new Char2IntConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Char2IntArrayMap
		 */
		public Char2IntArrayMap arrayMap() {
			return new Char2IntArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Char2IntRBTreeMap
		 */
		public Char2IntRBTreeMap rbTreeMap() {
			return putElements(new Char2IntRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2IntRBTreeMap
		 */
		public Char2IntRBTreeMap rbTreeMap(CharComparator comp) {
			return putElements(new Char2IntRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Char2IntAVLTreeMap
		 */
		public Char2IntAVLTreeMap avlTreeMap() {
			return putElements(new Char2IntAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Char2IntAVLTreeMap
		 */
		public Char2IntAVLTreeMap avlTreeMap(CharComparator comp) {
			return putElements(new Char2IntAVLTreeMap(comp));
		}
	}
}