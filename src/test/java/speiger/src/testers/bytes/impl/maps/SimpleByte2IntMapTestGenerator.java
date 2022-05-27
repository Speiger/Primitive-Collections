package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2IntMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2IntMapTestGenerator<E extends Byte2IntMap>
{
	BiFunction<byte[], int[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleByte2IntMapTestGenerator(BiFunction<byte[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2IntMap.Entry> getSamples() {
		return new ObjectSamples<Byte2IntMap.Entry>(
			new AbstractByte2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2IntMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Integer>> order(List<Map.Entry<Byte, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2IntMap.Entry> order(ObjectList<Byte2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2IntMapTestGenerator<Byte2IntMap> implements TestByte2IntMapGenerator
	{
		public Maps(BiFunction<byte[], int[], Byte2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2IntMapTestGenerator<Byte2IntSortedMap> implements TestByte2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], int[], Byte2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Integer>> order(List<Map.Entry<Byte, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedByte2IntMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2IntMap.Entry> order(ObjectList<Byte2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2IntMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2IntMap.Entry belowSamplesLesser() { return new AbstractByte2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2IntMap.Entry belowSamplesGreater() { return new AbstractByte2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2IntMap.Entry aboveSamplesLesser() { return new AbstractByte2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2IntMap.Entry aboveSamplesGreater() { return new AbstractByte2IntMap.BasicEntry(keys[8], values[8]); }
	}
}