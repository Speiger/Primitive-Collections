package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ONE;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortListAbsentTester extends AbstractShortListTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void testIfAbsent() {
		assertTrue("addIfAbsent(absent) should return true", getList().addIfAbsent(e1()));
		expectAdded(e1());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void testIfPresent() {
		assertFalse("addIfAbsent(present) should return false", getList().addIfAbsent(e0()));
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void test_IfAbsentUnsupported() {
		try {
			assertFalse("addIfAbsent(absent) should return false", getList().addIfAbsent(e1()));			
		} catch (UnsupportedOperationException e) {
			//Success
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void test_IfPresentUnsupported() {
		try {
			assertFalse("addIfAbsent(present) should return false", getList().addIfAbsent(e0()));			
		} catch (UnsupportedOperationException e) {
			//Success
		}
	}
}