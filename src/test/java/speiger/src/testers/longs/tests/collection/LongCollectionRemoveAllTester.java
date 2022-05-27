package speiger.src.testers.longs.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.longs.utils.MinimalLongCollection;

@Ignore
@SuppressWarnings("javadoc")
public class LongCollectionRemoveAllTester extends AbstractLongCollectionTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollection() {
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalLongCollection.of()));
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresent() {
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalLongCollection.of(e3())));
		expectUnchanged();
	}
	

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresentFetchRemoved() {
		LongList list = new LongArrayList();
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalLongCollection.of(e3()), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollectionFetching() {
		LongList list = new LongArrayList();
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalLongCollection.of(), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());		
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_allPresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalLongCollection.of(e0())));
		expectMissing(e0());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalLongCollection.of(e0(), e3())));
		expectMissing(e0());
	}

	@CollectionFeature.Require({ SUPPORTS_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testRemoveAllSomePresentConcurrentWithIteration() {
		try {
			LongIterator iterator = collection.iterator();
			assertTrue(collection.removeAll(MinimalLongCollection.of(e0(), e3())));
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresentLargeCollectionToRemove() {
		assertTrue("removeAll(largeIntersectingCollection) should return true", collection.removeAll(MinimalLongCollection.of(e0(), e0(), e0(), e3(), e3(), e3())));
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_someFetchRemovedElements() {
		LongList list = new LongArrayList();
		assertTrue("removeAll(largeIntersectingCollection, RemovedElements) should return true", collection.removeAll(MinimalLongCollection.of(e0(), e0(), e0(), e3(), e3(), e3()), list::add));
		expectMissing(e0());
		LongHelpers.assertContentsAnyOrder(list, e0());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedEmptyCollection() {
		try {
			assertFalse("removeAll(emptyCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalLongCollection.of()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedNonePresent() {
		try {
			assertFalse("removeAll(disjointCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalLongCollection.of(e3())));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_unsupportedPresent() {
		try {
			collection.removeAll(MinimalLongCollection.of(e0()));
			fail("removeAll(intersectingCollection) should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertTrue(collection.contains(e0()));
	}
}