package speiger.src.testers.longs.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.longs.generators.TestLongOrderedSetGenerator;
import speiger.src.testers.longs.tests.set.LongOrderedSetMoveTester;
import speiger.src.testers.longs.tests.set.LongOrderedSetNavigationTester;

public class LongOrderedSetTestSuiteBuilder extends LongSetTestSuiteBuilder {
	public static LongOrderedSetTestSuiteBuilder using(TestLongOrderedSetGenerator generator) {
		return (LongOrderedSetTestSuiteBuilder) new LongOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(LongOrderedSetNavigationTester.class);
		testers.add(LongOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}