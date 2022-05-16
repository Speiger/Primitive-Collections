package speiger.src.collections.bytes.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.bytes.functions.consumer.ByteLongConsumer;
import speiger.src.collections.bytes.functions.function.Byte2LongFunction;
import speiger.src.collections.bytes.functions.function.ByteLongUnaryOperator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2LongLinkedOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2LongOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2LongLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2LongOpenHashMap;
import speiger.src.collections.bytes.maps.impl.immutable.ImmutableByte2LongOpenHashMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2LongAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2LongRBTreeMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2LongArrayMap;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2LongConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.bytes.utils.maps.Byte2LongMaps;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Byte2LongMap extends Map<Byte, Long>, Byte2LongFunction
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
	public long getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Byte2LongMap setDefaultReturnValue(long v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Byte2LongMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public long put(byte key, long value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(byte[] keys, long[] values) {
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
	public void putAll(byte[] keys, long[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Byte[] keys, Long[] values) {
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
	public void putAll(Byte[] keys, Long[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public long putIfAbsent(byte key, long value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Byte2LongMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public long addTo(byte key, long value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Byte2LongMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public long subFrom(byte key, long value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Byte2LongMap m);
	
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
	public boolean containsValue(long value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Long && containsValue(((Long)value).longValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public long remove(byte key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Byte but just have to support equals with Byte.
	 */
	@Override
	public default Long remove(Object key) {
		return key instanceof Byte ? Long.valueOf(remove(((Byte)key).byteValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(byte key, long value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Byte && value instanceof Long && remove(((Byte)key).byteValue(), ((Long)value).longValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public long removeOrDefault(byte key, long defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(byte key, long oldValue, long newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public long replace(byte key, long value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceLongs(Byte2LongMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceLongs(ByteLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public long computeLong(byte key, ByteLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public long computeLongIfAbsent(byte key, Byte2LongFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public long supplyLongIfAbsent(byte key, LongSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public long computeLongIfPresent(byte key, ByteLongUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public long mergeLong(byte key, long value, LongLongUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllLong(Byte2LongMap m, LongLongUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Long oldValue, Long newValue) {
		return replace(key.byteValue(), oldValue.longValue(), newValue.longValue());
	}
	
	@Override
	@Deprecated
	public default Long replace(Byte key, Long value) {
		return Long.valueOf(replace(key.byteValue(), value.longValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public long get(byte key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public long getOrDefault(byte key, long defaultValue);
	
	@Override
	@Deprecated
	public default Long get(Object key) {
		return Long.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Long getOrDefault(Object key, Long defaultValue) {
		Long value = Long.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceLongs(mappingFunction instanceof ByteLongUnaryOperator ? (ByteLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Long.valueOf(V)).longValue());
	}
	
	@Override
	@Deprecated
	public default Long compute(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLong(key.byteValue(), mappingFunction instanceof ByteLongUnaryOperator ? (ByteLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfAbsent(Byte key, Function<? super Byte, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfAbsent(key.byteValue(), mappingFunction instanceof Byte2LongFunction ? (Byte2LongFunction)mappingFunction : K -> mappingFunction.apply(Byte.valueOf(K)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long computeIfPresent(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfPresent(key.byteValue(), mappingFunction instanceof ByteLongUnaryOperator ? (ByteLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	@Override
	@Deprecated
	public default Long merge(Byte key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(mergeLong(key.byteValue(), value.longValue(), mappingFunction instanceof LongLongUnaryOperator ? (LongLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ByteLongConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Long> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ByteLongConsumer ? (ByteLongConsumer)action : (K, V) -> action.accept(Byte.valueOf(K), Long.valueOf(V)));
	}
	
	@Override
	public ByteSet keySet();
	@Override
	public LongCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Byte, Long>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> byte2LongEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Byte2LongMaps#synchronize
	 */
	public default Byte2LongMap synchronize() { return Byte2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Byte2LongMaps#synchronize
	 */
	public default Byte2LongMap synchronize(Object mutex) { return Byte2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Byte2LongMaps#unmodifiable
	 */
	public default Byte2LongMap unmodifiable() { return Byte2LongMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Long put(Byte key, Long value) {
		return Long.valueOf(put(key.byteValue(), value.longValue()));
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Byte key, Long value) {
		return Long.valueOf(put(key.byteValue(), value.longValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Byte2LongMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Byte2LongMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Byte2LongMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Byte, Long>
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
		public long getLongValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public long setValue(long value);
		@Override
		public default Long getValue() { return Long.valueOf(getLongValue()); }
		@Override
		public default Long setValue(Long value) { return Long.valueOf(setValue(value.longValue())); }
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
		public BuilderCache put(byte key, long value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Byte key, Long value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2LongOpenHashMap map() {
			return new Byte2LongOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2LongOpenHashMap map(int size) {
			return new Byte2LongOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2LongOpenHashMap map(byte[] keys, long[] values) {
			return new Byte2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2LongOpenHashMap map(Byte[] keys, Long[] values) {
			return new Byte2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2LongOpenHashMap map(Byte2LongMap map) {
			return new Byte2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongOpenHashMap map(Map<? extends Byte, ? extends Long> map) {
			return new Byte2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Byte2LongLinkedOpenHashMap linkedMap() {
			return new Byte2LongLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2LongLinkedOpenHashMap linkedMap(int size) {
			return new Byte2LongLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Byte2LongLinkedOpenHashMap linkedMap(byte[] keys, long[] values) {
			return new Byte2LongLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2LongLinkedOpenHashMap linkedMap(Byte[] keys, Long[] values) {
			return new Byte2LongLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2LongLinkedOpenHashMap linkedMap(Byte2LongMap map) {
			return new Byte2LongLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2LongOpenHashMap linkedMap(Map<? extends Byte, ? extends Long> map) {
			return new ImmutableByte2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableByte2LongOpenHashMap immutable(byte[] keys, long[] values) {
			return new ImmutableByte2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableByte2LongOpenHashMap immutable(Byte[] keys, Long[] values) {
			return new ImmutableByte2LongOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableByte2LongOpenHashMap immutable(Byte2LongMap map) {
			return new ImmutableByte2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableByte2LongOpenHashMap immutable(Map<? extends Byte, ? extends Long> map) {
			return new ImmutableByte2LongOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Byte2LongOpenCustomHashMap customMap(ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Byte2LongOpenCustomHashMap customMap(int size, ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Byte2LongOpenCustomHashMap customMap(byte[] keys, long[] values, ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(keys, values, strategy);
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
		public Byte2LongOpenCustomHashMap customMap(Byte[] keys, Long[] values, ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2LongOpenCustomHashMap customMap(Byte2LongMap map, ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongOpenCustomHashMap customMap(Map<? extends Byte, ? extends Long> map, ByteStrategy strategy) {
			return new Byte2LongOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(int size, ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(byte[] keys, long[] values, ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(Byte[] keys, Long[] values, ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(Byte2LongMap map, ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(Map<? extends Byte, ? extends Long> map, ByteStrategy strategy) {
			return new Byte2LongLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Byte2LongArrayMap arrayMap() {
			return new Byte2LongArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Byte2LongArrayMap arrayMap(int size) {
			return new Byte2LongArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Byte2LongArrayMap arrayMap(byte[] keys, long[] values) {
			return new Byte2LongArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Byte2LongArrayMap arrayMap(Byte[] keys, Long[] values) {
			return new Byte2LongArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Byte2LongArrayMap arrayMap(Byte2LongMap map) {
			return new Byte2LongArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongArrayMap arrayMap(Map<? extends Byte, ? extends Long> map) {
			return new Byte2LongArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Byte2LongRBTreeMap rbTreeMap() {
			return new Byte2LongRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Byte2LongRBTreeMap rbTreeMap(ByteComparator comp) {
			return new Byte2LongRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Byte2LongRBTreeMap rbTreeMap(byte[] keys, long[] values, ByteComparator comp) {
			return new Byte2LongRBTreeMap(keys, values, comp);
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
		public Byte2LongRBTreeMap rbTreeMap(Byte[] keys, Long[] values, ByteComparator comp) {
			return new Byte2LongRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Byte2LongRBTreeMap rbTreeMap(Byte2LongMap map, ByteComparator comp) {
			return new Byte2LongRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongRBTreeMap rbTreeMap(Map<? extends Byte, ? extends Long> map, ByteComparator comp) {
			return new Byte2LongRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Byte2LongAVLTreeMap avlTreeMap() {
			return new Byte2LongAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Byte2LongAVLTreeMap avlTreeMap(ByteComparator comp) {
			return new Byte2LongAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Byte2LongAVLTreeMap avlTreeMap(byte[] keys, long[] values, ByteComparator comp) {
			return new Byte2LongAVLTreeMap(keys, values, comp);
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
		public Byte2LongAVLTreeMap avlTreeMap(Byte[] keys, Long[] values, ByteComparator comp) {
			return new Byte2LongAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Byte2LongAVLTreeMap avlTreeMap(Byte2LongMap map, ByteComparator comp) {
			return new Byte2LongAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Byte2LongAVLTreeMap avlTreeMap(Map<? extends Byte, ? extends Long> map, ByteComparator comp) {
			return new Byte2LongAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		byte[] keys;
		long[] values;
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
			values = new long[initialSize];
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
		public BuilderCache put(byte key, long value) {
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
		public BuilderCache put(Byte key, Long value) {
			return put(key.byteValue(), value.longValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Byte2LongMap.Entry entry) {
			return put(entry.getByteKey(), entry.getLongValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Byte2LongMap map) {
			return putAll(Byte2LongMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Byte, ? extends Long> map) {
			for(Map.Entry<? extends Byte, ? extends Long> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Byte2LongMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Byte2LongMap.Entry>)c).size());
			
			for(Byte2LongMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Byte2LongMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Byte2LongOpenHashMap
		 */
		public Byte2LongOpenHashMap map() {
			return putElements(new Byte2LongOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Byte2LongLinkedOpenHashMap
		 */
		public Byte2LongLinkedOpenHashMap linkedMap() {
			return putElements(new Byte2LongLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableByte2LongOpenHashMap
		 */
		public ImmutableByte2LongOpenHashMap immutable() {
			return new ImmutableByte2LongOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2LongOpenCustomHashMap
		 */
		public Byte2LongOpenCustomHashMap customMap(ByteStrategy strategy) {
			return putElements(new Byte2LongOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2LongLinkedOpenCustomHashMap
		 */
		public Byte2LongLinkedOpenCustomHashMap customLinkedMap(ByteStrategy strategy) {
			return putElements(new Byte2LongLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Byte2LongConcurrentOpenHashMap
		 */
		public Byte2LongConcurrentOpenHashMap concurrentMap() {
			return putElements(new Byte2LongConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Byte2LongArrayMap
		 */
		public Byte2LongArrayMap arrayMap() {
			return new Byte2LongArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Byte2LongRBTreeMap
		 */
		public Byte2LongRBTreeMap rbTreeMap() {
			return putElements(new Byte2LongRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2LongRBTreeMap
		 */
		public Byte2LongRBTreeMap rbTreeMap(ByteComparator comp) {
			return putElements(new Byte2LongRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Byte2LongAVLTreeMap
		 */
		public Byte2LongAVLTreeMap avlTreeMap() {
			return putElements(new Byte2LongAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2LongAVLTreeMap
		 */
		public Byte2LongAVLTreeMap avlTreeMap(ByteComparator comp) {
			return putElements(new Byte2LongAVLTreeMap(comp));
		}
	}
}