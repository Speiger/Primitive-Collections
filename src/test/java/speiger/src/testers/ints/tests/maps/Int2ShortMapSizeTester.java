package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ShortMapSizeTester extends AbstractInt2ShortMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}