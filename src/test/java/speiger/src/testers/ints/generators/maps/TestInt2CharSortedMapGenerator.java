package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2CharSortedMap;

public interface TestInt2CharSortedMapGenerator extends TestInt2CharMapGenerator, TestSortedMapGenerator<Integer, Character>
{
	@Override
	default Int2CharSortedMap create(Object... elements) { return (Int2CharSortedMap) TestInt2CharMapGenerator.super.create(elements); }
	@Override
	Int2CharSortedMap create(Int2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}