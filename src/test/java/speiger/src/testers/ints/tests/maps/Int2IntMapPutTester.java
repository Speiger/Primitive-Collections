package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2IntMapPutTester extends AbstractInt2IntMapTester
{	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_supportedPresent() {
		assertEquals("put(present, value) should return the old value", v0(), getMap().put(k0(), v3()));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPut_supportedNotPresent() {
		assertEquals("put(notPresent, value) should return -1", -1, put(e3()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithEntrySetIteration() {
		try {
			Iterator<Int2IntMap.Entry> iterator = getMap().int2IntEntrySet().iterator();
			put(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithKeySetIteration() {
		try {
			IntIterator iterator = getMap().keySet().iterator();
			put(e3());
			iterator.nextInt();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithValueIteration() {
		try {
			IntIterator iterator = getMap().values().iterator();
			put(e3());
			iterator.nextInt();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPut_unsupportedNotPresent() {
		try {
			put(e3());
			fail("put(notPresent, value) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_unsupportedPresentExistingValue() {
		try {
			assertEquals("put(present, existingValue) should return present or throw", v0(), put(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_unsupportedPresentDifferentValue() {
		try {
			getMap().put(k0(), v3());
			fail("put(present, differentValue) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	private int put(Int2IntMap.Entry entry) {
		return getMap().put(entry.getIntKey(), entry.getIntValue());
	}
}