package speiger.src.collections.ints.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.ints.functions.function.Int2ByteFunction;
import speiger.src.collections.ints.functions.function.IntByteUnaryOperator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.maps.impl.customHash.Int2ByteLinkedOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.customHash.Int2ByteOpenCustomHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ByteLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ByteOpenHashMap;
import speiger.src.collections.ints.maps.impl.immutable.ImmutableInt2ByteOpenHashMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ByteAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ByteRBTreeMap;
import speiger.src.collections.ints.maps.impl.misc.Int2ByteArrayMap;
import speiger.src.collections.ints.maps.impl.concurrent.Int2ByteConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.collections.ints.utils.maps.Int2ByteMaps;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Int2ByteMap extends Map<Integer, Byte>, Int2ByteFunction
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
	public byte getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Int2ByteMap setDefaultReturnValue(byte v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Int2ByteMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public byte put(int key, byte value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(int[] keys, byte[] values) {
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
	public void putAll(int[] keys, byte[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Integer[] keys, Byte[] values) {
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
	public void putAll(Integer[] keys, Byte[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public byte putIfAbsent(int key, byte value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Int2ByteMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public byte addTo(int key, byte value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Int2ByteMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public byte subFrom(int key, byte value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Int2ByteMap m);
	
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
	public boolean containsValue(byte value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Byte && containsValue(((Byte)value).byteValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public byte remove(int key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Integer but just have to support equals with Integer.
	 */
	@Override
	public default Byte remove(Object key) {
		return key instanceof Integer ? Byte.valueOf(remove(((Integer)key).intValue())) : Byte.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(int key, byte value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Integer && value instanceof Byte && remove(((Integer)key).intValue(), ((Byte)value).byteValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public byte removeOrDefault(int key, byte defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(int key, byte oldValue, byte newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public byte replace(int key, byte value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBytes(Int2ByteMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBytes(IntByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByte(int key, IntByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsent(int key, Int2ByteFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsent(int key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresent(int key, IntByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public byte mergeByte(int key, byte value, ByteByteUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllByte(Int2ByteMap m, ByteByteUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Byte oldValue, Byte newValue) {
		return replace(key.intValue(), oldValue.byteValue(), newValue.byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte replace(Integer key, Byte value) {
		return Byte.valueOf(replace(key.intValue(), value.byteValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public byte get(int key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public byte getOrDefault(int key, byte defaultValue);
	
	@Override
	@Deprecated
	public default Byte get(Object key) {
		return Byte.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		Byte value = Byte.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBytes(mappingFunction instanceof IntByteUnaryOperator ? (IntByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Byte.valueOf(V)).byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte compute(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByte(key.intValue(), mappingFunction instanceof IntByteUnaryOperator ? (IntByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfAbsent(Integer key, Function<? super Integer, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfAbsent(key.intValue(), mappingFunction instanceof Int2ByteFunction ? (Int2ByteFunction)mappingFunction : K -> mappingFunction.apply(Integer.valueOf(K)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfPresent(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfPresent(key.intValue(), mappingFunction instanceof IntByteUnaryOperator ? (IntByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte merge(Integer key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(mergeByte(key.intValue(), value.byteValue(), mappingFunction instanceof ByteByteUnaryOperator ? (ByteByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(IntByteConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Byte> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof IntByteConsumer ? (IntByteConsumer)action : (K, V) -> action.accept(Integer.valueOf(K), Byte.valueOf(V)));
	}
	
	@Override
	public IntSet keySet();
	@Override
	public ByteCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Integer, Byte>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> int2ByteEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteMap synchronize() { return Int2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteMap synchronize(Object mutex) { return Int2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Int2ByteMaps#unmodifiable
	 */
	public default Int2ByteMap unmodifiable() { return Int2ByteMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Byte put(Integer key, Byte value) {
		return Byte.valueOf(put(key.intValue(), value.byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Integer key, Byte value) {
		return Byte.valueOf(put(key.intValue(), value.byteValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Int2ByteMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Int2ByteMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Int2ByteMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Integer, Byte>
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
		public byte getByteValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public byte setValue(byte value);
		@Override
		public default Byte getValue() { return Byte.valueOf(getByteValue()); }
		@Override
		public default Byte setValue(Byte value) { return Byte.valueOf(setValue(value.byteValue())); }
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
		public BuilderCache put(int key, byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Integer key, Byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2ByteOpenHashMap map() {
			return new Int2ByteOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2ByteOpenHashMap map(int size) {
			return new Int2ByteOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2ByteOpenHashMap map(int[] keys, byte[] values) {
			return new Int2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2ByteOpenHashMap map(Integer[] keys, Byte[] values) {
			return new Int2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2ByteOpenHashMap map(Int2ByteMap map) {
			return new Int2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteOpenHashMap map(Map<? extends Integer, ? extends Byte> map) {
			return new Int2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Int2ByteLinkedOpenHashMap linkedMap() {
			return new Int2ByteLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Int2ByteLinkedOpenHashMap linkedMap(int size) {
			return new Int2ByteLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Int2ByteLinkedOpenHashMap linkedMap(int[] keys, byte[] values) {
			return new Int2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2ByteLinkedOpenHashMap linkedMap(Integer[] keys, Byte[] values) {
			return new Int2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2ByteLinkedOpenHashMap linkedMap(Int2ByteMap map) {
			return new Int2ByteLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2ByteOpenHashMap linkedMap(Map<? extends Integer, ? extends Byte> map) {
			return new ImmutableInt2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableInt2ByteOpenHashMap immutable(int[] keys, byte[] values) {
			return new ImmutableInt2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableInt2ByteOpenHashMap immutable(Integer[] keys, Byte[] values) {
			return new ImmutableInt2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableInt2ByteOpenHashMap immutable(Int2ByteMap map) {
			return new ImmutableInt2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableInt2ByteOpenHashMap immutable(Map<? extends Integer, ? extends Byte> map) {
			return new ImmutableInt2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Int2ByteOpenCustomHashMap customMap(IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Int2ByteOpenCustomHashMap customMap(int size, IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Int2ByteOpenCustomHashMap customMap(int[] keys, byte[] values, IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(keys, values, strategy);
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
		public Int2ByteOpenCustomHashMap customMap(Integer[] keys, Byte[] values, IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Int2ByteOpenCustomHashMap customMap(Int2ByteMap map, IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteOpenCustomHashMap customMap(Map<? extends Integer, ? extends Byte> map, IntStrategy strategy) {
			return new Int2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(int size, IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(int[] keys, byte[] values, IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(Integer[] keys, Byte[] values, IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(Int2ByteMap map, IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(Map<? extends Integer, ? extends Byte> map, IntStrategy strategy) {
			return new Int2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Int2ByteArrayMap arrayMap() {
			return new Int2ByteArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Int2ByteArrayMap arrayMap(int size) {
			return new Int2ByteArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Int2ByteArrayMap arrayMap(int[] keys, byte[] values) {
			return new Int2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Int2ByteArrayMap arrayMap(Integer[] keys, Byte[] values) {
			return new Int2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Int2ByteArrayMap arrayMap(Int2ByteMap map) {
			return new Int2ByteArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteArrayMap arrayMap(Map<? extends Integer, ? extends Byte> map) {
			return new Int2ByteArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Int2ByteRBTreeMap rbTreeMap() {
			return new Int2ByteRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Int2ByteRBTreeMap rbTreeMap(IntComparator comp) {
			return new Int2ByteRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Int2ByteRBTreeMap rbTreeMap(int[] keys, byte[] values, IntComparator comp) {
			return new Int2ByteRBTreeMap(keys, values, comp);
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
		public Int2ByteRBTreeMap rbTreeMap(Integer[] keys, Byte[] values, IntComparator comp) {
			return new Int2ByteRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Int2ByteRBTreeMap rbTreeMap(Int2ByteMap map, IntComparator comp) {
			return new Int2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteRBTreeMap rbTreeMap(Map<? extends Integer, ? extends Byte> map, IntComparator comp) {
			return new Int2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Int2ByteAVLTreeMap avlTreeMap() {
			return new Int2ByteAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Int2ByteAVLTreeMap avlTreeMap(IntComparator comp) {
			return new Int2ByteAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Int2ByteAVLTreeMap avlTreeMap(int[] keys, byte[] values, IntComparator comp) {
			return new Int2ByteAVLTreeMap(keys, values, comp);
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
		public Int2ByteAVLTreeMap avlTreeMap(Integer[] keys, Byte[] values, IntComparator comp) {
			return new Int2ByteAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Int2ByteAVLTreeMap avlTreeMap(Int2ByteMap map, IntComparator comp) {
			return new Int2ByteAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Int2ByteAVLTreeMap avlTreeMap(Map<? extends Integer, ? extends Byte> map, IntComparator comp) {
			return new Int2ByteAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		int[] keys;
		byte[] values;
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
			keys = new int[initialSize];
			values = new byte[initialSize];
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
		public BuilderCache put(int key, byte value) {
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
		public BuilderCache put(Integer key, Byte value) {
			return put(key.intValue(), value.byteValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Int2ByteMap.Entry entry) {
			return put(entry.getIntKey(), entry.getByteValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Int2ByteMap map) {
			return putAll(Int2ByteMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Integer, ? extends Byte> map) {
			for(Map.Entry<? extends Integer, ? extends Byte> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Int2ByteMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Int2ByteMap.Entry>)c).size());
			
			for(Int2ByteMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Int2ByteMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Int2ByteOpenHashMap
		 */
		public Int2ByteOpenHashMap map() {
			return putElements(new Int2ByteOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Int2ByteLinkedOpenHashMap
		 */
		public Int2ByteLinkedOpenHashMap linkedMap() {
			return putElements(new Int2ByteLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableInt2ByteOpenHashMap
		 */
		public ImmutableInt2ByteOpenHashMap immutable() {
			return new ImmutableInt2ByteOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2ByteOpenCustomHashMap
		 */
		public Int2ByteOpenCustomHashMap customMap(IntStrategy strategy) {
			return putElements(new Int2ByteOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Int2ByteLinkedOpenCustomHashMap
		 */
		public Int2ByteLinkedOpenCustomHashMap customLinkedMap(IntStrategy strategy) {
			return putElements(new Int2ByteLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Int2ByteConcurrentOpenHashMap
		 */
		public Int2ByteConcurrentOpenHashMap concurrentMap() {
			return putElements(new Int2ByteConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Int2ByteArrayMap
		 */
		public Int2ByteArrayMap arrayMap() {
			return new Int2ByteArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Int2ByteRBTreeMap
		 */
		public Int2ByteRBTreeMap rbTreeMap() {
			return putElements(new Int2ByteRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2ByteRBTreeMap
		 */
		public Int2ByteRBTreeMap rbTreeMap(IntComparator comp) {
			return putElements(new Int2ByteRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Int2ByteAVLTreeMap
		 */
		public Int2ByteAVLTreeMap avlTreeMap() {
			return putElements(new Int2ByteAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Int2ByteAVLTreeMap
		 */
		public Int2ByteAVLTreeMap avlTreeMap(IntComparator comp) {
			return putElements(new Int2ByteAVLTreeMap(comp));
		}
	}
}