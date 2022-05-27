package speiger.src.testers.doubles.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.doubles.generators.TestDoubleOrderedSetGenerator;
import speiger.src.testers.doubles.tests.set.DoubleOrderedSetMoveTester;
import speiger.src.testers.doubles.tests.set.DoubleOrderedSetNavigationTester;

@SuppressWarnings("javadoc")
public class DoubleOrderedSetTestSuiteBuilder extends DoubleSetTestSuiteBuilder {
	public static DoubleOrderedSetTestSuiteBuilder using(TestDoubleOrderedSetGenerator generator) {
		return (DoubleOrderedSetTestSuiteBuilder) new DoubleOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(DoubleOrderedSetNavigationTester.class);
		testers.add(DoubleOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}