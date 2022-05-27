package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleObject2BooleanMapTestGenerator<T, E extends Object2BooleanMap<T>>
{
	BiFunction<T[], boolean[], E> mapper;
	T[] keys;
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleObject2BooleanMapTestGenerator(BiFunction<T[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2BooleanMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2BooleanMap.Entry<T>>(
			new AbstractObject2BooleanMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2BooleanMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2BooleanMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2BooleanMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2BooleanMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2BooleanMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Boolean>> order(List<Map.Entry<T, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2BooleanMap.Entry<T>> order(ObjectList<Object2BooleanMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2BooleanMapTestGenerator<T, Object2BooleanMap<T>> implements TestObject2BooleanMapGenerator<T>
	{
		public Maps(BiFunction<T[], boolean[], Object2BooleanMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2BooleanMapTestGenerator<T, Object2BooleanSortedMap<T>> implements TestObject2BooleanSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], boolean[], Object2BooleanSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Boolean>> order(List<Map.Entry<T, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedObject2BooleanMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2BooleanMap.Entry<T>> order(ObjectList<Object2BooleanMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2BooleanMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2BooleanMap.Entry<T> belowSamplesLesser() { return new AbstractObject2BooleanMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2BooleanMap.Entry<T> belowSamplesGreater() { return new AbstractObject2BooleanMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2BooleanMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2BooleanMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2BooleanMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2BooleanMap.BasicEntry<>(keys[8], values[8]); }
	}
}