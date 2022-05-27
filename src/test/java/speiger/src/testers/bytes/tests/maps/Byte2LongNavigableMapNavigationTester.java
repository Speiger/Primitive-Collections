package speiger.src.testers.bytes.tests.maps;

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

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongNavigableMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2LongNavigableMapNavigationTester extends AbstractByte2LongMapTester
{
	private Byte2LongNavigableMap navigableMap;
	private List<Entry> entries;
	private Entry a;
	private Entry b;
	private Entry c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableMap = (Byte2LongNavigableMap) getMap();
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
	
	public static Comparator<Entry> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return (keyComparator == null) ? Byte.compare(a.getByteKey(), b.getByteKey()) : keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private void resetWithHole() {
		Entry[] entries = new Entry[] { a, c };
		super.resetMap(entries);
		navigableMap = (Byte2LongNavigableMap) getMap();
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
		assertEquals(Byte.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertNull(navigableMap.floorEntry(k0()));
		assertEquals(Byte.MAX_VALUE, navigableMap.floorKey(k0()));
		assertNull(navigableMap.ceilingEntry(k0()));
		assertEquals(Byte.MIN_VALUE, navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Byte.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(Byte.MAX_VALUE, navigableMap.lowerKey(k0()));
		assertEquals(a, navigableMap.floorEntry(k0()));
		assertEquals(a.getByteKey(), navigableMap.floorKey(k0()));
		assertEquals(a, navigableMap.ceilingEntry(k0()));
		assertEquals(a.getByteKey(), navigableMap.ceilingKey(k0()));
		assertNull(navigableMap.higherEntry(k0()));
		assertEquals(Byte.MIN_VALUE, navigableMap.higherKey(k0()));
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
		assertEquals(entries.subList(1, entries.size()), ObjectHelpers.copyToList(navigableMap.byte2LongEntrySet()));
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
		assertEquals(null, navigableMap.lowerEntry(a.getByteKey()));
		assertEquals(Byte.MAX_VALUE, navigableMap.lowerKey(a.getByteKey()));
		assertEquals(a, navigableMap.lowerEntry(b.getByteKey()));
		assertEquals(a.getByteKey(), navigableMap.lowerKey(b.getByteKey()));
		assertEquals(a, navigableMap.lowerEntry(c.getByteKey()));
		assertEquals(a.getByteKey(), navigableMap.lowerKey(c.getByteKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		resetWithHole();
		assertEquals(a, navigableMap.floorEntry(a.getByteKey()));
		assertEquals(a.getByteKey(), navigableMap.floorKey(a.getByteKey()));
		assertEquals(a, navigableMap.floorEntry(b.getByteKey()));
		assertEquals(a.getByteKey(), navigableMap.floorKey(b.getByteKey()));
		assertEquals(c, navigableMap.floorEntry(c.getByteKey()));
		assertEquals(c.getByteKey(), navigableMap.floorKey(c.getByteKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		resetWithHole();
		assertEquals(a, navigableMap.ceilingEntry(a.getByteKey()));
		assertEquals(a.getByteKey(), navigableMap.ceilingKey(a.getByteKey()));
		assertEquals(c, navigableMap.ceilingEntry(b.getByteKey()));
		assertEquals(c.getByteKey(), navigableMap.ceilingKey(b.getByteKey()));
		assertEquals(c, navigableMap.ceilingEntry(c.getByteKey()));
		assertEquals(c.getByteKey(), navigableMap.ceilingKey(c.getByteKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		resetWithHole();
		assertEquals(c, navigableMap.higherEntry(a.getByteKey()));
		assertEquals(c.getByteKey(), navigableMap.higherKey(a.getByteKey()));
		assertEquals(c, navigableMap.higherEntry(b.getByteKey()));
		assertEquals(c.getByteKey(), navigableMap.higherKey(b.getByteKey()));
		assertEquals(null, navigableMap.higherEntry(c.getByteKey()));
		assertEquals(Byte.MIN_VALUE, navigableMap.higherKey(c.getByteKey()));
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, navigableMap.lastEntry());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableMap.pollLastEntry());
		assertEquals(entries.subList(0, entries.size() - 1), ObjectHelpers.copyToList(navigableMap.byte2LongEntrySet()));
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
		ObjectList<Entry> descending = new ObjectArrayList<>(navigableMap.descendingMap().byte2LongEntrySet());
		Collections.reverse(descending);
		assertEquals(entries, descending);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(navigableMap.headMap(a.getByteKey(), false).containsKey(a.getByteKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapInclusive() {
		assertTrue(navigableMap.headMap(a.getByteKey(), true).containsKey(a.getByteKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapExclusive() {
		assertFalse(navigableMap.tailMap(a.getByteKey(), false).containsKey(a.getByteKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(navigableMap.tailMap(a.getByteKey(), true).containsKey(a.getByteKey()));
	}
}