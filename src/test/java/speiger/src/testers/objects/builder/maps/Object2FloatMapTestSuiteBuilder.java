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
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2FloatMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2FloatMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2FloatMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Object2FloatMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Float> {
	public static <T> Object2FloatMapTestSuiteBuilder<T> using(TestObject2FloatMapGenerator<T> generator) {
		return (Object2FloatMapTestSuiteBuilder<T>) new Object2FloatMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2FloatMapClearTester.class);
		testers.add(Object2FloatMapComputeTester.class);
		testers.add(Object2FloatMapComputeIfAbsentTester.class);
		testers.add(Object2FloatMapComputeIfPresentTester.class);
		testers.add(Object2FloatMapSupplyIfAbsentTester.class);
		testers.add(Object2FloatMapContainsKeyTester.class);
		testers.add(Object2FloatMapContainsValueTester.class);
		testers.add(Object2FloatMapEntrySetTester.class);
		testers.add(Object2FloatMapEqualsTester.class);
		testers.add(Object2FloatMapForEachTester.class);
		testers.add(Object2FloatMapGetTester.class);
		testers.add(Object2FloatMapGetOrDefaultTester.class);
		testers.add(Object2FloatMapHashCodeTester.class);
		testers.add(Object2FloatMapIsEmptyTester.class);
		testers.add(Object2FloatMapMergeTester.class);
		testers.add(Object2FloatMapPutTester.class);
		testers.add(Object2FloatMapAddToTester.class);
		testers.add(Object2FloatMapPutAllTester.class);
		testers.add(Object2FloatMapPutAllArrayTester.class);
		testers.add(Object2FloatMapPutIfAbsentTester.class);
		testers.add(Object2FloatMapRemoveTester.class);
		testers.add(Object2FloatMapRemoveEntryTester.class);
		testers.add(Object2FloatMapRemoveOrDefaultTester.class);
		testers.add(Object2FloatMapReplaceTester.class);
		testers.add(Object2FloatMapReplaceAllTester.class);
		testers.add(Object2FloatMapReplaceEntryTester.class);
		testers.add(Object2FloatMapSizeTester.class);
		testers.add(Object2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2FloatMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2FloatMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2FloatMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2FloatMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected FloatCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestFloatCollectionGenerator generator) {
		return FloatCollectionTestSuiteBuilder.using(generator);
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