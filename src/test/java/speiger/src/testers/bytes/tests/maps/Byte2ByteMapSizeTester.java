package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ByteMapSizeTester extends AbstractByte2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}