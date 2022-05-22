package speiger.src.testers.longs.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
public class LongCollectionContainsTester extends AbstractLongCollectionTester {
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("contains(present) should return true", collection.contains(e0()));
	}

	public void testContains_no() {
		assertFalse("contains(notPresent) should return false", collection.contains(e3()));
	}
}