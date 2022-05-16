package speiger.src.collections.booleans.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.functions.function.Boolean2ObjectFunction;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;

/**
 * A Helper class for Iterables
 */
public class BooleanIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(Iterable<? extends Boolean> iterable, Boolean2ObjectFunction<E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(BooleanIterable iterable, Boolean2ObjectFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends Boolean> iterable, Boolean2ObjectFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(BooleanIterable iterable, Boolean2ObjectFunction<V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(Iterable<? extends Boolean> iterable, Boolean2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(BooleanIterable iterable, Boolean2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static BooleanIterable filter(Iterable<? extends Boolean> iterable, Boolean2BooleanFunction filter) {
		return new FilteredIterable(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static BooleanIterable filter(BooleanIterable iterable, Boolean2BooleanFunction filter) {
		return new FilteredIterable(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static BooleanIterable distinct(BooleanIterable iterable) {
		return new DistinctIterable(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static BooleanIterable distinct(Iterable<? extends Boolean> iterable) {
		return new DistinctIterable(wrap(iterable));
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static BooleanIterable limit(BooleanIterable iterable, long limit) {
		return new LimitedIterable(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static BooleanIterable limit(Iterable<? extends Boolean> iterable, long limit) {
		return new LimitedIterable(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static BooleanIterable sorted(BooleanIterable iterable, BooleanComparator sorter) {
		return new SortedIterable(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static BooleanIterable sorted(Iterable<? extends Boolean> iterable, BooleanComparator sorter) {
		return new SortedIterable(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static BooleanIterable peek(BooleanIterable iterable, BooleanConsumer action) {
		return new PeekIterable(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static BooleanIterable peek(Iterable<? extends Boolean> iterable, BooleanConsumer action) {
		return new PeekIterable(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @return a type specific iterable
	 */
	public static BooleanIterable wrap(Iterable<? extends Boolean> iterable) {
		return new WrappedIterable(iterable);
	}
	
	private static class WrappedIterable implements BooleanIterable
	{
		Iterable<? extends Boolean> iterable;
		
		public WrappedIterable(Iterable<? extends Boolean> iterable) {
			this.iterable = iterable;
		}
		
		public BooleanIterator iterator() {
			return BooleanIterators.wrap(iterable.iterator());
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<T> implements ObjectIterable<T>
	{
		BooleanIterable iterable;
		Boolean2ObjectFunction<T> mapper;
		
		MappedIterable(BooleanIterable iterable, Boolean2ObjectFunction<T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return BooleanIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.get(E)));
		}
	}
	
	private static class FlatMappedIterable<T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		BooleanIterable iterable;
		Boolean2ObjectFunction<V> mapper;
		
		FlatMappedIterable(BooleanIterable iterable, Boolean2ObjectFunction<V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return BooleanIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.get(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<T> implements ObjectIterable<T>
	{
		BooleanIterable iterable;
		Boolean2ObjectFunction<T[]> mapper;
		
		FlatMappedArrayIterable(BooleanIterable iterable, Boolean2ObjectFunction<T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return BooleanIterators.arrayFlatMap(iterable.iterator(), mapper);
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
	
	private static class FilteredIterable implements BooleanIterable
	{
		BooleanIterable iterable;
		Boolean2BooleanFunction filter;
		
		public FilteredIterable(BooleanIterable iterable, Boolean2BooleanFunction filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public BooleanIterator iterator() {
			return BooleanIterators.filter(iterable.iterator(), filter);
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.get(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable implements BooleanIterable
	{
		BooleanIterable iterable;
		long limit;
		
		public LimitedIterable(BooleanIterable iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public BooleanIterator iterator() {
			return BooleanIterators.limit(iterable.iterator(), limit);
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable implements BooleanIterable
	{
		BooleanIterable iterable;
		BooleanComparator sorter;
		
		public SortedIterable(BooleanIterable iterable, BooleanComparator sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public BooleanIterator iterator() {
			return BooleanIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			BooleanList list = new BooleanArrayList();
			iterable.forEach(list::add);
			list.unstableSort(sorter);
			list.forEach(action);
		}
	}
	
	private static class DistinctIterable implements BooleanIterable
	{
		BooleanIterable iterable;
		
		public DistinctIterable(BooleanIterable iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public BooleanIterator iterator() {
			return BooleanIterators.distinct(iterable.iterator());
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			AtomicInteger result = new AtomicInteger();
			iterable.forEach(T -> {
				if(((result.get() & (T ? 2 : 1)) != 0)) return;
				result.getAndAdd(T ? 2 : 1);
				action.accept(T);
			});
		}
	}
	
	private static class PeekIterable implements BooleanIterable
	{
		BooleanIterable iterable;
		BooleanConsumer action;
		
		public PeekIterable(BooleanIterable iterable, BooleanConsumer action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public BooleanIterator iterator() {
			return BooleanIterators.peek(iterable.iterator(), action);
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}