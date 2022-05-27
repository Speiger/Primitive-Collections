package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2IntMapSizeTester extends AbstractChar2IntMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}