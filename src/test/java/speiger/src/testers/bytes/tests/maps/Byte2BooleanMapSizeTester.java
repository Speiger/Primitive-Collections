package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2BooleanMapSizeTester extends AbstractByte2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}