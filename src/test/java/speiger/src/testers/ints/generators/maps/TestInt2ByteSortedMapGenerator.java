package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2ByteSortedMap;

public interface TestInt2ByteSortedMapGenerator extends TestInt2ByteMapGenerator, TestSortedMapGenerator<Integer, Byte>
{
	@Override
	default Int2ByteSortedMap create(Object... elements) { return (Int2ByteSortedMap) TestInt2ByteMapGenerator.super.create(elements); }
	@Override
	Int2ByteSortedMap create(Int2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}