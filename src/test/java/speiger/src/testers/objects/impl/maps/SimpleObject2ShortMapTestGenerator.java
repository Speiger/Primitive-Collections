package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2ShortMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleObject2ShortMapTestGenerator<T, E extends Object2ShortMap<T>>
{
	BiFunction<T[], short[], E> mapper;
	T[] keys;
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleObject2ShortMapTestGenerator(BiFunction<T[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2ShortMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2ShortMap.Entry<T>>(
			new AbstractObject2ShortMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2ShortMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2ShortMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2ShortMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2ShortMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2ShortMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Short>> order(List<Map.Entry<T, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2ShortMap.Entry<T>> order(ObjectList<Object2ShortMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2ShortMapTestGenerator<T, Object2ShortMap<T>> implements TestObject2ShortMapGenerator<T>
	{
		public Maps(BiFunction<T[], short[], Object2ShortMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2ShortMapTestGenerator<T, Object2ShortSortedMap<T>> implements TestObject2ShortSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], short[], Object2ShortSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Short>> order(List<Map.Entry<T, Short>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ShortMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2ShortMap.Entry<T>> order(ObjectList<Object2ShortMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ShortMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2ShortMap.Entry<T> belowSamplesLesser() { return new AbstractObject2ShortMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2ShortMap.Entry<T> belowSamplesGreater() { return new AbstractObject2ShortMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2ShortMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2ShortMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2ShortMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2ShortMap.BasicEntry<>(keys[8], values[8]); }
	}
}