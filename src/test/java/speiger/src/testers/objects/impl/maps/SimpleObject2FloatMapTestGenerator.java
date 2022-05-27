package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2FloatMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleObject2FloatMapTestGenerator<T, E extends Object2FloatMap<T>>
{
	BiFunction<T[], float[], E> mapper;
	T[] keys;
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleObject2FloatMapTestGenerator(BiFunction<T[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2FloatMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2FloatMap.Entry<T>>(
			new AbstractObject2FloatMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2FloatMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2FloatMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2FloatMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2FloatMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2FloatMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Float>> order(List<Map.Entry<T, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2FloatMap.Entry<T>> order(ObjectList<Object2FloatMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2FloatMapTestGenerator<T, Object2FloatMap<T>> implements TestObject2FloatMapGenerator<T>
	{
		public Maps(BiFunction<T[], float[], Object2FloatMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2FloatMapTestGenerator<T, Object2FloatSortedMap<T>> implements TestObject2FloatSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], float[], Object2FloatSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Float>> order(List<Map.Entry<T, Float>> insertionOrder) {
			insertionOrder.sort(DerivedObject2FloatMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2FloatMap.Entry<T>> order(ObjectList<Object2FloatMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2FloatMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2FloatMap.Entry<T> belowSamplesLesser() { return new AbstractObject2FloatMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2FloatMap.Entry<T> belowSamplesGreater() { return new AbstractObject2FloatMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2FloatMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2FloatMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2FloatMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2FloatMap.BasicEntry<>(keys[8], values[8]); }
	}
}