package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2ByteMapTestGenerator<E extends Double2ByteMap>
{
	BiFunction<double[], byte[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleDouble2ByteMapTestGenerator(BiFunction<double[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Double2ByteMap.Entry>(
			new AbstractDouble2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2ByteMap.Entry... elements) {
		double[] keys = new double[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Byte>> order(List<Map.Entry<Double, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2ByteMap.Entry> order(ObjectList<Double2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2ByteMapTestGenerator<Double2ByteMap> implements TestDouble2ByteMapGenerator
	{
		public Maps(BiFunction<double[], byte[], Double2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2ByteMapTestGenerator<Double2ByteSortedMap> implements TestDouble2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], byte[], Double2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Byte>> order(List<Map.Entry<Double, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ByteMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2ByteMap.Entry> order(ObjectList<Double2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ByteMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2ByteMap.Entry belowSamplesLesser() { return new AbstractDouble2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2ByteMap.Entry belowSamplesGreater() { return new AbstractDouble2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2ByteMap.Entry aboveSamplesLesser() { return new AbstractDouble2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2ByteMap.Entry aboveSamplesGreater() { return new AbstractDouble2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}