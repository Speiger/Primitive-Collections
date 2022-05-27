package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2DoubleMapSizeTester extends AbstractFloat2DoubleMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}