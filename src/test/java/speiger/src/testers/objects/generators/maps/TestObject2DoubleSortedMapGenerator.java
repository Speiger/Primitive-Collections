package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleSortedMap;

public interface TestObject2DoubleSortedMapGenerator<T> extends TestObject2DoubleMapGenerator<T>, TestSortedMapGenerator<T, Double>
{
	@Override
	default Object2DoubleSortedMap<T> create(Object... elements) { return (Object2DoubleSortedMap<T>) TestObject2DoubleMapGenerator.super.create(elements); }
	@Override
	Object2DoubleSortedMap<T> create(Object2DoubleMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}