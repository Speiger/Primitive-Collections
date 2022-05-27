package speiger.src.testers.objects.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectSetTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectSetAddTester<T> extends AbstractObjectSetTester<T>
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(absent = ZERO)
	public void testAdd_supportedPresent() {
		assertFalse("add(present) should return false", getSet().add(e0()));
		expectUnchanged();
	}
}