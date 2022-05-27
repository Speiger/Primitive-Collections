package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2LongMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2LongMapTestGenerator<T, E extends Object2LongMap<T>>
{
	BiFunction<T[], long[], E> mapper;
	T[] keys;
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleObject2LongMapTestGenerator(BiFunction<T[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2LongMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2LongMap.Entry<T>>(
			new AbstractObject2LongMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2LongMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2LongMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2LongMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2LongMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2LongMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Long>> order(List<Map.Entry<T, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2LongMap.Entry<T>> order(ObjectList<Object2LongMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2LongMapTestGenerator<T, Object2LongMap<T>> implements TestObject2LongMapGenerator<T>
	{
		public Maps(BiFunction<T[], long[], Object2LongMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2LongMapTestGenerator<T, Object2LongSortedMap<T>> implements TestObject2LongSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], long[], Object2LongSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Long>> order(List<Map.Entry<T, Long>> insertionOrder) {
			insertionOrder.sort(DerivedObject2LongMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2LongMap.Entry<T>> order(ObjectList<Object2LongMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2LongMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2LongMap.Entry<T> belowSamplesLesser() { return new AbstractObject2LongMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2LongMap.Entry<T> belowSamplesGreater() { return new AbstractObject2LongMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2LongMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2LongMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2LongMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2LongMap.BasicEntry<>(keys[8], values[8]); }
	}
}