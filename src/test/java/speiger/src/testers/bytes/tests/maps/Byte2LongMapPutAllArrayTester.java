package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.bytes.maps.impl.hash.Byte2LongLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public class Byte2LongMapPutAllArrayTester extends AbstractByte2LongMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_supportedNothing() {
		getMap().putAll(emptyKeyArray(), emptyValueArray());
		expectUnchanged();
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_supportedNothing() {
		getMap().putAll(emptyKeyObjectArray(), emptyValueObjectArray());
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllArray_unsupportedNothing() {
		try {
			getMap().putAll(emptyKeyArray(), emptyValueArray());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllObjectArray_unsupportedNothing() {
		try {
			getMap().putAll(emptyKeyObjectArray(), emptyValueObjectArray());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_supportedNonePresent() {
		putAll(createDisjointCollection());
		expectAdded(e3(), e4());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_supportedNonePresent() {
		putAllObjects(createDisjointCollection());
		expectAdded(e3(), e4());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllArray_unsupportedNonePresent() {
		try {
			putAll(createDisjointCollection());
			fail("putAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllObjectArray_unsupportedNonePresent() {
		try {
			putAllObjects(createDisjointCollection());
			fail("putAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_supportedSomePresent() {
		putAll(MinimalObjectCollection.of(e3(), e0()));
		expectAdded(e3());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_supportedSomePresent() {
		putAllObjects(MinimalObjectCollection.of(e3(), e0()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArraySomePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Byte2LongMap.Entry> iterator = getMap().byte2LongEntrySet().iterator();
			putAll(MinimalObjectCollection.of(e3(), e0()));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
	
	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArraySomePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Byte2LongMap.Entry> iterator = getMap().byte2LongEntrySet().iterator();
			putAllObjects(MinimalObjectCollection.of(e3(), e0()));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_unsupportedSomePresent() {
		try {
			putAll(MinimalObjectCollection.of(e3(), e0()));
			fail("putAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_unsupportedSomePresent() {
		try {
			putAllObjects(MinimalObjectCollection.of(e3(), e0()));
			fail("putAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_unsupportedAllPresent() {
		try {
			putAll(MinimalObjectCollection.of(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_unsupportedAllPresent() {
		try {
			putAllObjects(MinimalObjectCollection.of(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_nullCollectionReference() {
		try {
			getMap().putAll((byte[])null, (long[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_nullCollectionReference() {
		try {
			getMap().putAll((Byte[])null, (Long[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	private void putAll(ObjectIterable<Byte2LongMap.Entry> entries) {
		Byte2LongMap map = new Byte2LongLinkedOpenHashMap();
		for (Byte2LongMap.Entry entry : entries) {
			map.put(entry.getByteKey(), entry.getLongValue());
		}
		getMap().putAll(map.keySet().toByteArray(new byte[map.size()]), map.values().toLongArray(new long[map.size()]));
	}
	
	private void putAllObjects(ObjectIterable<Byte2LongMap.Entry> entries) {
		Map<Byte, Long> map = new Byte2LongLinkedOpenHashMap();
		for (Byte2LongMap.Entry entry : entries) {
			map.put(entry.getByteKey(), entry.getLongValue());
		}
		getMap().putAll(map.keySet().toArray(new Byte[map.size()]), map.values().toArray(new Long[map.size()]));
	}
}