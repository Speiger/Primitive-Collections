package speiger.src.testers.objects.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;
import java.util.Objects;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
public class ObjectCollectionRemoveIfTester<T> extends AbstractObjectCollectionTester<T>
{
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testRemoveIf_alwaysFalse() {
		assertFalse("remoIf(x -> false) should return false", collection.removeIf(x -> false));
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_sometimesTrue() {
		assertTrue("remIf(isEqual(present)) should return true",
		collection.removeIf(T -> Objects.equals(T, e0())));
		expectMissing(samples.e0());
	}

	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_allPresent() {
		assertTrue("remIf(x -> true) should return true", collection.removeIf(x -> true));
		expectContents();
	}

	@CollectionFeature.Require({ SUPPORTS_ITERATOR_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION })
	@CollectionSize.Require(SEVERAL)
	public void testRemoveIfSomeMatchesConcurrentWithIteration() {
		try {
			ObjectIterator<T> iterator = collection.iterator();
			assertTrue(collection.removeIf(T -> Objects.equals(T, e0())));
			iterator.next();
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
			collection.removeIf(x -> {throw new AssertionError("predicate should never be called");}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveIf_alwaysTrueUnsupported() {
		try {
			collection.removeIf(x -> true);
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertTrue(collection.contains(samples.e0()));
	}
}