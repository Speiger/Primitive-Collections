package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2FloatMapSizeTester extends AbstractByte2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}