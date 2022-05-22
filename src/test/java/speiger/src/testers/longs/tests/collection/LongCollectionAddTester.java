package speiger.src.testers.longs.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
public class LongCollectionAddTester extends AbstractLongCollectionTester {
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAdd_supportedNotPresent() {
		assertTrue("add(notPresent) should return true", collection.add(e3()));
		expectAdded(e3());
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testAdd_unsupportedNotPresent() {
		try {
			collection.add(e3());
			fail("add(notPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_unsupportedPresent() {
		try {
			assertFalse("add(present) should return false or throw", collection.add(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require({ SUPPORTS_ADD, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(absent = ZERO)
	public void testAddConcurrentWithIteration() {
		try {
			LongIterator iterator = collection.iterator();
			assertTrue(collection.add(e3()));
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
}