package speiger.src.testers.longs.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.longs.generators.TestLongListGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.TestLongOrderedSetGenerator;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.utils.LongSamples;

public class SimpleLongTestGenerator<T extends LongCollection> {
	Function<long[], T> mapper;
	
	public SimpleLongTestGenerator(Function<long[], T> mapper) {
		this.mapper = mapper;
	}
	
	public LongSamples getSamples() {
		return new LongSamples(1, 0, 2, 3, 4);
	}
	
	public T create(long... elements) {
		return mapper.apply(elements);
	}
	
	public T create(Object... elements) {
		long[] array = new long[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Long)e).longValue();
		}
		return mapper.apply(array);
	}
	
	public LongIterable order(LongList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Long> order(List<Long> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleLongTestGenerator<LongCollection> implements TestLongCollectionGenerator
	{
		public Collections(Function<long[], LongCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleLongTestGenerator<LongList> implements TestLongListGenerator
	{
		public Lists(Function<long[], LongList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleLongTestGenerator<LongSet> implements TestLongSetGenerator
	{
		public Sets(Function<long[], LongSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleLongTestGenerator<LongOrderedSet> implements TestLongOrderedSetGenerator
	{
		public OrderedSets(Function<long[], LongOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleLongTestGenerator<LongSortedSet> implements TestLongSortedSetGenerator
	{
		public SortedSets(Function<long[], LongSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public LongIterable order(LongList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public long belowSamplesLesser() { return -2; }
		@Override
		public long belowSamplesGreater() { return -1; }
		@Override
		public long aboveSamplesLesser() { return 5; }
		@Override
		public long aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleLongTestGenerator<LongNavigableSet> implements TestLongNavigableSetGenerator
	{
		public NavigableSets(Function<long[], LongNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public LongIterable order(LongList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public long belowSamplesLesser() { return -2; }
		@Override
		public long belowSamplesGreater() { return -1; }
		@Override
		public long aboveSamplesLesser() { return 5; }
		@Override
		public long aboveSamplesGreater() { return 6; }
	}
}