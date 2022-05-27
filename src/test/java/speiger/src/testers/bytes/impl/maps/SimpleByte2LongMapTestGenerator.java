package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2LongMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2LongMapTestGenerator<E extends Byte2LongMap>
{
	BiFunction<byte[], long[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleByte2LongMapTestGenerator(BiFunction<byte[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2LongMap.Entry> getSamples() {
		return new ObjectSamples<Byte2LongMap.Entry>(
			new AbstractByte2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2LongMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Long>> order(List<Map.Entry<Byte, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2LongMap.Entry> order(ObjectList<Byte2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2LongMapTestGenerator<Byte2LongMap> implements TestByte2LongMapGenerator
	{
		public Maps(BiFunction<byte[], long[], Byte2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2LongMapTestGenerator<Byte2LongSortedMap> implements TestByte2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], long[], Byte2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Long>> order(List<Map.Entry<Byte, Long>> insertionOrder) {
			insertionOrder.sort(DerivedByte2LongMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2LongMap.Entry> order(ObjectList<Byte2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2LongMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2LongMap.Entry belowSamplesLesser() { return new AbstractByte2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2LongMap.Entry belowSamplesGreater() { return new AbstractByte2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2LongMap.Entry aboveSamplesLesser() { return new AbstractByte2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2LongMap.Entry aboveSamplesGreater() { return new AbstractByte2LongMap.BasicEntry(keys[8], values[8]); }
	}
}