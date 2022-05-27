package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2ShortMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleLong2ShortMapTestGenerator<E extends Long2ShortMap>
{
	BiFunction<long[], short[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleLong2ShortMapTestGenerator(BiFunction<long[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Long2ShortMap.Entry>(
			new AbstractLong2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2ShortMap.Entry... elements) {
		long[] keys = new long[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Short>> order(List<Map.Entry<Long, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2ShortMap.Entry> order(ObjectList<Long2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2ShortMapTestGenerator<Long2ShortMap> implements TestLong2ShortMapGenerator
	{
		public Maps(BiFunction<long[], short[], Long2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2ShortMapTestGenerator<Long2ShortSortedMap> implements TestLong2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], short[], Long2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Short>> order(List<Map.Entry<Long, Short>> insertionOrder) {
			insertionOrder.sort(DerivedLong2ShortMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2ShortMap.Entry> order(ObjectList<Long2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2ShortMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2ShortMap.Entry belowSamplesLesser() { return new AbstractLong2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2ShortMap.Entry belowSamplesGreater() { return new AbstractLong2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2ShortMap.Entry aboveSamplesLesser() { return new AbstractLong2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2ShortMap.Entry aboveSamplesGreater() { return new AbstractLong2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}