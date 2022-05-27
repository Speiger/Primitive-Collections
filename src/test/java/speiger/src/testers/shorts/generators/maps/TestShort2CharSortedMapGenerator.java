package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestShort2CharSortedMapGenerator extends TestShort2CharMapGenerator, TestSortedMapGenerator<Short, Character>
{
	@Override
	default Short2CharSortedMap create(Object... elements) { return (Short2CharSortedMap) TestShort2CharMapGenerator.super.create(elements); }
	@Override
	Short2CharSortedMap create(Short2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}