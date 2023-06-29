package speiger.src.collections.floats.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.floats.functions.consumer.FloatObjectConsumer;
import speiger.src.collections.floats.functions.function.FloatFunction;
import speiger.src.collections.floats.functions.function.FloatObjectUnaryOperator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.impl.customHash.Float2ObjectLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2ObjectOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ObjectLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ObjectOpenHashMap;
import speiger.src.collections.floats.maps.impl.immutable.ImmutableFloat2ObjectOpenHashMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ObjectAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ObjectRBTreeMap;
import speiger.src.collections.floats.maps.impl.misc.Float2ObjectArrayMap;
import speiger.src.collections.floats.maps.impl.concurrent.Float2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.collections.floats.utils.maps.Float2ObjectMaps;
import speiger.src.collections.floats.sets.FloatSet;
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
public interface Float2ObjectMap<V> extends Map<Float, V>, FloatFunction<V>
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
	public Float2ObjectMap<V> setDefaultReturnValue(V v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Float2ObjectMap<V> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public V put(float key, V value);
	
	/**
	 * A Helper method that allows to put int a Float2ObjectMap.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return value.
	 */
	public default V put(Entry<V> entry) {
		return put(entry.getFloatKey(), entry.getValue());
	}
	
	/**
	 * A Helper method that allows to put int a Map.Entry into a map.
	 * @param entry then Entry that should be inserted.
	 * @return the last present value or default return	value.
	 */
	public default V put(Map.Entry<Float, V> entry) {
		return put(entry.getKey(), entry.getValue());
	}

	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(float[] keys, V[] values) {
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
	public void putAll(float[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Float[] keys, V[] values) {
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
	public void putAll(Float[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public V putIfAbsent(float key, V value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Float2ObjectMap<V> m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Float2ObjectMap<V> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(float key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Float && containsKey(((Float)key).floatValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public V remove(float key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Float but just have to support equals with Float.
	 */
	@Override
	public default V remove(Object key) {
		return key instanceof Float ? remove(((Float)key).floatValue()) : getDefaultReturnValue();
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(float key, V value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Float && remove(((Float)key).floatValue(), (V)value);
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public V removeOrDefault(float key, V defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(float key, V oldValue, V newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public V replace(float key, V value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceObjects(Float2ObjectMap<V> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceObjects(FloatObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public V compute(float key, FloatObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific computeIfAbsent method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public V computeIfAbsent(float key, FloatFunction<V> mappingFunction);
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public V supplyIfAbsent(float key, ObjectSupplier<V> valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction);
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
	public V merge(float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * If the generated value equals the getDefaultReturnValue it will simply not insert it since that is treated as "null".
	 * A "Null Value" will be treated as "Do not insert/remove" based on how the Java has specified it.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAll(Float2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Float key, V oldValue, V newValue) {
		return replace(key.floatValue(), oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Float key, V value) {
		return replace(key.floatValue(), value);
	}
	
	@Override
	public default V apply(float key) {
		return get(key);
	}
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	public V get(float key);
	
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public V getOrDefault(float key, V defaultValue);
	
	@Override
	@Deprecated
	public default V get(Object key) {
		return key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue();
	}
	
	@Override
	@Deprecated
	public default V getOrDefault(Object key, V defaultValue) {
		V value = key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue();
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceObjects(mappingFunction instanceof FloatObjectUnaryOperator ? (FloatObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V compute(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return compute(key.floatValue(), mappingFunction instanceof FloatObjectUnaryOperator ? (FloatObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V computeIfAbsent(Float key, Function<? super Float, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfAbsent(key.floatValue(), mappingFunction instanceof FloatFunction ? (FloatFunction<V>)mappingFunction : K -> mappingFunction.apply(Float.valueOf(K)));
	}
	
	@Override
	@Deprecated
	public default V computeIfPresent(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfPresent(key.floatValue(), mappingFunction instanceof FloatObjectUnaryOperator ? (FloatObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Float.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V merge(Float key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Objects.requireNonNull(value);
		return merge(key.floatValue(), value, mappingFunction instanceof ObjectObjectUnaryOperator ? (ObjectObjectUnaryOperator<V, V>)mappingFunction : (K, V) -> mappingFunction.apply(K, V));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(FloatObjectConsumer<V> action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super V> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof FloatObjectConsumer ? (FloatObjectConsumer<V>)action : (K, V) -> action.accept(Float.valueOf(K), V));
	}
	
	@Override
	public FloatSet keySet();
	@Override
	public ObjectCollection<V> values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Float, V>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<V>> float2ObjectEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Float2ObjectMaps#synchronize
	 */
	public default Float2ObjectMap<V> synchronize() { return Float2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Float2ObjectMaps#synchronize
	 */
	public default Float2ObjectMap<V> synchronize(Object mutex) { return Float2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Float2ObjectMaps#unmodifiable
	 */
	public default Float2ObjectMap<V> unmodifiable() { return Float2ObjectMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default V put(Float key, V value) {
		return put(key.floatValue(), value);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Float key, V value) {
		return put(key.floatValue(), value);
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public interface FastEntrySet<V> extends ObjectSet<Float2ObjectMap.Entry<V>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Float2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public interface Entry<V> extends Map.Entry<Float, V>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public float getFloatKey();
		public default Float getKey() { return Float.valueOf(getFloatKey()); }
		
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
		public <V> BuilderCache<V> put(float key, V value) {
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
		public <V> BuilderCache<V> put(Float key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Float2ObjectOpenHashMap<V> map() {
			return new Float2ObjectOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Float2ObjectOpenHashMap<V> map(int size) {
			return new Float2ObjectOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Float2ObjectOpenHashMap<V> map(float[] keys, V[] values) {
			return new Float2ObjectOpenHashMap<>(keys, values);
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
		public <V> Float2ObjectOpenHashMap<V> map(Float[] keys, V[] values) {
			return new Float2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectOpenHashMap<V> map(Float2ObjectMap<V> map) {
			return new Float2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectOpenHashMap<V> map(Map<? extends Float, ? extends V> map) {
			return new Float2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <V> Float2ObjectLinkedOpenHashMap<V> linkedMap() {
			return new Float2ObjectLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Float2ObjectLinkedOpenHashMap<V> linkedMap(int size) {
			return new Float2ObjectLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <V> Float2ObjectLinkedOpenHashMap<V> linkedMap(float[] keys, V[] values) {
			return new Float2ObjectLinkedOpenHashMap<>(keys, values);
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
		public <V> Float2ObjectLinkedOpenHashMap<V> linkedMap(Float[] keys, V[] values) {
			return new Float2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectLinkedOpenHashMap<V> linkedMap(Float2ObjectMap<V> map) {
			return new Float2ObjectLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableFloat2ObjectOpenHashMap<V> linkedMap(Map<? extends Float, ? extends V> map) {
			return new ImmutableFloat2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <V> ImmutableFloat2ObjectOpenHashMap<V> immutable(float[] keys, V[] values) {
			return new ImmutableFloat2ObjectOpenHashMap<>(keys, values);
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
		public <V> ImmutableFloat2ObjectOpenHashMap<V> immutable(Float[] keys, V[] values) {
			return new ImmutableFloat2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <V> ImmutableFloat2ObjectOpenHashMap<V> immutable(Float2ObjectMap<V> map) {
			return new ImmutableFloat2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableFloat2ObjectOpenHashMap<V> immutable(Map<? extends Float, ? extends V> map) {
			return new ImmutableFloat2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(int size, FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(size, strategy);
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
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(float[] keys, V[] values, FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(keys, values, strategy);
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
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(Float[] keys, V[] values, FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(Float2ObjectMap<V> map, FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectOpenCustomHashMap<V> customMap(Map<? extends Float, ? extends V> map, FloatStrategy strategy) {
			return new Float2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(int size, FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(size, strategy);
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
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(float[] keys, V[] values, FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
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
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Float[] keys, V[] values, FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Float2ObjectMap<V> map, FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the keyType of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Map<? extends Float, ? extends V> map, FloatStrategy strategy) {
			return new Float2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Float2ObjectArrayMap<V> arrayMap() {
			return new Float2ObjectArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Float2ObjectArrayMap<V> arrayMap(int size) {
			return new Float2ObjectArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the keyType of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Float2ObjectArrayMap<V> arrayMap(float[] keys, V[] values) {
			return new Float2ObjectArrayMap<>(keys, values);
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
		public <V> Float2ObjectArrayMap<V> arrayMap(Float[] keys, V[] values) {
			return new Float2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectArrayMap<V> arrayMap(Float2ObjectMap<V> map) {
			return new Float2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the keyType of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectArrayMap<V> arrayMap(Map<? extends Float, ? extends V> map) {
			return new Float2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap() {
			return new Float2ObjectRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap(FloatComparator comp) {
			return new Float2ObjectRBTreeMap<>(comp);
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
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap(float[] keys, V[] values, FloatComparator comp) {
			return new Float2ObjectRBTreeMap<>(keys, values, comp);
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
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap(Float[] keys, V[] values, FloatComparator comp) {
			return new Float2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap(Float2ObjectMap<V> map, FloatComparator comp) {
			return new Float2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectRBTreeMap<V> rbTreeMap(Map<? extends Float, ? extends V> map, FloatComparator comp) {
			return new Float2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap() {
			return new Float2ObjectAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap(FloatComparator comp) {
			return new Float2ObjectAVLTreeMap<>(comp);
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
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap(float[] keys, V[] values, FloatComparator comp) {
			return new Float2ObjectAVLTreeMap<>(keys, values, comp);
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
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap(Float[] keys, V[] values, FloatComparator comp) {
			return new Float2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap(Float2ObjectMap<V> map, FloatComparator comp) {
			return new Float2ObjectAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the keyType of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Float2ObjectAVLTreeMap<V> avlTreeMap(Map<? extends Float, ? extends V> map, FloatComparator comp) {
			return new Float2ObjectAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BuilderCache<V>
	{
		float[] keys;
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
			keys = new float[initialSize];
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
		public BuilderCache<V> put(float key, V value) {
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
		public BuilderCache<V> put(Float key, V value) {
			return put(key.floatValue(), value);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<V> put(Float2ObjectMap.Entry<V> entry) {
			return put(entry.getFloatKey(), entry.getValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Float2ObjectMap<V> map) {
			return putAll(Float2ObjectMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Map<? extends Float, ? extends V> map) {
			for(Map.Entry<? extends Float, ? extends V> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(ObjectIterable<Float2ObjectMap.Entry<V>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Float2ObjectMap.Entry<V>>)c).size());
			
			for(Float2ObjectMap.Entry<V> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Float2ObjectMap<V>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Float2ObjectOpenHashMap
		 */
		public Float2ObjectOpenHashMap<V> map() {
			return putElements(new Float2ObjectOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Float2ObjectLinkedOpenHashMap
		 */
		public Float2ObjectLinkedOpenHashMap<V> linkedMap() {
			return putElements(new Float2ObjectLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableFloat2ObjectOpenHashMap
		 */
		public ImmutableFloat2ObjectOpenHashMap<V> immutable() {
			return new ImmutableFloat2ObjectOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2ObjectOpenCustomHashMap
		 */
		public Float2ObjectOpenCustomHashMap<V> customMap(FloatStrategy strategy) {
			return putElements(new Float2ObjectOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Float2ObjectLinkedOpenCustomHashMap
		 */
		public Float2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(FloatStrategy strategy) {
			return putElements(new Float2ObjectLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Float2ObjectConcurrentOpenHashMap
		 */
		public Float2ObjectConcurrentOpenHashMap<V> concurrentMap() {
			return putElements(new Float2ObjectConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Float2ObjectArrayMap
		 */
		public Float2ObjectArrayMap<V> arrayMap() {
			return new Float2ObjectArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Float2ObjectRBTreeMap
		 */
		public Float2ObjectRBTreeMap<V> rbTreeMap() {
			return putElements(new Float2ObjectRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2ObjectRBTreeMap
		 */
		public Float2ObjectRBTreeMap<V> rbTreeMap(FloatComparator comp) {
			return putElements(new Float2ObjectRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Float2ObjectAVLTreeMap
		 */
		public Float2ObjectAVLTreeMap<V> avlTreeMap() {
			return putElements(new Float2ObjectAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Float2ObjectAVLTreeMap
		 */
		public Float2ObjectAVLTreeMap<V> avlTreeMap(FloatComparator comp) {
			return putElements(new Float2ObjectAVLTreeMap<>(comp));
		}
	}
}