package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2FloatMapSizeTester extends AbstractFloat2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}