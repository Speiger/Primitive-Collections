package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2ShortMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleInt2ShortMapTestGenerator<E extends Int2ShortMap>
{
	BiFunction<int[], short[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleInt2ShortMapTestGenerator(BiFunction<int[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Int2ShortMap.Entry>(
			new AbstractInt2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2ShortMap.Entry... elements) {
		int[] keys = new int[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Short>> order(List<Map.Entry<Integer, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2ShortMap.Entry> order(ObjectList<Int2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2ShortMapTestGenerator<Int2ShortMap> implements TestInt2ShortMapGenerator
	{
		public Maps(BiFunction<int[], short[], Int2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2ShortMapTestGenerator<Int2ShortSortedMap> implements TestInt2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], short[], Int2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Short>> order(List<Map.Entry<Integer, Short>> insertionOrder) {
			insertionOrder.sort(DerivedInt2ShortMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2ShortMap.Entry> order(ObjectList<Int2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2ShortMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2ShortMap.Entry belowSamplesLesser() { return new AbstractInt2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2ShortMap.Entry belowSamplesGreater() { return new AbstractInt2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2ShortMap.Entry aboveSamplesLesser() { return new AbstractInt2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2ShortMap.Entry aboveSamplesGreater() { return new AbstractInt2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}