package speiger.src.testers.ints.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

@Ignore
public class IntCollectionClearTester extends AbstractIntCollectionTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testClear() {
		collection.clear();
		assertTrue("After clear(), a collection should be empty.", collection.isEmpty());
		assertEquals(0, collection.size());
		assertFalse(collection.iterator().hasNext());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testClear_unsupported() {
		try {
			collection.clear();
			fail("clear() should throw UnsupportedOperation if a collection does "
					+ "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testClear_unsupportedByEmptyCollection() {
		try {
			collection.clear();
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require({ SUPPORTS_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithIteration() {
		try {
			IntIterator iterator = collection.iterator();
			collection.clear();
			iterator.nextInt();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
}