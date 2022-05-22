package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.chars.utils.MinimalCharCollection;

@Ignore
public class CharCollectionRemoveAllTester extends AbstractCharCollectionTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollection() {
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalCharCollection.of()));
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresent() {
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalCharCollection.of(e3())));
		expectUnchanged();
	}
	

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresentFetchRemoved() {
		CharList list = new CharArrayList();
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalCharCollection.of(e3()), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollectionFetching() {
		CharList list = new CharArrayList();
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalCharCollection.of(), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());		
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_allPresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalCharCollection.of(e0())));
		expectMissing(e0());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalCharCollection.of(e0(), e3())));
		expectMissing(e0());
	}

	@CollectionFeature.Require({ SUPPORTS_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testRemoveAllSomePresentConcurrentWithIteration() {
		try {
			CharIterator iterator = collection.iterator();
			assertTrue(collection.removeAll(MinimalCharCollection.of(e0(), e3())));
			iterator.nextChar();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresentLargeCollectionToRemove() {
		assertTrue("removeAll(largeIntersectingCollection) should return true", collection.removeAll(MinimalCharCollection.of(e0(), e0(), e0(), e3(), e3(), e3())));
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_someFetchRemovedElements() {
		CharList list = new CharArrayList();
		assertTrue("removeAll(largeIntersectingCollection, RemovedElements) should return true", collection.removeAll(MinimalCharCollection.of(e0(), e0(), e0(), e3(), e3(), e3()), list::add));
		expectMissing(e0());
		CharHelpers.assertContentsAnyOrder(list, e0());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedEmptyCollection() {
		try {
			assertFalse("removeAll(emptyCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalCharCollection.of()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedNonePresent() {
		try {
			assertFalse("removeAll(disjointCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalCharCollection.of(e3())));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_unsupportedPresent() {
		try {
			collection.removeAll(MinimalCharCollection.of(e0()));
			fail("removeAll(intersectingCollection) should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertTrue(collection.contains(e0()));
	}
}