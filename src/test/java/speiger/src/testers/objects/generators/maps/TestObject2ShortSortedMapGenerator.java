package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ShortSortedMap;

public interface TestObject2ShortSortedMapGenerator<T> extends TestObject2ShortMapGenerator<T>, TestSortedMapGenerator<T, Short>
{
	@Override
	default Object2ShortSortedMap<T> create(Object... elements) { return (Object2ShortSortedMap<T>) TestObject2ShortMapGenerator.super.create(elements); }
	@Override
	Object2ShortSortedMap<T> create(Object2ShortMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}