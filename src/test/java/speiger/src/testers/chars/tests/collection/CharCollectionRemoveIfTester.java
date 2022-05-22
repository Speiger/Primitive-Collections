package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
public class CharCollectionRemoveIfTester extends AbstractCharCollectionTester {
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testRemoveIf_alwaysFalse() {
		assertFalse("remoIf(x -> false) should return false", collection.remIf(x -> false));
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_sometimesTrue() {
		assertTrue("remIf(isEqual(present)) should return true",
				collection.remIf(T -> T == e0()));
		expectMissing(samples.e0());
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_allPresent() {
		assertTrue("remIf(x -> true) should return true", collection.remIf(x -> true));
		expectContents();
	}

	@CollectionFeature.Require({ SUPPORTS_ITERATOR_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testRemoveIfSomeMatchesConcurrentWithIteration() {
		try {
			CharIterator iterator = collection.iterator();
			assertTrue(collection.remIf(T -> T == e0()));
			iterator.nextChar();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRemoveIf_unsupportedEmptyCollection() {
		try {
			assertFalse("remIf(Predicate) should return false or throw UnsupportedOperationException",
					collection.remIf(x -> {throw new AssertionError("predicate should never be called");}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_alwaysTrueUnsupported() {
		try {
			collection.remIf(x -> true);
			fail("remIf(x -> true) should throw " + "UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertTrue(collection.contains(samples.e0()));
	}
}