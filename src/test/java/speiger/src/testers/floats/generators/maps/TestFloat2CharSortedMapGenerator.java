package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestFloat2CharSortedMapGenerator extends TestFloat2CharMapGenerator, TestSortedMapGenerator<Float, Character>
{
	@Override
	default Float2CharSortedMap create(Object... elements) { return (Float2CharSortedMap) TestFloat2CharMapGenerator.super.create(elements); }
	@Override
	Float2CharSortedMap create(Float2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}