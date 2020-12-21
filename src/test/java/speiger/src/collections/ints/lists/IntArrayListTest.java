package speiger.src.collections.ints.lists;

import org.junit.Test;

import speiger.src.collections.ints.base.BaseIntListTest;
import speiger.src.collections.ints.base.IIntArrayTest;
import speiger.src.collections.ints.base.IIntStackTests;

public class IntArrayListTest extends BaseIntListTest implements IIntStackTests, IIntArrayTest
{   
	@Override
	public IntArrayList create(int[] data) {
		return new IntArrayList(data);
	}
	
	@Test
	@Override
	public void testPush() {
		IIntStackTests.super.testPush();
	}
	
	@Test
	@Override
	public void testPop() {
		IIntStackTests.super.testPop();
	}
	
	@Test
	@Override
	public void testEnsureCapacity() {
		IIntArrayTest.super.testEnsureCapacity();
	}
	
	@Test
	@Override
	public void testElements() {
		IIntArrayTest.super.testElements();
	}
	
	@Test
	@Override
	public void testTrim() {
		IIntArrayTest.super.testTrim();
	}
}
