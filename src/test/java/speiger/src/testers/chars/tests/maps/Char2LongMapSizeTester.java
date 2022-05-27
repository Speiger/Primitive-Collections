package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2LongMapSizeTester extends AbstractChar2LongMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}