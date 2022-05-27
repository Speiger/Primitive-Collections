package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Byte2ObjectSortedMapNavigationTester<V> extends AbstractByte2ObjectMapTester<V>
{
	private Byte2ObjectSortedMap<V> sortedMap;
	private Entry<V> a;
	private Entry<V> c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Byte2ObjectSortedMap<V>) getMap();
		ObjectList<Entry<V>> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static <V> Comparator<Entry<V>> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				return (keyComparator == null) ? Byte.compare(a.getByteKey(), b.getByteKey()) : keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstByteKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastByteKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getByteKey(), sortedMap.firstByteKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getByteKey(), sortedMap.lastByteKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getByteKey(), sortedMap.firstByteKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getByteKey(), sortedMap.lastByteKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getByteKey()).containsKey(a.getByteKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getByteKey()).containsKey(a.getByteKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry<V>> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getByteKey(), entries.get(j).getByteKey()).byte2ObjectEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getByteKey(), a.getByteKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		ByteComparator comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = Byte::compare;
		}
		Iterator<Entry<V>> entryItr = sortedMap.byte2ObjectEntrySet().iterator();
		Entry<V> prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry<V> nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getByteKey(), nextEntry.getByteKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}