package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2CharMapContainsValueTester extends AbstractLong2CharMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsValue(present) should return true", getMap().containsValue(v0()));
	}

	public void testContains_no() {
		assertFalse("containsValue(notPresent) should return false", getMap().containsValue(v3()));
	}
}