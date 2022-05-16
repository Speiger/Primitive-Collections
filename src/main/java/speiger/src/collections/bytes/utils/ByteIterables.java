package speiger.src.collections.bytes.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.functions.function.Byte2ObjectFunction;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.sets.ByteOpenHashSet;
import speiger.src.collections.bytes.sets.ByteSet;

/**
 * A Helper class for Iterables
 */
public class ByteIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(Iterable<? extends Byte> iterable, Byte2ObjectFunction<E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(ByteIterable iterable, Byte2ObjectFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends Byte> iterable, Byte2ObjectFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(ByteIterable iterable, Byte2ObjectFunction<V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(Iterable<? extends Byte> iterable, Byte2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(ByteIterable iterable, Byte2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static ByteIterable filter(Iterable<? extends Byte> iterable, Byte2BooleanFunction filter) {
		return new FilteredIterable(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static ByteIterable filter(ByteIterable iterable, Byte2BooleanFunction filter) {
		return new FilteredIterable(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static ByteIterable distinct(ByteIterable iterable) {
		return new DistinctIterable(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static ByteIterable distinct(Iterable<? extends Byte> iterable) {
		return new DistinctIterable(wrap(iterable));
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static ByteIterable limit(ByteIterable iterable, long limit) {
		return new LimitedIterable(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static ByteIterable limit(Iterable<? extends Byte> iterable, long limit) {
		return new LimitedIterable(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static ByteIterable sorted(ByteIterable iterable, ByteComparator sorter) {
		return new SortedIterable(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static ByteIterable sorted(Iterable<? extends Byte> iterable, ByteComparator sorter) {
		return new SortedIterable(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static ByteIterable peek(ByteIterable iterable, ByteConsumer action) {
		return new PeekIterable(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static ByteIterable peek(Iterable<? extends Byte> iterable, ByteConsumer action) {
		return new PeekIterable(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @return a type specific iterable
	 */
	public static ByteIterable wrap(Iterable<? extends Byte> iterable) {
		return new WrappedIterable(iterable);
	}
	
	private static class WrappedIterable implements ByteIterable
	{
		Iterable<? extends Byte> iterable;
		
		public WrappedIterable(Iterable<? extends Byte> iterable) {
			this.iterable = iterable;
		}
		
		public ByteIterator iterator() {
			return ByteIterators.wrap(iterable.iterator());
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<T> implements ObjectIterable<T>
	{
		ByteIterable iterable;
		Byte2ObjectFunction<T> mapper;
		
		MappedIterable(ByteIterable iterable, Byte2ObjectFunction<T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return ByteIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.get(E)));
		}
	}
	
	private static class FlatMappedIterable<T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		ByteIterable iterable;
		Byte2ObjectFunction<V> mapper;
		
		FlatMappedIterable(ByteIterable iterable, Byte2ObjectFunction<V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ByteIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.get(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<T> implements ObjectIterable<T>
	{
		ByteIterable iterable;
		Byte2ObjectFunction<T[]> mapper;
		
		FlatMappedArrayIterable(ByteIterable iterable, Byte2ObjectFunction<T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return ByteIterators.arrayFlatMap(iterable.iterator(), mapper);
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
	
	private static class FilteredIterable implements ByteIterable
	{
		ByteIterable iterable;
		Byte2BooleanFunction filter;
		
		public FilteredIterable(ByteIterable iterable, Byte2BooleanFunction filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public ByteIterator iterator() {
			return ByteIterators.filter(iterable.iterator(), filter);
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.get(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable implements ByteIterable
	{
		ByteIterable iterable;
		long limit;
		
		public LimitedIterable(ByteIterable iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public ByteIterator iterator() {
			return ByteIterators.limit(iterable.iterator(), limit);
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable implements ByteIterable
	{
		ByteIterable iterable;
		ByteComparator sorter;
		
		public SortedIterable(ByteIterable iterable, ByteComparator sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public ByteIterator iterator() {
			return ByteIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			ByteList list = new ByteArrayList();
			iterable.forEach(list::add);
			list.unstableSort(sorter);
			list.forEach(action);
		}
	}
	
	private static class DistinctIterable implements ByteIterable
	{
		ByteIterable iterable;
		
		public DistinctIterable(ByteIterable iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public ByteIterator iterator() {
			return ByteIterators.distinct(iterable.iterator());
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			ByteSet filtered = new ByteOpenHashSet();
			iterable.forEach(T -> { if(filtered.add(T)) action.accept(T); });
		}
	}
	
	private static class PeekIterable implements ByteIterable
	{
		ByteIterable iterable;
		ByteConsumer action;
		
		public PeekIterable(ByteIterable iterable, ByteConsumer action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public ByteIterator iterator() {
			return ByteIterators.peek(iterable.iterator(), action);
		}
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}