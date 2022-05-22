package speiger.src.testers.floats.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddAllArrayTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionClearTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsAnyTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionCopyTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionEqualsTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionForEachTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionIteratorTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRemoveAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRemoveIfTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionStreamTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRetainAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionToArrayTester;

public class FloatCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Float> {
	public static FloatCollectionTestSuiteBuilder using(TestFloatCollectionGenerator generator) {
		return (FloatCollectionTestSuiteBuilder) new FloatCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(FloatCollectionAddAllTester.class);
		testers.add(FloatCollectionAddAllArrayTester.class);
		testers.add(FloatCollectionAddTester.class);
		testers.add(FloatCollectionClearTester.class);
		testers.add(FloatCollectionContainsAllTester.class);
		testers.add(FloatCollectionContainsAnyTester.class);
		testers.add(FloatCollectionContainsTester.class);
		testers.add(FloatCollectionCopyTester.class);
		testers.add(FloatCollectionEqualsTester.class);
		testers.add(FloatCollectionForEachTester.class);
		testers.add(FloatCollectionIteratorTester.class);
		testers.add(FloatCollectionRemoveAllTester.class);
		testers.add(FloatCollectionRetainAllTester.class);
		testers.add(FloatCollectionRemoveIfTester.class);
		testers.add(FloatCollectionStreamTester.class);
		testers.add(FloatCollectionToArrayTester.class);
		return testers;
	}
}