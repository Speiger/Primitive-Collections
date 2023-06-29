package speiger.src.collections.longs.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.longs.functions.consumer.LongByteConsumer;
import speiger.src.collections.longs.functions.function.Long2ByteFunction;
import speiger.src.collections.longs.functions.function.LongByteUnaryOperator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.maps.impl.customHash.Long2ByteLinkedOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.customHash.Long2ByteOpenCustomHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ByteLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ByteOpenHashMap;
import speiger.src.collections.longs.maps.impl.immutable.ImmutableLong2ByteOpenHashMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ByteAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ByteRBTreeMap;
import speiger.src.collections.longs.maps.impl.misc.Long2ByteArrayMap;
import speiger.src.collections.longs.maps.impl.concurrent.Long2ByteConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.collections.longs.utils.maps.Long2ByteMaps;
import speiger.src.collections.longs.sets.LongSet;
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
public interface Long2ByteMap extends Map<Long, Byte>, Long2ByteFunction
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
	public Long2ByteMap setDefaultReturnValue(byte v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Long2ByteMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public byte put(long key, byte value);
	
	/**
	 * A Helper method that allows to put int a Long2ByteMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default byte put(Entry entry) {
		return put(entry.getLongKey(), entry.getByteValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default Byte put(Map.Entry<Long, Byte> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(long[] keys, byte[] values) {
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
	public void putAll(long[] keys, byte[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Long[] keys, Byte[] values) {
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
	public void putAll(Long[] keys, Byte[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public byte putIfAbsent(long key, byte value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Long2ByteMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public byte addTo(long key, byte value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Long2ByteMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public byte subFrom(long key, byte value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Long2ByteMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(long key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Long but just have to support equals with Long.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Long && containsKey(((Long)key).longValue());
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
	public byte remove(long key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Long but just have to support equals with Long.
	 */
	@Override
	public default Byte remove(Object key) {
		return key instanceof Long ? Byte.valueOf(remove(((Long)key).longValue())) : Byte.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(long key, byte value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Long && value instanceof Byte && remove(((Long)key).longValue(), ((Byte)value).byteValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public byte removeOrDefault(long key, byte defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(long key, byte oldValue, byte newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public byte replace(long key, byte value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBytes(Long2ByteMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBytes(LongByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByte(long key, LongByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsent(long key, Long2ByteFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsent(long key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresent(long key, LongByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByteNonDefault(long key, LongByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsentNonDefault(long key, Long2ByteFunction mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsentNonDefault(long key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresentNonDefault(long key, LongByteUnaryOperator mappingFunction);
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
	public byte mergeByte(long key, byte value, ByteByteUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllByte(Long2ByteMap m, ByteByteUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Byte oldValue, Byte newValue) {
		return replace(key.longValue(), oldValue.byteValue(), newValue.byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte replace(Long key, Byte value) {
		return Byte.valueOf(replace(key.longValue(), value.byteValue()));
	}
	
	@Override
	public default byte applyAsByte(long key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public byte get(long key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public byte getOrDefault(long key, byte defaultValue);
	
	@Override
	@Deprecated
	public default Byte get(Object key) {
		return Byte.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		Byte value = Byte.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBytes(mappingFunction instanceof LongByteUnaryOperator ? (LongByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Byte.valueOf(V)).byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte compute(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByte(key.longValue(), mappingFunction instanceof LongByteUnaryOperator ? (LongByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfAbsent(Long key, Function<? super Long, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfAbsent(key.longValue(), mappingFunction instanceof Long2ByteFunction ? (Long2ByteFunction)mappingFunction : K -> mappingFunction.apply(Long.valueOf(K)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfPresent(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfPresent(key.longValue(), mappingFunction instanceof LongByteUnaryOperator ? (LongByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte merge(Long key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(mergeByte(key.longValue(), value.byteValue(), mappingFunction instanceof ByteByteUnaryOperator ? (ByteByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(LongByteConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Byte> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof LongByteConsumer ? (LongByteConsumer)action : (K, V) -> action.accept(Long.valueOf(K), Byte.valueOf(V)));
	}
	
	@Override
	public LongSet keySet();
	@Override
	public ByteCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Long, Byte>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> long2ByteEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Long2ByteMaps#synchronize
	 */
	public default Long2ByteMap synchronize() { return Long2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Long2ByteMaps#synchronize
	 */
	public default Long2ByteMap synchronize(Object mutex) { return Long2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Long2ByteMaps#unmodifiable
	 */
	public default Long2ByteMap unmodifiable() { return Long2ByteMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Byte put(Long key, Byte value) {
		return Byte.valueOf(put(key.longValue(), value.byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Long key, Byte value) {
		return Byte.valueOf(put(key.longValue(), value.byteValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Long2ByteMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Long2ByteMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Long2ByteMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Long, Byte>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public long getLongKey();
		public default Long getKey() { return Long.valueOf(getLongKey()); }
		
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
		public BuilderCache put(long key, byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Long key, Byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2ByteOpenHashMap map() {
			return new Long2ByteOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2ByteOpenHashMap map(int size) {
			return new Long2ByteOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2ByteOpenHashMap map(long[] keys, byte[] values) {
			return new Long2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ByteOpenHashMap map(Long[] keys, Byte[] values) {
			return new Long2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2ByteOpenHashMap map(Long2ByteMap map) {
			return new Long2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteOpenHashMap map(Map<? extends Long, ? extends Byte> map) {
			return new Long2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Long2ByteLinkedOpenHashMap linkedMap() {
			return new Long2ByteLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Long2ByteLinkedOpenHashMap linkedMap(int size) {
			return new Long2ByteLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Long2ByteLinkedOpenHashMap linkedMap(long[] keys, byte[] values) {
			return new Long2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ByteLinkedOpenHashMap linkedMap(Long[] keys, Byte[] values) {
			return new Long2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ByteLinkedOpenHashMap linkedMap(Long2ByteMap map) {
			return new Long2ByteLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2ByteOpenHashMap linkedMap(Map<? extends Long, ? extends Byte> map) {
			return new ImmutableLong2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableLong2ByteOpenHashMap immutable(long[] keys, byte[] values) {
			return new ImmutableLong2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableLong2ByteOpenHashMap immutable(Long[] keys, Byte[] values) {
			return new ImmutableLong2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableLong2ByteOpenHashMap immutable(Long2ByteMap map) {
			return new ImmutableLong2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableLong2ByteOpenHashMap immutable(Map<? extends Long, ? extends Byte> map) {
			return new ImmutableLong2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Long2ByteOpenCustomHashMap customMap(LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Long2ByteOpenCustomHashMap customMap(int size, LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Long2ByteOpenCustomHashMap customMap(long[] keys, byte[] values, LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(keys, values, strategy);
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
		public Long2ByteOpenCustomHashMap customMap(Long[] keys, Byte[] values, LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ByteOpenCustomHashMap customMap(Long2ByteMap map, LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteOpenCustomHashMap customMap(Map<? extends Long, ? extends Byte> map, LongStrategy strategy) {
			return new Long2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(int size, LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(long[] keys, byte[] values, LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(Long[] keys, Byte[] values, LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(Long2ByteMap map, LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(Map<? extends Long, ? extends Byte> map, LongStrategy strategy) {
			return new Long2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Long2ByteArrayMap arrayMap() {
			return new Long2ByteArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Long2ByteArrayMap arrayMap(int size) {
			return new Long2ByteArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Long2ByteArrayMap arrayMap(long[] keys, byte[] values) {
			return new Long2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Long2ByteArrayMap arrayMap(Long[] keys, Byte[] values) {
			return new Long2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Long2ByteArrayMap arrayMap(Long2ByteMap map) {
			return new Long2ByteArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteArrayMap arrayMap(Map<? extends Long, ? extends Byte> map) {
			return new Long2ByteArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Long2ByteRBTreeMap rbTreeMap() {
			return new Long2ByteRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Long2ByteRBTreeMap rbTreeMap(LongComparator comp) {
			return new Long2ByteRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Long2ByteRBTreeMap rbTreeMap(long[] keys, byte[] values, LongComparator comp) {
			return new Long2ByteRBTreeMap(keys, values, comp);
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
		public Long2ByteRBTreeMap rbTreeMap(Long[] keys, Byte[] values, LongComparator comp) {
			return new Long2ByteRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Long2ByteRBTreeMap rbTreeMap(Long2ByteMap map, LongComparator comp) {
			return new Long2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteRBTreeMap rbTreeMap(Map<? extends Long, ? extends Byte> map, LongComparator comp) {
			return new Long2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Long2ByteAVLTreeMap avlTreeMap() {
			return new Long2ByteAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Long2ByteAVLTreeMap avlTreeMap(LongComparator comp) {
			return new Long2ByteAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Long2ByteAVLTreeMap avlTreeMap(long[] keys, byte[] values, LongComparator comp) {
			return new Long2ByteAVLTreeMap(keys, values, comp);
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
		public Long2ByteAVLTreeMap avlTreeMap(Long[] keys, Byte[] values, LongComparator comp) {
			return new Long2ByteAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Long2ByteAVLTreeMap avlTreeMap(Long2ByteMap map, LongComparator comp) {
			return new Long2ByteAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Long2ByteAVLTreeMap avlTreeMap(Map<? extends Long, ? extends Byte> map, LongComparator comp) {
			return new Long2ByteAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		long[] keys;
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
			if(initialSize < 0) throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			keys = new long[initialSize];
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
		public BuilderCache put(long key, byte value) {
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
		public BuilderCache put(Long key, Byte value) {
			return put(key.longValue(), value.byteValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Long2ByteMap.Entry entry) {
			return put(entry.getLongKey(), entry.getByteValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Long2ByteMap map) {
			return putAll(Long2ByteMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Long, ? extends Byte> map) {
			for(Map.Entry<? extends Long, ? extends Byte> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Long2ByteMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Long2ByteMap.Entry>)c).size());
			
			for(Long2ByteMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Long2ByteMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Long2ByteOpenHashMap
		 */
		public Long2ByteOpenHashMap map() {
			return putElements(new Long2ByteOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Long2ByteLinkedOpenHashMap
		 */
		public Long2ByteLinkedOpenHashMap linkedMap() {
			return putElements(new Long2ByteLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableLong2ByteOpenHashMap
		 */
		public ImmutableLong2ByteOpenHashMap immutable() {
			return new ImmutableLong2ByteOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2ByteOpenCustomHashMap
		 */
		public Long2ByteOpenCustomHashMap customMap(LongStrategy strategy) {
			return putElements(new Long2ByteOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Long2ByteLinkedOpenCustomHashMap
		 */
		public Long2ByteLinkedOpenCustomHashMap customLinkedMap(LongStrategy strategy) {
			return putElements(new Long2ByteLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Long2ByteConcurrentOpenHashMap
		 */
		public Long2ByteConcurrentOpenHashMap concurrentMap() {
			return putElements(new Long2ByteConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Long2ByteArrayMap
		 */
		public Long2ByteArrayMap arrayMap() {
			return new Long2ByteArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Long2ByteRBTreeMap
		 */
		public Long2ByteRBTreeMap rbTreeMap() {
			return putElements(new Long2ByteRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2ByteRBTreeMap
		 */
		public Long2ByteRBTreeMap rbTreeMap(LongComparator comp) {
			return putElements(new Long2ByteRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Long2ByteAVLTreeMap
		 */
		public Long2ByteAVLTreeMap avlTreeMap() {
			return putElements(new Long2ByteAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Long2ByteAVLTreeMap
		 */
		public Long2ByteAVLTreeMap avlTreeMap(LongComparator comp) {
			return putElements(new Long2ByteAVLTreeMap(comp));
		}
	}
}