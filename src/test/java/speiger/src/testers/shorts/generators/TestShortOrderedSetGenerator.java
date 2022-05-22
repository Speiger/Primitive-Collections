package speiger.src.testers.shorts.generators;

import speiger.src.collections.shorts.sets.ShortOrderedSet;

public interface TestShortOrderedSetGenerator extends TestShortSetGenerator {
	@Override
	ShortOrderedSet create(short... elements);
	@Override
	ShortOrderedSet create(Object... elements);
}