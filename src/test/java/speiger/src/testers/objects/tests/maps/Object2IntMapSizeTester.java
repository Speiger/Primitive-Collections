package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2IntMapSizeTester<T> extends AbstractObject2IntMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}