package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2ByteMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2ByteMapTestGenerator<T, E extends Object2ByteMap<T>>
{
	BiFunction<T[], byte[], E> mapper;
	T[] keys;
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleObject2ByteMapTestGenerator(BiFunction<T[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2ByteMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2ByteMap.Entry<T>>(
			new AbstractObject2ByteMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2ByteMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2ByteMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2ByteMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2ByteMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2ByteMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Byte>> order(List<Map.Entry<T, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2ByteMap.Entry<T>> order(ObjectList<Object2ByteMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2ByteMapTestGenerator<T, Object2ByteMap<T>> implements TestObject2ByteMapGenerator<T>
	{
		public Maps(BiFunction<T[], byte[], Object2ByteMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2ByteMapTestGenerator<T, Object2ByteSortedMap<T>> implements TestObject2ByteSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], byte[], Object2ByteSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Byte>> order(List<Map.Entry<T, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ByteMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2ByteMap.Entry<T>> order(ObjectList<Object2ByteMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ByteMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2ByteMap.Entry<T> belowSamplesLesser() { return new AbstractObject2ByteMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2ByteMap.Entry<T> belowSamplesGreater() { return new AbstractObject2ByteMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2ByteMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2ByteMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2ByteMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2ByteMap.BasicEntry<>(keys[8], values[8]); }
	}
}