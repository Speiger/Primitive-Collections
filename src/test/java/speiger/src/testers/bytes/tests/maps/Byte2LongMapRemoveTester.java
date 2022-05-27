package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import java.util.ConcurrentModificationException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2LongMapRemoveTester extends AbstractByte2LongMapTester
{
	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_present() {
		int initialSize = getMap().size();
		assertEquals("remove(present) should return the associated value", v0(), getMap().remove(k0()));
		assertEquals("remove(present) should decrease a map's size by one.", initialSize - 1, getMap().size());
		expectMissing(e0());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testRemovePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Byte2LongMap.Entry> iterator = getMap().byte2LongEntrySet().iterator();
			getMap().remove(k0());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testRemovePresentConcurrentWithKeySetIteration() {
		try {
			ByteIterator iterator = getMap().keySet().iterator();
			getMap().remove(k0());
			iterator.nextByte();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testRemovePresentConcurrentWithValuesIteration() {
		try {
			LongIterator iterator = getMap().values().iterator();
			getMap().remove(k0());
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testRemove_notPresent() {
		assertEquals("remove(notPresent) should return -1L", -1L, getMap().remove(k3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_unsupported() {
		try {
			getMap().remove(k0());
			fail("remove(present) should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertEquals("remove(present) should not remove the element", v0(), get(k0()));
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemove_unsupportedNotPresent() {
		try {
			assertEquals("remove(notPresent) should return -1L or throw UnsupportedOperationException", -1L, getMap().remove(k3()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
		expectMissing(e3());
	}
}