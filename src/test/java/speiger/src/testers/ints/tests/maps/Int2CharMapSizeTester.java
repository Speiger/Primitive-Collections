package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2CharMapSizeTester extends AbstractInt2CharMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}