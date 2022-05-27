package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import java.util.ConcurrentModificationException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap.Entry;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;

@Ignore
public class Char2IntMapClearTester extends AbstractChar2IntMapTester 
{
	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testClear() {
		getMap().clear();
		assertTrue("After clear(), a map should be empty.", getMap().isEmpty());
		assertEquals(0, getMap().size());
		assertFalse(getMap().char2IntEntrySet().iterator().hasNext());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Entry> iterator = getMap().char2IntEntrySet().iterator();
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
			CharIterator iterator = getMap().keySet().iterator();
			getMap().clear();
			iterator.nextChar();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testClearConcurrentWithValuesIteration() {
		try {
			IntIterator iterator = getMap().values().iterator();
			getMap().clear();
			iterator.nextInt();
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