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
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2CharMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2CharMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2CharMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Object2CharMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Character> {
	public static <T> Object2CharMapTestSuiteBuilder<T> using(TestObject2CharMapGenerator<T> generator) {
		return (Object2CharMapTestSuiteBuilder<T>) new Object2CharMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2CharMapClearTester.class);
		testers.add(Object2CharMapComputeTester.class);
		testers.add(Object2CharMapComputeIfAbsentTester.class);
		testers.add(Object2CharMapComputeIfPresentTester.class);
		testers.add(Object2CharMapSupplyIfAbsentTester.class);
		testers.add(Object2CharMapContainsKeyTester.class);
		testers.add(Object2CharMapContainsValueTester.class);
		testers.add(Object2CharMapEntrySetTester.class);
		testers.add(Object2CharMapEqualsTester.class);
		testers.add(Object2CharMapForEachTester.class);
		testers.add(Object2CharMapGetTester.class);
		testers.add(Object2CharMapGetOrDefaultTester.class);
		testers.add(Object2CharMapHashCodeTester.class);
		testers.add(Object2CharMapIsEmptyTester.class);
		testers.add(Object2CharMapMergeTester.class);
		testers.add(Object2CharMapPutTester.class);
		testers.add(Object2CharMapAddToTester.class);
		testers.add(Object2CharMapPutAllTester.class);
		testers.add(Object2CharMapPutAllArrayTester.class);
		testers.add(Object2CharMapPutIfAbsentTester.class);
		testers.add(Object2CharMapRemoveTester.class);
		testers.add(Object2CharMapRemoveEntryTester.class);
		testers.add(Object2CharMapRemoveOrDefaultTester.class);
		testers.add(Object2CharMapReplaceTester.class);
		testers.add(Object2CharMapReplaceAllTester.class);
		testers.add(Object2CharMapReplaceEntryTester.class);
		testers.add(Object2CharMapSizeTester.class);
		testers.add(Object2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2CharMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2CharMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2CharMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2CharMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected CharCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestCharCollectionGenerator generator) {
		return CharCollectionTestSuiteBuilder.using(generator);
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