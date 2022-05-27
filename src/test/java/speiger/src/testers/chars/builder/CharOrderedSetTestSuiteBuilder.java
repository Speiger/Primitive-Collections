package speiger.src.testers.chars.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.chars.generators.TestCharOrderedSetGenerator;
import speiger.src.testers.chars.tests.set.CharOrderedSetMoveTester;
import speiger.src.testers.chars.tests.set.CharOrderedSetNavigationTester;

@SuppressWarnings("javadoc")
public class CharOrderedSetTestSuiteBuilder extends CharSetTestSuiteBuilder {
	public static CharOrderedSetTestSuiteBuilder using(TestCharOrderedSetGenerator generator) {
		return (CharOrderedSetTestSuiteBuilder) new CharOrderedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(CharOrderedSetNavigationTester.class);
		testers.add(CharOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}