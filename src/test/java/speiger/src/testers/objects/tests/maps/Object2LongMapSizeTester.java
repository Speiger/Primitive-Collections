package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2LongMapSizeTester<T> extends AbstractObject2LongMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}