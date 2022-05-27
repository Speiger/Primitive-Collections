package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2FloatNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2FloatMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Char2FloatNavigableMapNavigationTester extends AbstractChar2FloatMapTester
{
	private Char2FloatNavigableMap navigableMap;
	private List<Entry> entries;
	private Entry a;
	private Entry b;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Char2FloatNavigableMap) getMap();
		entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(navigableMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				b = entries.get(1);
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
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Char2FloatNavigableMap) getMap();
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		assertNull(navigableMap.firstEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptyMapPollFirst() {
		assertNull(navigableMap.pollFirstEntry());
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapNearby() {
		assertNull(navigableMap.lowerEntry(k0()));
		assertEquals(Character.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertEquals(Character.MAX_VALUE, navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertEquals(Character.MIN_VALUE, navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Character.MIN_VALUE, navigableMap.higherKey(k0()));
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		assertNull(navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptyMapPollLast() {
		assertNull(navigableMap.pollLastEntry());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a, navigableMap.firstEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonMapPollFirst() {
		assertEquals(a, navigableMap.pollFirstEntry());
		assertTrue(navigableMap.isEmpty());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapNearby() {
		assertNull(navigableMap.lowerEntry(k0()));
		assertEquals(Character.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getCharKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getCharKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Character.MIN_VALUE, navigableMap.higherKey(k0()));
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonMapPollLast() {
		assertEquals(a, navigableMap.pollLastEntry());
		assertTrue(navigableMap.isEmpty());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, navigableMap.firstEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, navigableMap.pollFirstEntry());
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.char2FloatEntrySet()));
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			navigableMap.pollFirstEntry();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testLower() {
		resetWithHole();
		assertEquals(null, navigableMap.lowerEntry(a.getCharKey()));
		assertEquals(Character.MAX_VALUE, navigableMap.lowerKey(a.getCharKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getCharKey()));
		assertEquals(a.getCharKey(), navigableMap.lowerKey(b.getCharKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getCharKey()));
		assertEquals(a.getCharKey(), navigableMap.lowerKey(c.getCharKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getCharKey()));
		assertEquals(a.getCharKey(), navigableMap.floorKey(a.getCharKey()));
		assertEquals(a, navigableMap.floorEntry(b.getCharKey()));
		assertEquals(a.getCharKey(), navigableMap.floorKey(b.getCharKey()));
		assertEquals(c, navigableMap.floorEntry(c.getCharKey()));
		assertEquals(c.getCharKey(), navigableMap.floorKey(c.getCharKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getCharKey()));
		assertEquals(a.getCharKey(), navigableMap.ceilingKey(a.getCharKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getCharKey()));
		assertEquals(c.getCharKey(), navigableMap.ceilingKey(b.getCharKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getCharKey()));
		assertEquals(c.getCharKey(), navigableMap.ceilingKey(c.getCharKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getCharKey()));
		assertEquals(c.getCharKey(), navigableMap.higherKey(a.getCharKey()));
		assertEquals(c, navigableMap.higherEntry(b.getCharKey()));
		assertEquals(c.getCharKey(), navigableMap.higherKey(b.getCharKey()));
		assertEquals(null, navigableMap.higherEntry(c.getCharKey()));
		assertEquals(Character.MIN_VALUE, navigableMap.higherKey(c.getCharKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.char2FloatEntrySet()));
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLastUnsupported() {
		try {
			navigableMap.pollLastEntry();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testDescendingNavigation() {
		ObjectList<Entry> descending = new ObjectArrayList<>(navigableMap.descendingMap().char2FloatEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getCharKey(), false).containsKey(a.getCharKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getCharKey(), true).containsKey(a.getCharKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getCharKey(), false).containsKey(a.getCharKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getCharKey(), true).containsKey(a.getCharKey()));
	}
}