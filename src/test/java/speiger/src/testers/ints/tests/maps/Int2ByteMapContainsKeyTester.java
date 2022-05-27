package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ByteMapContainsKeyTester extends AbstractInt2ByteMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsKey(present) should return true", getMap().containsKey(k0()));
	}
	
	public void testContains_no() {
		assertFalse("containsKey(notPresent) should return false", getMap().containsKey(k3()));
	}
}