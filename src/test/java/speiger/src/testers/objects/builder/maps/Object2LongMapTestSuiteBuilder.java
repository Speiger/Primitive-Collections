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
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2LongMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2LongMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2LongMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Object2LongMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Long> {
	public static <T> Object2LongMapTestSuiteBuilder<T> using(TestObject2LongMapGenerator<T> generator) {
		return (Object2LongMapTestSuiteBuilder<T>) new Object2LongMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2LongMapClearTester.class);
		testers.add(Object2LongMapComputeTester.class);
		testers.add(Object2LongMapComputeIfAbsentTester.class);
		testers.add(Object2LongMapComputeIfPresentTester.class);
		testers.add(Object2LongMapSupplyIfAbsentTester.class);
		testers.add(Object2LongMapContainsKeyTester.class);
		testers.add(Object2LongMapContainsValueTester.class);
		testers.add(Object2LongMapEntrySetTester.class);
		testers.add(Object2LongMapEqualsTester.class);
		testers.add(Object2LongMapForEachTester.class);
		testers.add(Object2LongMapGetTester.class);
		testers.add(Object2LongMapGetOrDefaultTester.class);
		testers.add(Object2LongMapHashCodeTester.class);
		testers.add(Object2LongMapIsEmptyTester.class);
		testers.add(Object2LongMapMergeTester.class);
		testers.add(Object2LongMapPutTester.class);
		testers.add(Object2LongMapAddToTester.class);
		testers.add(Object2LongMapPutAllTester.class);
		testers.add(Object2LongMapPutAllArrayTester.class);
		testers.add(Object2LongMapPutIfAbsentTester.class);
		testers.add(Object2LongMapRemoveTester.class);
		testers.add(Object2LongMapRemoveEntryTester.class);
		testers.add(Object2LongMapRemoveOrDefaultTester.class);
		testers.add(Object2LongMapReplaceTester.class);
		testers.add(Object2LongMapReplaceAllTester.class);
		testers.add(Object2LongMapReplaceEntryTester.class);
		testers.add(Object2LongMapSizeTester.class);
		testers.add(Object2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Long>, Map.Entry<T, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2LongMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2LongMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2LongMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2LongMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected LongCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestLongCollectionGenerator generator) {
		return LongCollectionTestSuiteBuilder.using(generator);
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