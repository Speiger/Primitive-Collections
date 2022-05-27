package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Char2IntSortedMapNavigationTester extends AbstractChar2IntMapTester
{
	private Char2IntSortedMap sortedMap;
	private Entry a;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Char2IntSortedMap) getMap();
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static Comparator<Entry> entryComparator(CharComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Character.compare(a.getCharKey(), b.getCharKey()) : keyComparator.compare(a.getCharKey(), b.getCharKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstCharKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastCharKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getCharKey(), sortedMap.firstCharKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getCharKey(), sortedMap.lastCharKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getCharKey(), sortedMap.firstCharKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getCharKey(), sortedMap.lastCharKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getCharKey()).containsKey(a.getCharKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getCharKey()).containsKey(a.getCharKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getCharKey(), entries.get(j).getCharKey()).char2IntEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getCharKey(), a.getCharKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		CharComparator comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = Character::compare;
		}
		Iterator<Entry> entryItr = sortedMap.char2IntEntrySet().iterator();
		Entry prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getCharKey(), nextEntry.getCharKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}