package speiger.src.testers.doubles.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.doubles.lists.DoubleList;

public interface TestDoubleListGenerator extends TestListGenerator<Double>, TestDoubleCollectionGenerator
{
	@Override
	DoubleList create(Object... elements);
	@Override
	DoubleList create(double... elements);
}