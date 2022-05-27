package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2LongSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2LongSortedMapGenerator<T> extends TestObject2LongMapGenerator<T>, TestSortedMapGenerator<T, Long>
{
	@Override
	default Object2LongSortedMap<T> create(Object... elements) { return (Object2LongSortedMap<T>) TestObject2LongMapGenerator.super.create(elements); }
	@Override
	Object2LongSortedMap<T> create(Object2LongMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}