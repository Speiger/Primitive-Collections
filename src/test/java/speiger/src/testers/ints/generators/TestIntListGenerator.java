package speiger.src.testers.ints.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.ints.lists.IntList;

@SuppressWarnings("javadoc")
public interface TestIntListGenerator extends TestListGenerator<Integer>, TestIntCollectionGenerator
{
	@Override
	IntList create(Object... elements);
	@Override
	IntList create(int... elements);
}