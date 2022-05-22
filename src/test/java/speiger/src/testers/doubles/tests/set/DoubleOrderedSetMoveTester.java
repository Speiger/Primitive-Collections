package speiger.src.testers.doubles.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.testers.doubles.tests.base.AbstractDoubleSetTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
public class DoubleOrderedSetMoveTester extends AbstractDoubleSetTester {
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
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstDouble());
		assertEquals(true, orderedSet.addAndMoveToFirst(e4()));
		assertNotEquals(a, orderedSet.firstDouble());
		assertEquals(e4(), orderedSet.firstDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstDouble());
		assertEquals(false, orderedSet.addAndMoveToFirst(c));
		assertNotEquals(a, orderedSet.firstDouble());
		assertEquals(c, orderedSet.firstDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastDouble());
		assertEquals(true, orderedSet.addAndMoveToLast(e4()));
		assertNotEquals(c, orderedSet.lastDouble());
		assertEquals(e4(), orderedSet.lastDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastDouble());
		assertEquals(false, orderedSet.addAndMoveToLast(a));
		assertNotEquals(c, orderedSet.lastDouble());
		assertEquals(a, orderedSet.lastDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstDouble());
		assertEquals(false, orderedSet.moveToFirst(e4()));
		assertEquals(a, orderedSet.firstDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstDouble());
		assertEquals(true, orderedSet.moveToFirst(c));
		assertNotEquals(a, orderedSet.firstDouble());
		assertEquals(c, orderedSet.firstDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastDouble());
		assertEquals(false, orderedSet.moveToLast(e4()));
		assertEquals(c, orderedSet.lastDouble());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastDouble());
		assertEquals(true, orderedSet.moveToLast(a));
		assertNotEquals(c, orderedSet.lastDouble());
		assertEquals(a, orderedSet.lastDouble());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToFirstUnsupported()
	{
		try {
			orderedSet.addAndMoveToFirst(e4());
			fail("addAndMoveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToLastUnsupported()
	{
		try {
			orderedSet.addAndMoveToLast(e4());
			fail("addAndMoveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToFirstUnsupported()
	{
		try {
			orderedSet.moveToFirst(c);
			fail("moveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToLastUnsupported()
	{
		try {
			orderedSet.moveToLast(a);
			fail("moveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
}