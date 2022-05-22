package speiger.src.testers.doubles.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionAddAllArrayTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionAddAllTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionAddTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionClearTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionContainsAllTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionContainsAnyTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionContainsTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionCopyTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionEqualsTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionForEachTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionIteratorTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionRemoveAllTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionRemoveIfTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionStreamTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionRetainAllTester;
import speiger.src.testers.doubles.tests.collection.DoubleCollectionToArrayTester;

public class DoubleCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Double> {
	public static DoubleCollectionTestSuiteBuilder using(TestDoubleCollectionGenerator generator) {
		return (DoubleCollectionTestSuiteBuilder) new DoubleCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(DoubleCollectionAddAllTester.class);
		testers.add(DoubleCollectionAddAllArrayTester.class);
		testers.add(DoubleCollectionAddTester.class);
		testers.add(DoubleCollectionClearTester.class);
		testers.add(DoubleCollectionContainsAllTester.class);
		testers.add(DoubleCollectionContainsAnyTester.class);
		testers.add(DoubleCollectionContainsTester.class);
		testers.add(DoubleCollectionCopyTester.class);
		testers.add(DoubleCollectionEqualsTester.class);
		testers.add(DoubleCollectionForEachTester.class);
		testers.add(DoubleCollectionIteratorTester.class);
		testers.add(DoubleCollectionRemoveAllTester.class);
		testers.add(DoubleCollectionRetainAllTester.class);
		testers.add(DoubleCollectionRemoveIfTester.class);
		testers.add(DoubleCollectionStreamTester.class);
		testers.add(DoubleCollectionToArrayTester.class);
		return testers;
	}
}