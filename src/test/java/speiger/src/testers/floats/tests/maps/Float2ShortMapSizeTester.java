package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortMapSizeTester extends AbstractFloat2ShortMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}