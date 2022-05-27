package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2ByteMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleLong2ByteMapTestGenerator<E extends Long2ByteMap>
{
	BiFunction<long[], byte[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleLong2ByteMapTestGenerator(BiFunction<long[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Long2ByteMap.Entry>(
			new AbstractLong2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2ByteMap.Entry... elements) {
		long[] keys = new long[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Byte>> order(List<Map.Entry<Long, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2ByteMap.Entry> order(ObjectList<Long2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2ByteMapTestGenerator<Long2ByteMap> implements TestLong2ByteMapGenerator
	{
		public Maps(BiFunction<long[], byte[], Long2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2ByteMapTestGenerator<Long2ByteSortedMap> implements TestLong2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], byte[], Long2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Byte>> order(List<Map.Entry<Long, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedLong2ByteMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2ByteMap.Entry> order(ObjectList<Long2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2ByteMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2ByteMap.Entry belowSamplesLesser() { return new AbstractLong2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2ByteMap.Entry belowSamplesGreater() { return new AbstractLong2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2ByteMap.Entry aboveSamplesLesser() { return new AbstractLong2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2ByteMap.Entry aboveSamplesGreater() { return new AbstractLong2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}