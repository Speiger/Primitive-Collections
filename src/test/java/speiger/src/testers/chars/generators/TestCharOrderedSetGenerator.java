package speiger.src.testers.chars.generators;

import speiger.src.collections.chars.sets.CharOrderedSet;

@SuppressWarnings("javadoc")
public interface TestCharOrderedSetGenerator extends TestCharSetGenerator {
	@Override
	CharOrderedSet create(char... elements);
	@Override
	CharOrderedSet create(Object... elements);
}