package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2ByteSortedMap;

public interface TestLong2ByteSortedMapGenerator extends TestLong2ByteMapGenerator, TestSortedMapGenerator<Long, Byte>
{
	@Override
	default Long2ByteSortedMap create(Object... elements) { return (Long2ByteSortedMap) TestLong2ByteMapGenerator.super.create(elements); }
	@Override
	Long2ByteSortedMap create(Long2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}