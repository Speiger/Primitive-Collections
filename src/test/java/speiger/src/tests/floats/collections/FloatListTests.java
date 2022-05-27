package speiger.src.tests.floats.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.lists.ImmutableFloatList;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatLinkedList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.builder.FloatListTestSuiteBuilder;
import speiger.src.testers.floats.impl.SimpleFloatTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class FloatListTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("FloatLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("FloatArrayList", FloatArrayList::new));
		suite.addTest(listSuite("FloatLinkedList", FloatLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableFloatList", ImmutableFloatList::new));
	}
	
	private static Test listSuite(String name, Function<float[], FloatList> factory) {
		return FloatListTestSuiteBuilder.using(new SimpleFloatTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<float[], FloatList> factory) {
		return FloatListTestSuiteBuilder.using(new SimpleFloatTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
}