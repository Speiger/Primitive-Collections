package speiger.src.testers.floats.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.floats.generators.TestFloatOrderedSetGenerator;
import speiger.src.testers.floats.tests.set.FloatOrderedSetMoveTester;
import speiger.src.testers.floats.tests.set.FloatOrderedSetNavigationTester;

public class FloatOrderedSetTestSuiteBuilder extends FloatSetTestSuiteBuilder {
	public static FloatOrderedSetTestSuiteBuilder using(TestFloatOrderedSetGenerator generator) {
		return (FloatOrderedSetTestSuiteBuilder) new FloatOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(FloatOrderedSetNavigationTester.class);
		testers.add(FloatOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}