package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2IntMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2IntMapTestGenerator<T, E extends Object2IntMap<T>>
{
	BiFunction<T[], int[], E> mapper;
	T[] keys;
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleObject2IntMapTestGenerator(BiFunction<T[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2IntMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2IntMap.Entry<T>>(
			new AbstractObject2IntMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2IntMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2IntMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2IntMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2IntMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2IntMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Integer>> order(List<Map.Entry<T, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2IntMap.Entry<T>> order(ObjectList<Object2IntMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2IntMapTestGenerator<T, Object2IntMap<T>> implements TestObject2IntMapGenerator<T>
	{
		public Maps(BiFunction<T[], int[], Object2IntMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2IntMapTestGenerator<T, Object2IntSortedMap<T>> implements TestObject2IntSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], int[], Object2IntSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Integer>> order(List<Map.Entry<T, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedObject2IntMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2IntMap.Entry<T>> order(ObjectList<Object2IntMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2IntMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2IntMap.Entry<T> belowSamplesLesser() { return new AbstractObject2IntMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2IntMap.Entry<T> belowSamplesGreater() { return new AbstractObject2IntMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2IntMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2IntMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2IntMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2IntMap.BasicEntry<>(keys[8], values[8]); }
	}
}