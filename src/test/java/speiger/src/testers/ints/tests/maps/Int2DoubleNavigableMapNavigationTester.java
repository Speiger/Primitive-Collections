package speiger.src.testers.ints.tests.maps;

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

import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2DoubleMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Int2DoubleNavigableMapNavigationTester extends AbstractInt2DoubleMapTester
{
	private Int2DoubleNavigableMap navigableMap;
	private List<Entry> entries;
	private Entry a;
	private Entry b;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Int2DoubleNavigableMap) getMap();
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
	
	public static Comparator<Entry> entryComparator(IntComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Integer.compare(a.getIntKey(), b.getIntKey()) : keyComparator.compare(a.getIntKey(), b.getIntKey());
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Int2DoubleNavigableMap) getMap();
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
		assertEquals(Integer.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertEquals(Integer.MAX_VALUE, navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertEquals(Integer.MIN_VALUE, navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Integer.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(Integer.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getIntKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getIntKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Integer.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.int2DoubleEntrySet()));
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
		assertEquals(null, navigableMap.lowerEntry(a.getIntKey()));
		assertEquals(Integer.MAX_VALUE, navigableMap.lowerKey(a.getIntKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getIntKey()));
		assertEquals(a.getIntKey(), navigableMap.lowerKey(b.getIntKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getIntKey()));
		assertEquals(a.getIntKey(), navigableMap.lowerKey(c.getIntKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getIntKey()));
		assertEquals(a.getIntKey(), navigableMap.floorKey(a.getIntKey()));
		assertEquals(a, navigableMap.floorEntry(b.getIntKey()));
		assertEquals(a.getIntKey(), navigableMap.floorKey(b.getIntKey()));
		assertEquals(c, navigableMap.floorEntry(c.getIntKey()));
		assertEquals(c.getIntKey(), navigableMap.floorKey(c.getIntKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getIntKey()));
		assertEquals(a.getIntKey(), navigableMap.ceilingKey(a.getIntKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getIntKey()));
		assertEquals(c.getIntKey(), navigableMap.ceilingKey(b.getIntKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getIntKey()));
		assertEquals(c.getIntKey(), navigableMap.ceilingKey(c.getIntKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getIntKey()));
		assertEquals(c.getIntKey(), navigableMap.higherKey(a.getIntKey()));
		assertEquals(c, navigableMap.higherEntry(b.getIntKey()));
		assertEquals(c.getIntKey(), navigableMap.higherKey(b.getIntKey()));
		assertEquals(null, navigableMap.higherEntry(c.getIntKey()));
		assertEquals(Integer.MIN_VALUE, navigableMap.higherKey(c.getIntKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.int2DoubleEntrySet()));
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
		ObjectList<Entry> descending = new ObjectArrayList<>(navigableMap.descendingMap().int2DoubleEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getIntKey(), false).containsKey(a.getIntKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getIntKey(), true).containsKey(a.getIntKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getIntKey(), false).containsKey(a.getIntKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getIntKey(), true).containsKey(a.getIntKey()));
	}
}