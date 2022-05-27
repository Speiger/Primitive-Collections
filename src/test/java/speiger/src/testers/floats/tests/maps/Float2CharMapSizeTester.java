package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2CharMapSizeTester extends AbstractFloat2CharMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}