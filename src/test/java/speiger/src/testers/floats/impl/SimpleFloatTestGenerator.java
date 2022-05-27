package speiger.src.testers.floats.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.generators.TestFloatListGenerator;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.TestFloatOrderedSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.utils.FloatSamples;

@SuppressWarnings("javadoc")
public class SimpleFloatTestGenerator<E extends FloatCollection> {
	Function<float[], E> mapper;
	public SimpleFloatTestGenerator(Function<float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public FloatSamples getSamples() {
		return new FloatSamples(1, 0, 2, 3, 4);
	}
	
	public E create(float... elements) {
		return mapper.apply(elements);
	}
	
	public E create(Object... elements) {
		float[] array = new float[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Float)e).floatValue();
		}
		return mapper.apply(array);
	}
	
	public FloatIterable order(FloatList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Float> order(List<Float> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleFloatTestGenerator<FloatCollection> implements TestFloatCollectionGenerator
	{
		public Collections(Function<float[], FloatCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleFloatTestGenerator<FloatList> implements TestFloatListGenerator
	{
		public Lists(Function<float[], FloatList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleFloatTestGenerator<FloatSet> implements TestFloatSetGenerator
	{
		public Sets(Function<float[], FloatSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleFloatTestGenerator<FloatOrderedSet> implements TestFloatOrderedSetGenerator
	{
		public OrderedSets(Function<float[], FloatOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleFloatTestGenerator<FloatSortedSet> implements TestFloatSortedSetGenerator
	{
		public SortedSets(Function<float[], FloatSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public FloatIterable order(FloatList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public float belowSamplesLesser() { return -2; }
		@Override
		public float belowSamplesGreater() { return -1; }
		@Override
		public float aboveSamplesLesser() { return 5; }
		@Override
		public float aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleFloatTestGenerator<FloatNavigableSet> implements TestFloatNavigableSetGenerator
	{
		public NavigableSets(Function<float[], FloatNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public FloatIterable order(FloatList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public float belowSamplesLesser() { return -2; }
		@Override
		public float belowSamplesGreater() { return -1; }
		@Override
		public float aboveSamplesLesser() { return 5; }
		@Override
		public float aboveSamplesGreater() { return 6; }
	}
}