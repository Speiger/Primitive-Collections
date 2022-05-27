package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2LongMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleLong2LongMapTestGenerator<E extends Long2LongMap>
{
	BiFunction<long[], long[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleLong2LongMapTestGenerator(BiFunction<long[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2LongMap.Entry> getSamples() {
		return new ObjectSamples<Long2LongMap.Entry>(
			new AbstractLong2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2LongMap.Entry... elements) {
		long[] keys = new long[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Long>> order(List<Map.Entry<Long, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2LongMap.Entry> order(ObjectList<Long2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2LongMapTestGenerator<Long2LongMap> implements TestLong2LongMapGenerator
	{
		public Maps(BiFunction<long[], long[], Long2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2LongMapTestGenerator<Long2LongSortedMap> implements TestLong2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], long[], Long2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Long>> order(List<Map.Entry<Long, Long>> insertionOrder) {
			insertionOrder.sort(DerivedLong2LongMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2LongMap.Entry> order(ObjectList<Long2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2LongMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2LongMap.Entry belowSamplesLesser() { return new AbstractLong2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2LongMap.Entry belowSamplesGreater() { return new AbstractLong2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2LongMap.Entry aboveSamplesLesser() { return new AbstractLong2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2LongMap.Entry aboveSamplesGreater() { return new AbstractLong2LongMap.BasicEntry(keys[8], values[8]); }
	}
}