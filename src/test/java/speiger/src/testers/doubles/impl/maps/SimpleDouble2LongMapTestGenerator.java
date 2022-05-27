package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2LongMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleDouble2LongMapTestGenerator<E extends Double2LongMap>
{
	BiFunction<double[], long[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleDouble2LongMapTestGenerator(BiFunction<double[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2LongMap.Entry> getSamples() {
		return new ObjectSamples<Double2LongMap.Entry>(
			new AbstractDouble2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2LongMap.Entry... elements) {
		double[] keys = new double[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Long>> order(List<Map.Entry<Double, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2LongMap.Entry> order(ObjectList<Double2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2LongMapTestGenerator<Double2LongMap> implements TestDouble2LongMapGenerator
	{
		public Maps(BiFunction<double[], long[], Double2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2LongMapTestGenerator<Double2LongSortedMap> implements TestDouble2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], long[], Double2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Long>> order(List<Map.Entry<Double, Long>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2LongMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2LongMap.Entry> order(ObjectList<Double2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2LongMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2LongMap.Entry belowSamplesLesser() { return new AbstractDouble2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2LongMap.Entry belowSamplesGreater() { return new AbstractDouble2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2LongMap.Entry aboveSamplesLesser() { return new AbstractDouble2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2LongMap.Entry aboveSamplesGreater() { return new AbstractDouble2LongMap.BasicEntry(keys[8], values[8]); }
	}
}