package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2IntMapTestGenerator<E extends Double2IntMap>
{
	BiFunction<double[], int[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleDouble2IntMapTestGenerator(BiFunction<double[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2IntMap.Entry> getSamples() {
		return new ObjectSamples<Double2IntMap.Entry>(
			new AbstractDouble2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2IntMap.Entry... elements) {
		double[] keys = new double[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Integer>> order(List<Map.Entry<Double, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2IntMap.Entry> order(ObjectList<Double2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2IntMapTestGenerator<Double2IntMap> implements TestDouble2IntMapGenerator
	{
		public Maps(BiFunction<double[], int[], Double2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2IntMapTestGenerator<Double2IntSortedMap> implements TestDouble2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], int[], Double2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Integer>> order(List<Map.Entry<Double, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2IntMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2IntMap.Entry> order(ObjectList<Double2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2IntMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2IntMap.Entry belowSamplesLesser() { return new AbstractDouble2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2IntMap.Entry belowSamplesGreater() { return new AbstractDouble2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2IntMap.Entry aboveSamplesLesser() { return new AbstractDouble2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2IntMap.Entry aboveSamplesGreater() { return new AbstractDouble2IntMap.BasicEntry(keys[8], values[8]); }
	}
}