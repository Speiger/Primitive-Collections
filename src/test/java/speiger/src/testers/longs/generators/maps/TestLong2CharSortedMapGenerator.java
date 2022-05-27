package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestLong2CharSortedMapGenerator extends TestLong2CharMapGenerator, TestSortedMapGenerator<Long, Character>
{
	@Override
	default Long2CharSortedMap create(Object... elements) { return (Long2CharSortedMap) TestLong2CharMapGenerator.super.create(elements); }
	@Override
	Long2CharSortedMap create(Long2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}