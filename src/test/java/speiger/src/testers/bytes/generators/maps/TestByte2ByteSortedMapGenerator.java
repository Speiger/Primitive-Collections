package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2ByteSortedMapGenerator extends TestByte2ByteMapGenerator, TestSortedMapGenerator<Byte, Byte>
{
	@Override
	default Byte2ByteSortedMap create(Object... elements) { return (Byte2ByteSortedMap) TestByte2ByteMapGenerator.super.create(elements); }
	@Override
	Byte2ByteSortedMap create(Byte2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}