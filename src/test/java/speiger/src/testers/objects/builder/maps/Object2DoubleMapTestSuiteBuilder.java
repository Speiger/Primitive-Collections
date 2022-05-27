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
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2DoubleMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2DoubleMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Object2DoubleMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Double> {
	public static <T> Object2DoubleMapTestSuiteBuilder<T> using(TestObject2DoubleMapGenerator<T> generator) {
		return (Object2DoubleMapTestSuiteBuilder<T>) new Object2DoubleMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2DoubleMapClearTester.class);
		testers.add(Object2DoubleMapComputeTester.class);
		testers.add(Object2DoubleMapComputeIfAbsentTester.class);
		testers.add(Object2DoubleMapComputeIfPresentTester.class);
		testers.add(Object2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Object2DoubleMapContainsKeyTester.class);
		testers.add(Object2DoubleMapContainsValueTester.class);
		testers.add(Object2DoubleMapEntrySetTester.class);
		testers.add(Object2DoubleMapEqualsTester.class);
		testers.add(Object2DoubleMapForEachTester.class);
		testers.add(Object2DoubleMapGetTester.class);
		testers.add(Object2DoubleMapGetOrDefaultTester.class);
		testers.add(Object2DoubleMapHashCodeTester.class);
		testers.add(Object2DoubleMapIsEmptyTester.class);
		testers.add(Object2DoubleMapMergeTester.class);
		testers.add(Object2DoubleMapPutTester.class);
		testers.add(Object2DoubleMapAddToTester.class);
		testers.add(Object2DoubleMapPutAllTester.class);
		testers.add(Object2DoubleMapPutAllArrayTester.class);
		testers.add(Object2DoubleMapPutIfAbsentTester.class);
		testers.add(Object2DoubleMapRemoveTester.class);
		testers.add(Object2DoubleMapRemoveEntryTester.class);
		testers.add(Object2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Object2DoubleMapReplaceTester.class);
		testers.add(Object2DoubleMapReplaceAllTester.class);
		testers.add(Object2DoubleMapReplaceEntryTester.class);
		testers.add(Object2DoubleMapSizeTester.class);
		testers.add(Object2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Double>, Map.Entry<T, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2DoubleMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2DoubleMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2DoubleMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2DoubleMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected DoubleCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestDoubleCollectionGenerator generator) {
		return DoubleCollectionTestSuiteBuilder.using(generator);
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