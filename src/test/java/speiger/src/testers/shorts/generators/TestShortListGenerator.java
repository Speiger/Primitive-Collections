package speiger.src.testers.shorts.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.shorts.lists.ShortList;

public interface TestShortListGenerator extends TestListGenerator<Short>, TestShortCollectionGenerator
{
	@Override
	ShortList create(Object... elements);
	@Override
	ShortList create(short... elements);
}