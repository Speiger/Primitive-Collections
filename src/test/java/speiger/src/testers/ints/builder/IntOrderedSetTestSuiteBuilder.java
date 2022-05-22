package speiger.src.testers.ints.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.ints.generators.TestIntOrderedSetGenerator;
import speiger.src.testers.ints.tests.set.IntOrderedSetMoveTester;
import speiger.src.testers.ints.tests.set.IntOrderedSetNavigationTester;

public class IntOrderedSetTestSuiteBuilder extends IntSetTestSuiteBuilder {
	public static IntOrderedSetTestSuiteBuilder using(TestIntOrderedSetGenerator generator) {
		return (IntOrderedSetTestSuiteBuilder) new IntOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(IntOrderedSetNavigationTester.class);
		testers.add(IntOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}