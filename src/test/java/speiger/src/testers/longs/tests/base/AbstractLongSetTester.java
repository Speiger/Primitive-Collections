package speiger.src.testers.longs.tests.base;

import org.junit.Ignore;

import speiger.src.collections.longs.sets.LongSet;

@Ignore
public class AbstractLongSetTester extends AbstractLongCollectionTester
{
	protected final LongSet getSet() {
		return (LongSet)collection;
	}
}