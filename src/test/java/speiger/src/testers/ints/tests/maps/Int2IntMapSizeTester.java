package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2IntMapSizeTester extends AbstractInt2IntMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}