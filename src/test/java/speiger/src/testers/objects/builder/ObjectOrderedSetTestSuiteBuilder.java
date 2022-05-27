package speiger.src.testers.objects.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.TestSuite;
import speiger.src.testers.objects.generators.TestObjectOrderedSetGenerator;
import speiger.src.testers.objects.tests.set.ObjectOrderedSetMoveTester;
import speiger.src.testers.objects.tests.set.ObjectOrderedSetNavigationTester;

@SuppressWarnings("javadoc")
public class ObjectOrderedSetTestSuiteBuilder<T> extends ObjectSetTestSuiteBuilder<T> {
	public static <T> ObjectOrderedSetTestSuiteBuilder<T> using(TestObjectOrderedSetGenerator<T> generator) {
		return (ObjectOrderedSetTestSuiteBuilder<T>) new ObjectOrderedSetTestSuiteBuilder<T>().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ObjectOrderedSetNavigationTester.class);
		testers.add(ObjectOrderedSetMoveTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		withFeatures(CollectionFeature.KNOWN_ORDER);
		return super.createTestSuite();
	}
}