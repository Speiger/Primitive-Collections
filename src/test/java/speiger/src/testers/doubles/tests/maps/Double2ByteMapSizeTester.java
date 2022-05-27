package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ByteMapSizeTester extends AbstractDouble2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}