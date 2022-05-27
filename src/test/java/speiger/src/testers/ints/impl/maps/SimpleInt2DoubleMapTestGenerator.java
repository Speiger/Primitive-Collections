package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2DoubleMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2DoubleMapTestGenerator<E extends Int2DoubleMap>
{
	BiFunction<int[], double[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleInt2DoubleMapTestGenerator(BiFunction<int[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Int2DoubleMap.Entry>(
			new AbstractInt2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2DoubleMap.Entry... elements) {
		int[] keys = new int[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Double>> order(List<Map.Entry<Integer, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2DoubleMap.Entry> order(ObjectList<Int2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2DoubleMapTestGenerator<Int2DoubleMap> implements TestInt2DoubleMapGenerator
	{
		public Maps(BiFunction<int[], double[], Int2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2DoubleMapTestGenerator<Int2DoubleSortedMap> implements TestInt2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], double[], Int2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Double>> order(List<Map.Entry<Integer, Double>> insertionOrder) {
			insertionOrder.sort(DerivedInt2DoubleMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2DoubleMap.Entry> order(ObjectList<Int2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2DoubleMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2DoubleMap.Entry belowSamplesLesser() { return new AbstractInt2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2DoubleMap.Entry belowSamplesGreater() { return new AbstractInt2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2DoubleMap.Entry aboveSamplesLesser() { return new AbstractInt2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2DoubleMap.Entry aboveSamplesGreater() { return new AbstractInt2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}