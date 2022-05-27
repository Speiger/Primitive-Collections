package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2ShortSortedMapGenerator extends TestByte2ShortMapGenerator, TestSortedMapGenerator<Byte, Short>
{
	@Override
	default Byte2ShortSortedMap create(Object... elements) { return (Byte2ShortSortedMap) TestByte2ShortMapGenerator.super.create(elements); }
	@Override
	Byte2ShortSortedMap create(Byte2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}