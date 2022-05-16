package speiger.src.collections.objects.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Comparator;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectBooleanUnaryOperator;
import speiger.src.collections.objects.maps.impl.customHash.Object2BooleanLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2BooleanOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2BooleanLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2BooleanOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2BooleanOpenHashMap;
import speiger.src.collections.objects.maps.impl.tree.Object2BooleanAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2BooleanRBTreeMap;
import speiger.src.collections.objects.maps.impl.misc.Object2BooleanArrayMap;
import speiger.src.collections.objects.maps.impl.concurrent.Object2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2BooleanMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.objects.utils.maps.Object2BooleanMaps;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2BooleanMap<T> extends Map<T, Boolean>, Object2BooleanFunction<T>
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
	public boolean getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Object2BooleanMap<T> setDefaultReturnValue(boolean v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Object2BooleanMap<T> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public boolean put(T key, boolean value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, boolean[] values) {
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
	public void putAll(T[] keys, boolean[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, Boolean[] values) {
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
	public void putAll(T[] keys, Boolean[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public boolean putIfAbsent(T key, boolean value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Object2BooleanMap<T> m);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Object2BooleanMap<T> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(boolean value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Boolean && containsValue(((Boolean)value).booleanValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public boolean rem(T key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be T but just have to support equals with T.
	 */
	@Override
	public default Boolean remove(Object key) {
		return Boolean.valueOf(rem((T)key));
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(T key, boolean value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return value instanceof Boolean && remove((T)key, ((Boolean)value).booleanValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public boolean remOrDefault(T key, boolean defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(T key, boolean oldValue, boolean newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public boolean replace(T key, boolean value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceBooleans(Object2BooleanMap<T> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceBooleans(ObjectBooleanUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public boolean computeBooleanIfAbsent(T key, Object2BooleanFunction<T> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction);
	
	@Override
	public default boolean replace(T key, Boolean oldValue, Boolean newValue) {
		return replace(key, oldValue.booleanValue(), newValue.booleanValue());
	}
	
	@Override
	public default Boolean replace(T key, Boolean value) {
		return Boolean.valueOf(replace(key, value.booleanValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public boolean getBoolean(T key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public boolean getOrDefault(T key, boolean defaultValue);
	
	@Override
	public default Boolean get(Object key) {
		return Boolean.valueOf(getBoolean((T)key));
	}
	
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = Boolean.valueOf(getBoolean((T)key));
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public default void replaceAll(BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceBooleans(mappingFunction instanceof ObjectBooleanUnaryOperator ? (ObjectBooleanUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Boolean.valueOf(V)).booleanValue());
	}
	
	@Override
	public default Boolean compute(T key, BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBoolean(key, mappingFunction instanceof ObjectBooleanUnaryOperator ? (ObjectBooleanUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	public default Boolean computeIfAbsent(T key, Function<? super T, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfAbsent(key, mappingFunction instanceof Object2BooleanFunction ? (Object2BooleanFunction<T>)mappingFunction : K -> mappingFunction.apply(K).booleanValue()));
	}
	
	@Override
	public default Boolean computeIfPresent(T key, BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(computeBooleanIfPresent(key, mappingFunction instanceof ObjectBooleanUnaryOperator ? (ObjectBooleanUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Boolean.valueOf(V)).booleanValue()));
	}
	
	@Override
	public default Boolean merge(T key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Boolean.valueOf(mergeBoolean(key, value.booleanValue(), mappingFunction instanceof BooleanBooleanUnaryOperator ? (BooleanBooleanUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Boolean.valueOf(K), Boolean.valueOf(V)).booleanValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ObjectBooleanConsumer<T> action);
	
	@Override
	public default void forEach(BiConsumer<? super T, ? super Boolean> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ObjectBooleanConsumer ? (ObjectBooleanConsumer<T>)action : (K, V) -> action.accept(K, Boolean.valueOf(V)));
	}
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public BooleanCollection values();
	@Override
	public ObjectSet<Map.Entry<T, Boolean>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<T>> object2BooleanEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Object2BooleanMaps#synchronize
	 */
	public default Object2BooleanMap<T> synchronize() { return Object2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Object2BooleanMaps#synchronize
	 */
	public default Object2BooleanMap<T> synchronize(Object mutex) { return Object2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Object2BooleanMaps#unmodifiable
	 */
	public default Object2BooleanMap<T> unmodifiable() { return Object2BooleanMaps.unmodifiable(this); }
	
	@Override
	public default Boolean put(T key, Boolean value) {
		return Boolean.valueOf(put(key, value.booleanValue()));
	}
	
	@Override
	public default Boolean putIfAbsent(T key, Boolean value) {
		return Boolean.valueOf(put(key, value.booleanValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<T> extends ObjectSet<Object2BooleanMap.Entry<T>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Object2BooleanMap.Entry<T>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Object2BooleanMap.Entry<T>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface Entry<T> extends Map.Entry<T, Boolean>
	{
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public boolean getBooleanValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public boolean setValue(boolean value);
		@Override
		public default Boolean getValue() { return Boolean.valueOf(getBooleanValue()); }
		@Override
		public default Boolean setValue(Boolean value) { return Boolean.valueOf(setValue(value.booleanValue())); }
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
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start() {
			return new BuilderCache<T>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start(int size) {
			return new BuilderCache<T>(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, boolean value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, Boolean value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2BooleanOpenHashMap<T> map() {
			return new Object2BooleanOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2BooleanOpenHashMap<T> map(int size) {
			return new Object2BooleanOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2BooleanOpenHashMap<T> map(T[] keys, boolean[] values) {
			return new Object2BooleanOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanOpenHashMap<T> map(T[] keys, Boolean[] values) {
			return new Object2BooleanOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanOpenHashMap<T> map(Object2BooleanMap<T> map) {
			return new Object2BooleanOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanOpenHashMap<T> map(Map<? extends T, ? extends Boolean> map) {
			return new Object2BooleanOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <T> Object2BooleanLinkedOpenHashMap<T> linkedMap() {
			return new Object2BooleanLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2BooleanLinkedOpenHashMap<T> linkedMap(int size) {
			return new Object2BooleanLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2BooleanLinkedOpenHashMap<T> linkedMap(T[] keys, boolean[] values) {
			return new Object2BooleanLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanLinkedOpenHashMap<T> linkedMap(T[] keys, Boolean[] values) {
			return new Object2BooleanLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanLinkedOpenHashMap<T> linkedMap(Object2BooleanMap<T> map) {
			return new Object2BooleanLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2BooleanOpenHashMap<T> linkedMap(Map<? extends T, ? extends Boolean> map) {
			return new ImmutableObject2BooleanOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <T> ImmutableObject2BooleanOpenHashMap<T> immutable(T[] keys, boolean[] values) {
			return new ImmutableObject2BooleanOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> ImmutableObject2BooleanOpenHashMap<T> immutable(T[] keys, Boolean[] values) {
			return new ImmutableObject2BooleanOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <T> ImmutableObject2BooleanOpenHashMap<T> immutable(Object2BooleanMap<T> map) {
			return new ImmutableObject2BooleanOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2BooleanOpenHashMap<T> immutable(Map<? extends T, ? extends Boolean> map) {
			return new ImmutableObject2BooleanOpenHashMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty EnumMap
		 */
		public <T extends Enum<T>> Enum2BooleanMap<T> enumMap(Class<T> keyType) {
			 return new Enum2BooleanMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> Enum2BooleanMap<T> enumMap(T[] keys, Boolean[] values) {
			return new Enum2BooleanMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> Enum2BooleanMap<T> enumMap(T[] keys, boolean[] values) {
			return new Enum2BooleanMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a EnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> Enum2BooleanMap<T> enumMap(Map<? extends T, ? extends Boolean> map) {
			return new Enum2BooleanMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a EnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> Enum2BooleanMap<T> enumMap(Object2BooleanMap<T> map) {
			return new Enum2BooleanMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty LinkedEnumMap
		 */
		public <T extends Enum<T>> LinkedEnum2BooleanMap<T> linkedEnumMap(Class<T> keyType) {
			 return new LinkedEnum2BooleanMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2BooleanMap<T> linkedEnumMap(T[] keys, Boolean[] values) {
			return new LinkedEnum2BooleanMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> LinkedEnum2BooleanMap<T> linkedEnumMap(T[] keys, boolean[] values) {
			return new LinkedEnum2BooleanMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a LinkedEnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2BooleanMap<T> linkedEnumMap(Map<? extends T, ? extends Boolean> map) {
			return new LinkedEnum2BooleanMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a LinkedEnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> LinkedEnum2BooleanMap<T> linkedEnumMap(Object2BooleanMap<T> map) {
			return new LinkedEnum2BooleanMap<>(map);
		}
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(int size, ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(T[] keys, boolean[] values, ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(T[] keys, Boolean[] values, ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(Object2BooleanMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanOpenCustomHashMap<T> customMap(Map<? extends T, ? extends Boolean> map, ObjectStrategy<T> strategy) {
			return new Object2BooleanOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(int size, ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, boolean[] values, ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, Boolean[] values, ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(Object2BooleanMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(Map<? extends T, ? extends Boolean> map, ObjectStrategy<T> strategy) {
			return new Object2BooleanLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap() {
			return new Object2BooleanArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap(int size) {
			return new Object2BooleanArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap(T[] keys, boolean[] values) {
			return new Object2BooleanArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap(T[] keys, Boolean[] values) {
			return new Object2BooleanArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap(Object2BooleanMap<T> map) {
			return new Object2BooleanArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanArrayMap<T> arrayMap(Map<? extends T, ? extends Boolean> map) {
			return new Object2BooleanArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap() {
			return new Object2BooleanRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return new Object2BooleanRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap(T[] keys, boolean[] values, Comparator<T> comp) {
			return new Object2BooleanRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap(T[] keys, Boolean[] values, Comparator<T> comp) {
			return new Object2BooleanRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap(Object2BooleanMap<T> map, Comparator<T> comp) {
			return new Object2BooleanRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanRBTreeMap<T> rbTreeMap(Map<? extends T, ? extends Boolean> map, Comparator<T> comp) {
			return new Object2BooleanRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap() {
			return new Object2BooleanAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return new Object2BooleanAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap(T[] keys, boolean[] values, Comparator<T> comp) {
			return new Object2BooleanAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap(T[] keys, Boolean[] values, Comparator<T> comp) {
			return new Object2BooleanAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap(Object2BooleanMap<T> map, Comparator<T> comp) {
			return new Object2BooleanAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2BooleanAVLTreeMap<T> avlTreeMap(Map<? extends T, ? extends Boolean> map, Comparator<T> comp) {
			return new Object2BooleanAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<T>
	{
		T[] keys;
		boolean[] values;
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
			keys = (T[])new Object[initialSize];
			values = new boolean[initialSize];
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
		public BuilderCache<T> put(T key, boolean value) {
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
		public BuilderCache<T> put(T key, Boolean value) {
			return put(key, value.booleanValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<T> put(Object2BooleanMap.Entry<T> entry) {
			return put(entry.getKey(), entry.getBooleanValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Object2BooleanMap<T> map) {
			return putAll(Object2BooleanMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Map<? extends T, ? extends Boolean> map) {
			for(Map.Entry<? extends T, ? extends Boolean> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(ObjectIterable<Object2BooleanMap.Entry<T>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Object2BooleanMap.Entry<T>>)c).size());
			
			for(Object2BooleanMap.Entry<T> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Object2BooleanMap<T>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Object2BooleanOpenHashMap
		 */
		public Object2BooleanOpenHashMap<T> map() {
			return putElements(new Object2BooleanOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Object2BooleanLinkedOpenHashMap
		 */
		public Object2BooleanLinkedOpenHashMap<T> linkedMap() {
			return putElements(new Object2BooleanLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableObject2BooleanOpenHashMap
		 */
		public ImmutableObject2BooleanOpenHashMap<T> immutable() {
			return new ImmutableObject2BooleanOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2BooleanOpenCustomHashMap
		 */
		public Object2BooleanOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2BooleanOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2BooleanLinkedOpenCustomHashMap
		 */
		public Object2BooleanLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2BooleanLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Object2BooleanConcurrentOpenHashMap
		 */
		public Object2BooleanConcurrentOpenHashMap<T> concurrentMap() {
			return putElements(new Object2BooleanConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Object2BooleanArrayMap
		 */
		public Object2BooleanArrayMap<T> arrayMap() {
			return new Object2BooleanArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Object2BooleanRBTreeMap
		 */
		public Object2BooleanRBTreeMap<T> rbTreeMap() {
			return putElements(new Object2BooleanRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2BooleanRBTreeMap
		 */
		public Object2BooleanRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return putElements(new Object2BooleanRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Object2BooleanAVLTreeMap
		 */
		public Object2BooleanAVLTreeMap<T> avlTreeMap() {
			return putElements(new Object2BooleanAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2BooleanAVLTreeMap
		 */
		public Object2BooleanAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return putElements(new Object2BooleanAVLTreeMap<>(comp));
		}
	}
}