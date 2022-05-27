package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2LongMapSizeTester extends AbstractDouble2LongMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}