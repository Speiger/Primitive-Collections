package speiger.src.testers.longs.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.longs.lists.LongList;

@SuppressWarnings("javadoc")
public interface TestLongListGenerator extends TestListGenerator<Long>, TestLongCollectionGenerator
{
	@Override
	LongList create(Object... elements);
	@Override
	LongList create(long... elements);
}