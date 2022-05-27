package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2LongSortedMapGenerator extends TestByte2LongMapGenerator, TestSortedMapGenerator<Byte, Long>
{
	@Override
	default Byte2LongSortedMap create(Object... elements) { return (Byte2LongSortedMap) TestByte2LongMapGenerator.super.create(elements); }
	@Override
	Byte2LongSortedMap create(Byte2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}