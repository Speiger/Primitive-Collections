package speiger.src.tests.longs.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.lists.ImmutableLongList;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongLinkedList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.builder.LongListTestSuiteBuilder;
import speiger.src.testers.longs.impl.SimpleLongTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class LongListTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("LongLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("LongArrayList", LongArrayList::new));
		suite.addTest(listSuite("LongLinkedList", LongLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableLongList", ImmutableLongList::new));
	}
	
	private static Test listSuite(String name, Function<long[], LongList> factory) {
		return LongListTestSuiteBuilder.using(new SimpleLongTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<long[], LongList> factory) {
		return LongListTestSuiteBuilder.using(new SimpleLongTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
}