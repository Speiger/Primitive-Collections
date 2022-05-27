package speiger.src.testers.ints.builder.maps;

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
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2ObjectMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Integer, V> {
	public static <V> Int2ObjectMapTestSuiteBuilder<V> using(TestInt2ObjectMapGenerator<V> generator) {
		return (Int2ObjectMapTestSuiteBuilder<V>) new Int2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2ObjectMapClearTester.class);
		testers.add(Int2ObjectMapComputeTester.class);
		testers.add(Int2ObjectMapComputeIfAbsentTester.class);
		testers.add(Int2ObjectMapComputeIfPresentTester.class);
		testers.add(Int2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Int2ObjectMapContainsKeyTester.class);
		testers.add(Int2ObjectMapContainsValueTester.class);
		testers.add(Int2ObjectMapEntrySetTester.class);
		testers.add(Int2ObjectMapEqualsTester.class);
		testers.add(Int2ObjectMapForEachTester.class);
		testers.add(Int2ObjectMapGetTester.class);
		testers.add(Int2ObjectMapGetOrDefaultTester.class);
		testers.add(Int2ObjectMapHashCodeTester.class);
		testers.add(Int2ObjectMapIsEmptyTester.class);
		testers.add(Int2ObjectMapMergeTester.class);
		testers.add(Int2ObjectMapPutTester.class);
		testers.add(Int2ObjectMapPutAllTester.class);
		testers.add(Int2ObjectMapPutAllArrayTester.class);
		testers.add(Int2ObjectMapPutIfAbsentTester.class);
		testers.add(Int2ObjectMapRemoveTester.class);
		testers.add(Int2ObjectMapRemoveEntryTester.class);
		testers.add(Int2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Int2ObjectMapReplaceTester.class);
		testers.add(Int2ObjectMapReplaceAllTester.class);
		testers.add(Int2ObjectMapReplaceEntryTester.class);
		testers.add(Int2ObjectMapSizeTester.class);
		testers.add(Int2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
	}
	
	protected ObjectCollectionTestSuiteBuilder<V> createDerivedValueCollectionSuite(TestObjectCollectionGenerator<V> generator) {
		return ObjectCollectionTestSuiteBuilder.using(generator);
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