package speiger.src.testers.doubles.tests.maps;

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

import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ByteMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ByteNavigableMapNavigationTester extends AbstractDouble2ByteMapTester
{
	private Double2ByteNavigableMap navigableMap;
	private List<Entry> entries;
	private Entry a;
	private Entry b;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Double2ByteNavigableMap) getMap();
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
	
	public static Comparator<Entry> entryComparator(DoubleComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Double.compare(a.getDoubleKey(), b.getDoubleKey()) : keyComparator.compare(a.getDoubleKey(), b.getDoubleKey());
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Double2ByteNavigableMap) getMap();
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
		assertEquals(Double.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertEquals(Double.MAX_VALUE, navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertEquals(Double.MIN_VALUE, navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Double.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(Double.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getDoubleKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getDoubleKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Double.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.double2ByteEntrySet()));
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
		assertEquals(null, navigableMap.lowerEntry(a.getDoubleKey()));
		assertEquals(Double.MAX_VALUE, navigableMap.lowerKey(a.getDoubleKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getDoubleKey()));
		assertEquals(a.getDoubleKey(), navigableMap.lowerKey(b.getDoubleKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getDoubleKey()));
		assertEquals(a.getDoubleKey(), navigableMap.lowerKey(c.getDoubleKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getDoubleKey()));
		assertEquals(a.getDoubleKey(), navigableMap.floorKey(a.getDoubleKey()));
		assertEquals(a, navigableMap.floorEntry(b.getDoubleKey()));
		assertEquals(a.getDoubleKey(), navigableMap.floorKey(b.getDoubleKey()));
		assertEquals(c, navigableMap.floorEntry(c.getDoubleKey()));
		assertEquals(c.getDoubleKey(), navigableMap.floorKey(c.getDoubleKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getDoubleKey()));
		assertEquals(a.getDoubleKey(), navigableMap.ceilingKey(a.getDoubleKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getDoubleKey()));
		assertEquals(c.getDoubleKey(), navigableMap.ceilingKey(b.getDoubleKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getDoubleKey()));
		assertEquals(c.getDoubleKey(), navigableMap.ceilingKey(c.getDoubleKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getDoubleKey()));
		assertEquals(c.getDoubleKey(), navigableMap.higherKey(a.getDoubleKey()));
		assertEquals(c, navigableMap.higherEntry(b.getDoubleKey()));
		assertEquals(c.getDoubleKey(), navigableMap.higherKey(b.getDoubleKey()));
		assertEquals(null, navigableMap.higherEntry(c.getDoubleKey()));
		assertEquals(Double.MIN_VALUE, navigableMap.higherKey(c.getDoubleKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.double2ByteEntrySet()));
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
		ObjectList<Entry> descending = new ObjectArrayList<>(navigableMap.descendingMap().double2ByteEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getDoubleKey(), false).containsKey(a.getDoubleKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getDoubleKey(), true).containsKey(a.getDoubleKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getDoubleKey(), false).containsKey(a.getDoubleKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getDoubleKey(), true).containsKey(a.getDoubleKey()));
	}
}