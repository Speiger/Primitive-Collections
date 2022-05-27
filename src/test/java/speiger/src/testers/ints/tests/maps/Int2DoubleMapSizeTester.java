package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2DoubleMapSizeTester extends AbstractInt2DoubleMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}