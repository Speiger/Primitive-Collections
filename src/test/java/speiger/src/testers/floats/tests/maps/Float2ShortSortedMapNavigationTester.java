package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ShortSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortSortedMapNavigationTester extends AbstractFloat2ShortMapTester
{
	private Float2ShortSortedMap sortedMap;
	private Entry a;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Float2ShortSortedMap) getMap();
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static Comparator<Entry> entryComparator(FloatComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Float.compare(a.getFloatKey(), b.getFloatKey()) : keyComparator.compare(a.getFloatKey(), b.getFloatKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstFloatKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastFloatKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getFloatKey(), sortedMap.firstFloatKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getFloatKey(), sortedMap.lastFloatKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getFloatKey(), sortedMap.firstFloatKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getFloatKey(), sortedMap.lastFloatKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getFloatKey()).containsKey(a.getFloatKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getFloatKey()).containsKey(a.getFloatKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getFloatKey(), entries.get(j).getFloatKey()).float2ShortEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getFloatKey(), a.getFloatKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		FloatComparator comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = Float::compare;
		}
		Iterator<Entry> entryItr = sortedMap.float2ShortEntrySet().iterator();
		Entry prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getFloatKey(), nextEntry.getFloatKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}