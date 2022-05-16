package speiger.src.collections.doubles.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.doubles.functions.consumer.DoubleCharConsumer;
import speiger.src.collections.doubles.functions.function.Double2CharFunction;
import speiger.src.collections.doubles.functions.function.DoubleCharUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.maps.impl.customHash.Double2CharLinkedOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.customHash.Double2CharOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2CharLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2CharOpenHashMap;
import speiger.src.collections.doubles.maps.impl.immutable.ImmutableDouble2CharOpenHashMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2CharAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2CharRBTreeMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2CharArrayMap;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2CharConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.collections.doubles.utils.maps.Double2CharMaps;
import speiger.src.collections.doubles.sets.DoubleSet;
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
public interface Double2CharMap extends Map<Double, Character>, Double2CharFunction
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
	public Double2CharMap setDefaultReturnValue(char v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Double2CharMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public char put(double key, char value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(double[] keys, char[] values) {
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
	public void putAll(double[] keys, char[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Double[] keys, Character[] values) {
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
	public void putAll(Double[] keys, Character[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public char putIfAbsent(double key, char value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Double2CharMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public char addTo(double key, char value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Double2CharMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public char subFrom(double key, char value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Double2CharMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(double key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Double && containsKey(((Double)key).doubleValue());
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
	public char remove(double key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default Character remove(Object key) {
		return key instanceof Double ? Character.valueOf(remove(((Double)key).doubleValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(double key, char value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Double && value instanceof Character && remove(((Double)key).doubleValue(), ((Character)value).charValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public char removeOrDefault(double key, char defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(double key, char oldValue, char newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public char replace(double key, char value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceChars(Double2CharMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceChars(DoubleCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public char computeChar(double key, DoubleCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public char supplyCharIfAbsent(double key, CharSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Character oldValue, Character newValue) {
		return replace(key.doubleValue(), oldValue.charValue(), newValue.charValue());
	}
	
	@Override
	@Deprecated
	public default Character replace(Double key, Character value) {
		return Character.valueOf(replace(key.doubleValue(), value.charValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public char get(double key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public char getOrDefault(double key, char defaultValue);
	
	@Override
	@Deprecated
	public default Character get(Object key) {
		return Character.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Character getOrDefault(Object key, Character defaultValue) {
		Character value = Character.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceChars(mappingFunction instanceof DoubleCharUnaryOperator ? (DoubleCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Character.valueOf(V)).charValue());
	}
	
	@Override
	@Deprecated
	public default Character compute(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeChar(key.doubleValue(), mappingFunction instanceof DoubleCharUnaryOperator ? (DoubleCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfAbsent(Double key, Function<? super Double, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfAbsent(key.doubleValue(), mappingFunction instanceof Double2CharFunction ? (Double2CharFunction)mappingFunction : K -> mappingFunction.apply(Double.valueOf(K)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfPresent(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfPresent(key.doubleValue(), mappingFunction instanceof DoubleCharUnaryOperator ? (DoubleCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character merge(Double key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(mergeChar(key.doubleValue(), value.charValue(), mappingFunction instanceof CharCharUnaryOperator ? (CharCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(DoubleCharConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Character> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof DoubleCharConsumer ? (DoubleCharConsumer)action : (K, V) -> action.accept(Double.valueOf(K), Character.valueOf(V)));
	}
	
	@Override
	public DoubleSet keySet();
	@Override
	public CharCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Double, Character>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> double2CharEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	public default Double2CharMap synchronize() { return Double2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	public default Double2CharMap synchronize(Object mutex) { return Double2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Double2CharMaps#unmodifiable
	 */
	public default Double2CharMap unmodifiable() { return Double2CharMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Character put(Double key, Character value) {
		return Character.valueOf(put(key.doubleValue(), value.charValue()));
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Double key, Character value) {
		return Character.valueOf(put(key.doubleValue(), value.charValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Double2CharMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Double2CharMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Double2CharMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Double, Character>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public double getDoubleKey();
		public default Double getKey() { return Double.valueOf(getDoubleKey()); }
		
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
		public BuilderCache put(double key, char value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Double key, Character value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2CharOpenHashMap map() {
			return new Double2CharOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2CharOpenHashMap map(int size) {
			return new Double2CharOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2CharOpenHashMap map(double[] keys, char[] values) {
			return new Double2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2CharOpenHashMap map(Double[] keys, Character[] values) {
			return new Double2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2CharOpenHashMap map(Double2CharMap map) {
			return new Double2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharOpenHashMap map(Map<? extends Double, ? extends Character> map) {
			return new Double2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Double2CharLinkedOpenHashMap linkedMap() {
			return new Double2CharLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Double2CharLinkedOpenHashMap linkedMap(int size) {
			return new Double2CharLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Double2CharLinkedOpenHashMap linkedMap(double[] keys, char[] values) {
			return new Double2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2CharLinkedOpenHashMap linkedMap(Double[] keys, Character[] values) {
			return new Double2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2CharLinkedOpenHashMap linkedMap(Double2CharMap map) {
			return new Double2CharLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2CharOpenHashMap linkedMap(Map<? extends Double, ? extends Character> map) {
			return new ImmutableDouble2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableDouble2CharOpenHashMap immutable(double[] keys, char[] values) {
			return new ImmutableDouble2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableDouble2CharOpenHashMap immutable(Double[] keys, Character[] values) {
			return new ImmutableDouble2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableDouble2CharOpenHashMap immutable(Double2CharMap map) {
			return new ImmutableDouble2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableDouble2CharOpenHashMap immutable(Map<? extends Double, ? extends Character> map) {
			return new ImmutableDouble2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Double2CharOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Double2CharOpenCustomHashMap customMap(int size, DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Double2CharOpenCustomHashMap customMap(double[] keys, char[] values, DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(keys, values, strategy);
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
		public Double2CharOpenCustomHashMap customMap(Double[] keys, Character[] values, DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Double2CharOpenCustomHashMap customMap(Double2CharMap map, DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharOpenCustomHashMap customMap(Map<? extends Double, ? extends Character> map, DoubleStrategy strategy) {
			return new Double2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(int size, DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(double[] keys, char[] values, DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(Double[] keys, Character[] values, DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(Double2CharMap map, DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(Map<? extends Double, ? extends Character> map, DoubleStrategy strategy) {
			return new Double2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Double2CharArrayMap arrayMap() {
			return new Double2CharArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Double2CharArrayMap arrayMap(int size) {
			return new Double2CharArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Double2CharArrayMap arrayMap(double[] keys, char[] values) {
			return new Double2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Double2CharArrayMap arrayMap(Double[] keys, Character[] values) {
			return new Double2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Double2CharArrayMap arrayMap(Double2CharMap map) {
			return new Double2CharArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharArrayMap arrayMap(Map<? extends Double, ? extends Character> map) {
			return new Double2CharArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Double2CharRBTreeMap rbTreeMap() {
			return new Double2CharRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Double2CharRBTreeMap rbTreeMap(DoubleComparator comp) {
			return new Double2CharRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Double2CharRBTreeMap rbTreeMap(double[] keys, char[] values, DoubleComparator comp) {
			return new Double2CharRBTreeMap(keys, values, comp);
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
		public Double2CharRBTreeMap rbTreeMap(Double[] keys, Character[] values, DoubleComparator comp) {
			return new Double2CharRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Double2CharRBTreeMap rbTreeMap(Double2CharMap map, DoubleComparator comp) {
			return new Double2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharRBTreeMap rbTreeMap(Map<? extends Double, ? extends Character> map, DoubleComparator comp) {
			return new Double2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Double2CharAVLTreeMap avlTreeMap() {
			return new Double2CharAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Double2CharAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return new Double2CharAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Double2CharAVLTreeMap avlTreeMap(double[] keys, char[] values, DoubleComparator comp) {
			return new Double2CharAVLTreeMap(keys, values, comp);
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
		public Double2CharAVLTreeMap avlTreeMap(Double[] keys, Character[] values, DoubleComparator comp) {
			return new Double2CharAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Double2CharAVLTreeMap avlTreeMap(Double2CharMap map, DoubleComparator comp) {
			return new Double2CharAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Double2CharAVLTreeMap avlTreeMap(Map<? extends Double, ? extends Character> map, DoubleComparator comp) {
			return new Double2CharAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		double[] keys;
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
			keys = new double[initialSize];
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
		public BuilderCache put(double key, char value) {
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
		public BuilderCache put(Double key, Character value) {
			return put(key.doubleValue(), value.charValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Double2CharMap.Entry entry) {
			return put(entry.getDoubleKey(), entry.getCharValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Double2CharMap map) {
			return putAll(Double2CharMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Double, ? extends Character> map) {
			for(Map.Entry<? extends Double, ? extends Character> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Double2CharMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Double2CharMap.Entry>)c).size());
			
			for(Double2CharMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Double2CharMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Double2CharOpenHashMap
		 */
		public Double2CharOpenHashMap map() {
			return putElements(new Double2CharOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Double2CharLinkedOpenHashMap
		 */
		public Double2CharLinkedOpenHashMap linkedMap() {
			return putElements(new Double2CharLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableDouble2CharOpenHashMap
		 */
		public ImmutableDouble2CharOpenHashMap immutable() {
			return new ImmutableDouble2CharOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2CharOpenCustomHashMap
		 */
		public Double2CharOpenCustomHashMap customMap(DoubleStrategy strategy) {
			return putElements(new Double2CharOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2CharLinkedOpenCustomHashMap
		 */
		public Double2CharLinkedOpenCustomHashMap customLinkedMap(DoubleStrategy strategy) {
			return putElements(new Double2CharLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Double2CharConcurrentOpenHashMap
		 */
		public Double2CharConcurrentOpenHashMap concurrentMap() {
			return putElements(new Double2CharConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Double2CharArrayMap
		 */
		public Double2CharArrayMap arrayMap() {
			return new Double2CharArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Double2CharRBTreeMap
		 */
		public Double2CharRBTreeMap rbTreeMap() {
			return putElements(new Double2CharRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2CharRBTreeMap
		 */
		public Double2CharRBTreeMap rbTreeMap(DoubleComparator comp) {
			return putElements(new Double2CharRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Double2CharAVLTreeMap
		 */
		public Double2CharAVLTreeMap avlTreeMap() {
			return putElements(new Double2CharAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2CharAVLTreeMap
		 */
		public Double2CharAVLTreeMap avlTreeMap(DoubleComparator comp) {
			return putElements(new Double2CharAVLTreeMap(comp));
		}
	}
}