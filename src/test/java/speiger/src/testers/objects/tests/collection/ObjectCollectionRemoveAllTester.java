package speiger.src.testers.objects.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.ObjectHelpers;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectCollectionRemoveAllTester<T> extends AbstractObjectCollectionTester<T>
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollection() {
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalObjectCollection.of()));
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresent() {
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalObjectCollection.of(e3())));
		expectUnchanged();
	}
	

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_nonePresentFetchRemoved() {
		ObjectList<T> list = new ObjectArrayList<>();
		assertFalse("removeAll(disjointCollection) should return false", collection.removeAll(MinimalObjectCollection.of(e3()), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRemoveAll_emptyCollectionFetching() {
		ObjectList<T> list = new ObjectArrayList<>();
		assertFalse("removeAll(emptyCollection) should return false", collection.removeAll(MinimalObjectCollection.of(), list::add));
		expectUnchanged();
		assertTrue(list.isEmpty());		
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_allPresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalObjectCollection.of(e0())));
		expectMissing(e0());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresent() {
		assertTrue("removeAll(intersectingCollection) should return true", collection.removeAll(MinimalObjectCollection.of(e0(), e3())));
		expectMissing(e0());
	}

	@CollectionFeature.Require({ SUPPORTS_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testRemoveAllSomePresentConcurrentWithIteration() {
		try {
			ObjectIterator<T> iterator = collection.iterator();
			assertTrue(collection.removeAll(MinimalObjectCollection.of(e0(), e3())));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_somePresentLargeCollectionToRemove() {
		assertTrue("removeAll(largeIntersectingCollection) should return true", collection.removeAll(MinimalObjectCollection.of(e0(), e0(), e0(), e3(), e3(), e3())));
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_someFetchRemovedElements() {
		ObjectList<T> list = new ObjectArrayList<>();
		assertTrue("removeAll(largeIntersectingCollection, RemovedElements) should return true", collection.removeAll(MinimalObjectCollection.of(e0(), e0(), e0(), e3(), e3(), e3()), list::add));
		expectMissing(e0());
		ObjectHelpers.assertContentsAnyOrder(list, e0());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedEmptyCollection() {
		try {
			assertFalse("removeAll(emptyCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalObjectCollection.of()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemoveAll_unsupportedNonePresent() {
		try {
			assertFalse("removeAll(disjointCollection) should return false or throw UnsupportedOperationException", collection.removeAll(MinimalObjectCollection.of(e3())));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAll_unsupportedPresent() {
		try {
			collection.removeAll(MinimalObjectCollection.of(e0()));
			fail("removeAll(intersectingCollection) should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertTrue(collection.contains(e0()));
	}
}