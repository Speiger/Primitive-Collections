package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import java.util.ConcurrentModificationException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap.Entry;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2CharMapClearTester<T> extends AbstractObject2CharMapTester<T> 
{
	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testClear() {
		getMap().clear();
		assertTrue("After clear(), a map should be empty.", getMap().isEmpty());
		assertEquals(0, getMap().size());
		assertFalse(getMap().object2CharEntrySet().iterator().hasNext());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Entry<T>> iterator = getMap().object2CharEntrySet().iterator();
			getMap().clear();
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithKeySetIteration() {
		try {
			ObjectIterator<T> iterator = getMap().keySet().iterator();
			getMap().clear();
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithValuesIteration() {
		try {
			CharIterator iterator = getMap().values().iterator();
			getMap().clear();
			iterator.nextChar();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testClear_unsupported() {
		try {
			getMap().clear();
			fail("clear() should throw UnsupportedOperation if a map does not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testClear_unsupportedByEmptyCollection() {
		try {
			getMap().clear();
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}