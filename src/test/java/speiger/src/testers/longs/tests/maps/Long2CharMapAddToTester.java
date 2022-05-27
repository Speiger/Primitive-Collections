package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;


import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2CharMapAddToTester extends AbstractLong2CharMapTester
{	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_supportedPresent() {
		assertEquals("addTo(present, value) should return the old value", v0(), getMap().addTo(k0(), v3()));
		expectReplacement(entry(k0(), (char)(v0()+v3())));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testAddTo_supportedNotPresent() {
		assertEquals("addTo(notPresent, value) should return (char)-1", (char)-1, addTo(e3()));
		expectAdded(e3());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testAddToSum_supportedNotPresent() {
		assertEquals("addTo(notPresent, value) should return (char)-1", (char)-1, addTo(e3()));
		assertEquals("addTo(notPresent, value) should return "+v3(), v3(), addTo(e3()));
		expectAdded(entry(k3(), (char)(v3()+v3())));
	}


	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithEntrySetIteration() {
		try {
			Iterator<Long2CharMap.Entry> iterator = getMap().long2CharEntrySet().iterator();
			addTo(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithKeySetIteration() {
		try {
			LongIterator iterator = getMap().keySet().iterator();
			addTo(e3());
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithValueIteration() {
		try {
			CharIterator iterator = getMap().values().iterator();
			addTo(e3());
			iterator.nextChar();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testAddTo_unsupportedNotPresent() {
		try {
			addTo(e3());
			fail("addTo(notPresent, value) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_unsupportedPresentExistingValue() {
		try {
			assertEquals("addTo(present, existingValue) should return present or throw", v0(), addTo(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_unsupportedPresentDifferentValue() {
		try {
			getMap().addTo(k0(), v3());
			fail("addTo(present, differentValue) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	private char addTo(Long2CharMap.Entry entry) {
		return getMap().addTo(entry.getLongKey(), entry.getCharValue());
	}
}