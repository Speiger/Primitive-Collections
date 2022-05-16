package speiger.src.collections.doubles.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.doubles.functions.consumer.DoubleObjectConsumer;
import speiger.src.collections.doubles.functions.function.Double2ObjectFunction;
import speiger.src.collections.doubles.functions.function.DoubleObjectUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.maps.impl.customHash.Double2ObjectLinkedOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.customHash.Double2ObjectOpenCustomHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ObjectLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ObjectOpenHashMap;
import speiger.src.collections.doubles.maps.impl.immutable.ImmutableDouble2ObjectOpenHashMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ObjectAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ObjectRBTreeMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2ObjectArrayMap;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.collections.doubles.utils.maps.Double2ObjectMaps;
import speiger.src.collections.doubles.sets.DoubleSet;
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
public interface Double2ObjectMap<V> extends Map<Double, V>, Double2ObjectFunction<V>
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
	public Double2ObjectMap<V> setDefaultReturnValue(V v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Double2ObjectMap<V> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public V put(double key, V value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(double[] keys, V[] values) {
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
	public void putAll(double[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Double[] keys, V[] values) {
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
	public void putAll(Double[] keys, V[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public V putIfAbsent(double key, V value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Double2ObjectMap<V> m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Double2ObjectMap<V> m);
	
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
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public V remove(double key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Double but just have to support equals with Double.
	 */
	@Override
	public default V remove(Object key) {
		return key instanceof Double ? remove(((Double)key).doubleValue()) : getDefaultReturnValue();
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(double key, V value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Double && remove(((Double)key).doubleValue(), (V)value);
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public V removeOrDefault(double key, V defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(double key, V oldValue, V newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public V replace(double key, V value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceObjects(Double2ObjectMap<V> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceObjects(DoubleObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public V compute(double key, DoubleObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public V computeIfAbsent(double key, Double2ObjectFunction<V> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public V supplyIfAbsent(double key, ObjectSupplier<V> valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public V computeIfPresent(double key, DoubleObjectUnaryOperator<V> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public V merge(double key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAll(Double2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Double key, V oldValue, V newValue) {
		return replace(key.doubleValue(), oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Double key, V value) {
		return replace(key.doubleValue(), value);
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public V get(double key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public V getOrDefault(double key, V defaultValue);
	
	@Override
	@Deprecated
	public default V get(Object key) {
		return key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue();
	}
	
	@Override
	@Deprecated
	public default V getOrDefault(Object key, V defaultValue) {
		V value = key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue();
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceObjects(mappingFunction instanceof DoubleObjectUnaryOperator ? (DoubleObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V compute(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return compute(key.doubleValue(), mappingFunction instanceof DoubleObjectUnaryOperator ? (DoubleObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V computeIfAbsent(Double key, Function<? super Double, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfAbsent(key.doubleValue(), mappingFunction instanceof Double2ObjectFunction ? (Double2ObjectFunction<V>)mappingFunction : K -> mappingFunction.apply(Double.valueOf(K)));
	}
	
	@Override
	@Deprecated
	public default V computeIfPresent(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return computeIfPresent(key.doubleValue(), mappingFunction instanceof DoubleObjectUnaryOperator ? (DoubleObjectUnaryOperator<V>)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), V));
	}
	
	@Override
	@Deprecated
	public default V merge(Double key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Objects.requireNonNull(value);
		return merge(key.doubleValue(), value, mappingFunction instanceof ObjectObjectUnaryOperator ? (ObjectObjectUnaryOperator<V, V>)mappingFunction : (K, V) -> mappingFunction.apply(K, V));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(DoubleObjectConsumer<V> action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super V> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof DoubleObjectConsumer ? (DoubleObjectConsumer<V>)action : (K, V) -> action.accept(Double.valueOf(K), V));
	}
	
	@Override
	public DoubleSet keySet();
	@Override
	public ObjectCollection<V> values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Double, V>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<V>> double2ObjectEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Double2ObjectMaps#synchronize
	 */
	public default Double2ObjectMap<V> synchronize() { return Double2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Double2ObjectMaps#synchronize
	 */
	public default Double2ObjectMap<V> synchronize(Object mutex) { return Double2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Double2ObjectMaps#unmodifiable
	 */
	public default Double2ObjectMap<V> unmodifiable() { return Double2ObjectMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default V put(Double key, V value) {
		return put(key.doubleValue(), value);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Double key, V value) {
		return put(key.doubleValue(), value);
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<V> extends ObjectSet<Double2ObjectMap.Entry<V>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Double2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Double2ObjectMap.Entry<V>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <V> the type of elements maintained by this Collection
	 */
	public interface Entry<V> extends Map.Entry<Double, V>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public double getDoubleKey();
		public default Double getKey() { return Double.valueOf(getDoubleKey()); }
		
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
		public <V> BuilderCache<V> put(double key, V value) {
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
		public <V> BuilderCache<V> put(Double key, V value) {
			return new BuilderCache<V>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Double2ObjectOpenHashMap<V> map() {
			return new Double2ObjectOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Double2ObjectOpenHashMap<V> map(int size) {
			return new Double2ObjectOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Double2ObjectOpenHashMap<V> map(double[] keys, V[] values) {
			return new Double2ObjectOpenHashMap<>(keys, values);
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
		public <V> Double2ObjectOpenHashMap<V> map(Double[] keys, V[] values) {
			return new Double2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectOpenHashMap<V> map(Double2ObjectMap<V> map) {
			return new Double2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectOpenHashMap<V> map(Map<? extends Double, ? extends V> map) {
			return new Double2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <V> Double2ObjectLinkedOpenHashMap<V> linkedMap() {
			return new Double2ObjectLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Double2ObjectLinkedOpenHashMap<V> linkedMap(int size) {
			return new Double2ObjectLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <V> Double2ObjectLinkedOpenHashMap<V> linkedMap(double[] keys, V[] values) {
			return new Double2ObjectLinkedOpenHashMap<>(keys, values);
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
		public <V> Double2ObjectLinkedOpenHashMap<V> linkedMap(Double[] keys, V[] values) {
			return new Double2ObjectLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectLinkedOpenHashMap<V> linkedMap(Double2ObjectMap<V> map) {
			return new Double2ObjectLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableDouble2ObjectOpenHashMap<V> linkedMap(Map<? extends Double, ? extends V> map) {
			return new ImmutableDouble2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <V> ImmutableDouble2ObjectOpenHashMap<V> immutable(double[] keys, V[] values) {
			return new ImmutableDouble2ObjectOpenHashMap<>(keys, values);
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
		public <V> ImmutableDouble2ObjectOpenHashMap<V> immutable(Double[] keys, V[] values) {
			return new ImmutableDouble2ObjectOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <V> ImmutableDouble2ObjectOpenHashMap<V> immutable(Double2ObjectMap<V> map) {
			return new ImmutableDouble2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> ImmutableDouble2ObjectOpenHashMap<V> immutable(Map<? extends Double, ? extends V> map) {
			return new ImmutableDouble2ObjectOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(int size, DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(size, strategy);
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
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(double[] keys, V[] values, DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(keys, values, strategy);
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
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(Double[] keys, V[] values, DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(Double2ObjectMap<V> map, DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectOpenCustomHashMap<V> customMap(Map<? extends Double, ? extends V> map, DoubleStrategy strategy) {
			return new Double2ObjectOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(int size, DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(size, strategy);
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
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(double[] keys, V[] values, DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
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
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Double[] keys, V[] values, DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Double2ObjectMap<V> map, DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <V> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(Map<? extends Double, ? extends V> map, DoubleStrategy strategy) {
			return new Double2ObjectLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <V> Double2ObjectArrayMap<V> arrayMap() {
			return new Double2ObjectArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <V> Double2ObjectArrayMap<V> arrayMap(int size) {
			return new Double2ObjectArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <V> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <V> Double2ObjectArrayMap<V> arrayMap(double[] keys, V[] values) {
			return new Double2ObjectArrayMap<>(keys, values);
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
		public <V> Double2ObjectArrayMap<V> arrayMap(Double[] keys, V[] values) {
			return new Double2ObjectArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectArrayMap<V> arrayMap(Double2ObjectMap<V> map) {
			return new Double2ObjectArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <V> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectArrayMap<V> arrayMap(Map<? extends Double, ? extends V> map) {
			return new Double2ObjectArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap() {
			return new Double2ObjectRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap(DoubleComparator comp) {
			return new Double2ObjectRBTreeMap<>(comp);
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
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap(double[] keys, V[] values, DoubleComparator comp) {
			return new Double2ObjectRBTreeMap<>(keys, values, comp);
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
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap(Double[] keys, V[] values, DoubleComparator comp) {
			return new Double2ObjectRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap(Double2ObjectMap<V> map, DoubleComparator comp) {
			return new Double2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectRBTreeMap<V> rbTreeMap(Map<? extends Double, ? extends V> map, DoubleComparator comp) {
			return new Double2ObjectRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap() {
			return new Double2ObjectAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap(DoubleComparator comp) {
			return new Double2ObjectAVLTreeMap<>(comp);
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
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap(double[] keys, V[] values, DoubleComparator comp) {
			return new Double2ObjectAVLTreeMap<>(keys, values, comp);
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
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap(Double[] keys, V[] values, DoubleComparator comp) {
			return new Double2ObjectAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap(Double2ObjectMap<V> map, DoubleComparator comp) {
			return new Double2ObjectAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <V> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <V> Double2ObjectAVLTreeMap<V> avlTreeMap(Map<? extends Double, ? extends V> map, DoubleComparator comp) {
			return new Double2ObjectAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<V>
	{
		double[] keys;
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
			keys = new double[initialSize];
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
		public BuilderCache<V> put(double key, V value) {
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
		public BuilderCache<V> put(Double key, V value) {
			return put(key.doubleValue(), value);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<V> put(Double2ObjectMap.Entry<V> entry) {
			return put(entry.getDoubleKey(), entry.getValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Double2ObjectMap<V> map) {
			return putAll(Double2ObjectMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(Map<? extends Double, ? extends V> map) {
			for(Map.Entry<? extends Double, ? extends V> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<V> putAll(ObjectIterable<Double2ObjectMap.Entry<V>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Double2ObjectMap.Entry<V>>)c).size());
			
			for(Double2ObjectMap.Entry<V> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Double2ObjectMap<V>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Double2ObjectOpenHashMap
		 */
		public Double2ObjectOpenHashMap<V> map() {
			return putElements(new Double2ObjectOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Double2ObjectLinkedOpenHashMap
		 */
		public Double2ObjectLinkedOpenHashMap<V> linkedMap() {
			return putElements(new Double2ObjectLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableDouble2ObjectOpenHashMap
		 */
		public ImmutableDouble2ObjectOpenHashMap<V> immutable() {
			return new ImmutableDouble2ObjectOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2ObjectOpenCustomHashMap
		 */
		public Double2ObjectOpenCustomHashMap<V> customMap(DoubleStrategy strategy) {
			return putElements(new Double2ObjectOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Double2ObjectLinkedOpenCustomHashMap
		 */
		public Double2ObjectLinkedOpenCustomHashMap<V> customLinkedMap(DoubleStrategy strategy) {
			return putElements(new Double2ObjectLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Double2ObjectConcurrentOpenHashMap
		 */
		public Double2ObjectConcurrentOpenHashMap<V> concurrentMap() {
			return putElements(new Double2ObjectConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Double2ObjectArrayMap
		 */
		public Double2ObjectArrayMap<V> arrayMap() {
			return new Double2ObjectArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Double2ObjectRBTreeMap
		 */
		public Double2ObjectRBTreeMap<V> rbTreeMap() {
			return putElements(new Double2ObjectRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2ObjectRBTreeMap
		 */
		public Double2ObjectRBTreeMap<V> rbTreeMap(DoubleComparator comp) {
			return putElements(new Double2ObjectRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Double2ObjectAVLTreeMap
		 */
		public Double2ObjectAVLTreeMap<V> avlTreeMap() {
			return putElements(new Double2ObjectAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Double2ObjectAVLTreeMap
		 */
		public Double2ObjectAVLTreeMap<V> avlTreeMap(DoubleComparator comp) {
			return putElements(new Double2ObjectAVLTreeMap<>(comp));
		}
	}
}