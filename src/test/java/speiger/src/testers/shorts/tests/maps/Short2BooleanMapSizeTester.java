package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2BooleanMapSizeTester extends AbstractShort2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}