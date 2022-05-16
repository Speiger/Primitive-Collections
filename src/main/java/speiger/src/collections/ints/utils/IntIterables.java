package speiger.src.collections.ints.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.functions.function.Int2ObjectFunction;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.sets.IntOpenHashSet;
import speiger.src.collections.ints.sets.IntSet;

/**
 * A Helper class for Iterables
 */
public class IntIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(Iterable<? extends Integer> iterable, Int2ObjectFunction<E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(IntIterable iterable, Int2ObjectFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends Integer> iterable, Int2ObjectFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(IntIterable iterable, Int2ObjectFunction<V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(Iterable<? extends Integer> iterable, Int2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(IntIterable iterable, Int2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static IntIterable filter(Iterable<? extends Integer> iterable, Int2BooleanFunction filter) {
		return new FilteredIterable(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static IntIterable filter(IntIterable iterable, Int2BooleanFunction filter) {
		return new FilteredIterable(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static IntIterable distinct(IntIterable iterable) {
		return new DistinctIterable(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static IntIterable distinct(Iterable<? extends Integer> iterable) {
		return new DistinctIterable(wrap(iterable));
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static IntIterable limit(IntIterable iterable, long limit) {
		return new LimitedIterable(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static IntIterable limit(Iterable<? extends Integer> iterable, long limit) {
		return new LimitedIterable(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static IntIterable sorted(IntIterable iterable, IntComparator sorter) {
		return new SortedIterable(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static IntIterable sorted(Iterable<? extends Integer> iterable, IntComparator sorter) {
		return new SortedIterable(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static IntIterable peek(IntIterable iterable, IntConsumer action) {
		return new PeekIterable(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static IntIterable peek(Iterable<? extends Integer> iterable, IntConsumer action) {
		return new PeekIterable(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @return a type specific iterable
	 */
	public static IntIterable wrap(Iterable<? extends Integer> iterable) {
		return new WrappedIterable(iterable);
	}
	
	private static class WrappedIterable implements IntIterable
	{
		Iterable<? extends Integer> iterable;
		
		public WrappedIterable(Iterable<? extends Integer> iterable) {
			this.iterable = iterable;
		}
		
		public IntIterator iterator() {
			return IntIterators.wrap(iterable.iterator());
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<T> implements ObjectIterable<T>
	{
		IntIterable iterable;
		Int2ObjectFunction<T> mapper;
		
		MappedIterable(IntIterable iterable, Int2ObjectFunction<T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return IntIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.get(E)));
		}
	}
	
	private static class FlatMappedIterable<T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		IntIterable iterable;
		Int2ObjectFunction<V> mapper;
		
		FlatMappedIterable(IntIterable iterable, Int2ObjectFunction<V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return IntIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.get(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<T> implements ObjectIterable<T>
	{
		IntIterable iterable;
		Int2ObjectFunction<T[]> mapper;
		
		FlatMappedArrayIterable(IntIterable iterable, Int2ObjectFunction<T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return IntIterators.arrayFlatMap(iterable.iterator(), mapper);
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
	
	private static class FilteredIterable implements IntIterable
	{
		IntIterable iterable;
		Int2BooleanFunction filter;
		
		public FilteredIterable(IntIterable iterable, Int2BooleanFunction filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public IntIterator iterator() {
			return IntIterators.filter(iterable.iterator(), filter);
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.get(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable implements IntIterable
	{
		IntIterable iterable;
		long limit;
		
		public LimitedIterable(IntIterable iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public IntIterator iterator() {
			return IntIterators.limit(iterable.iterator(), limit);
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable implements IntIterable
	{
		IntIterable iterable;
		IntComparator sorter;
		
		public SortedIterable(IntIterable iterable, IntComparator sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public IntIterator iterator() {
			return IntIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			IntList list = new IntArrayList();
			iterable.forEach(list::add);
			list.unstableSort(sorter);
			list.forEach(action);
		}
	}
	
	private static class DistinctIterable implements IntIterable
	{
		IntIterable iterable;
		
		public DistinctIterable(IntIterable iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public IntIterator iterator() {
			return IntIterators.distinct(iterable.iterator());
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			IntSet filtered = new IntOpenHashSet();
			iterable.forEach(T -> { if(filtered.add(T)) action.accept(T); });
		}
	}
	
	private static class PeekIterable implements IntIterable
	{
		IntIterable iterable;
		IntConsumer action;
		
		public PeekIterable(IntIterable iterable, IntConsumer action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public IntIterator iterator() {
			return IntIterators.peek(iterable.iterator(), action);
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}