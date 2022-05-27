package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2FloatMapSizeTester extends AbstractChar2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}