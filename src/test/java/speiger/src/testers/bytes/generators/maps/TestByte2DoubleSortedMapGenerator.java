package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2DoubleSortedMapGenerator extends TestByte2DoubleMapGenerator, TestSortedMapGenerator<Byte, Double>
{
	@Override
	default Byte2DoubleSortedMap create(Object... elements) { return (Byte2DoubleSortedMap) TestByte2DoubleMapGenerator.super.create(elements); }
	@Override
	Byte2DoubleSortedMap create(Byte2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}