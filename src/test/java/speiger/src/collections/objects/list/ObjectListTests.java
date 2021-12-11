package speiger.src.collections.objects.list;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.lists.ImmutableObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectLinkedList;

@SuppressWarnings("javadoc")
public class ObjectListTests extends TestCase
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Lists");
		suite.addTest(suite("ArrayList", T -> new ObjectArrayList<>(T)));
		suite.addTest(suite("LinkedList", T -> new ObjectLinkedList<>(T)));
		suite.addTest(immutableSuite("ImmutableList", T -> new ImmutableObjectList<>(T)));
		return suite;
	}
	
	public static Test suite(String name, Function<String[], List<String>> factory)
	{
		return ListTestSuiteBuilder.using(new TestStringListGenerator() {
			@Override
			protected List<String> create(String[] elements) { return factory.apply(elements); }
		}).named(name).withFeatures(ListFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, CollectionSize.ANY).createTestSuite();
	}
	
	public static Test immutableSuite(String name, Function<String[], List<String>> factory)
	{
		return ListTestSuiteBuilder.using(new TestStringListGenerator() {
			@Override
			protected List<String> create(String[] elements) { return factory.apply(elements); }
		}).named(name).withFeatures(CollectionFeature.ALLOWS_NULL_VALUES, CollectionSize.ANY).createTestSuite();
	}
}
