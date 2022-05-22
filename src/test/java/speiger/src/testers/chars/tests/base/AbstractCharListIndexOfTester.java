package speiger.src.testers.chars.tests.base;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

public abstract class AbstractCharListIndexOfTester extends AbstractCharListTester {
	protected abstract int find(char o);
	
	protected abstract String getMethodName();
	
	@CollectionSize.Require(absent = ZERO)
	public void testFind_yes() {
		assertEquals(getMethodName() + "(firstElement) should return 0", 0, find(getOrderedElements().getChar(0)));
	}
	
	public void testFind_no() {
		assertEquals(getMethodName() + "(notPresent) should return -1", -1, find(e3()));
	}
}