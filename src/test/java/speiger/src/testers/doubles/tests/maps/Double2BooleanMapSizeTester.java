package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2BooleanMapSizeTester extends AbstractDouble2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}