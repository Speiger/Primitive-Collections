package speiger.src.testers.doubles.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.generators.TestDoubleListGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleOrderedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;

public class SimpleDoubleTestGenerator<E extends DoubleCollection> {
	Function<double[], E> mapper;
	public SimpleDoubleTestGenerator(Function<double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public DoubleSamples getSamples() {
		return new DoubleSamples(1, 0, 2, 3, 4);
	}
	
	public E create(double... elements) {
		return mapper.apply(elements);
	}
	
	public E create(Object... elements) {
		double[] array = new double[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Double)e).doubleValue();
		}
		return mapper.apply(array);
	}
	
	public DoubleIterable order(DoubleList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Double> order(List<Double> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleDoubleTestGenerator<DoubleCollection> implements TestDoubleCollectionGenerator
	{
		public Collections(Function<double[], DoubleCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleDoubleTestGenerator<DoubleList> implements TestDoubleListGenerator
	{
		public Lists(Function<double[], DoubleList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleDoubleTestGenerator<DoubleSet> implements TestDoubleSetGenerator
	{
		public Sets(Function<double[], DoubleSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleDoubleTestGenerator<DoubleOrderedSet> implements TestDoubleOrderedSetGenerator
	{
		public OrderedSets(Function<double[], DoubleOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleDoubleTestGenerator<DoubleSortedSet> implements TestDoubleSortedSetGenerator
	{
		public SortedSets(Function<double[], DoubleSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public double belowSamplesLesser() { return -2; }
		@Override
		public double belowSamplesGreater() { return -1; }
		@Override
		public double aboveSamplesLesser() { return 5; }
		@Override
		public double aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleDoubleTestGenerator<DoubleNavigableSet> implements TestDoubleNavigableSetGenerator
	{
		public NavigableSets(Function<double[], DoubleNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public double belowSamplesLesser() { return -2; }
		@Override
		public double belowSamplesGreater() { return -1; }
		@Override
		public double aboveSamplesLesser() { return 5; }
		@Override
		public double aboveSamplesGreater() { return 6; }
	}
}