package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2BooleanMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleByte2BooleanMapTestGenerator<E extends Byte2BooleanMap>
{
	BiFunction<byte[], boolean[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleByte2BooleanMapTestGenerator(BiFunction<byte[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Byte2BooleanMap.Entry>(
			new AbstractByte2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2BooleanMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Boolean>> order(List<Map.Entry<Byte, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2BooleanMap.Entry> order(ObjectList<Byte2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2BooleanMapTestGenerator<Byte2BooleanMap> implements TestByte2BooleanMapGenerator
	{
		public Maps(BiFunction<byte[], boolean[], Byte2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2BooleanMapTestGenerator<Byte2BooleanSortedMap> implements TestByte2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], boolean[], Byte2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Boolean>> order(List<Map.Entry<Byte, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedByte2BooleanMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2BooleanMap.Entry> order(ObjectList<Byte2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2BooleanMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2BooleanMap.Entry belowSamplesLesser() { return new AbstractByte2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2BooleanMap.Entry belowSamplesGreater() { return new AbstractByte2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2BooleanMap.Entry aboveSamplesLesser() { return new AbstractByte2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2BooleanMap.Entry aboveSamplesGreater() { return new AbstractByte2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}