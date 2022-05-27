package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2ShortMapTestGenerator<E extends Byte2ShortMap>
{
	BiFunction<byte[], short[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleByte2ShortMapTestGenerator(BiFunction<byte[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Byte2ShortMap.Entry>(
			new AbstractByte2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2ShortMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Short>> order(List<Map.Entry<Byte, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2ShortMap.Entry> order(ObjectList<Byte2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2ShortMapTestGenerator<Byte2ShortMap> implements TestByte2ShortMapGenerator
	{
		public Maps(BiFunction<byte[], short[], Byte2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2ShortMapTestGenerator<Byte2ShortSortedMap> implements TestByte2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], short[], Byte2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Short>> order(List<Map.Entry<Byte, Short>> insertionOrder) {
			insertionOrder.sort(DerivedByte2ShortMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2ShortMap.Entry> order(ObjectList<Byte2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2ShortMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2ShortMap.Entry belowSamplesLesser() { return new AbstractByte2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2ShortMap.Entry belowSamplesGreater() { return new AbstractByte2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2ShortMap.Entry aboveSamplesLesser() { return new AbstractByte2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2ShortMap.Entry aboveSamplesGreater() { return new AbstractByte2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}