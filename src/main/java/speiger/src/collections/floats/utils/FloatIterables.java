package speiger.src.collections.floats.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.functions.function.Float2ObjectFunction;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.sets.FloatOpenHashSet;
import speiger.src.collections.floats.sets.FloatSet;

/**
 * A Helper class for Iterables
 */
public class FloatIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(Iterable<? extends Float> iterable, Float2ObjectFunction<E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(FloatIterable iterable, Float2ObjectFunction<E> mapper) {
		return new MappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends Float> iterable, Float2ObjectFunction<V> mapper) {
		return new FlatMappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(FloatIterable iterable, Float2ObjectFunction<V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(Iterable<? extends Float> iterable, Float2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(FloatIterable iterable, Float2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static FloatIterable filter(Iterable<? extends Float> iterable, Float2BooleanFunction filter) {
		return new FilteredIterable(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static FloatIterable filter(FloatIterable iterable, Float2BooleanFunction filter) {
		return new FilteredIterable(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static FloatIterable distinct(FloatIterable iterable) {
		return new DistinctIterable(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static FloatIterable distinct(Iterable<? extends Float> iterable) {
		return new DistinctIterable(wrap(iterable));
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static FloatIterable limit(FloatIterable iterable, long limit) {
		return new LimitedIterable(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static FloatIterable limit(Iterable<? extends Float> iterable, long limit) {
		return new LimitedIterable(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static FloatIterable sorted(FloatIterable iterable, FloatComparator sorter) {
		return new SortedIterable(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static FloatIterable sorted(Iterable<? extends Float> iterable, FloatComparator sorter) {
		return new SortedIterable(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static FloatIterable peek(FloatIterable iterable, FloatConsumer action) {
		return new PeekIterable(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static FloatIterable peek(Iterable<? extends Float> iterable, FloatConsumer action) {
		return new PeekIterable(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @return a type specific iterable
	 */
	public static FloatIterable wrap(Iterable<? extends Float> iterable) {
		return new WrappedIterable(iterable);
	}
	
	private static class WrappedIterable implements FloatIterable
	{
		Iterable<? extends Float> iterable;
		
		public WrappedIterable(Iterable<? extends Float> iterable) {
			this.iterable = iterable;
		}
		
		public FloatIterator iterator() {
			return FloatIterators.wrap(iterable.iterator());
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<T> implements ObjectIterable<T>
	{
		FloatIterable iterable;
		Float2ObjectFunction<T> mapper;
		
		MappedIterable(FloatIterable iterable, Float2ObjectFunction<T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return FloatIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.get(E)));
		}
	}
	
	private static class FlatMappedIterable<T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		FloatIterable iterable;
		Float2ObjectFunction<V> mapper;
		
		FlatMappedIterable(FloatIterable iterable, Float2ObjectFunction<V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return FloatIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.get(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<T> implements ObjectIterable<T>
	{
		FloatIterable iterable;
		Float2ObjectFunction<T[]> mapper;
		
		FlatMappedArrayIterable(FloatIterable iterable, Float2ObjectFunction<T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return FloatIterators.arrayFlatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> {
				T[] array = mapper.get(E);
				for(int i = 0,m=array.length;i<m;action.accept(array[i++]));
			});
		}
	}
	
	private static class FilteredIterable implements FloatIterable
	{
		FloatIterable iterable;
		Float2BooleanFunction filter;
		
		public FilteredIterable(FloatIterable iterable, Float2BooleanFunction filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public FloatIterator iterator() {
			return FloatIterators.filter(iterable.iterator(), filter);
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.get(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable implements FloatIterable
	{
		FloatIterable iterable;
		long limit;
		
		public LimitedIterable(FloatIterable iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public FloatIterator iterator() {
			return FloatIterators.limit(iterable.iterator(), limit);
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable implements FloatIterable
	{
		FloatIterable iterable;
		FloatComparator sorter;
		
		public SortedIterable(FloatIterable iterable, FloatComparator sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public FloatIterator iterator() {
			return FloatIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			FloatList list = new FloatArrayList();
			iterable.forEach(list::add);
			list.unstableSort(sorter);
			list.forEach(action);
		}
	}
	
	private static class DistinctIterable implements FloatIterable
	{
		FloatIterable iterable;
		
		public DistinctIterable(FloatIterable iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public FloatIterator iterator() {
			return FloatIterators.distinct(iterable.iterator());
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			FloatSet filtered = new FloatOpenHashSet();
			iterable.forEach(T -> { if(filtered.add(T)) action.accept(T); });
		}
	}
	
	private static class PeekIterable implements FloatIterable
	{
		FloatIterable iterable;
		FloatConsumer action;
		
		public PeekIterable(FloatIterable iterable, FloatConsumer action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public FloatIterator iterator() {
			return FloatIterators.peek(iterable.iterator(), action);
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}