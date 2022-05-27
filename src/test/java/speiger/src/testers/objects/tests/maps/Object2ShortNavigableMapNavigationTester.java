package speiger.src.testers.objects.tests.maps;

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

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ShortNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Object2ShortNavigableMapNavigationTester<T> extends AbstractObject2ShortMapTester<T>
{
	private Object2ShortNavigableMap<T> navigableMap;
	private List<Entry<T>> entries;
	private Entry<T> a;
	private Entry<T> b;
	private Entry<T> c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Object2ShortNavigableMap<T>) getMap();
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
	
	public static <T> Comparator<Entry<T>> entryComparator(Comparator<T> keyComparator) {
		return new Comparator<Entry<T>>() {
			@Override
			public int compare(Entry<T> a, Entry<T> b) {
				return (keyComparator == null) ? ((Comparable<T>)a.getKey()).compareTo((T)b.getKey()) : keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry<T>[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Object2ShortNavigableMap<T>) getMap();
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
		assertNull(navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertNull(navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertNull(navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertNull(navigableMap.higherKey(k0()));
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
		assertNull(navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertNull(navigableMap.higherKey(k0()));
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
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.object2ShortEntrySet()));
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
		assertEquals(null, navigableMap.lowerEntry(a.getKey()));
		assertNull(navigableMap.lowerKey(a.getKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getKey()));
		assertEquals(a.getKey(), navigableMap.lowerKey(b.getKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getKey()));
		assertEquals(a.getKey(), navigableMap.lowerKey(c.getKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getKey()));
		assertEquals(a.getKey(), navigableMap.floorKey(a.getKey()));
		assertEquals(a, navigableMap.floorEntry(b.getKey()));
		assertEquals(a.getKey(), navigableMap.floorKey(b.getKey()));
		assertEquals(c, navigableMap.floorEntry(c.getKey()));
		assertEquals(c.getKey(), navigableMap.floorKey(c.getKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getKey()));
		assertEquals(a.getKey(), navigableMap.ceilingKey(a.getKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getKey()));
		assertEquals(c.getKey(), navigableMap.ceilingKey(b.getKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getKey()));
		assertEquals(c.getKey(), navigableMap.ceilingKey(c.getKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getKey()));
		assertEquals(c.getKey(), navigableMap.higherKey(a.getKey()));
		assertEquals(c, navigableMap.higherEntry(b.getKey()));
		assertEquals(c.getKey(), navigableMap.higherKey(b.getKey()));
		assertEquals(null, navigableMap.higherEntry(c.getKey()));
		assertNull(navigableMap.higherKey(c.getKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.object2ShortEntrySet()));
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
		ObjectList<Entry<T>> descending = new ObjectArrayList<>(navigableMap.descendingMap().object2ShortEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getKey(), false).containsKey(a.getKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getKey(), true).containsKey(a.getKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getKey(), false).containsKey(a.getKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getKey(), true).containsKey(a.getKey()));
	}
}