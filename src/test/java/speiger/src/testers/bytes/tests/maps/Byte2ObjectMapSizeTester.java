package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ObjectMapTester;

@Ignore
public class Byte2ObjectMapSizeTester<V> extends AbstractByte2ObjectMapTester<V> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}