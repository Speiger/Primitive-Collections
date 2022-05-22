package speiger.src.testers.ints.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.generators.TestIntListGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.TestIntOrderedSetGenerator;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.utils.IntSamples;

public class SimpleIntTestGenerator<T extends IntCollection> {
	Function<int[], T> mapper;
	
	public SimpleIntTestGenerator(Function<int[], T> mapper) {
		this.mapper = mapper;
	}
	
	public IntSamples getSamples() {
		return new IntSamples(1, 0, 2, 3, 4);
	}
	
	public T create(int... elements) {
		return mapper.apply(elements);
	}
	
	public T create(Object... elements) {
		int[] array = new int[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Integer)e).intValue();
		}
		return mapper.apply(array);
	}
	
	public IntIterable order(IntList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Integer> order(List<Integer> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleIntTestGenerator<IntCollection> implements TestIntCollectionGenerator
	{
		public Collections(Function<int[], IntCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleIntTestGenerator<IntList> implements TestIntListGenerator
	{
		public Lists(Function<int[], IntList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleIntTestGenerator<IntSet> implements TestIntSetGenerator
	{
		public Sets(Function<int[], IntSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleIntTestGenerator<IntOrderedSet> implements TestIntOrderedSetGenerator
	{
		public OrderedSets(Function<int[], IntOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleIntTestGenerator<IntSortedSet> implements TestIntSortedSetGenerator
	{
		public SortedSets(Function<int[], IntSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public IntIterable order(IntList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public int belowSamplesLesser() { return -2; }
		@Override
		public int belowSamplesGreater() { return -1; }
		@Override
		public int aboveSamplesLesser() { return 5; }
		@Override
		public int aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleIntTestGenerator<IntNavigableSet> implements TestIntNavigableSetGenerator
	{
		public NavigableSets(Function<int[], IntNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public IntIterable order(IntList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public int belowSamplesLesser() { return -2; }
		@Override
		public int belowSamplesGreater() { return -1; }
		@Override
		public int aboveSamplesLesser() { return 5; }
		@Override
		public int aboveSamplesGreater() { return 6; }
	}
}