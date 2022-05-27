package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2BooleanSortedMapGenerator extends TestByte2BooleanMapGenerator, TestSortedMapGenerator<Byte, Boolean>
{
	@Override
	default Byte2BooleanSortedMap create(Object... elements) { return (Byte2BooleanSortedMap) TestByte2BooleanMapGenerator.super.create(elements); }
	@Override
	Byte2BooleanSortedMap create(Byte2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}