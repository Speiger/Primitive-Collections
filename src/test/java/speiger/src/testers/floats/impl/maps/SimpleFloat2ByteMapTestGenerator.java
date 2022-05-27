package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2ByteMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleFloat2ByteMapTestGenerator<E extends Float2ByteMap>
{
	BiFunction<float[], byte[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleFloat2ByteMapTestGenerator(BiFunction<float[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Float2ByteMap.Entry>(
			new AbstractFloat2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2ByteMap.Entry... elements) {
		float[] keys = new float[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Byte>> order(List<Map.Entry<Float, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2ByteMap.Entry> order(ObjectList<Float2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2ByteMapTestGenerator<Float2ByteMap> implements TestFloat2ByteMapGenerator
	{
		public Maps(BiFunction<float[], byte[], Float2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2ByteMapTestGenerator<Float2ByteSortedMap> implements TestFloat2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], byte[], Float2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Byte>> order(List<Map.Entry<Float, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ByteMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2ByteMap.Entry> order(ObjectList<Float2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ByteMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2ByteMap.Entry belowSamplesLesser() { return new AbstractFloat2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2ByteMap.Entry belowSamplesGreater() { return new AbstractFloat2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2ByteMap.Entry aboveSamplesLesser() { return new AbstractFloat2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2ByteMap.Entry aboveSamplesGreater() { return new AbstractFloat2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}