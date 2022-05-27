package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2CharMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleInt2CharMapTestGenerator<E extends Int2CharMap>
{
	BiFunction<int[], char[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleInt2CharMapTestGenerator(BiFunction<int[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2CharMap.Entry> getSamples() {
		return new ObjectSamples<Int2CharMap.Entry>(
			new AbstractInt2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2CharMap.Entry... elements) {
		int[] keys = new int[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Character>> order(List<Map.Entry<Integer, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2CharMap.Entry> order(ObjectList<Int2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2CharMapTestGenerator<Int2CharMap> implements TestInt2CharMapGenerator
	{
		public Maps(BiFunction<int[], char[], Int2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2CharMapTestGenerator<Int2CharSortedMap> implements TestInt2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], char[], Int2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Character>> order(List<Map.Entry<Integer, Character>> insertionOrder) {
			insertionOrder.sort(DerivedInt2CharMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2CharMap.Entry> order(ObjectList<Int2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2CharMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2CharMap.Entry belowSamplesLesser() { return new AbstractInt2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2CharMap.Entry belowSamplesGreater() { return new AbstractInt2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2CharMap.Entry aboveSamplesLesser() { return new AbstractInt2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2CharMap.Entry aboveSamplesGreater() { return new AbstractInt2CharMap.BasicEntry(keys[8], values[8]); }
	}
}