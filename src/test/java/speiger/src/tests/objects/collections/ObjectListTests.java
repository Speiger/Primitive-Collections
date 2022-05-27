package speiger.src.tests.objects.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.lists.ImmutableObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectLinkedList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.builder.ObjectListTestSuiteBuilder;
import speiger.src.testers.objects.impl.SimpleObjectTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class ObjectListTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ObjectLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("ObjectArrayList", T -> new ObjectArrayList<>(T)));
		suite.addTest(listSuite("ObjectLinkedList", T -> new ObjectLinkedList<>(T)));
		suite.addTest(listImmutableSuite("ImmutableObjectList", T -> new ImmutableObjectList<>(T)));
	}

	private static Test listSuite(String name, Function<String[], ObjectList<String>> factory) {
		return ObjectListTestSuiteBuilder.using(new SimpleObjectTestGenerator.Lists<>(factory, createStrings())).named(name)
			.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	private static Test listImmutableSuite(String name, Function<String[], ObjectList<String>> factory) {
		return ObjectListTestSuiteBuilder.using(new SimpleObjectTestGenerator.Lists<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static ObjectSamples<String> createStrings() {
		return new ObjectSamples<>("b", "a", "c", "d", "e");
	}
}