package speiger.src.tests.doubles.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.lists.ImmutableDoubleList;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleLinkedList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.builder.DoubleListTestSuiteBuilder;
import speiger.src.testers.doubles.impl.SimpleDoubleTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class DoubleListTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("DoubleLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("DoubleArrayList", DoubleArrayList::new));
		suite.addTest(listSuite("DoubleLinkedList", DoubleLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableDoubleList", ImmutableDoubleList::new));
	}
	
	private static Test listSuite(String name, Function<double[], DoubleList> factory) {
		return DoubleListTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<double[], DoubleList> factory) {
		return DoubleListTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
}