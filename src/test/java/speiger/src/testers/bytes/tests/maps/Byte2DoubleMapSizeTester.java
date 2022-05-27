package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2DoubleMapSizeTester extends AbstractByte2DoubleMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}