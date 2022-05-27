package speiger.src.testers.floats.tests.maps;

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

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ShortNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortNavigableMapNavigationTester extends AbstractFloat2ShortMapTester
{
	private Float2ShortNavigableMap navigableMap;
	private List<Entry> entries;
	private Entry a;
	private Entry b;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Float2ShortNavigableMap) getMap();
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
	
	public static Comparator<Entry> entryComparator(FloatComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Float.compare(a.getFloatKey(), b.getFloatKey()) : keyComparator.compare(a.getFloatKey(), b.getFloatKey());
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Float2ShortNavigableMap) getMap();
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
		assertEquals(Float.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertEquals(Float.MAX_VALUE, navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertEquals(Float.MIN_VALUE, navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Float.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(Float.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getFloatKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getFloatKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Float.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.float2ShortEntrySet()));
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
		assertEquals(null, navigableMap.lowerEntry(a.getFloatKey()));
		assertEquals(Float.MAX_VALUE, navigableMap.lowerKey(a.getFloatKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getFloatKey()));
		assertEquals(a.getFloatKey(), navigableMap.lowerKey(b.getFloatKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getFloatKey()));
		assertEquals(a.getFloatKey(), navigableMap.lowerKey(c.getFloatKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getFloatKey()));
		assertEquals(a.getFloatKey(), navigableMap.floorKey(a.getFloatKey()));
		assertEquals(a, navigableMap.floorEntry(b.getFloatKey()));
		assertEquals(a.getFloatKey(), navigableMap.floorKey(b.getFloatKey()));
		assertEquals(c, navigableMap.floorEntry(c.getFloatKey()));
		assertEquals(c.getFloatKey(), navigableMap.floorKey(c.getFloatKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getFloatKey()));
		assertEquals(a.getFloatKey(), navigableMap.ceilingKey(a.getFloatKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getFloatKey()));
		assertEquals(c.getFloatKey(), navigableMap.ceilingKey(b.getFloatKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getFloatKey()));
		assertEquals(c.getFloatKey(), navigableMap.ceilingKey(c.getFloatKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getFloatKey()));
		assertEquals(c.getFloatKey(), navigableMap.higherKey(a.getFloatKey()));
		assertEquals(c, navigableMap.higherEntry(b.getFloatKey()));
		assertEquals(c.getFloatKey(), navigableMap.higherKey(b.getFloatKey()));
		assertEquals(null, navigableMap.higherEntry(c.getFloatKey()));
		assertEquals(Float.MIN_VALUE, navigableMap.higherKey(c.getFloatKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.float2ShortEntrySet()));
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
		ObjectList<Entry> descending = new ObjectArrayList<>(navigableMap.descendingMap().float2ShortEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getFloatKey(), false).containsKey(a.getFloatKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getFloatKey(), true).containsKey(a.getFloatKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getFloatKey(), false).containsKey(a.getFloatKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getFloatKey(), true).containsKey(a.getFloatKey()));
	}
}