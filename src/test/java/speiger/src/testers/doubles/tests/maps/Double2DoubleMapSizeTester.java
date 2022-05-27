package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2DoubleMapSizeTester extends AbstractDouble2DoubleMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}