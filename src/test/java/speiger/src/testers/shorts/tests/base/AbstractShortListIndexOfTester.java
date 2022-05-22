package speiger.src.testers.shorts.tests.base;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

@Ignore
public abstract class AbstractShortListIndexOfTester extends AbstractShortListTester {
	protected abstract int find(short o);
	
	protected abstract String getMethodName();
	
	@CollectionSize.Require(absent = ZERO)
	public void testFind_yes() {
		assertEquals(getMethodName() + "(firstElement) should return 0", 0, find(getOrderedElements().getShort(0)));
	}
	
	public void testFind_no() {
		assertEquals(getMethodName() + "(notPresent) should return -1", -1, find(e3()));
	}
}