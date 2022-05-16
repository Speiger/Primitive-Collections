package speiger.src.collections.shorts.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.shorts.functions.consumer.ShortByteConsumer;
import speiger.src.collections.shorts.functions.function.Short2ByteFunction;
import speiger.src.collections.shorts.functions.function.ShortByteUnaryOperator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.maps.impl.customHash.Short2ByteLinkedOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.customHash.Short2ByteOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ByteLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ByteOpenHashMap;
import speiger.src.collections.shorts.maps.impl.immutable.ImmutableShort2ByteOpenHashMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ByteAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ByteRBTreeMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2ByteArrayMap;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2ByteConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.collections.shorts.utils.maps.Short2ByteMaps;
import speiger.src.collections.shorts.sets.ShortSet;
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
public interface Short2ByteMap extends Map<Short, Byte>, Short2ByteFunction
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
	public Short2ByteMap setDefaultReturnValue(byte v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Short2ByteMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public byte put(short key, byte value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(short[] keys, byte[] values) {
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
	public void putAll(short[] keys, byte[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Short[] keys, Byte[] values) {
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
	public void putAll(Short[] keys, Byte[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public byte putIfAbsent(short key, byte value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Short2ByteMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public byte addTo(short key, byte value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Short2ByteMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public byte subFrom(short key, byte value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Short2ByteMap m);
	
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
	public byte remove(short key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default Byte remove(Object key) {
		return key instanceof Short ? Byte.valueOf(remove(((Short)key).shortValue())) : Byte.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(short key, byte value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Short && value instanceof Byte && remove(((Short)key).shortValue(), ((Byte)value).byteValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public byte removeOrDefault(short key, byte defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(short key, byte oldValue, byte newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public byte replace(short key, byte value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBytes(Short2ByteMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBytes(ShortByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public byte computeByte(short key, ShortByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public byte computeByteIfAbsent(short key, Short2ByteFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public byte supplyByteIfAbsent(short key, ByteSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public byte computeByteIfPresent(short key, ShortByteUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public byte mergeByte(short key, byte value, ByteByteUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllByte(Short2ByteMap m, ByteByteUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Byte oldValue, Byte newValue) {
		return replace(key.shortValue(), oldValue.byteValue(), newValue.byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte replace(Short key, Byte value) {
		return Byte.valueOf(replace(key.shortValue(), value.byteValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public byte get(short key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public byte getOrDefault(short key, byte defaultValue);
	
	@Override
	@Deprecated
	public default Byte get(Object key) {
		return Byte.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		Byte value = Byte.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBytes(mappingFunction instanceof ShortByteUnaryOperator ? (ShortByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Byte.valueOf(V)).byteValue());
	}
	
	@Override
	@Deprecated
	public default Byte compute(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByte(key.shortValue(), mappingFunction instanceof ShortByteUnaryOperator ? (ShortByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfAbsent(Short key, Function<? super Short, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfAbsent(key.shortValue(), mappingFunction instanceof Short2ByteFunction ? (Short2ByteFunction)mappingFunction : K -> mappingFunction.apply(Short.valueOf(K)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte computeIfPresent(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(computeByteIfPresent(key.shortValue(), mappingFunction instanceof ShortByteUnaryOperator ? (ShortByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte merge(Short key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Byte.valueOf(mergeByte(key.shortValue(), value.byteValue(), mappingFunction instanceof ByteByteUnaryOperator ? (ByteByteUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), Byte.valueOf(V)).byteValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ShortByteConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Byte> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ShortByteConsumer ? (ShortByteConsumer)action : (K, V) -> action.accept(Short.valueOf(K), Byte.valueOf(V)));
	}
	
	@Override
	public ShortSet keySet();
	@Override
	public ByteCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Short, Byte>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> short2ByteEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Short2ByteMaps#synchronize
	 */
	public default Short2ByteMap synchronize() { return Short2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Short2ByteMaps#synchronize
	 */
	public default Short2ByteMap synchronize(Object mutex) { return Short2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Short2ByteMaps#unmodifiable
	 */
	public default Short2ByteMap unmodifiable() { return Short2ByteMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Byte put(Short key, Byte value) {
		return Byte.valueOf(put(key.shortValue(), value.byteValue()));
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Short key, Byte value) {
		return Byte.valueOf(put(key.shortValue(), value.byteValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Short2ByteMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Short2ByteMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Short2ByteMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Short, Byte>
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
		public BuilderCache put(short key, byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Short key, Byte value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2ByteOpenHashMap map() {
			return new Short2ByteOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2ByteOpenHashMap map(int size) {
			return new Short2ByteOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2ByteOpenHashMap map(short[] keys, byte[] values) {
			return new Short2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2ByteOpenHashMap map(Short[] keys, Byte[] values) {
			return new Short2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2ByteOpenHashMap map(Short2ByteMap map) {
			return new Short2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteOpenHashMap map(Map<? extends Short, ? extends Byte> map) {
			return new Short2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Short2ByteLinkedOpenHashMap linkedMap() {
			return new Short2ByteLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Short2ByteLinkedOpenHashMap linkedMap(int size) {
			return new Short2ByteLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Short2ByteLinkedOpenHashMap linkedMap(short[] keys, byte[] values) {
			return new Short2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2ByteLinkedOpenHashMap linkedMap(Short[] keys, Byte[] values) {
			return new Short2ByteLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2ByteLinkedOpenHashMap linkedMap(Short2ByteMap map) {
			return new Short2ByteLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2ByteOpenHashMap linkedMap(Map<? extends Short, ? extends Byte> map) {
			return new ImmutableShort2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableShort2ByteOpenHashMap immutable(short[] keys, byte[] values) {
			return new ImmutableShort2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableShort2ByteOpenHashMap immutable(Short[] keys, Byte[] values) {
			return new ImmutableShort2ByteOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableShort2ByteOpenHashMap immutable(Short2ByteMap map) {
			return new ImmutableShort2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2ByteOpenHashMap immutable(Map<? extends Short, ? extends Byte> map) {
			return new ImmutableShort2ByteOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Short2ByteOpenCustomHashMap customMap(ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Short2ByteOpenCustomHashMap customMap(int size, ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Short2ByteOpenCustomHashMap customMap(short[] keys, byte[] values, ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(keys, values, strategy);
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
		public Short2ByteOpenCustomHashMap customMap(Short[] keys, Byte[] values, ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Short2ByteOpenCustomHashMap customMap(Short2ByteMap map, ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteOpenCustomHashMap customMap(Map<? extends Short, ? extends Byte> map, ShortStrategy strategy) {
			return new Short2ByteOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(int size, ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(short[] keys, byte[] values, ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(keys, values, strategy);
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
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(Short[] keys, Byte[] values, ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(Short2ByteMap map, ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(Map<? extends Short, ? extends Byte> map, ShortStrategy strategy) {
			return new Short2ByteLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2ByteArrayMap arrayMap() {
			return new Short2ByteArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2ByteArrayMap arrayMap(int size) {
			return new Short2ByteArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2ByteArrayMap arrayMap(short[] keys, byte[] values) {
			return new Short2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2ByteArrayMap arrayMap(Short[] keys, Byte[] values) {
			return new Short2ByteArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2ByteArrayMap arrayMap(Short2ByteMap map) {
			return new Short2ByteArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteArrayMap arrayMap(Map<? extends Short, ? extends Byte> map) {
			return new Short2ByteArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Short2ByteRBTreeMap rbTreeMap() {
			return new Short2ByteRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Short2ByteRBTreeMap rbTreeMap(ShortComparator comp) {
			return new Short2ByteRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Short2ByteRBTreeMap rbTreeMap(short[] keys, byte[] values, ShortComparator comp) {
			return new Short2ByteRBTreeMap(keys, values, comp);
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
		public Short2ByteRBTreeMap rbTreeMap(Short[] keys, Byte[] values, ShortComparator comp) {
			return new Short2ByteRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Short2ByteRBTreeMap rbTreeMap(Short2ByteMap map, ShortComparator comp) {
			return new Short2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteRBTreeMap rbTreeMap(Map<? extends Short, ? extends Byte> map, ShortComparator comp) {
			return new Short2ByteRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Short2ByteAVLTreeMap avlTreeMap() {
			return new Short2ByteAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Short2ByteAVLTreeMap avlTreeMap(ShortComparator comp) {
			return new Short2ByteAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Short2ByteAVLTreeMap avlTreeMap(short[] keys, byte[] values, ShortComparator comp) {
			return new Short2ByteAVLTreeMap(keys, values, comp);
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
		public Short2ByteAVLTreeMap avlTreeMap(Short[] keys, Byte[] values, ShortComparator comp) {
			return new Short2ByteAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Short2ByteAVLTreeMap avlTreeMap(Short2ByteMap map, ShortComparator comp) {
			return new Short2ByteAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2ByteAVLTreeMap avlTreeMap(Map<? extends Short, ? extends Byte> map, ShortComparator comp) {
			return new Short2ByteAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		short[] keys;
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
			keys = new short[initialSize];
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
		public BuilderCache put(short key, byte value) {
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
		public BuilderCache put(Short key, Byte value) {
			return put(key.shortValue(), value.byteValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Short2ByteMap.Entry entry) {
			return put(entry.getShortKey(), entry.getByteValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Short2ByteMap map) {
			return putAll(Short2ByteMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Short, ? extends Byte> map) {
			for(Map.Entry<? extends Short, ? extends Byte> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Short2ByteMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Short2ByteMap.Entry>)c).size());
			
			for(Short2ByteMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Short2ByteMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Short2ByteOpenHashMap
		 */
		public Short2ByteOpenHashMap map() {
			return putElements(new Short2ByteOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Short2ByteLinkedOpenHashMap
		 */
		public Short2ByteLinkedOpenHashMap linkedMap() {
			return putElements(new Short2ByteLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableShort2ByteOpenHashMap
		 */
		public ImmutableShort2ByteOpenHashMap immutable() {
			return new ImmutableShort2ByteOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2ByteOpenCustomHashMap
		 */
		public Short2ByteOpenCustomHashMap customMap(ShortStrategy strategy) {
			return putElements(new Short2ByteOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2ByteLinkedOpenCustomHashMap
		 */
		public Short2ByteLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return putElements(new Short2ByteLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Short2ByteConcurrentOpenHashMap
		 */
		public Short2ByteConcurrentOpenHashMap concurrentMap() {
			return putElements(new Short2ByteConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Short2ByteArrayMap
		 */
		public Short2ByteArrayMap arrayMap() {
			return new Short2ByteArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Short2ByteRBTreeMap
		 */
		public Short2ByteRBTreeMap rbTreeMap() {
			return putElements(new Short2ByteRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2ByteRBTreeMap
		 */
		public Short2ByteRBTreeMap rbTreeMap(ShortComparator comp) {
			return putElements(new Short2ByteRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Short2ByteAVLTreeMap
		 */
		public Short2ByteAVLTreeMap avlTreeMap() {
			return putElements(new Short2ByteAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2ByteAVLTreeMap
		 */
		public Short2ByteAVLTreeMap avlTreeMap(ShortComparator comp) {
			return putElements(new Short2ByteAVLTreeMap(comp));
		}
	}
}