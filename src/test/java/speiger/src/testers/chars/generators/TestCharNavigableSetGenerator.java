package speiger.src.testers.chars.generators;

import speiger.src.collections.chars.sets.CharNavigableSet;

public interface TestCharNavigableSetGenerator extends TestCharSortedSetGenerator {
	@Override
	CharNavigableSet create(char... elements);
	@Override
	CharNavigableSet create(Object... elements);
}