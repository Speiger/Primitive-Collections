package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2LongMapSizeTester extends AbstractByte2LongMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}