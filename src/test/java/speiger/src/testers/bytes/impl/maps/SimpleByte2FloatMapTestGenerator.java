package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleByte2FloatMapTestGenerator<E extends Byte2FloatMap>
{
	BiFunction<byte[], float[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleByte2FloatMapTestGenerator(BiFunction<byte[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Byte2FloatMap.Entry>(
			new AbstractByte2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2FloatMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Float>> order(List<Map.Entry<Byte, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2FloatMap.Entry> order(ObjectList<Byte2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2FloatMapTestGenerator<Byte2FloatMap> implements TestByte2FloatMapGenerator
	{
		public Maps(BiFunction<byte[], float[], Byte2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2FloatMapTestGenerator<Byte2FloatSortedMap> implements TestByte2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], float[], Byte2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Float>> order(List<Map.Entry<Byte, Float>> insertionOrder) {
			insertionOrder.sort(DerivedByte2FloatMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2FloatMap.Entry> order(ObjectList<Byte2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2FloatMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2FloatMap.Entry belowSamplesLesser() { return new AbstractByte2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2FloatMap.Entry belowSamplesGreater() { return new AbstractByte2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2FloatMap.Entry aboveSamplesLesser() { return new AbstractByte2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2FloatMap.Entry aboveSamplesGreater() { return new AbstractByte2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}