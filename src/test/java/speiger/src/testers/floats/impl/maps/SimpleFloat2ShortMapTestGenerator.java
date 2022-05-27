package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2ShortMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleFloat2ShortMapTestGenerator<E extends Float2ShortMap>
{
	BiFunction<float[], short[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleFloat2ShortMapTestGenerator(BiFunction<float[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Float2ShortMap.Entry>(
			new AbstractFloat2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2ShortMap.Entry... elements) {
		float[] keys = new float[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Short>> order(List<Map.Entry<Float, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2ShortMap.Entry> order(ObjectList<Float2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2ShortMapTestGenerator<Float2ShortMap> implements TestFloat2ShortMapGenerator
	{
		public Maps(BiFunction<float[], short[], Float2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2ShortMapTestGenerator<Float2ShortSortedMap> implements TestFloat2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], short[], Float2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Short>> order(List<Map.Entry<Float, Short>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ShortMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2ShortMap.Entry> order(ObjectList<Float2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ShortMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2ShortMap.Entry belowSamplesLesser() { return new AbstractFloat2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2ShortMap.Entry belowSamplesGreater() { return new AbstractFloat2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2ShortMap.Entry aboveSamplesLesser() { return new AbstractFloat2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2ShortMap.Entry aboveSamplesGreater() { return new AbstractFloat2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}