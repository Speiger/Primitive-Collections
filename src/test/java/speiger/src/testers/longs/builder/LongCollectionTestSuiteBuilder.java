package speiger.src.testers.longs.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.longs.tests.collection.LongCollectionAddAllArrayTester;
import speiger.src.testers.longs.tests.collection.LongCollectionAddAllTester;
import speiger.src.testers.longs.tests.collection.LongCollectionAddTester;
import speiger.src.testers.longs.tests.collection.LongCollectionClearTester;
import speiger.src.testers.longs.tests.collection.LongCollectionContainsAllTester;
import speiger.src.testers.longs.tests.collection.LongCollectionContainsAnyTester;
import speiger.src.testers.longs.tests.collection.LongCollectionContainsTester;
import speiger.src.testers.longs.tests.collection.LongCollectionCopyTester;
import speiger.src.testers.longs.tests.collection.LongCollectionEqualsTester;
import speiger.src.testers.longs.tests.collection.LongCollectionForEachTester;
import speiger.src.testers.longs.tests.collection.LongCollectionIteratorTester;
import speiger.src.testers.longs.tests.collection.LongCollectionRemoveAllTester;
import speiger.src.testers.longs.tests.collection.LongCollectionRemoveIfTester;
import speiger.src.testers.longs.tests.collection.LongCollectionStreamTester;
import speiger.src.testers.longs.tests.collection.LongCollectionRetainAllTester;
import speiger.src.testers.longs.tests.collection.LongCollectionToArrayTester;

public class LongCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Long> {
	public static LongCollectionTestSuiteBuilder using(TestLongCollectionGenerator generator) {
		return (LongCollectionTestSuiteBuilder) new LongCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(LongCollectionAddAllTester.class);
		testers.add(LongCollectionAddAllArrayTester.class);
		testers.add(LongCollectionAddTester.class);
		testers.add(LongCollectionClearTester.class);
		testers.add(LongCollectionContainsAllTester.class);
		testers.add(LongCollectionContainsAnyTester.class);
		testers.add(LongCollectionContainsTester.class);
		testers.add(LongCollectionCopyTester.class);
		testers.add(LongCollectionEqualsTester.class);
		testers.add(LongCollectionForEachTester.class);
		testers.add(LongCollectionIteratorTester.class);
		testers.add(LongCollectionRemoveAllTester.class);
		testers.add(LongCollectionRetainAllTester.class);
		testers.add(LongCollectionRemoveIfTester.class);
		testers.add(LongCollectionStreamTester.class);
		testers.add(LongCollectionToArrayTester.class);
		return testers;
	}
}