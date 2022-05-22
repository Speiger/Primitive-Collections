package speiger.src.tests.bytes.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.lists.ImmutableByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteLinkedList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.builder.ByteListTestSuiteBuilder;
import speiger.src.testers.bytes.impl.SimpleByteTestGenerator;

public class ByteListTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ByteLists");
		suite(suite);
		return suite;
	}
	
	public static void suite(TestSuite suite)
	{
		suite.addTest(listSuite("ByteArrayList", ByteArrayList::new));
		suite.addTest(listSuite("ByteLinkedList", ByteLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableByteList", ImmutableByteList::new));
	}
	
	private static Test listSuite(String name, Function<byte[], ByteList> factory) {
		return ByteListTestSuiteBuilder.using(new SimpleByteTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<byte[], ByteList> factory) {
		return ByteListTestSuiteBuilder.using(new SimpleByteTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
}