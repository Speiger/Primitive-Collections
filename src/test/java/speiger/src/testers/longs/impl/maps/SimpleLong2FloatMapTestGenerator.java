package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2FloatMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleLong2FloatMapTestGenerator<E extends Long2FloatMap>
{
	BiFunction<long[], float[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleLong2FloatMapTestGenerator(BiFunction<long[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Long2FloatMap.Entry>(
			new AbstractLong2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2FloatMap.Entry... elements) {
		long[] keys = new long[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Float>> order(List<Map.Entry<Long, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2FloatMap.Entry> order(ObjectList<Long2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2FloatMapTestGenerator<Long2FloatMap> implements TestLong2FloatMapGenerator
	{
		public Maps(BiFunction<long[], float[], Long2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2FloatMapTestGenerator<Long2FloatSortedMap> implements TestLong2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], float[], Long2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Float>> order(List<Map.Entry<Long, Float>> insertionOrder) {
			insertionOrder.sort(DerivedLong2FloatMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2FloatMap.Entry> order(ObjectList<Long2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2FloatMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2FloatMap.Entry belowSamplesLesser() { return new AbstractLong2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2FloatMap.Entry belowSamplesGreater() { return new AbstractLong2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2FloatMap.Entry aboveSamplesLesser() { return new AbstractLong2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2FloatMap.Entry aboveSamplesGreater() { return new AbstractLong2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}