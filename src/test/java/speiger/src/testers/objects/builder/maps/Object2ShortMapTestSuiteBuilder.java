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
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ShortMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2ShortMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2ShortMapAddToTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Object2ShortMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Short> {
	public static <T> Object2ShortMapTestSuiteBuilder<T> using(TestObject2ShortMapGenerator<T> generator) {
		return (Object2ShortMapTestSuiteBuilder<T>) new Object2ShortMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2ShortMapClearTester.class);
		testers.add(Object2ShortMapComputeTester.class);
		testers.add(Object2ShortMapComputeIfAbsentTester.class);
		testers.add(Object2ShortMapComputeIfPresentTester.class);
		testers.add(Object2ShortMapSupplyIfAbsentTester.class);
		testers.add(Object2ShortMapContainsKeyTester.class);
		testers.add(Object2ShortMapContainsValueTester.class);
		testers.add(Object2ShortMapEntrySetTester.class);
		testers.add(Object2ShortMapEqualsTester.class);
		testers.add(Object2ShortMapForEachTester.class);
		testers.add(Object2ShortMapGetTester.class);
		testers.add(Object2ShortMapGetOrDefaultTester.class);
		testers.add(Object2ShortMapHashCodeTester.class);
		testers.add(Object2ShortMapIsEmptyTester.class);
		testers.add(Object2ShortMapMergeTester.class);
		testers.add(Object2ShortMapPutTester.class);
		testers.add(Object2ShortMapAddToTester.class);
		testers.add(Object2ShortMapPutAllTester.class);
		testers.add(Object2ShortMapPutAllArrayTester.class);
		testers.add(Object2ShortMapPutIfAbsentTester.class);
		testers.add(Object2ShortMapRemoveTester.class);
		testers.add(Object2ShortMapRemoveEntryTester.class);
		testers.add(Object2ShortMapRemoveOrDefaultTester.class);
		testers.add(Object2ShortMapReplaceTester.class);
		testers.add(Object2ShortMapReplaceAllTester.class);
		testers.add(Object2ShortMapReplaceEntryTester.class);
		testers.add(Object2ShortMapSizeTester.class);
		testers.add(Object2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Short>, Map.Entry<T, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2ShortMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2ShortMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2ShortMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2ShortMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected ShortCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestShortCollectionGenerator generator) {
		return ShortCollectionTestSuiteBuilder.using(generator);
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