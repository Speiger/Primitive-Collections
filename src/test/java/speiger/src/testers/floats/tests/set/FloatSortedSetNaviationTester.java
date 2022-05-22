package speiger.src.testers.floats.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.testers.floats.tests.base.AbstractFloatSetTester;
import speiger.src.testers.floats.utils.FloatHelpers;

@Ignore
public class FloatSortedSetNaviationTester extends AbstractFloatSetTester {
	private FloatSortedSet sortedSet;
	private FloatList values;
	private float a;
	private float c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedSet = (FloatSortedSet) getSet();
		values = FloatHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(sortedSet.comparator());

		// some tests assume SEVERAL == 3
		if (values.size() >= 1) {
			a = values.getFloat(0);
			if (values.size() >= 3) {
				c = values.getFloat(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(Float.MAX_VALUE, sortedSet.pollFirstFloat());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(Float.MIN_VALUE, sortedSet.pollLastFloat());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, sortedSet.pollFirstFloat());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, sortedSet.pollLastFloat());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, sortedSet.pollFirstFloat());
		assertEquals(values.subList(1, values.size()), FloatHelpers.copyToList(sortedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, sortedSet.pollLastFloat());
		assertEquals(values.subList(0, values.size()-1), FloatHelpers.copyToList(sortedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			sortedSet.pollFirstFloat();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			sortedSet.pollLastFloat();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			sortedSet.firstFloat();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			sortedSet.lastFloat();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, sortedSet.firstFloat());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, sortedSet.lastFloat());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, sortedSet.firstFloat());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, sortedSet.lastFloat());
	}
}