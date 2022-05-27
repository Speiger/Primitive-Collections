package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2LongMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleShort2LongMapTestGenerator<E extends Short2LongMap>
{
	BiFunction<short[], long[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleShort2LongMapTestGenerator(BiFunction<short[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2LongMap.Entry> getSamples() {
		return new ObjectSamples<Short2LongMap.Entry>(
			new AbstractShort2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2LongMap.Entry... elements) {
		short[] keys = new short[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Long>> order(List<Map.Entry<Short, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2LongMap.Entry> order(ObjectList<Short2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2LongMapTestGenerator<Short2LongMap> implements TestShort2LongMapGenerator
	{
		public Maps(BiFunction<short[], long[], Short2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2LongMapTestGenerator<Short2LongSortedMap> implements TestShort2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], long[], Short2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Long>> order(List<Map.Entry<Short, Long>> insertionOrder) {
			insertionOrder.sort(DerivedShort2LongMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2LongMap.Entry> order(ObjectList<Short2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2LongMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2LongMap.Entry belowSamplesLesser() { return new AbstractShort2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2LongMap.Entry belowSamplesGreater() { return new AbstractShort2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2LongMap.Entry aboveSamplesLesser() { return new AbstractShort2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2LongMap.Entry aboveSamplesGreater() { return new AbstractShort2LongMap.BasicEntry(keys[8], values[8]); }
	}
}