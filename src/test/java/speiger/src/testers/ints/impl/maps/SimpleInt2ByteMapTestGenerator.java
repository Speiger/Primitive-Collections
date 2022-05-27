package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2ByteMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2ByteMapTestGenerator<E extends Int2ByteMap>
{
	BiFunction<int[], byte[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleInt2ByteMapTestGenerator(BiFunction<int[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Int2ByteMap.Entry>(
			new AbstractInt2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2ByteMap.Entry... elements) {
		int[] keys = new int[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Byte>> order(List<Map.Entry<Integer, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2ByteMap.Entry> order(ObjectList<Int2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2ByteMapTestGenerator<Int2ByteMap> implements TestInt2ByteMapGenerator
	{
		public Maps(BiFunction<int[], byte[], Int2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2ByteMapTestGenerator<Int2ByteSortedMap> implements TestInt2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], byte[], Int2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Byte>> order(List<Map.Entry<Integer, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedInt2ByteMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2ByteMap.Entry> order(ObjectList<Int2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2ByteMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2ByteMap.Entry belowSamplesLesser() { return new AbstractInt2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2ByteMap.Entry belowSamplesGreater() { return new AbstractInt2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2ByteMap.Entry aboveSamplesLesser() { return new AbstractInt2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2ByteMap.Entry aboveSamplesGreater() { return new AbstractInt2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}