package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2FloatSortedMap;

public interface TestObject2FloatSortedMapGenerator<T> extends TestObject2FloatMapGenerator<T>, TestSortedMapGenerator<T, Float>
{
	@Override
	default Object2FloatSortedMap<T> create(Object... elements) { return (Object2FloatSortedMap<T>) TestObject2FloatMapGenerator.super.create(elements); }
	@Override
	Object2FloatSortedMap<T> create(Object2FloatMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}