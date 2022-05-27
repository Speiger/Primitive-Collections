package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ByteMapSizeTester extends AbstractShort2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}