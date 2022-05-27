package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2DoubleMapTestGenerator<E extends Byte2DoubleMap>
{
	BiFunction<byte[], double[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleByte2DoubleMapTestGenerator(BiFunction<byte[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Byte2DoubleMap.Entry>(
			new AbstractByte2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2DoubleMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Double>> order(List<Map.Entry<Byte, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2DoubleMap.Entry> order(ObjectList<Byte2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2DoubleMapTestGenerator<Byte2DoubleMap> implements TestByte2DoubleMapGenerator
	{
		public Maps(BiFunction<byte[], double[], Byte2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2DoubleMapTestGenerator<Byte2DoubleSortedMap> implements TestByte2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], double[], Byte2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Double>> order(List<Map.Entry<Byte, Double>> insertionOrder) {
			insertionOrder.sort(DerivedByte2DoubleMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2DoubleMap.Entry> order(ObjectList<Byte2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2DoubleMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2DoubleMap.Entry belowSamplesLesser() { return new AbstractByte2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2DoubleMap.Entry belowSamplesGreater() { return new AbstractByte2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2DoubleMap.Entry aboveSamplesLesser() { return new AbstractByte2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2DoubleMap.Entry aboveSamplesGreater() { return new AbstractByte2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}