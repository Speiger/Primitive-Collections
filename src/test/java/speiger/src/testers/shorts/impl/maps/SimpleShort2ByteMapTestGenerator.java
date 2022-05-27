package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleShort2ByteMapTestGenerator<E extends Short2ByteMap>
{
	BiFunction<short[], byte[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleShort2ByteMapTestGenerator(BiFunction<short[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Short2ByteMap.Entry>(
			new AbstractShort2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2ByteMap.Entry... elements) {
		short[] keys = new short[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Byte>> order(List<Map.Entry<Short, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2ByteMap.Entry> order(ObjectList<Short2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2ByteMapTestGenerator<Short2ByteMap> implements TestShort2ByteMapGenerator
	{
		public Maps(BiFunction<short[], byte[], Short2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2ByteMapTestGenerator<Short2ByteSortedMap> implements TestShort2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], byte[], Short2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Byte>> order(List<Map.Entry<Short, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedShort2ByteMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2ByteMap.Entry> order(ObjectList<Short2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2ByteMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2ByteMap.Entry belowSamplesLesser() { return new AbstractShort2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2ByteMap.Entry belowSamplesGreater() { return new AbstractShort2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2ByteMap.Entry aboveSamplesLesser() { return new AbstractShort2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2ByteMap.Entry aboveSamplesGreater() { return new AbstractShort2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}