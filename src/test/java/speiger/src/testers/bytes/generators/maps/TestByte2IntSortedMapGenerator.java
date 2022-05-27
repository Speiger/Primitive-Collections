package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntSortedMap;

public interface TestByte2IntSortedMapGenerator extends TestByte2IntMapGenerator, TestSortedMapGenerator<Byte, Integer>
{
	@Override
	default Byte2IntSortedMap create(Object... elements) { return (Byte2IntSortedMap) TestByte2IntMapGenerator.super.create(elements); }
	@Override
	Byte2IntSortedMap create(Byte2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}