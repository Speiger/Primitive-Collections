package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2BooleanMapSizeTester extends AbstractInt2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}