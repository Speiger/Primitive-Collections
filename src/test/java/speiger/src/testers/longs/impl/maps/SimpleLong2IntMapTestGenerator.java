package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2IntMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleLong2IntMapTestGenerator<E extends Long2IntMap>
{
	BiFunction<long[], int[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleLong2IntMapTestGenerator(BiFunction<long[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2IntMap.Entry> getSamples() {
		return new ObjectSamples<Long2IntMap.Entry>(
			new AbstractLong2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2IntMap.Entry... elements) {
		long[] keys = new long[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Integer>> order(List<Map.Entry<Long, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2IntMap.Entry> order(ObjectList<Long2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2IntMapTestGenerator<Long2IntMap> implements TestLong2IntMapGenerator
	{
		public Maps(BiFunction<long[], int[], Long2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2IntMapTestGenerator<Long2IntSortedMap> implements TestLong2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], int[], Long2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Integer>> order(List<Map.Entry<Long, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedLong2IntMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2IntMap.Entry> order(ObjectList<Long2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2IntMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2IntMap.Entry belowSamplesLesser() { return new AbstractLong2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2IntMap.Entry belowSamplesGreater() { return new AbstractLong2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2IntMap.Entry aboveSamplesLesser() { return new AbstractLong2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2IntMap.Entry aboveSamplesGreater() { return new AbstractLong2IntMap.BasicEntry(keys[8], values[8]); }
	}
}