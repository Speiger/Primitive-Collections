package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2CharMapSizeTester extends AbstractChar2CharMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}