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

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.objects.functions.function.Object2IntFunction;
import speiger.src.collections.objects.functions.function.ObjectIntUnaryOperator;
import speiger.src.collections.objects.maps.impl.customHash.Object2IntLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2IntOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2IntLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2IntOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2IntOpenHashMap;
import speiger.src.collections.objects.maps.impl.tree.Object2IntAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2IntRBTreeMap;
import speiger.src.collections.objects.maps.impl.misc.Object2IntArrayMap;
import speiger.src.collections.objects.maps.impl.concurrent.Object2IntConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2IntMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.objects.utils.maps.Object2IntMaps;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2IntMap<T> extends Map<T, Integer>, Object2IntFunction<T>
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
	public int getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Object2IntMap<T> setDefaultReturnValue(int v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Object2IntMap<T> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public int put(T key, int value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, int[] values) {
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
	public void putAll(T[] keys, int[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, Integer[] values) {
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
	public void putAll(T[] keys, Integer[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public int putIfAbsent(T key, int value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Object2IntMap<T> m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public int addTo(T key, int value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Object2IntMap<T> m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public int subFrom(T key, int value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Object2IntMap<T> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(int value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Integer && containsValue(((Integer)value).intValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public int rem(T key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be T but just have to support equals with T.
	 */
	@Override
	public default Integer remove(Object key) {
		return Integer.valueOf(rem((T)key));
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(T key, int value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return value instanceof Integer && remove((T)key, ((Integer)value).intValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public int remOrDefault(T key, int defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(T key, int oldValue, int newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public int replace(T key, int value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceInts(Object2IntMap<T> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceInts(ObjectIntUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public int computeInt(T key, ObjectIntUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public int computeIntIfAbsent(T key, Object2IntFunction<T> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public int supplyIntIfAbsent(T key, IntSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public int computeIntIfPresent(T key, ObjectIntUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public int mergeInt(T key, int value, IntIntUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllInt(Object2IntMap<T> m, IntIntUnaryOperator mappingFunction);
	
	@Override
	public default boolean replace(T key, Integer oldValue, Integer newValue) {
		return replace(key, oldValue.intValue(), newValue.intValue());
	}
	
	@Override
	public default Integer replace(T key, Integer value) {
		return Integer.valueOf(replace(key, value.intValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public int getInt(T key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public int getOrDefault(T key, int defaultValue);
	
	@Override
	public default Integer get(Object key) {
		return Integer.valueOf(getInt((T)key));
	}
	
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		Integer value = Integer.valueOf(getInt((T)key));
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public default void replaceAll(BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceInts(mappingFunction instanceof ObjectIntUnaryOperator ? (ObjectIntUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Integer.valueOf(V)).intValue());
	}
	
	@Override
	public default Integer compute(T key, BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeInt(key, mappingFunction instanceof ObjectIntUnaryOperator ? (ObjectIntUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Integer.valueOf(V)).intValue()));
	}
	
	@Override
	public default Integer computeIfAbsent(T key, Function<? super T, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfAbsent(key, mappingFunction instanceof Object2IntFunction ? (Object2IntFunction<T>)mappingFunction : K -> mappingFunction.apply(K).intValue()));
	}
	
	@Override
	public default Integer computeIfPresent(T key, BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(computeIntIfPresent(key, mappingFunction instanceof ObjectIntUnaryOperator ? (ObjectIntUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Integer.valueOf(V)).intValue()));
	}
	
	@Override
	public default Integer merge(T key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Integer.valueOf(mergeInt(key, value.intValue(), mappingFunction instanceof IntIntUnaryOperator ? (IntIntUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Integer.valueOf(K), Integer.valueOf(V)).intValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ObjectIntConsumer<T> action);
	
	@Override
	public default void forEach(BiConsumer<? super T, ? super Integer> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ObjectIntConsumer ? (ObjectIntConsumer<T>)action : (K, V) -> action.accept(K, Integer.valueOf(V)));
	}
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public IntCollection values();
	@Override
	public ObjectSet<Map.Entry<T, Integer>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<T>> object2IntEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Object2IntMaps#synchronize
	 */
	public default Object2IntMap<T> synchronize() { return Object2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Object2IntMaps#synchronize
	 */
	public default Object2IntMap<T> synchronize(Object mutex) { return Object2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Object2IntMaps#unmodifiable
	 */
	public default Object2IntMap<T> unmodifiable() { return Object2IntMaps.unmodifiable(this); }
	
	@Override
	public default Integer put(T key, Integer value) {
		return Integer.valueOf(put(key, value.intValue()));
	}
	
	@Override
	public default Integer putIfAbsent(T key, Integer value) {
		return Integer.valueOf(put(key, value.intValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<T> extends ObjectSet<Object2IntMap.Entry<T>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Object2IntMap.Entry<T>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Object2IntMap.Entry<T>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface Entry<T> extends Map.Entry<T, Integer>
	{
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public int getIntValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public int setValue(int value);
		@Override
		public default Integer getValue() { return Integer.valueOf(getIntValue()); }
		@Override
		public default Integer setValue(Integer value) { return Integer.valueOf(setValue(value.intValue())); }
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
		public <T> BuilderCache<T> put(T key, int value) {
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
		public <T> BuilderCache<T> put(T key, Integer value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2IntOpenHashMap<T> map() {
			return new Object2IntOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2IntOpenHashMap<T> map(int size) {
			return new Object2IntOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2IntOpenHashMap<T> map(T[] keys, int[] values) {
			return new Object2IntOpenHashMap<>(keys, values);
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
		public <T> Object2IntOpenHashMap<T> map(T[] keys, Integer[] values) {
			return new Object2IntOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2IntOpenHashMap<T> map(Object2IntMap<T> map) {
			return new Object2IntOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntOpenHashMap<T> map(Map<? extends T, ? extends Integer> map) {
			return new Object2IntOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <T> Object2IntLinkedOpenHashMap<T> linkedMap() {
			return new Object2IntLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2IntLinkedOpenHashMap<T> linkedMap(int size) {
			return new Object2IntLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2IntLinkedOpenHashMap<T> linkedMap(T[] keys, int[] values) {
			return new Object2IntLinkedOpenHashMap<>(keys, values);
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
		public <T> Object2IntLinkedOpenHashMap<T> linkedMap(T[] keys, Integer[] values) {
			return new Object2IntLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2IntLinkedOpenHashMap<T> linkedMap(Object2IntMap<T> map) {
			return new Object2IntLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2IntOpenHashMap<T> linkedMap(Map<? extends T, ? extends Integer> map) {
			return new ImmutableObject2IntOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <T> ImmutableObject2IntOpenHashMap<T> immutable(T[] keys, int[] values) {
			return new ImmutableObject2IntOpenHashMap<>(keys, values);
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
		public <T> ImmutableObject2IntOpenHashMap<T> immutable(T[] keys, Integer[] values) {
			return new ImmutableObject2IntOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <T> ImmutableObject2IntOpenHashMap<T> immutable(Object2IntMap<T> map) {
			return new ImmutableObject2IntOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2IntOpenHashMap<T> immutable(Map<? extends T, ? extends Integer> map) {
			return new ImmutableObject2IntOpenHashMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty EnumMap
		 */
		public <T extends Enum<T>> Enum2IntMap<T> enumMap(Class<T> keyType) {
			 return new Enum2IntMap<>(keyType);
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
		public <T extends Enum<T>> Enum2IntMap<T> enumMap(T[] keys, Integer[] values) {
			return new Enum2IntMap<>(keys, values);
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
		public <T extends Enum<T>> Enum2IntMap<T> enumMap(T[] keys, int[] values) {
			return new Enum2IntMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a EnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> Enum2IntMap<T> enumMap(Map<? extends T, ? extends Integer> map) {
			return new Enum2IntMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a EnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> Enum2IntMap<T> enumMap(Object2IntMap<T> map) {
			return new Enum2IntMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty LinkedEnumMap
		 */
		public <T extends Enum<T>> LinkedEnum2IntMap<T> linkedEnumMap(Class<T> keyType) {
			 return new LinkedEnum2IntMap<>(keyType);
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
		public <T extends Enum<T>> LinkedEnum2IntMap<T> linkedEnumMap(T[] keys, Integer[] values) {
			return new LinkedEnum2IntMap<>(keys, values);
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
		public <T extends Enum<T>> LinkedEnum2IntMap<T> linkedEnumMap(T[] keys, int[] values) {
			return new LinkedEnum2IntMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a LinkedEnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2IntMap<T> linkedEnumMap(Map<? extends T, ? extends Integer> map) {
			return new LinkedEnum2IntMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a LinkedEnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> LinkedEnum2IntMap<T> linkedEnumMap(Object2IntMap<T> map) {
			return new LinkedEnum2IntMap<>(map);
		}
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <T> Object2IntOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <T> Object2IntOpenCustomHashMap<T> customMap(int size, ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(size, strategy);
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
		public <T> Object2IntOpenCustomHashMap<T> customMap(T[] keys, int[] values, ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(keys, values, strategy);
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
		public <T> Object2IntOpenCustomHashMap<T> customMap(T[] keys, Integer[] values, ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2IntOpenCustomHashMap<T> customMap(Object2IntMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntOpenCustomHashMap<T> customMap(Map<? extends T, ? extends Integer> map, ObjectStrategy<T> strategy) {
			return new Object2IntOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(int size, ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(size, strategy);
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
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, int[] values, ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(keys, values, strategy);
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
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, Integer[] values, ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(Object2IntMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(Map<? extends T, ? extends Integer> map, ObjectStrategy<T> strategy) {
			return new Object2IntLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2IntArrayMap<T> arrayMap() {
			return new Object2IntArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2IntArrayMap<T> arrayMap(int size) {
			return new Object2IntArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2IntArrayMap<T> arrayMap(T[] keys, int[] values) {
			return new Object2IntArrayMap<>(keys, values);
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
		public <T> Object2IntArrayMap<T> arrayMap(T[] keys, Integer[] values) {
			return new Object2IntArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2IntArrayMap<T> arrayMap(Object2IntMap<T> map) {
			return new Object2IntArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntArrayMap<T> arrayMap(Map<? extends T, ? extends Integer> map) {
			return new Object2IntArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2IntRBTreeMap<T> rbTreeMap() {
			return new Object2IntRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2IntRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return new Object2IntRBTreeMap<>(comp);
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
		public <T> Object2IntRBTreeMap<T> rbTreeMap(T[] keys, int[] values, Comparator<T> comp) {
			return new Object2IntRBTreeMap<>(keys, values, comp);
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
		public <T> Object2IntRBTreeMap<T> rbTreeMap(T[] keys, Integer[] values, Comparator<T> comp) {
			return new Object2IntRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2IntRBTreeMap<T> rbTreeMap(Object2IntMap<T> map, Comparator<T> comp) {
			return new Object2IntRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntRBTreeMap<T> rbTreeMap(Map<? extends T, ? extends Integer> map, Comparator<T> comp) {
			return new Object2IntRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2IntAVLTreeMap<T> avlTreeMap() {
			return new Object2IntAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2IntAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return new Object2IntAVLTreeMap<>(comp);
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
		public <T> Object2IntAVLTreeMap<T> avlTreeMap(T[] keys, int[] values, Comparator<T> comp) {
			return new Object2IntAVLTreeMap<>(keys, values, comp);
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
		public <T> Object2IntAVLTreeMap<T> avlTreeMap(T[] keys, Integer[] values, Comparator<T> comp) {
			return new Object2IntAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2IntAVLTreeMap<T> avlTreeMap(Object2IntMap<T> map, Comparator<T> comp) {
			return new Object2IntAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2IntAVLTreeMap<T> avlTreeMap(Map<? extends T, ? extends Integer> map, Comparator<T> comp) {
			return new Object2IntAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<T>
	{
		T[] keys;
		int[] values;
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
			values = new int[initialSize];
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
		public BuilderCache<T> put(T key, int value) {
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
		public BuilderCache<T> put(T key, Integer value) {
			return put(key, value.intValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<T> put(Object2IntMap.Entry<T> entry) {
			return put(entry.getKey(), entry.getIntValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Object2IntMap<T> map) {
			return putAll(Object2IntMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Map<? extends T, ? extends Integer> map) {
			for(Map.Entry<? extends T, ? extends Integer> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(ObjectIterable<Object2IntMap.Entry<T>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Object2IntMap.Entry<T>>)c).size());
			
			for(Object2IntMap.Entry<T> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Object2IntMap<T>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Object2IntOpenHashMap
		 */
		public Object2IntOpenHashMap<T> map() {
			return putElements(new Object2IntOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Object2IntLinkedOpenHashMap
		 */
		public Object2IntLinkedOpenHashMap<T> linkedMap() {
			return putElements(new Object2IntLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableObject2IntOpenHashMap
		 */
		public ImmutableObject2IntOpenHashMap<T> immutable() {
			return new ImmutableObject2IntOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2IntOpenCustomHashMap
		 */
		public Object2IntOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2IntOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2IntLinkedOpenCustomHashMap
		 */
		public Object2IntLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2IntLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Object2IntConcurrentOpenHashMap
		 */
		public Object2IntConcurrentOpenHashMap<T> concurrentMap() {
			return putElements(new Object2IntConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Object2IntArrayMap
		 */
		public Object2IntArrayMap<T> arrayMap() {
			return new Object2IntArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Object2IntRBTreeMap
		 */
		public Object2IntRBTreeMap<T> rbTreeMap() {
			return putElements(new Object2IntRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2IntRBTreeMap
		 */
		public Object2IntRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return putElements(new Object2IntRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Object2IntAVLTreeMap
		 */
		public Object2IntAVLTreeMap<T> avlTreeMap() {
			return putElements(new Object2IntAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2IntAVLTreeMap
		 */
		public Object2IntAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return putElements(new Object2IntAVLTreeMap<>(comp));
		}
	}
}