package speiger.src.collections.shorts.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.shorts.functions.consumer.ShortCharConsumer;
import speiger.src.collections.shorts.functions.function.Short2CharFunction;
import speiger.src.collections.shorts.functions.function.ShortCharUnaryOperator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.maps.impl.customHash.Short2CharLinkedOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.customHash.Short2CharOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2CharLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2CharOpenHashMap;
import speiger.src.collections.shorts.maps.impl.immutable.ImmutableShort2CharOpenHashMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2CharAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2CharRBTreeMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2CharArrayMap;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2CharConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.collections.shorts.utils.maps.Short2CharMaps;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Short2CharMap extends Map<Short, Character>, Short2CharFunction
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
	public char getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Short2CharMap setDefaultReturnValue(char v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Short2CharMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public char put(short key, char value);
	
	/**
	 * A Helper method that allows to put int a Short2CharMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default char put(Entry entry) {
		return put(entry.getShortKey(), entry.getCharValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Character put(Map.Entry<Short, Character> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(short[] keys, char[] values) {
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
	public void putAll(short[] keys, char[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Short[] keys, Character[] values) {
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
	public void putAll(Short[] keys, Character[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public char putIfAbsent(short key, char value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Short2CharMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public char addTo(short key, char value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Short2CharMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public char subFrom(short key, char value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Short2CharMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(short key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Short && containsKey(((Short)key).shortValue());
	}
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(char value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Character && containsValue(((Character)value).charValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public char remove(short key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default Character remove(Object key) {
		return key instanceof Short ? Character.valueOf(remove(((Short)key).shortValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(short key, char value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Short && value instanceof Character && remove(((Short)key).shortValue(), ((Character)value).charValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public char removeOrDefault(short key, char defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(short key, char oldValue, char newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public char replace(short key, char value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceChars(Short2CharMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceChars(ShortCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public char computeChar(short key, ShortCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public char supplyCharIfAbsent(short key, CharSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public char computeCharNonDefault(short key, ShortCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public char computeCharIfAbsentNonDefault(short key, Short2CharFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public char supplyCharIfAbsentNonDefault(short key, CharSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public char computeCharIfPresentNonDefault(short key, ShortCharUnaryOperator mappingFunction);
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
	public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Character oldValue, Character newValue) {
		return replace(key.shortValue(), oldValue.charValue(), newValue.charValue());
	}
	
	@Override
	@Deprecated
	public default Character replace(Short key, Character value) {
		return Character.valueOf(replace(key.shortValue(), value.charValue()));
	}
	
	@Override
	public default char applyAsChar(short key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public char get(short key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public char getOrDefault(short key, char defaultValue);
	
	@Override
	@Deprecated
	public default Character get(Object key) {
		return Character.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Character getOrDefault(Object key, Character defaultValue) {
		Character value = Character.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceChars(mappingFunction instanceof ShortCharUnaryOperator ? (ShortCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Character.valueOf(V)).charValue());
	}
	
	@Override
	@Deprecated
	public default Character compute(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeChar(key.shortValue(), mappingFunction instanceof ShortCharUnaryOperator ? (ShortCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfAbsent(Short key, Function<? super Short, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfAbsent(key.shortValue(), mappingFunction instanceof Short2CharFunction ? (Short2CharFunction)mappingFunction : K -> mappingFunction.apply(Short.valueOf(K)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfPresent(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfPresent(key.shortValue(), mappingFunction instanceof ShortCharUnaryOperator ? (ShortCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character merge(Short key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(mergeChar(key.shortValue(), value.charValue(), mappingFunction instanceof CharCharUnaryOperator ? (CharCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ShortCharConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Character> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ShortCharConsumer ? (ShortCharConsumer)action : (K, V) -> action.accept(Short.valueOf(K), Character.valueOf(V)));
	}
	
	@Override
	public ShortSet keySet();
	@Override
	public CharCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Short, Character>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> short2CharEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Short2CharMaps#synchronize
	 */
	public default Short2CharMap synchronize() { return Short2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Short2CharMaps#synchronize
	 */
	public default Short2CharMap synchronize(Object mutex) { return Short2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Short2CharMaps#unmodifiable
	 */
	public default Short2CharMap unmodifiable() { return Short2CharMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Character put(Short key, Character value) {
		return Character.valueOf(put(key.shortValue(), value.charValue()));
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Short key, Character value) {
		return Character.valueOf(put(key.shortValue(), value.charValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Short2CharMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Short2CharMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Short2CharMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Short, Character>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public short getShortKey();
		public default Short getKey() { return Short.valueOf(getShortKey()); }
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public char getCharValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public char setValue(char value);
		@Override
		public default Character getValue() { return Character.valueOf(getCharValue()); }
		@Override
		public default Character setValue(Character value) { return Character.valueOf(setValue(value.charValue())); }
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
		public BuilderCache put(short key, char value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Short key, Character value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2CharOpenHashMap map() {
			return new Short2CharOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2CharOpenHashMap map(int size) {
			return new Short2CharOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2CharOpenHashMap map(short[] keys, char[] values) {
			return new Short2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2CharOpenHashMap map(Short[] keys, Character[] values) {
			return new Short2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2CharOpenHashMap map(Short2CharMap map) {
			return new Short2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharOpenHashMap map(Map<? extends Short, ? extends Character> map) {
			return new Short2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Short2CharLinkedOpenHashMap linkedMap() {
			return new Short2CharLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Short2CharLinkedOpenHashMap linkedMap(int size) {
			return new Short2CharLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Short2CharLinkedOpenHashMap linkedMap(short[] keys, char[] values) {
			return new Short2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2CharLinkedOpenHashMap linkedMap(Short[] keys, Character[] values) {
			return new Short2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2CharLinkedOpenHashMap linkedMap(Short2CharMap map) {
			return new Short2CharLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2CharOpenHashMap linkedMap(Map<? extends Short, ? extends Character> map) {
			return new ImmutableShort2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableShort2CharOpenHashMap immutable(short[] keys, char[] values) {
			return new ImmutableShort2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableShort2CharOpenHashMap immutable(Short[] keys, Character[] values) {
			return new ImmutableShort2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableShort2CharOpenHashMap immutable(Short2CharMap map) {
			return new ImmutableShort2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2CharOpenHashMap immutable(Map<? extends Short, ? extends Character> map) {
			return new ImmutableShort2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Short2CharOpenCustomHashMap customMap(ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Short2CharOpenCustomHashMap customMap(int size, ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Short2CharOpenCustomHashMap customMap(short[] keys, char[] values, ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(keys, values, strategy);
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
		public Short2CharOpenCustomHashMap customMap(Short[] keys, Character[] values, ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Short2CharOpenCustomHashMap customMap(Short2CharMap map, ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharOpenCustomHashMap customMap(Map<? extends Short, ? extends Character> map, ShortStrategy strategy) {
			return new Short2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(int size, ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(short[] keys, char[] values, ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(Short[] keys, Character[] values, ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(Short2CharMap map, ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(Map<? extends Short, ? extends Character> map, ShortStrategy strategy) {
			return new Short2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2CharArrayMap arrayMap() {
			return new Short2CharArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2CharArrayMap arrayMap(int size) {
			return new Short2CharArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2CharArrayMap arrayMap(short[] keys, char[] values) {
			return new Short2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2CharArrayMap arrayMap(Short[] keys, Character[] values) {
			return new Short2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2CharArrayMap arrayMap(Short2CharMap map) {
			return new Short2CharArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharArrayMap arrayMap(Map<? extends Short, ? extends Character> map) {
			return new Short2CharArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Short2CharRBTreeMap rbTreeMap() {
			return new Short2CharRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Short2CharRBTreeMap rbTreeMap(ShortComparator comp) {
			return new Short2CharRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Short2CharRBTreeMap rbTreeMap(short[] keys, char[] values, ShortComparator comp) {
			return new Short2CharRBTreeMap(keys, values, comp);
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
		public Short2CharRBTreeMap rbTreeMap(Short[] keys, Character[] values, ShortComparator comp) {
			return new Short2CharRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Short2CharRBTreeMap rbTreeMap(Short2CharMap map, ShortComparator comp) {
			return new Short2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharRBTreeMap rbTreeMap(Map<? extends Short, ? extends Character> map, ShortComparator comp) {
			return new Short2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Short2CharAVLTreeMap avlTreeMap() {
			return new Short2CharAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Short2CharAVLTreeMap avlTreeMap(ShortComparator comp) {
			return new Short2CharAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Short2CharAVLTreeMap avlTreeMap(short[] keys, char[] values, ShortComparator comp) {
			return new Short2CharAVLTreeMap(keys, values, comp);
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
		public Short2CharAVLTreeMap avlTreeMap(Short[] keys, Character[] values, ShortComparator comp) {
			return new Short2CharAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Short2CharAVLTreeMap avlTreeMap(Short2CharMap map, ShortComparator comp) {
			return new Short2CharAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2CharAVLTreeMap avlTreeMap(Map<? extends Short, ? extends Character> map, ShortComparator comp) {
			return new Short2CharAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		short[] keys;
		char[] values;
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
			keys = new short[initialSize];
			values = new char[initialSize];
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
		public BuilderCache put(short key, char value) {
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
		public BuilderCache put(Short key, Character value) {
			return put(key.shortValue(), value.charValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Short2CharMap.Entry entry) {
			return put(entry.getShortKey(), entry.getCharValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Short2CharMap map) {
			return putAll(Short2CharMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Short, ? extends Character> map) {
			for(Map.Entry<? extends Short, ? extends Character> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Short2CharMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Short2CharMap.Entry>)c).size());
			
			for(Short2CharMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Short2CharMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Short2CharOpenHashMap
		 */
		public Short2CharOpenHashMap map() {
			return putElements(new Short2CharOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Short2CharLinkedOpenHashMap
		 */
		public Short2CharLinkedOpenHashMap linkedMap() {
			return putElements(new Short2CharLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableShort2CharOpenHashMap
		 */
		public ImmutableShort2CharOpenHashMap immutable() {
			return new ImmutableShort2CharOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2CharOpenCustomHashMap
		 */
		public Short2CharOpenCustomHashMap customMap(ShortStrategy strategy) {
			return putElements(new Short2CharOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2CharLinkedOpenCustomHashMap
		 */
		public Short2CharLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return putElements(new Short2CharLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Short2CharConcurrentOpenHashMap
		 */
		public Short2CharConcurrentOpenHashMap concurrentMap() {
			return putElements(new Short2CharConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Short2CharArrayMap
		 */
		public Short2CharArrayMap arrayMap() {
			return new Short2CharArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Short2CharRBTreeMap
		 */
		public Short2CharRBTreeMap rbTreeMap() {
			return putElements(new Short2CharRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2CharRBTreeMap
		 */
		public Short2CharRBTreeMap rbTreeMap(ShortComparator comp) {
			return putElements(new Short2CharRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Short2CharAVLTreeMap
		 */
		public Short2CharAVLTreeMap avlTreeMap() {
			return putElements(new Short2CharAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2CharAVLTreeMap
		 */
		public Short2CharAVLTreeMap avlTreeMap(ShortComparator comp) {
			return putElements(new Short2CharAVLTreeMap(comp));
		}
	}
}