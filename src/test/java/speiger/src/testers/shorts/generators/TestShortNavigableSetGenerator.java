package speiger.src.testers.shorts.generators;

import speiger.src.collections.shorts.sets.ShortNavigableSet;

@SuppressWarnings("javadoc")
public interface TestShortNavigableSetGenerator extends TestShortSortedSetGenerator {
	@Override
	ShortNavigableSet create(short... elements);
	@Override
	ShortNavigableSet create(Object... elements);
}