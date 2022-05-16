package speiger.src.collections.bytes.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.bytes.functions.consumer.ByteObjectConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ObjectFunction;
import speiger.src.collections.bytes.functions.function.ByteObjectUnaryOperator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2ObjectLinkedOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.customHash.Byte2ObjectOpenCustomHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ObjectLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ObjectOpenHashMap;
import speiger.src.collections.bytes.maps.impl.immutable.ImmutableByte2ObjectOpenHashMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ObjectAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ObjectRBTreeMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2ObjectArrayMap;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.collections.bytes.utils.maps.Byte2ObjectMaps;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <V> the type of elements maintained by this Collection
 */
public interface Byte2ObjectMap<V> extends Map<Byte, V>, Byte2ObjectFunction<V>
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
	public V getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Byte2ObjectMap<V> setDefaultReturnValue(V v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Byte2ObjectMap<V> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public V put(byte key, V value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(byte[] keys, V[] values) {
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
	public void putAll(byte[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Byte[] keys, V[] values) {
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
	public void putAll(Byte[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public V putIfAbsent(byte key, V value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Byte2ObjectMap<V> m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Byte2ObjectMap<V> m);
	
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
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public V remove(byte key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Byte but just have to support equals with Byte.
	 */
	@Override
	public default V remove(Object key) {
		return key instanceof Byte ? remove(((Byte)key).byteValue()) : getDefaultReturnValue();
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(byte key, V value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Byte && remove(((Byte)key).byteValue(), (V)value);
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public V removeOrDefault(byte key, V defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(byte key, V oldValue, V newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public V replace(byte key, V value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceObjects(Byte2ObjectMap<V> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceObjects(ByteObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public V compute(byte key, ByteObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public V computeIfAbsent(byte key, Byte2ObjectFunction<V> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public V supplyIfAbsent(byte key, ObjectSupplier<V> valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public V computeIfPresent(byte key, ByteObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public V merge(byte key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAll(Byte2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, V oldValue, V newValue) {
		return replace(key.byteValue(), oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Byte key, V value) {
		return replace(key.byteValue(), value);
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public V get(byte key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public V getOrDefault(byte key, V defaultValue);
	
	@Override
	@Deprecated
	public default V get(Object key) {
		return key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue();
	}
	
	@Override
	@Deprecated
	public default V getOrDefault(Object key, V defaultValue) {
		V value = key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue();
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceObjects(mappingFunction instanceof ByteObjectUnaryOperator ? (ByteObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V compute(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return compute(key.byteValue(), mappingFunction instanceof ByteObjectUnaryOperator ? (ByteObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V computeIfAbsent(Byte key, Function<? super Byte, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfAbsent(key.byteValue(), mappingFunction instanceof Byte2ObjectFunction ? (Byte2ObjectFunction<V>)mappingFunction : K -> mappingFunction.apply(Byte.valueOf(K)));
	}
	
	@Override
	@Deprecated
	public default V computeIfPresent(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfPresent(key.byteValue(), mappingFunction instanceof ByteObjectUnaryOperator ? (ByteObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Byte.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V merge(Byte key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Objects.requireNonNull(value);
		return merge(key.byteValue(), value, mappingFunction instanceof ObjectObjectUnaryOperator ? (ObjectObjectUnaryOperator<V, V>)mappingFunction : (K, V) -> mappingFunction.apply(K, V));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ByteObjectConsumer<V> action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super V> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ByteObjectConsumer ? (ByteObjectConsumer<V>)action : (K, V) -> action.accept(Byte.valueOf(K), V));
	}
	
	@Override
	public ByteSet keySet();
	@Override
	public ObjectCollection<V> values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Byte, V>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<V>> byte2ObjectEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Byte2ObjectMaps#synchronize
	 */
	public default Byte2ObjectMap<V> synchronize() { return Byte2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Byte2ObjectMaps#synchronize
	 */
	public default Byte2ObjectMap<V> synchronize(Object mutex) { return Byte2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Byte2ObjectMaps#unmodifiable
	 */
	public default Byte2ObjectMap<V> unmodifiable() { return Byte2ObjectMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default V put(Byte key, V value) {
		return put(key.byteValue(), value);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Byte key, V value) {
		return put(key.byteValue(), value);
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<V> extends ObjectSet<Byte2ObjectMap.Entry<V>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Byte2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Byte2ObjectMap.Entry<V>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <V> the type of elements maintained by this Collection
	 */
	public interface Entry<V> extends Map.Entry<Byte, V>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public byte getByteKey();
		public default Byte getKey() { return Byte.valueOf(getByteKey()); }
		
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
		 * @param <V> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <V> BuilderCache<V> start() {
			return new BuilderCache<V>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <V> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <V> BuilderCache<V> start(int size) {
			return new BuilderCache<V>(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <V> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <V> BuilderCache<V> put(byte key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <V> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <V> BuilderCache<V> put(Byte key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Byte2ObjectOpenHashMap<V> map() {
			return new Byte2ObjectOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Byte2ObjectOpenHashMap<V> map(int size) {
			return new Byte2ObjectOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Byte2ObjectOpenHashMap<V> map(byte[] keys, V[] values) {
			return new Byte2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectOpenHashMap<V> map(Byte[] keys, V[] values) {
			return new Byte2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectOpenHashMap<V> map(Byte2ObjectMap<V> map) {
			return new Byte2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectOpenHashMap<V> map(Map<? extends Byte, ? extends V> map) {
			return new Byte2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <V> Byte2ObjectLinkedOpenHashMap<V> linkedMap() {
			return new Byte2ObjectLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Byte2ObjectLinkedOpenHashMap<V> linkedMap(int size) {
			return new Byte2ObjectLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <V> Byte2ObjectLinkedOpenHashMap<V> linkedMap(byte[] keys, V[] values) {
			return new Byte2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectLinkedOpenHashMap<V> linkedMap(Byte[] keys, V[] values) {
			return new Byte2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectLinkedOpenHashMap<V> linkedMap(Byte2ObjectMap<V> map) {
			return new Byte2ObjectLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableByte2ObjectOpenHashMap<V> linkedMap(Map<? extends Byte, ? extends V> map) {
			return new ImmutableByte2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <V> ImmutableByte2ObjectOpenHashMap<V> immutable(byte[] keys, V[] values) {
			return new ImmutableByte2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> ImmutableByte2ObjectOpenHashMap<V> immutable(Byte[] keys, V[] values) {
			return new ImmutableByte2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <V> ImmutableByte2ObjectOpenHashMap<V> immutable(Byte2ObjectMap<V> map) {
			return new ImmutableByte2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableByte2ObjectOpenHashMap<V> immutable(Map<? extends Byte, ? extends V> map) {
			return new ImmutableByte2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(int size, ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(byte[] keys, V[] values, ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(Byte[] keys, V[] values, ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(Byte2ObjectMap<V> map, ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectOpenCustomHashMap<V> customMap(Map<? extends Byte, ? extends V> map, ByteStrategy strategy) {
			return new Byte2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(int size, ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(byte[] keys, V[] values, ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Byte[] keys, V[] values, ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Byte2ObjectMap<V> map, ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Map<? extends Byte, ? extends V> map, ByteStrategy strategy) {
			return new Byte2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap() {
			return new Byte2ObjectArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap(int size) {
			return new Byte2ObjectArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap(byte[] keys, V[] values) {
			return new Byte2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap(Byte[] keys, V[] values) {
			return new Byte2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap(Byte2ObjectMap<V> map) {
			return new Byte2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectArrayMap<V> arrayMap(Map<? extends Byte, ? extends V> map) {
			return new Byte2ObjectArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap() {
			return new Byte2ObjectRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap(ByteComparator comp) {
			return new Byte2ObjectRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap(byte[] keys, V[] values, ByteComparator comp) {
			return new Byte2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap(Byte[] keys, V[] values, ByteComparator comp) {
			return new Byte2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap(Byte2ObjectMap<V> map, ByteComparator comp) {
			return new Byte2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectRBTreeMap<V> rbTreeMap(Map<? extends Byte, ? extends V> map, ByteComparator comp) {
			return new Byte2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap() {
			return new Byte2ObjectAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap(ByteComparator comp) {
			return new Byte2ObjectAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap(byte[] keys, V[] values, ByteComparator comp) {
			return new Byte2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap(Byte[] keys, V[] values, ByteComparator comp) {
			return new Byte2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap(Byte2ObjectMap<V> map, ByteComparator comp) {
			return new Byte2ObjectAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Byte2ObjectAVLTreeMap<V> avlTreeMap(Map<? extends Byte, ? extends V> map, ByteComparator comp) {
			return new Byte2ObjectAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<V>
	{
		byte[] keys;
		V[] values;
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
			values = (V[])new Object[initialSize];
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
		public BuilderCache<V> put(byte key, V value) {
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
		public BuilderCache<V> put(Byte key, V value) {
			return put(key.byteValue(), value);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<V> put(Byte2ObjectMap.Entry<V> entry) {
			return put(entry.getByteKey(), entry.getValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Byte2ObjectMap<V> map) {
			return putAll(Byte2ObjectMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Map<? extends Byte, ? extends V> map) {
			for(Map.Entry<? extends Byte, ? extends V> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(ObjectIterable<Byte2ObjectMap.Entry<V>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Byte2ObjectMap.Entry<V>>)c).size());
			
			for(Byte2ObjectMap.Entry<V> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Byte2ObjectMap<V>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Byte2ObjectOpenHashMap
		 */
		public Byte2ObjectOpenHashMap<V> map() {
			return putElements(new Byte2ObjectOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Byte2ObjectLinkedOpenHashMap
		 */
		public Byte2ObjectLinkedOpenHashMap<V> linkedMap() {
			return putElements(new Byte2ObjectLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableByte2ObjectOpenHashMap
		 */
		public ImmutableByte2ObjectOpenHashMap<V> immutable() {
			return new ImmutableByte2ObjectOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2ObjectOpenCustomHashMap
		 */
		public Byte2ObjectOpenCustomHashMap<V> customMap(ByteStrategy strategy) {
			return putElements(new Byte2ObjectOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Byte2ObjectLinkedOpenCustomHashMap
		 */
		public Byte2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(ByteStrategy strategy) {
			return putElements(new Byte2ObjectLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Byte2ObjectConcurrentOpenHashMap
		 */
		public Byte2ObjectConcurrentOpenHashMap<V> concurrentMap() {
			return putElements(new Byte2ObjectConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Byte2ObjectArrayMap
		 */
		public Byte2ObjectArrayMap<V> arrayMap() {
			return new Byte2ObjectArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Byte2ObjectRBTreeMap
		 */
		public Byte2ObjectRBTreeMap<V> rbTreeMap() {
			return putElements(new Byte2ObjectRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2ObjectRBTreeMap
		 */
		public Byte2ObjectRBTreeMap<V> rbTreeMap(ByteComparator comp) {
			return putElements(new Byte2ObjectRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Byte2ObjectAVLTreeMap
		 */
		public Byte2ObjectAVLTreeMap<V> avlTreeMap() {
			return putElements(new Byte2ObjectAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Byte2ObjectAVLTreeMap
		 */
		public Byte2ObjectAVLTreeMap<V> avlTreeMap(ByteComparator comp) {
			return putElements(new Byte2ObjectAVLTreeMap<>(comp));
		}
	}
}