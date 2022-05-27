package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2FloatSortedMapGenerator extends TestByte2FloatMapGenerator, TestSortedMapGenerator<Byte, Float>
{
	@Override
	default Byte2FloatSortedMap create(Object... elements) { return (Byte2FloatSortedMap) TestByte2FloatMapGenerator.super.create(elements); }
	@Override
	Byte2FloatSortedMap create(Byte2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}