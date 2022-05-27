package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2BooleanMapSizeTester extends AbstractChar2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}