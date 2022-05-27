package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2IntSortedMap;

public interface TestLong2IntSortedMapGenerator extends TestLong2IntMapGenerator, TestSortedMapGenerator<Long, Integer>
{
	@Override
	default Long2IntSortedMap create(Object... elements) { return (Long2IntSortedMap) TestLong2IntMapGenerator.super.create(elements); }
	@Override
	Long2IntSortedMap create(Long2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}