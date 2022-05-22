package speiger.src.testers.chars.tests.base;

import speiger.src.collections.chars.sets.CharSet;

public class AbstractCharSetTester extends AbstractCharCollectionTester {
	protected final CharSet getSet() {
		return (CharSet)collection;
	}
}