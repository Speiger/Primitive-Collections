package speiger.src.testers.longs.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.TreeSet;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.utils.LongLists;
import speiger.src.testers.longs.tests.base.AbstractLongSetTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongNavigableSetNavigationTester extends AbstractLongSetTester
{
	private LongNavigableSet navigableSet;
	private LongList values;
	private long a;
	private long b;
	private long c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		navigableSet = (LongNavigableSet) getSet();
		values = LongHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(navigableSet.comparator());
		if (values.size() >= 1) {
			a = values.getLong(0);
			if (values.size() >= 3) {
				b = values.getLong(1);
				c = values.getLong(2);
			}
		}
	}
	
	protected void resetWithHole() {
		super.resetContainer(primitiveGenerator.create(createArray(a, c)));
		navigableSet = (LongNavigableSet) getSet();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.pollFirstLong());
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetNearby() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(e0()));
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.floor(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.ceiling(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.pollLastLong());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, navigableSet.pollFirstLong());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetNearby() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(e0()));
		assertEquals(a, navigableSet.floor(e0()));
		assertEquals(a, navigableSet.ceiling(e0()));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(e0()));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, navigableSet.pollLastLong());
		assertTrue(navigableSet.isEmpty());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, navigableSet.pollFirstLong());
		assertEquals(values.subList(1, values.size()), LongHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			navigableSet.pollFirstLong();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testLowerHole() {
		resetWithHole();
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(a));
		assertEquals(a, navigableSet.lower(b));
		assertEquals(a, navigableSet.lower(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloorHole() {
		resetWithHole();
		assertEquals(a, navigableSet.floor(a));
		assertEquals(a, navigableSet.floor(b));
		assertEquals(c, navigableSet.floor(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeilingHole() {
		resetWithHole();
		assertEquals(a, navigableSet.ceiling(a));
		assertEquals(c, navigableSet.ceiling(b));
		assertEquals(c, navigableSet.ceiling(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigherHole() {
		resetWithHole();
		assertEquals(c, navigableSet.higher(a));
		assertEquals(c, navigableSet.higher(b));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(c));
	}

	/*
	 * TODO(cpovirk): make "too small" and "too large" elements available for
	 * better navigation testing. At that point, we may be able to eliminate the
	 * "hole" tests, which would mean that ContiguousSet's tests would no longer
	 * need to suppress them.
	 */
	@CollectionSize.Require(SEVERAL)
	public void testLower() {
		assertEquals(navigableSet.getDefaultMinValue(), navigableSet.lower(a));
		assertEquals(a, navigableSet.lower(b));
		assertEquals(b, navigableSet.lower(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testFloor() {
		assertEquals(a, navigableSet.floor(a));
		assertEquals(b, navigableSet.floor(b));
		assertEquals(c, navigableSet.floor(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testCeiling() {
		assertEquals(a, navigableSet.ceiling(a));
		assertEquals(b, navigableSet.ceiling(b));
		assertEquals(c, navigableSet.ceiling(c));
	}

	@CollectionSize.Require(SEVERAL)
	public void testHigher() {
		assertEquals(b, navigableSet.higher(a));
		assertEquals(c, navigableSet.higher(b));
		assertEquals(navigableSet.getDefaultMaxValue(), navigableSet.higher(c));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, navigableSet.pollLastLong());
		assertEquals(values.subList(0, values.size() - 1), LongHelpers.copyToList(navigableSet));
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			navigableSet.pollLastLong();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testDescendingNavigation() {
		LongList descending = new LongArrayList();
		for (LongIterator i = navigableSet.descendingIterator(); i.hasNext();) {
			descending.add(i.nextLong());
		}
		LongLists.reverse(descending);
		assertEquals(values, descending);
	}

	public void testEmptySubSet() {
		LongNavigableSet empty = navigableSet.subSet(e0(), false, e0(), false);
		assertEquals(new TreeSet<>(), empty);
	}
}