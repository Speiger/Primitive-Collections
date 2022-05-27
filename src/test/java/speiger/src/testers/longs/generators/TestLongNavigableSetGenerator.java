package speiger.src.testers.longs.generators;

import speiger.src.collections.longs.sets.LongNavigableSet;

public interface TestLongNavigableSetGenerator extends TestLongSortedSetGenerator {
	@Override
	LongNavigableSet create(long... elements);
	@Override
	LongNavigableSet create(Object... elements);
}