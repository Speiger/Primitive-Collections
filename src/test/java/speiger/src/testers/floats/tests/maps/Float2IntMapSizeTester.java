package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2IntMapSizeTester extends AbstractFloat2IntMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}