package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ByteMapSizeTester extends AbstractChar2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}