package speiger.src.collections.shorts.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.shorts.functions.consumer.ShortObjectConsumer;
import speiger.src.collections.shorts.functions.function.ShortFunction;
import speiger.src.collections.shorts.functions.function.ShortObjectUnaryOperator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.maps.impl.customHash.Short2ObjectLinkedOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.customHash.Short2ObjectOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectOpenHashMap;
import speiger.src.collections.shorts.maps.impl.immutable.ImmutableShort2ObjectOpenHashMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ObjectAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ObjectRBTreeMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2ObjectArrayMap;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.collections.shorts.utils.maps.Short2ObjectMaps;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface Short2ObjectMap<V> extends Map<Short, V>, ShortFunction<V>
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
	public Short2ObjectMap<V> setDefaultReturnValue(V v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Short2ObjectMap<V> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public V put(short key, V value);
	
	/**
	 * A Helper method that allows to put int a Short2ObjectMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default V put(Entry<V> entry) {
		return put(entry.getShortKey(), entry.getValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default V put(Map.Entry<Short, V> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(short[] keys, V[] values) {
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
	public void putAll(short[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Short[] keys, V[] values) {
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
	public void putAll(Short[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public V putIfAbsent(short key, V value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Short2ObjectMap<V> m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Short2ObjectMap<V> m);
	
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
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public V remove(short key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default V remove(Object key) {
		return key instanceof Short ? remove(((Short)key).shortValue()) : getDefaultReturnValue();
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(short key, V value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Short && remove(((Short)key).shortValue(), (V)value);
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public V removeOrDefault(short key, V defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(short key, V oldValue, V newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public V replace(short key, V value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceObjects(Short2ObjectMap<V> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceObjects(ShortObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public V compute(short key, ShortObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public V computeIfAbsent(short key, ShortFunction<V> mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public V supplyIfAbsent(short key, ObjectSupplier<V> valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public V computeIfPresent(short key, ShortObjectUnaryOperator<V> mappingFunction);
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
	public V merge(short key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAll(Short2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Short key, V oldValue, V newValue) {
		return replace(key.shortValue(), oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Short key, V value) {
		return replace(key.shortValue(), value);
	}
	
	@Override
	public default V apply(short key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public V get(short key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public V getOrDefault(short key, V defaultValue);
	
	@Override
	@Deprecated
	public default V get(Object key) {
		return key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue();
	}
	
	@Override
	@Deprecated
	public default V getOrDefault(Object key, V defaultValue) {
		V value = key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue();
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceObjects(mappingFunction instanceof ShortObjectUnaryOperator ? (ShortObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V compute(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return compute(key.shortValue(), mappingFunction instanceof ShortObjectUnaryOperator ? (ShortObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V computeIfAbsent(Short key, Function<? super Short, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfAbsent(key.shortValue(), mappingFunction instanceof ShortFunction ? (ShortFunction<V>)mappingFunction : K -> mappingFunction.apply(Short.valueOf(K)));
	}
	
	@Override
	@Deprecated
	public default V computeIfPresent(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfPresent(key.shortValue(), mappingFunction instanceof ShortObjectUnaryOperator ? (ShortObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V merge(Short key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Objects.requireNonNull(value);
		return merge(key.shortValue(), value, mappingFunction instanceof ObjectObjectUnaryOperator ? (ObjectObjectUnaryOperator<V, V>)mappingFunction : (K, V) -> mappingFunction.apply(K, V));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ShortObjectConsumer<V> action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super V> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ShortObjectConsumer ? (ShortObjectConsumer<V>)action : (K, V) -> action.accept(Short.valueOf(K), V));
	}
	
	@Override
	public ShortSet keySet();
	@Override
	public ObjectCollection<V> values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Short, V>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<V>> short2ObjectEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Short2ObjectMaps#synchronize
	 */
	public default Short2ObjectMap<V> synchronize() { return Short2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Short2ObjectMaps#synchronize
	 */
	public default Short2ObjectMap<V> synchronize(Object mutex) { return Short2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Short2ObjectMaps#unmodifiable
	 */
	public default Short2ObjectMap<V> unmodifiable() { return Short2ObjectMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default V put(Short key, V value) {
		return put(key.shortValue(), value);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Short key, V value) {
		return put(key.shortValue(), value);
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public interface FastEntrySet<V> extends ObjectSet<Short2ObjectMap.Entry<V>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Short2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Short2ObjectMap.Entry<V>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public interface Entry<V> extends Map.Entry<Short, V>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public short getShortKey();
		public default Short getKey() { return Short.valueOf(getShortKey()); }
		
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
		 * @param <V> the keyType of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <V> BuilderCache<V> start() {
			return new BuilderCache<V>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <V> the keyType of elements maintained by this Collection
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
		 * @param <V> the keyType of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <V> BuilderCache<V> put(short key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <V> the keyType of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <V> BuilderCache<V> put(Short key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Short2ObjectOpenHashMap<V> map() {
			return new Short2ObjectOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Short2ObjectOpenHashMap<V> map(int size) {
			return new Short2ObjectOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Short2ObjectOpenHashMap<V> map(short[] keys, V[] values) {
			return new Short2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectOpenHashMap<V> map(Short[] keys, V[] values) {
			return new Short2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectOpenHashMap<V> map(Short2ObjectMap<V> map) {
			return new Short2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectOpenHashMap<V> map(Map<? extends Short, ? extends V> map) {
			return new Short2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <V> Short2ObjectLinkedOpenHashMap<V> linkedMap() {
			return new Short2ObjectLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Short2ObjectLinkedOpenHashMap<V> linkedMap(int size) {
			return new Short2ObjectLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <V> Short2ObjectLinkedOpenHashMap<V> linkedMap(short[] keys, V[] values) {
			return new Short2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectLinkedOpenHashMap<V> linkedMap(Short[] keys, V[] values) {
			return new Short2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectLinkedOpenHashMap<V> linkedMap(Short2ObjectMap<V> map) {
			return new Short2ObjectLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableShort2ObjectOpenHashMap<V> linkedMap(Map<? extends Short, ? extends V> map) {
			return new ImmutableShort2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <V> ImmutableShort2ObjectOpenHashMap<V> immutable(short[] keys, V[] values) {
			return new ImmutableShort2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> ImmutableShort2ObjectOpenHashMap<V> immutable(Short[] keys, V[] values) {
			return new ImmutableShort2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <V> ImmutableShort2ObjectOpenHashMap<V> immutable(Short2ObjectMap<V> map) {
			return new ImmutableShort2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableShort2ObjectOpenHashMap<V> immutable(Map<? extends Short, ? extends V> map) {
			return new ImmutableShort2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(int size, ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(short[] keys, V[] values, ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(Short[] keys, V[] values, ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(Short2ObjectMap<V> map, ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectOpenCustomHashMap<V> customMap(Map<? extends Short, ? extends V> map, ShortStrategy strategy) {
			return new Short2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(int size, ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(short[] keys, V[] values, ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Short[] keys, V[] values, ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Short2ObjectMap<V> map, ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Map<? extends Short, ? extends V> map, ShortStrategy strategy) {
			return new Short2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap() {
			return new Short2ObjectArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap(int size) {
			return new Short2ObjectArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap(short[] keys, V[] values) {
			return new Short2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap(Short[] keys, V[] values) {
			return new Short2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap(Short2ObjectMap<V> map) {
			return new Short2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectArrayMap<V> arrayMap(Map<? extends Short, ? extends V> map) {
			return new Short2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap() {
			return new Short2ObjectRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap(ShortComparator comp) {
			return new Short2ObjectRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap(short[] keys, V[] values, ShortComparator comp) {
			return new Short2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap(Short[] keys, V[] values, ShortComparator comp) {
			return new Short2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap(Short2ObjectMap<V> map, ShortComparator comp) {
			return new Short2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectRBTreeMap<V> rbTreeMap(Map<? extends Short, ? extends V> map, ShortComparator comp) {
			return new Short2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap() {
			return new Short2ObjectAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap(ShortComparator comp) {
			return new Short2ObjectAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap(short[] keys, V[] values, ShortComparator comp) {
			return new Short2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap(Short[] keys, V[] values, ShortComparator comp) {
			return new Short2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap(Short2ObjectMap<V> map, ShortComparator comp) {
			return new Short2ObjectAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Short2ObjectAVLTreeMap<V> avlTreeMap(Map<? extends Short, ? extends V> map, ShortComparator comp) {
			return new Short2ObjectAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BuilderCache<V>
	{
		short[] keys;
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
			if(initialSize < 0) throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			keys = new short[initialSize];
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
		public BuilderCache<V> put(short key, V value) {
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
		public BuilderCache<V> put(Short key, V value) {
			return put(key.shortValue(), value);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<V> put(Short2ObjectMap.Entry<V> entry) {
			return put(entry.getShortKey(), entry.getValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Short2ObjectMap<V> map) {
			return putAll(Short2ObjectMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Map<? extends Short, ? extends V> map) {
			for(Map.Entry<? extends Short, ? extends V> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(ObjectIterable<Short2ObjectMap.Entry<V>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Short2ObjectMap.Entry<V>>)c).size());
			
			for(Short2ObjectMap.Entry<V> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Short2ObjectMap<V>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Short2ObjectOpenHashMap
		 */
		public Short2ObjectOpenHashMap<V> map() {
			return putElements(new Short2ObjectOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Short2ObjectLinkedOpenHashMap
		 */
		public Short2ObjectLinkedOpenHashMap<V> linkedMap() {
			return putElements(new Short2ObjectLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableShort2ObjectOpenHashMap
		 */
		public ImmutableShort2ObjectOpenHashMap<V> immutable() {
			return new ImmutableShort2ObjectOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2ObjectOpenCustomHashMap
		 */
		public Short2ObjectOpenCustomHashMap<V> customMap(ShortStrategy strategy) {
			return putElements(new Short2ObjectOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2ObjectLinkedOpenCustomHashMap
		 */
		public Short2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(ShortStrategy strategy) {
			return putElements(new Short2ObjectLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Short2ObjectConcurrentOpenHashMap
		 */
		public Short2ObjectConcurrentOpenHashMap<V> concurrentMap() {
			return putElements(new Short2ObjectConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Short2ObjectArrayMap
		 */
		public Short2ObjectArrayMap<V> arrayMap() {
			return new Short2ObjectArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Short2ObjectRBTreeMap
		 */
		public Short2ObjectRBTreeMap<V> rbTreeMap() {
			return putElements(new Short2ObjectRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2ObjectRBTreeMap
		 */
		public Short2ObjectRBTreeMap<V> rbTreeMap(ShortComparator comp) {
			return putElements(new Short2ObjectRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Short2ObjectAVLTreeMap
		 */
		public Short2ObjectAVLTreeMap<V> avlTreeMap() {
			return putElements(new Short2ObjectAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2ObjectAVLTreeMap
		 */
		public Short2ObjectAVLTreeMap<V> avlTreeMap(ShortComparator comp) {
			return putElements(new Short2ObjectAVLTreeMap<>(comp));
		}
	}
}