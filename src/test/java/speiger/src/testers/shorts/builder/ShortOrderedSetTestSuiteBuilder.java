package speiger.src.testers.shorts.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.shorts.generators.TestShortOrderedSetGenerator;
import speiger.src.testers.shorts.tests.set.ShortOrderedSetMoveTester;
import speiger.src.testers.shorts.tests.set.ShortOrderedSetNavigationTester;

@SuppressWarnings("javadoc")
public class ShortOrderedSetTestSuiteBuilder extends ShortSetTestSuiteBuilder {
	public static ShortOrderedSetTestSuiteBuilder using(TestShortOrderedSetGenerator generator) {
		return (ShortOrderedSetTestSuiteBuilder) new ShortOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ShortOrderedSetNavigationTester.class);
		testers.add(ShortOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}