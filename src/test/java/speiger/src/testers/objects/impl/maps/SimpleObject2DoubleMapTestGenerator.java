package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2DoubleMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2DoubleMapTestGenerator<T, E extends Object2DoubleMap<T>>
{
	BiFunction<T[], double[], E> mapper;
	T[] keys;
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleObject2DoubleMapTestGenerator(BiFunction<T[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2DoubleMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2DoubleMap.Entry<T>>(
			new AbstractObject2DoubleMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2DoubleMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2DoubleMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2DoubleMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2DoubleMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2DoubleMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Double>> order(List<Map.Entry<T, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2DoubleMap.Entry<T>> order(ObjectList<Object2DoubleMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2DoubleMapTestGenerator<T, Object2DoubleMap<T>> implements TestObject2DoubleMapGenerator<T>
	{
		public Maps(BiFunction<T[], double[], Object2DoubleMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2DoubleMapTestGenerator<T, Object2DoubleSortedMap<T>> implements TestObject2DoubleSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], double[], Object2DoubleSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Double>> order(List<Map.Entry<T, Double>> insertionOrder) {
			insertionOrder.sort(DerivedObject2DoubleMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2DoubleMap.Entry<T>> order(ObjectList<Object2DoubleMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2DoubleMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> belowSamplesLesser() { return new AbstractObject2DoubleMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2DoubleMap.Entry<T> belowSamplesGreater() { return new AbstractObject2DoubleMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2DoubleMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2DoubleMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2DoubleMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2DoubleMap.BasicEntry<>(keys[8], values[8]); }
	}
}