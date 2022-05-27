package speiger.src.testers.shorts.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
@SuppressWarnings("javadoc")
public class ShortCollectionAddAllTester extends AbstractShortCollectionTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_supportedNothing() {
		assertFalse("addAll(nothing) should return false", collection.addAll(emptyCollection()));
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testAddAll_unsupportedNothing() {
		try {
			assertFalse("addAll(nothing) should return false or throw", collection.addAll(emptyCollection()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAll_supportedNonePresent() {
		assertTrue("addAll(nonePresent) should return true", collection.addAll(createDisjointCollection()));
		expectAdded(e3(), e4());
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testAddAll_unsupportedNonePresent() {
		try {
			collection.addAll(createDisjointCollection());
			fail("addAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}

	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_supportedSomePresent() {
		assertTrue("addAll(somePresent) should return true", collection.addAll(MinimalShortCollection.of(e3(), e0())));
		assertTrue("should contain " + e3(), collection.contains(e3()));
		assertTrue("should contain " + e0(), collection.contains(e0()));
	}
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedSomePresent() {
		try {
			collection.addAll(MinimalShortCollection.of(e3(), e0()));
			fail("addAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require({ SUPPORTS_ADD, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllConcurrentWithIteration() {
		try {
			ShortIterator iterator = collection.iterator();
			assertTrue(collection.addAll(MinimalShortCollection.of(e3(), e0())));
			iterator.nextShort();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAll_unsupportedAllPresent() {
		try {
			assertFalse("addAll(allPresent) should return false or throw", collection.addAll(MinimalShortCollection.of(e0())));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}