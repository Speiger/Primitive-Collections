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
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2IntMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2IntMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2IntMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Object2IntMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Integer> {
	public static <T> Object2IntMapTestSuiteBuilder<T> using(TestObject2IntMapGenerator<T> generator) {
		return (Object2IntMapTestSuiteBuilder<T>) new Object2IntMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2IntMapClearTester.class);
		testers.add(Object2IntMapComputeTester.class);
		testers.add(Object2IntMapComputeIfAbsentTester.class);
		testers.add(Object2IntMapComputeIfPresentTester.class);
		testers.add(Object2IntMapSupplyIfAbsentTester.class);
		testers.add(Object2IntMapContainsKeyTester.class);
		testers.add(Object2IntMapContainsValueTester.class);
		testers.add(Object2IntMapEntrySetTester.class);
		testers.add(Object2IntMapEqualsTester.class);
		testers.add(Object2IntMapForEachTester.class);
		testers.add(Object2IntMapGetTester.class);
		testers.add(Object2IntMapGetOrDefaultTester.class);
		testers.add(Object2IntMapHashCodeTester.class);
		testers.add(Object2IntMapIsEmptyTester.class);
		testers.add(Object2IntMapMergeTester.class);
		testers.add(Object2IntMapPutTester.class);
		testers.add(Object2IntMapAddToTester.class);
		testers.add(Object2IntMapPutAllTester.class);
		testers.add(Object2IntMapPutAllArrayTester.class);
		testers.add(Object2IntMapPutIfAbsentTester.class);
		testers.add(Object2IntMapRemoveTester.class);
		testers.add(Object2IntMapRemoveEntryTester.class);
		testers.add(Object2IntMapRemoveOrDefaultTester.class);
		testers.add(Object2IntMapReplaceTester.class);
		testers.add(Object2IntMapReplaceAllTester.class);
		testers.add(Object2IntMapReplaceEntryTester.class);
		testers.add(Object2IntMapSizeTester.class);
		testers.add(Object2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Integer>, Map.Entry<T, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2IntMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2IntMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2IntMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2IntMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected IntCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestIntCollectionGenerator generator) {
		return IntCollectionTestSuiteBuilder.using(generator);
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