package speiger.src.collections.bytes.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.bytes.functions.consumer.ByteCharConsumer;
import speiger.src.collections.bytes.functions.function.Byte2CharFunction;
import speiger.src.collections.bytes.functions.function.ByteCharUnaryOperator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2CharLinkedOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2CharOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2CharLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2CharOpenHashMap;
import speiger.src.collections.bytes.maps.impl.immutable.ImmutableByte2CharOpenHashMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2CharAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2CharRBTreeMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2CharArrayMap;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2CharConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.bytes.utils.maps.Byte2CharMaps;
import speiger.src.collections.bytes.sets.ByteSet;
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
public interface Byte2CharMap extends Map<Byte, Character>, Byte2CharFunction
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
	public Byte2CharMap setDefaultReturnValue(char v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Byte2CharMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public char put(byte key, char value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(byte[] keys, char[] values) {
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
	public void putAll(byte[] keys, char[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Byte[] keys, Character[] values) {
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
	public void putAll(Byte[] keys, Character[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public char putIfAbsent(byte key, char value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Byte2CharMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public char addTo(byte key, char value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Byte2CharMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public char subFrom(byte key, char value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Byte2CharMap m);
	
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
	public char remove(byte key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Byte but just have to support equals with Byte.
	 */
	@Override
	public default Character remove(Object key) {
		return key instanceof Byte ? Character.valueOf(remove(((Byte)key).byteValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(byte key, char value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Byte && value instanceof Character && remove(((Byte)key).byteValue(), ((Character)value).charValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public char removeOrDefault(byte key, char defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(byte key, char oldValue, char newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public char replace(byte key, char value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceChars(Byte2CharMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceChars(ByteCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public char computeChar(byte key, ByteCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public char computeCharIfAbsent(byte key, Byte2CharFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public char supplyCharIfAbsent(byte key, CharSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public char computeCharIfPresent(byte key, ByteCharUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public char mergeChar(byte key, char value, CharCharUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllChar(Byte2CharMap m, CharCharUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Character oldValue, Character newValue) {
		return replace(key.byteValue(), oldValue.charValue(), newValue.charValue());
	}
	
	@Override
	@Deprecated
	public default Character replace(Byte key, Character value) {
		return Character.valueOf(replace(key.byteValue(), value.charValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public char get(byte key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public char getOrDefault(byte key, char defaultValue);
	
	@Override
	@Deprecated
	public default Character get(Object key) {
		return Character.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Character getOrDefault(Object key, Character defaultValue) {
		Character value = Character.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceChars(mappingFunction instanceof ByteCharUnaryOperator ? (ByteCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Character.valueOf(V)).charValue());
	}
	
	@Override
	@Deprecated
	public default Character compute(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeChar(key.byteValue(), mappingFunction instanceof ByteCharUnaryOperator ? (ByteCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfAbsent(Byte key, Function<? super Byte, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfAbsent(key.byteValue(), mappingFunction instanceof Byte2CharFunction ? (Byte2CharFunction)mappingFunction : K -> mappingFunction.apply(Byte.valueOf(K)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character computeIfPresent(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(computeCharIfPresent(key.byteValue(), mappingFunction instanceof ByteCharUnaryOperator ? (ByteCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	@Override
	@Deprecated
	public default Character merge(Byte key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Character.valueOf(mergeChar(key.byteValue(), value.charValue(), mappingFunction instanceof CharCharUnaryOperator ? (CharCharUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Character.valueOf(K), Character.valueOf(V)).charValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ByteCharConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Character> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ByteCharConsumer ? (ByteCharConsumer)action : (K, V) -> action.accept(Byte.valueOf(K), Character.valueOf(V)));
	}
	
	@Override
	public ByteSet keySet();
	@Override
	public CharCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Byte, Character>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> byte2CharEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Byte2CharMaps#synchronize
	 */
	public default Byte2CharMap synchronize() { return Byte2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Byte2CharMaps#synchronize
	 */
	public default Byte2CharMap synchronize(Object mutex) { return Byte2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Byte2CharMaps#unmodifiable
	 */
	public default Byte2CharMap unmodifiable() { return Byte2CharMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Character put(Byte key, Character value) {
		return Character.valueOf(put(key.byteValue(), value.charValue()));
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Byte key, Character value) {
		return Character.valueOf(put(key.byteValue(), value.charValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Byte2CharMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Byte2CharMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Byte2CharMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Byte, Character>
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
		public BuilderCache put(byte key, char value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Byte key, Character value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2CharOpenHashMap map() {
			return new Byte2CharOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2CharOpenHashMap map(int size) {
			return new Byte2CharOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2CharOpenHashMap map(byte[] keys, char[] values) {
			return new Byte2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2CharOpenHashMap map(Byte[] keys, Character[] values) {
			return new Byte2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2CharOpenHashMap map(Byte2CharMap map) {
			return new Byte2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharOpenHashMap map(Map<? extends Byte, ? extends Character> map) {
			return new Byte2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Byte2CharLinkedOpenHashMap linkedMap() {
			return new Byte2CharLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2CharLinkedOpenHashMap linkedMap(int size) {
			return new Byte2CharLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Byte2CharLinkedOpenHashMap linkedMap(byte[] keys, char[] values) {
			return new Byte2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2CharLinkedOpenHashMap linkedMap(Byte[] keys, Character[] values) {
			return new Byte2CharLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2CharLinkedOpenHashMap linkedMap(Byte2CharMap map) {
			return new Byte2CharLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2CharOpenHashMap linkedMap(Map<? extends Byte, ? extends Character> map) {
			return new ImmutableByte2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableByte2CharOpenHashMap immutable(byte[] keys, char[] values) {
			return new ImmutableByte2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableByte2CharOpenHashMap immutable(Byte[] keys, Character[] values) {
			return new ImmutableByte2CharOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableByte2CharOpenHashMap immutable(Byte2CharMap map) {
			return new ImmutableByte2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2CharOpenHashMap immutable(Map<? extends Byte, ? extends Character> map) {
			return new ImmutableByte2CharOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Byte2CharOpenCustomHashMap customMap(ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Byte2CharOpenCustomHashMap customMap(int size, ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Byte2CharOpenCustomHashMap customMap(byte[] keys, char[] values, ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(keys, values, strategy);
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
		public Byte2CharOpenCustomHashMap customMap(Byte[] keys, Character[] values, ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2CharOpenCustomHashMap customMap(Byte2CharMap map, ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharOpenCustomHashMap customMap(Map<? extends Byte, ? extends Character> map, ByteStrategy strategy) {
			return new Byte2CharOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(int size, ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(byte[] keys, char[] values, ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(Byte[] keys, Character[] values, ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(Byte2CharMap map, ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(Map<? extends Byte, ? extends Character> map, ByteStrategy strategy) {
			return new Byte2CharLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2CharArrayMap arrayMap() {
			return new Byte2CharArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2CharArrayMap arrayMap(int size) {
			return new Byte2CharArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2CharArrayMap arrayMap(byte[] keys, char[] values) {
			return new Byte2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2CharArrayMap arrayMap(Byte[] keys, Character[] values) {
			return new Byte2CharArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2CharArrayMap arrayMap(Byte2CharMap map) {
			return new Byte2CharArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharArrayMap arrayMap(Map<? extends Byte, ? extends Character> map) {
			return new Byte2CharArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Byte2CharRBTreeMap rbTreeMap() {
			return new Byte2CharRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Byte2CharRBTreeMap rbTreeMap(ByteComparator comp) {
			return new Byte2CharRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Byte2CharRBTreeMap rbTreeMap(byte[] keys, char[] values, ByteComparator comp) {
			return new Byte2CharRBTreeMap(keys, values, comp);
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
		public Byte2CharRBTreeMap rbTreeMap(Byte[] keys, Character[] values, ByteComparator comp) {
			return new Byte2CharRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Byte2CharRBTreeMap rbTreeMap(Byte2CharMap map, ByteComparator comp) {
			return new Byte2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharRBTreeMap rbTreeMap(Map<? extends Byte, ? extends Character> map, ByteComparator comp) {
			return new Byte2CharRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Byte2CharAVLTreeMap avlTreeMap() {
			return new Byte2CharAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Byte2CharAVLTreeMap avlTreeMap(ByteComparator comp) {
			return new Byte2CharAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Byte2CharAVLTreeMap avlTreeMap(byte[] keys, char[] values, ByteComparator comp) {
			return new Byte2CharAVLTreeMap(keys, values, comp);
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
		public Byte2CharAVLTreeMap avlTreeMap(Byte[] keys, Character[] values, ByteComparator comp) {
			return new Byte2CharAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Byte2CharAVLTreeMap avlTreeMap(Byte2CharMap map, ByteComparator comp) {
			return new Byte2CharAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2CharAVLTreeMap avlTreeMap(Map<? extends Byte, ? extends Character> map, ByteComparator comp) {
			return new Byte2CharAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		byte[] keys;
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
			keys = new byte[initialSize];
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
		public BuilderCache put(byte key, char value) {
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
		public BuilderCache put(Byte key, Character value) {
			return put(key.byteValue(), value.charValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Byte2CharMap.Entry entry) {
			return put(entry.getByteKey(), entry.getCharValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Byte2CharMap map) {
			return putAll(Byte2CharMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Byte, ? extends Character> map) {
			for(Map.Entry<? extends Byte, ? extends Character> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Byte2CharMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Byte2CharMap.Entry>)c).size());
			
			for(Byte2CharMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Byte2CharMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Byte2CharOpenHashMap
		 */
		public Byte2CharOpenHashMap map() {
			return putElements(new Byte2CharOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Byte2CharLinkedOpenHashMap
		 */
		public Byte2CharLinkedOpenHashMap linkedMap() {
			return putElements(new Byte2CharLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableByte2CharOpenHashMap
		 */
		public ImmutableByte2CharOpenHashMap immutable() {
			return new ImmutableByte2CharOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2CharOpenCustomHashMap
		 */
		public Byte2CharOpenCustomHashMap customMap(ByteStrategy strategy) {
			return putElements(new Byte2CharOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2CharLinkedOpenCustomHashMap
		 */
		public Byte2CharLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return putElements(new Byte2CharLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Byte2CharConcurrentOpenHashMap
		 */
		public Byte2CharConcurrentOpenHashMap concurrentMap() {
			return putElements(new Byte2CharConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Byte2CharArrayMap
		 */
		public Byte2CharArrayMap arrayMap() {
			return new Byte2CharArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Byte2CharRBTreeMap
		 */
		public Byte2CharRBTreeMap rbTreeMap() {
			return putElements(new Byte2CharRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2CharRBTreeMap
		 */
		public Byte2CharRBTreeMap rbTreeMap(ByteComparator comp) {
			return putElements(new Byte2CharRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Byte2CharAVLTreeMap
		 */
		public Byte2CharAVLTreeMap avlTreeMap() {
			return putElements(new Byte2CharAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2CharAVLTreeMap
		 */
		public Byte2CharAVLTreeMap avlTreeMap(ByteComparator comp) {
			return putElements(new Byte2CharAVLTreeMap(comp));
		}
	}
}