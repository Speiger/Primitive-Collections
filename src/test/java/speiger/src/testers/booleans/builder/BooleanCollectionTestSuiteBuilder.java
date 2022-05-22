package speiger.src.testers.booleans.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionClearTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsAnyTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionCopyTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionEqualsTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionForEachTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionIteratorTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRemoveAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRetainAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionToArrayTester;

public class BooleanCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Boolean> {
	public static BooleanCollectionTestSuiteBuilder using(TestBooleanCollectionGenerator generator) {
		return (BooleanCollectionTestSuiteBuilder) new BooleanCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(BooleanCollectionAddAllTester.class);
		testers.add(BooleanCollectionAddAllArrayTester.class);
		testers.add(BooleanCollectionAddTester.class);
		testers.add(BooleanCollectionClearTester.class);
		testers.add(BooleanCollectionContainsAllTester.class);
		testers.add(BooleanCollectionContainsAnyTester.class);
		testers.add(BooleanCollectionContainsTester.class);
		testers.add(BooleanCollectionCopyTester.class);
		testers.add(BooleanCollectionEqualsTester.class);
		testers.add(BooleanCollectionForEachTester.class);
		testers.add(BooleanCollectionIteratorTester.class);
		testers.add(BooleanCollectionRemoveAllTester.class);
		testers.add(BooleanCollectionRetainAllTester.class);
		testers.add(BooleanCollectionToArrayTester.class);
		return testers;
	}
}