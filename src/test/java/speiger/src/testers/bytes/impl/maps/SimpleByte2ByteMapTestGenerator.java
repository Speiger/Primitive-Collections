package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2ByteMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleByte2ByteMapTestGenerator<E extends Byte2ByteMap>
{
	BiFunction<byte[], byte[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleByte2ByteMapTestGenerator(BiFunction<byte[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Byte2ByteMap.Entry>(
			new AbstractByte2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2ByteMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Byte>> order(List<Map.Entry<Byte, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2ByteMap.Entry> order(ObjectList<Byte2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2ByteMapTestGenerator<Byte2ByteMap> implements TestByte2ByteMapGenerator
	{
		public Maps(BiFunction<byte[], byte[], Byte2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2ByteMapTestGenerator<Byte2ByteSortedMap> implements TestByte2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], byte[], Byte2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Byte>> order(List<Map.Entry<Byte, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedByte2ByteMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2ByteMap.Entry> order(ObjectList<Byte2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2ByteMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2ByteMap.Entry belowSamplesLesser() { return new AbstractByte2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2ByteMap.Entry belowSamplesGreater() { return new AbstractByte2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2ByteMap.Entry aboveSamplesLesser() { return new AbstractByte2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2ByteMap.Entry aboveSamplesGreater() { return new AbstractByte2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}