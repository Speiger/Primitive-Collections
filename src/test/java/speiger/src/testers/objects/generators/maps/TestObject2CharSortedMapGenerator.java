package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2CharSortedMapGenerator<T> extends TestObject2CharMapGenerator<T>, TestSortedMapGenerator<T, Character>
{
	@Override
	default Object2CharSortedMap<T> create(Object... elements) { return (Object2CharSortedMap<T>) TestObject2CharMapGenerator.super.create(elements); }
	@Override
	Object2CharSortedMap<T> create(Object2CharMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}