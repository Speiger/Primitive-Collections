package speiger.src.testers.ints.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.testers.ints.tests.base.AbstractIntSetTester;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
public class IntOrderedSetMoveTester extends AbstractIntSetTester
{
	private IntOrderedSet orderedSet;
	private IntList values;
	private int a;
	private int c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (IntOrderedSet) getSet();
		values = IntHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		if (values.size() >= 1) {
			a = values.getInt(0);
			if (values.size() >= 3) {
				c = values.getInt(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstInt());
		assertEquals(true, orderedSet.addAndMoveToFirst(e4()));
		assertNotEquals(a, orderedSet.firstInt());
		assertEquals(e4(), orderedSet.firstInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstInt());
		assertEquals(false, orderedSet.addAndMoveToFirst(c));
		assertNotEquals(a, orderedSet.firstInt());
		assertEquals(c, orderedSet.firstInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastInt());
		assertEquals(true, orderedSet.addAndMoveToLast(e4()));
		assertNotEquals(c, orderedSet.lastInt());
		assertEquals(e4(), orderedSet.lastInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastInt());
		assertEquals(false, orderedSet.addAndMoveToLast(a));
		assertNotEquals(c, orderedSet.lastInt());
		assertEquals(a, orderedSet.lastInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstInt());
		assertEquals(false, orderedSet.moveToFirst(e4()));
		assertEquals(a, orderedSet.firstInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstInt());
		assertEquals(true, orderedSet.moveToFirst(c));
		assertNotEquals(a, orderedSet.firstInt());
		assertEquals(c, orderedSet.firstInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastInt());
		assertEquals(false, orderedSet.moveToLast(e4()));
		assertEquals(c, orderedSet.lastInt());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastInt());
		assertEquals(true, orderedSet.moveToLast(a));
		assertNotEquals(c, orderedSet.lastInt());
		assertEquals(a, orderedSet.lastInt());
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