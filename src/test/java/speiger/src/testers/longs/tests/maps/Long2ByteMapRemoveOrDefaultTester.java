package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import java.util.ConcurrentModificationException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ByteMapRemoveOrDefaultTester extends AbstractLong2ByteMapTester
{
	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_present() {
		int initialSize = getMap().size();
		assertEquals("remove(present) should return the associated value", v0(), getMap().removeOrDefault(k0(), v1()));
		assertEquals("remove(present) should decrease a map's size by one.", initialSize - 1, getMap().size());
		expectMissing(e0());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testRemovePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Long2ByteMap.Entry> iterator = getMap().long2ByteEntrySet().iterator();
			getMap().removeOrDefault(k0(), v0());
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
			LongIterator iterator = getMap().keySet().iterator();
			getMap().removeOrDefault(k0(), v0());
			iterator.nextLong();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_REMOVE })
	@CollectionSize.Require(SEVERAL)
	public void testRemovePresentConcurrentWithValuesIteration() {
		try {
			ByteIterator iterator = getMap().values().iterator();
			getMap().removeOrDefault(k0(), v0());
			iterator.nextByte();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testRemove_notPresent() {
		assertEquals("remove(notPresent) should return "+v3(), v3(), getMap().removeOrDefault(k3(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_unsupported() {
		try {
			getMap().removeOrDefault(k0(), v0());
			fail("remove(present) should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		assertEquals("remove(present) should not remove the element", v0(), get(k0()));
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemove_unsupportedNotPresent() {
		try {
			assertNull("remove(notPresent) should return null or throw UnsupportedOperationException", getMap().removeOrDefault(k3(), v3()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

}