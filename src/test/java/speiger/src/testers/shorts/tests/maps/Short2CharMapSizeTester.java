package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2CharMapSizeTester extends AbstractShort2CharMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}