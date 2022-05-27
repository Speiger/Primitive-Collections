package speiger.src.testers.chars.tests.base;

import org.junit.Ignore;

import speiger.src.collections.chars.sets.CharSet;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractCharSetTester extends AbstractCharCollectionTester
{
	protected final CharSet getSet() {
		return (CharSet)collection;
	}
}