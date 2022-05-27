package speiger.src.testers.longs.tests.base;

import org.junit.Ignore;

import speiger.src.collections.longs.sets.LongSet;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLongSetTester extends AbstractLongCollectionTester
{
	protected final LongSet getSet() {
		return (LongSet)collection;
	}
}