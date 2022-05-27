package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2CharMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleFloat2CharMapTestGenerator<E extends Float2CharMap>
{
	BiFunction<float[], char[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleFloat2CharMapTestGenerator(BiFunction<float[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2CharMap.Entry> getSamples() {
		return new ObjectSamples<Float2CharMap.Entry>(
			new AbstractFloat2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2CharMap.Entry... elements) {
		float[] keys = new float[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Character>> order(List<Map.Entry<Float, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2CharMap.Entry> order(ObjectList<Float2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2CharMapTestGenerator<Float2CharMap> implements TestFloat2CharMapGenerator
	{
		public Maps(BiFunction<float[], char[], Float2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2CharMapTestGenerator<Float2CharSortedMap> implements TestFloat2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], char[], Float2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Character>> order(List<Map.Entry<Float, Character>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2CharMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2CharMap.Entry> order(ObjectList<Float2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2CharMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2CharMap.Entry belowSamplesLesser() { return new AbstractFloat2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2CharMap.Entry belowSamplesGreater() { return new AbstractFloat2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2CharMap.Entry aboveSamplesLesser() { return new AbstractFloat2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2CharMap.Entry aboveSamplesGreater() { return new AbstractFloat2CharMap.BasicEntry(keys[8], values[8]); }
	}
}