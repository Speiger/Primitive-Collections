package speiger.src.testers.objects.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ByteMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2ByteMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2ByteMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Object2ByteMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Byte> {
	public static <T> Object2ByteMapTestSuiteBuilder<T> using(TestObject2ByteMapGenerator<T> generator) {
		return (Object2ByteMapTestSuiteBuilder<T>) new Object2ByteMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2ByteMapClearTester.class);
		testers.add(Object2ByteMapComputeTester.class);
		testers.add(Object2ByteMapComputeIfAbsentTester.class);
		testers.add(Object2ByteMapComputeIfPresentTester.class);
		testers.add(Object2ByteMapSupplyIfAbsentTester.class);
		testers.add(Object2ByteMapContainsKeyTester.class);
		testers.add(Object2ByteMapContainsValueTester.class);
		testers.add(Object2ByteMapEntrySetTester.class);
		testers.add(Object2ByteMapEqualsTester.class);
		testers.add(Object2ByteMapForEachTester.class);
		testers.add(Object2ByteMapGetTester.class);
		testers.add(Object2ByteMapGetOrDefaultTester.class);
		testers.add(Object2ByteMapHashCodeTester.class);
		testers.add(Object2ByteMapIsEmptyTester.class);
		testers.add(Object2ByteMapMergeTester.class);
		testers.add(Object2ByteMapPutTester.class);
		testers.add(Object2ByteMapAddToTester.class);
		testers.add(Object2ByteMapPutAllTester.class);
		testers.add(Object2ByteMapPutAllArrayTester.class);
		testers.add(Object2ByteMapPutIfAbsentTester.class);
		testers.add(Object2ByteMapRemoveTester.class);
		testers.add(Object2ByteMapRemoveEntryTester.class);
		testers.add(Object2ByteMapRemoveOrDefaultTester.class);
		testers.add(Object2ByteMapReplaceTester.class);
		testers.add(Object2ByteMapReplaceAllTester.class);
		testers.add(Object2ByteMapReplaceEntryTester.class);
		testers.add(Object2ByteMapSizeTester.class);
		testers.add(Object2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Byte>, Map.Entry<T, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2ByteMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2ByteMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2ByteMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2ByteMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected ByteCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestByteCollectionGenerator generator) {
		return ByteCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}