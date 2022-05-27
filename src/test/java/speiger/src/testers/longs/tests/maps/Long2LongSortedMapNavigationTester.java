package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2LongMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Long2LongSortedMapNavigationTester extends AbstractLong2LongMapTester
{
	private Long2LongSortedMap sortedMap;
	private Entry a;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Long2LongSortedMap) getMap();
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static Comparator<Entry> entryComparator(LongComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Long.compare(a.getLongKey(), b.getLongKey()) : keyComparator.compare(a.getLongKey(), b.getLongKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstLongKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastLongKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getLongKey(), sortedMap.firstLongKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getLongKey(), sortedMap.lastLongKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getLongKey(), sortedMap.firstLongKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getLongKey(), sortedMap.lastLongKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getLongKey()).containsKey(a.getLongKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getLongKey()).containsKey(a.getLongKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getLongKey(), entries.get(j).getLongKey()).long2LongEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getLongKey(), a.getLongKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		LongComparator comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = Long::compare;
		}
		Iterator<Entry> entryItr = sortedMap.long2LongEntrySet().iterator();
		Entry prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getLongKey(), nextEntry.getLongKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}