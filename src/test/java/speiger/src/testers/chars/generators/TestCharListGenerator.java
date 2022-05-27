package speiger.src.testers.chars.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.chars.lists.CharList;

@SuppressWarnings("javadoc")
public interface TestCharListGenerator extends TestListGenerator<Character>, TestCharCollectionGenerator
{
	@Override
	CharList create(Object... elements);
	@Override
	CharList create(char... elements);
}