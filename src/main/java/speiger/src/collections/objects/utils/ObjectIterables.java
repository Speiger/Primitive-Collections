package speiger.src.collections.objects.utils;

import java.util.Objects;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.functions.function.Object2ObjectFunction;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.sets.ObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Helper class for Iterables
 */
public class ObjectIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <T, E> ObjectIterable<E> map(Iterable<? extends T> iterable, Object2ObjectFunction<T, E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <T, E> ObjectIterable<E> map(ObjectIterable<T> iterable, Object2ObjectFunction<T, E> mapper) {
		return new MappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <T, E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends T> iterable, Object2ObjectFunction<T, V> mapper) {
		return new FlatMappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <T, E, V extends Iterable<E>> ObjectIterable<E> flatMap(ObjectIterable<T> iterable, Object2ObjectFunction<T, V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <T, E> ObjectIterable<E> arrayFlatMap(Iterable<? extends T> iterable, Object2ObjectFunction<T, E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <T> the type of elements maintained by this Collection
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <T, E> ObjectIterable<E> arrayFlatMap(ObjectIterable<T> iterable, Object2ObjectFunction<T, E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @param <T> the type of elements maintained by this Collection
	 * @return a filtered iterable
	 */
	public static <T> ObjectIterable<T> filter(Iterable<? extends T> iterable, Object2BooleanFunction<T> filter) {
		return new FilteredIterable<>(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @param <T> the type of elements maintained by this Collection
	 * @return a filtered iterable
	 */
	public static <T> ObjectIterable<T> filter(ObjectIterable<T> iterable, Object2BooleanFunction<T> filter) {
		return new FilteredIterable<>(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @param <T> the type of elements maintained by this Collection
	 * @return a distinct iterable
	 */
	public static <T> ObjectIterable<T> distinct(ObjectIterable<T> iterable) {
		return new DistinctIterable<>(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @param <T> the type of elements maintained by this Collection
	 * @return a distinct iterable
	 */
	public static <T> ObjectIterable<T> distinct(Iterable<? extends T> iterable) {
		return new DistinctIterable<>(wrap(iterable));
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @param <T> the type of elements maintained by this Collection
	 * @return a limited iterable
	 */
	public static <T> ObjectIterable<T> limit(ObjectIterable<T> iterable, long limit) {
		return new LimitedIterable<>(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @param <T> the type of elements maintained by this Collection
	 * @return a limited iterable
	 */
	public static <T> ObjectIterable<T> limit(Iterable<? extends T> iterable, long limit) {
		return new LimitedIterable<>(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @param <T> the type of elements maintained by this Collection
	 * @return a sorted iterable.
	 */
	public static <T> ObjectIterable<T> sorted(ObjectIterable<T> iterable, Comparator<T> sorter) {
		return new SortedIterable<>(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @param <T> the type of elements maintained by this Collection
	 * @return a sorted iterable.
	 */
	public static <T> ObjectIterable<T> sorted(Iterable<? extends T> iterable, Comparator<T> sorter) {
		return new SortedIterable<>(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @param <T> the type of elements maintained by this Collection
	 * @return a peeked iterable
	 */
	public static <T> ObjectIterable<T> peek(ObjectIterable<T> iterable, Consumer<T> action) {
		return new PeekIterable<>(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @param <T> the type of elements maintained by this Collection
	 * @return a peeked iterable
	 */
	public static <T> ObjectIterable<T> peek(Iterable<? extends T> iterable, Consumer<T> action) {
		return new PeekIterable<>(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @param <T> the type of elements maintained by this Collection
	 * @return a type specific iterable
	 */
	public static <T> ObjectIterable<T> wrap(Iterable<? extends T> iterable) {
		return new WrappedIterable<>(iterable);
	}
	
	private static class WrappedIterable<T> implements ObjectIterable<T>
	{
		Iterable<? extends T> iterable;
		
		public WrappedIterable(Iterable<? extends T> iterable) {
			this.iterable = iterable;
		}
		
		public ObjectIterator<T> iterator() {
			return ObjectIterators.wrap(iterable.iterator());
		}
		
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<E, T> implements ObjectIterable<T>
	{
		ObjectIterable<E> iterable;
		Object2ObjectFunction<E, T> mapper;
		
		MappedIterable(ObjectIterable<E> iterable, Object2ObjectFunction<E, T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return ObjectIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.getObject(E)));
		}
	}
	
	private static class FlatMappedIterable<E, T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		ObjectIterable<E> iterable;
		Object2ObjectFunction<E, V> mapper;
		
		FlatMappedIterable(ObjectIterable<E> iterable, Object2ObjectFunction<E, V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.getObject(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<E, T> implements ObjectIterable<T>
	{
		ObjectIterable<E> iterable;
		Object2ObjectFunction<E, T[]> mapper;
		
		FlatMappedArrayIterable(ObjectIterable<E> iterable, Object2ObjectFunction<E, T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.arrayFlatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> {
				T[] array = mapper.getObject(E);
				for(int i = 0,m=array.length;i<m;action.accept(array[i++]));
			});
		}
	}
	
	private static class FilteredIterable<T> implements ObjectIterable<T>
	{
		ObjectIterable<T> iterable;
		Object2BooleanFunction<T> filter;
		
		public FilteredIterable(ObjectIterable<T> iterable, Object2BooleanFunction<T> filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.filter(iterable.iterator(), filter);
		}
		
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.getBoolean(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable<T> implements ObjectIterable<T>
	{
		ObjectIterable<T> iterable;
		long limit;
		
		public LimitedIterable(ObjectIterable<T> iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.limit(iterable.iterator(), limit);
		}
		
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable<T> implements ObjectIterable<T>
	{
		ObjectIterable<T> iterable;
		Comparator<T> sorter;
		
		public SortedIterable(ObjectIterable<T> iterable, Comparator<T> sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			ObjectList<T> list = new ObjectArrayList<>();
			iterable.forEach(list::add);
			list.unstableSort(sorter);
			list.forEach(action);
		}
	}
	
	private static class DistinctIterable<T> implements ObjectIterable<T>
	{
		ObjectIterable<T> iterable;
		
		public DistinctIterable(ObjectIterable<T> iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.distinct(iterable.iterator());
		}
		
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			ObjectSet<T> filtered = new ObjectOpenHashSet<>();
			iterable.forEach(T -> { if(filtered.add(T)) action.accept(T); });
		}
	}
	
	private static class PeekIterable<T> implements ObjectIterable<T>
	{
		ObjectIterable<T> iterable;
		Consumer<T> action;
		
		public PeekIterable(ObjectIterable<T> iterable, Consumer<T> action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ObjectIterators.peek(iterable.iterator(), action);
		}
		
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}