package speiger.src.collections.doubles.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;

import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.function.DoubleFunction;
import speiger.src.collections.utils.ISizeProvider;

/**
 * A Helper class for Iterables
 */
public class DoubleIterables
{
	/**
	 * A Helper function that maps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(Iterable<? extends Double> iterable, DoubleFunction<E> mapper) {
		return new MappedIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterable into a new Type.
	 * @param iterable the iterable that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is mapped to a new result
	 */
	public static <E> ObjectIterable<E> map(DoubleIterable iterable, DoubleFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Iterable<? extends Double> iterable, DoubleFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterable<E> flatMap(DoubleIterable iterable, DoubleFunction<V> mapper) {
		return new FlatMappedIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(Iterable<? extends Double> iterable, DoubleFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(wrap(iterable), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterable into a new Type.
	 * @param iterable the iterable that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterable that is flatMapped to a new result
	 */
	public static <E> ObjectIterable<E> arrayFlatMap(DoubleIterable iterable, DoubleFunction<E[]> mapper) {
		return new FlatMappedArrayIterable<>(iterable, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterable
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static DoubleIterable filter(Iterable<? extends Double> iterable, DoublePredicate filter) {
		return new FilteredIterable(wrap(iterable), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterable that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterable
	 */
	public static DoubleIterable filter(DoubleIterable iterable, DoublePredicate filter) {
		return new FilteredIterable(iterable, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static DoubleIterable distinct(DoubleIterable iterable) {
		return new DistinctIterable(iterable);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterable.
	 * @param iterable that should be distinct
	 * @return a distinct iterable
	 */
	public static DoubleIterable distinct(Iterable<? extends Double> iterable) {
		return new DistinctIterable(wrap(iterable));
	}
	
	/**
	 * A Helper function that repeats the Iterable a specific amount of times
	 * @param iterable that should be repeated
	 * @param repeats the amount of times the iterable should be repeated
	 * @return a repeating iterable
	 */
	public static DoubleIterable repeat(DoubleIterable iterable, int repeats) {
		return new RepeatingIterable(iterable, repeats);
	}
	
	/**
	 * A Helper function that repeats the Iterable a specific amount of times from a Java Iterable
	 * @param iterable that should be repeated
	 * @param repeats the amount of times the iterable should be repeated
	 * @return a repeating iterable
	 */
	public static DoubleIterable repeat(Iterable<? extends Double> iterable, int repeats) {
		return new RepeatingIterable(wrap(iterable), repeats);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static DoubleIterable limit(DoubleIterable iterable, long limit) {
		return new LimitedIterable(iterable, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterable to a specific size from a Java Iterable
	 * @param iterable that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterable
	 */
	public static DoubleIterable limit(Iterable<? extends Double> iterable, long limit) {
		return new LimitedIterable(wrap(iterable), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterable.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static DoubleIterable sorted(DoubleIterable iterable, DoubleComparator sorter) {
		return new SortedIterable(iterable, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterable from a Java Iterable
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterable that should be sorted
	 * @param sorter that sorts the iterable. Can be null.
	 * @return a sorted iterable.
	 */
	public static DoubleIterable sorted(Iterable<? extends Double> iterable, DoubleComparator sorter) {
		return new SortedIterable(wrap(iterable), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable.
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static DoubleIterable peek(DoubleIterable iterable, DoubleConsumer action) {
		return new PeekIterable(iterable, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterable from a Java Iterable
	 * @param iterable that should be peeked at
	 * @param action callback that receives the value before the iterable returns it
	 * @return a peeked iterable
	 */
	public static DoubleIterable peek(Iterable<? extends Double> iterable, DoubleConsumer action) {
		return new PeekIterable(wrap(iterable), action);
	}
	
	/**
	 * A Wrapper function that wraps a Java-Iterable into a Type Specific Iterable
	 * @param iterable that should be wrapped
	 * @return a type specific iterable
	 */
	public static DoubleIterable wrap(Iterable<? extends Double> iterable) {
		return new WrappedIterable(iterable);
	}
	
	private static class WrappedIterable implements DoubleIterable, ISizeProvider
	{
		Iterable<? extends Double> iterable;
		
		public WrappedIterable(Iterable<? extends Double> iterable) {
			this.iterable = iterable;
		}
		
		public DoubleIterator iterator() {
			return DoubleIterators.wrap(iterable.iterator());
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(iterable);
			return prov == null ? -1 : prov.size();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(action);
		}
	}
	
	private static class MappedIterable<T> implements ObjectIterable<T>, ISizeProvider
	{
		DoubleIterable iterable;
		DoubleFunction<T> mapper;
		
		MappedIterable(DoubleIterable iterable, DoubleFunction<T> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		public ObjectIterator<T> iterator() {
			return DoubleIterators.map(iterable.iterator(), mapper);
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(this);
			return prov == null ? -1 : prov.size();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> action.accept(mapper.apply(E)));
		}
	}
	
	private static class FlatMappedIterable<T, V extends Iterable<T>> implements ObjectIterable<T>
	{
		DoubleIterable iterable;
		DoubleFunction<V> mapper;
		
		FlatMappedIterable(DoubleIterable iterable, DoubleFunction<V> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return DoubleIterators.flatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> mapper.apply(E).forEach(action));
		}
		
	}
	
	private static class FlatMappedArrayIterable<T> implements ObjectIterable<T>
	{
		DoubleIterable iterable;
		DoubleFunction<T[]> mapper;
		
		FlatMappedArrayIterable(DoubleIterable iterable, DoubleFunction<T[]> mapper) {
			this.iterable = iterable;
			this.mapper = mapper;
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return DoubleIterators.arrayFlatMap(iterable.iterator(), mapper);
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			iterable.forEach(E -> {
				T[] array = mapper.apply(E);
				for(int i = 0,m=array.length;i<m;action.accept(array[i++]));
			});
		}
	}
	
	private static class RepeatingIterable implements DoubleIterable, ISizeProvider
	{
		DoubleIterable iterable;
		int repeats;
		
		public RepeatingIterable(DoubleIterable iterable, int repeats) {
			this.iterable = iterable;
			this.repeats = repeats;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.repeat(iterable.iterator(), repeats);
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(iterable);
			return prov == null ? -1 : prov.size() * (repeats+1);
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			DoubleCollection repeater = DoubleCollections.wrapper();
			iterable.forEach(action.andThen(repeater::add));
			for(int i = 0;i<repeats;i++)
				repeater.forEach(action);
		}
	}
	
	private static class FilteredIterable implements DoubleIterable
	{
		DoubleIterable iterable;
		DoublePredicate filter;
		
		public FilteredIterable(DoubleIterable iterable, DoublePredicate filter) {
			this.iterable = iterable;
			this.filter = filter;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.filter(iterable.iterator(), filter);
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(T -> { if(!filter.test(T)) action.accept(T); } );
		}
	}
	
	private static class LimitedIterable implements DoubleIterable, ISizeProvider
	{
		DoubleIterable iterable;
		long limit;
		
		public LimitedIterable(DoubleIterable iterable, long limit) {
			this.iterable = iterable;
			this.limit = limit;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.limit(iterable.iterator(), limit);
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(iterable);
			return prov == null ? -1 : (int)Math.min(prov.size(), limit);
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			AtomicLong counter = new AtomicLong();
			iterable.forEach(T -> {
				if(counter.get() >= limit) return;
				counter.incrementAndGet();
				action.accept(T);
			});
		}
	}
	
	private static class SortedIterable implements DoubleIterable, ISizeProvider
	{
		DoubleIterable iterable;
		DoubleComparator sorter;
		
		public SortedIterable(DoubleIterable iterable, DoubleComparator sorter) {
			this.iterable = iterable;
			this.sorter = sorter;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.sorted(iterable.iterator(), sorter);
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(iterable);
			return prov == null ? -1 : prov.size();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			DoubleCollections.CollectionWrapper wrapper = DoubleCollections.wrapper();
			iterable.forEach(wrapper::add);
			wrapper.unstableSort(sorter);
			wrapper.forEach(action);
		}
	}
	
	private static class DistinctIterable implements DoubleIterable
	{
		DoubleIterable iterable;
		
		public DistinctIterable(DoubleIterable iterable) {
			this.iterable = iterable;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.distinct(iterable.iterator());
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			DoubleCollection filtered = DoubleCollections.distinctWrapper();
			iterable.forEach(T -> { if(filtered.add(T)) action.accept(T); });
		}
	}
	
	private static class PeekIterable implements DoubleIterable, ISizeProvider
	{
		DoubleIterable iterable;
		DoubleConsumer action;
		
		public PeekIterable(DoubleIterable iterable, DoubleConsumer action) {
			this.iterable = iterable;
			this.action = action;
		}
		
		@Override
		public DoubleIterator iterator() {
			return DoubleIterators.peek(iterable.iterator(), action);
		}
		
		@Override
		public int size() {
			ISizeProvider prov = ISizeProvider.of(iterable);
			return prov == null ? -1 : prov.size();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			iterable.forEach(this.action.andThen(action));
		}
	}
}