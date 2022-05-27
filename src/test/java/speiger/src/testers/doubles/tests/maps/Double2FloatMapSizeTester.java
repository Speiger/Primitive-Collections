package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2FloatMapSizeTester extends AbstractDouble2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}