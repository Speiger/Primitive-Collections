package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2FloatMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Short2FloatSortedMapNavigationTester extends AbstractShort2FloatMapTester
{
	private Short2FloatSortedMap sortedMap;
	private Entry a;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Short2FloatSortedMap) getMap();
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static Comparator<Entry> entryComparator(ShortComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Short.compare(a.getShortKey(), b.getShortKey()) : keyComparator.compare(a.getShortKey(), b.getShortKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstShortKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastShortKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getShortKey(), sortedMap.firstShortKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getShortKey(), sortedMap.lastShortKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getShortKey(), sortedMap.firstShortKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getShortKey(), sortedMap.lastShortKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getShortKey()).containsKey(a.getShortKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getShortKey()).containsKey(a.getShortKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getShortKey(), entries.get(j).getShortKey()).short2FloatEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getShortKey(), a.getShortKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		ShortComparator comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = Short::compare;
		}
		Iterator<Entry> entryItr = sortedMap.short2FloatEntrySet().iterator();
		Entry prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getShortKey(), nextEntry.getShortKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}