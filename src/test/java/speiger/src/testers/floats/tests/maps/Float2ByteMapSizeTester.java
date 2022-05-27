package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ByteMapSizeTester extends AbstractFloat2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}