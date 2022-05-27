package speiger.src.testers.objects.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
public class ObjectCollectionClearTester<T> extends AbstractObjectCollectionTester<T>
{
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
			ObjectIterator<T> iterator = collection.iterator();
			collection.clear();
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
}