package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleLong2DoubleMapTestGenerator<E extends Long2DoubleMap>
{
	BiFunction<long[], double[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleLong2DoubleMapTestGenerator(BiFunction<long[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Long2DoubleMap.Entry>(
			new AbstractLong2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2DoubleMap.Entry... elements) {
		long[] keys = new long[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Double>> order(List<Map.Entry<Long, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2DoubleMap.Entry> order(ObjectList<Long2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2DoubleMapTestGenerator<Long2DoubleMap> implements TestLong2DoubleMapGenerator
	{
		public Maps(BiFunction<long[], double[], Long2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2DoubleMapTestGenerator<Long2DoubleSortedMap> implements TestLong2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], double[], Long2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Double>> order(List<Map.Entry<Long, Double>> insertionOrder) {
			insertionOrder.sort(DerivedLong2DoubleMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2DoubleMap.Entry> order(ObjectList<Long2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2DoubleMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2DoubleMap.Entry belowSamplesLesser() { return new AbstractLong2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2DoubleMap.Entry belowSamplesGreater() { return new AbstractLong2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2DoubleMap.Entry aboveSamplesLesser() { return new AbstractLong2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2DoubleMap.Entry aboveSamplesGreater() { return new AbstractLong2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}