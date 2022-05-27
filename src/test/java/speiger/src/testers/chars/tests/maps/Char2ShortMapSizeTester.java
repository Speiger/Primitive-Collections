package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ShortMapSizeTester extends AbstractChar2ShortMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}