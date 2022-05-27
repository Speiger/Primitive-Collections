package speiger.src.testers.floats.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.AbstractFloatSetTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatSetAddTester extends AbstractFloatSetTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_supportedPresent() {
		assertFalse("add(present) should return false", getSet().add(e0()));
		expectUnchanged();
	}
}