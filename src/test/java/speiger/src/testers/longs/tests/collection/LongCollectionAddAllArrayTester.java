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
public class LongCollectionAddAllArrayTester extends AbstractLongCollectionTester {
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedNothing() {
		assertFalse("addAll(nothing[]) should return false", collection.addAll(emptyArray()));
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testAddAllArray_unsupportedNothing() {
		try {
			assertFalse("addAll(nothing[]) should return false or throw", collection.addAll(emptyArray()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedNonePresent() {
		assertTrue("addAll(nonePresent[]) should return true", collection.addAll(createDisjointArray()));
		expectAdded(e3(), e4());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testAddAllArray_unsupportedNonePresent() {
		try {
			collection.addAll(createDisjointArray());
			fail("addAll(nonePresent[]) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
		@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedToLargeOffset() {
		try {
			collection.addAll(createDisjointArray(), 5, 2);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedToLargeArray() {
		try {
			collection.addAll(createDisjointArray(), 3);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedToSmallOffset() {
		try {
			collection.addAll(createDisjointArray(), -1, 2);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedAddSubArray() {
		try {
			collection.addAll(createDisjointArray(), 1);
		} catch (IndexOutOfBoundsException e) {
		}
		expectAdded(e3());
		expectMissing(e4());
	}
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArray_supportedSomePresent() {
		assertTrue("addAll(somePresent[]) should return true", collection.addAll(e3(), e0()));
		assertTrue("should contain " + e3(), collection.contains(e3()));
		assertTrue("should contain " + e0(), collection.contains(e0()));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArray_unsupportedSomePresent() {
		try {
			collection.addAll(e3(), e0());
			fail("addAll(somePresent[]) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require({ SUPPORTS_ADD, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayConcurrentWithIteration() {
		try {
			LongIterator iterator = collection.iterator();
			assertTrue(collection.addAll(e3(), e0()));
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArray_unsupportedAllPresent() {
		try {
			assertFalse("addAll(allPresent[]) should return false or throw", collection.addAll(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}