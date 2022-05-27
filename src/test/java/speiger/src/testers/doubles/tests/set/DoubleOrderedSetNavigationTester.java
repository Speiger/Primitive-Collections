package speiger.src.testers.doubles.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.testers.doubles.tests.base.AbstractDoubleSetTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleOrderedSetNavigationTester extends AbstractDoubleSetTester
{
	private DoubleOrderedSet orderedSet;
	private DoubleList values;
	private double a;
	private double c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (DoubleOrderedSet) getSet();
		values = DoubleHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getDouble(0);
			if (values.size() >= 3) {
				c = values.getDouble(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		try {
			orderedSet.pollFirstDouble();
			fail("OrderedSet.pollFirstDouble should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		try {
			orderedSet.pollLastDouble();
			fail("OrderedSet.pollLastDouble should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollFirst() {
		try {
			orderedSet.pollFirstDouble();
			fail("OrderedSet.pollFirstDouble should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollLast() {
		try {
			orderedSet.pollLastDouble();
			fail("OrderedSet.pollLastDouble should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, orderedSet.pollFirstDouble());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, orderedSet.pollLastDouble());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, orderedSet.pollFirstDouble());
		assertEquals(values.subList(1, values.size()), DoubleHelpers.copyToList(orderedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, orderedSet.pollLastDouble());
		assertEquals(values.subList(0, values.size()-1), DoubleHelpers.copyToList(orderedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			orderedSet.pollFirstDouble();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			orderedSet.pollLastDouble();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			orderedSet.firstDouble();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			orderedSet.lastDouble();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, orderedSet.firstDouble());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, orderedSet.lastDouble());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, orderedSet.firstDouble());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, orderedSet.lastDouble());
	}
}