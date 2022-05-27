package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2IntSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2IntSortedMapGenerator<T> extends TestObject2IntMapGenerator<T>, TestSortedMapGenerator<T, Integer>
{
	@Override
	default Object2IntSortedMap<T> create(Object... elements) { return (Object2IntSortedMap<T>) TestObject2IntMapGenerator.super.create(elements); }
	@Override
	Object2IntSortedMap<T> create(Object2IntMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}