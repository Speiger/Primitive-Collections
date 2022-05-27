package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleLong2BooleanMapTestGenerator<E extends Long2BooleanMap>
{
	BiFunction<long[], boolean[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleLong2BooleanMapTestGenerator(BiFunction<long[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Long2BooleanMap.Entry>(
			new AbstractLong2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2BooleanMap.Entry... elements) {
		long[] keys = new long[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Boolean>> order(List<Map.Entry<Long, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2BooleanMap.Entry> order(ObjectList<Long2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2BooleanMapTestGenerator<Long2BooleanMap> implements TestLong2BooleanMapGenerator
	{
		public Maps(BiFunction<long[], boolean[], Long2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2BooleanMapTestGenerator<Long2BooleanSortedMap> implements TestLong2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], boolean[], Long2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Boolean>> order(List<Map.Entry<Long, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedLong2BooleanMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2BooleanMap.Entry> order(ObjectList<Long2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2BooleanMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2BooleanMap.Entry belowSamplesLesser() { return new AbstractLong2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2BooleanMap.Entry belowSamplesGreater() { return new AbstractLong2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2BooleanMap.Entry aboveSamplesLesser() { return new AbstractLong2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2BooleanMap.Entry aboveSamplesGreater() { return new AbstractLong2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}