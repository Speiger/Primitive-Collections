package speiger.src.testers.bytes.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.bytes.generators.TestByteOrderedSetGenerator;
import speiger.src.testers.bytes.tests.set.ByteOrderedSetMoveTester;
import speiger.src.testers.bytes.tests.set.ByteOrderedSetNavigationTester;

@SuppressWarnings("javadoc")
public class ByteOrderedSetTestSuiteBuilder extends ByteSetTestSuiteBuilder {
	public static ByteOrderedSetTestSuiteBuilder using(TestByteOrderedSetGenerator generator) {
		return (ByteOrderedSetTestSuiteBuilder) new ByteOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ByteOrderedSetNavigationTester.class);
		testers.add(ByteOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}