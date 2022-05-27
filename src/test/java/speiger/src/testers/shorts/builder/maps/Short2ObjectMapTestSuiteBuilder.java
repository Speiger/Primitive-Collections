package speiger.src.testers.shorts.builder.maps;

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
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ObjectMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2ObjectMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Short, V> {
	public static <V> Short2ObjectMapTestSuiteBuilder<V> using(TestShort2ObjectMapGenerator<V> generator) {
		return (Short2ObjectMapTestSuiteBuilder<V>) new Short2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2ObjectMapClearTester.class);
		testers.add(Short2ObjectMapComputeTester.class);
		testers.add(Short2ObjectMapComputeIfAbsentTester.class);
		testers.add(Short2ObjectMapComputeIfPresentTester.class);
		testers.add(Short2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Short2ObjectMapContainsKeyTester.class);
		testers.add(Short2ObjectMapContainsValueTester.class);
		testers.add(Short2ObjectMapEntrySetTester.class);
		testers.add(Short2ObjectMapEqualsTester.class);
		testers.add(Short2ObjectMapForEachTester.class);
		testers.add(Short2ObjectMapGetTester.class);
		testers.add(Short2ObjectMapGetOrDefaultTester.class);
		testers.add(Short2ObjectMapHashCodeTester.class);
		testers.add(Short2ObjectMapIsEmptyTester.class);
		testers.add(Short2ObjectMapMergeTester.class);
		testers.add(Short2ObjectMapPutTester.class);
		testers.add(Short2ObjectMapPutAllTester.class);
		testers.add(Short2ObjectMapPutAllArrayTester.class);
		testers.add(Short2ObjectMapPutIfAbsentTester.class);
		testers.add(Short2ObjectMapRemoveTester.class);
		testers.add(Short2ObjectMapRemoveEntryTester.class);
		testers.add(Short2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Short2ObjectMapReplaceTester.class);
		testers.add(Short2ObjectMapReplaceAllTester.class);
		testers.add(Short2ObjectMapReplaceEntryTester.class);
		testers.add(Short2ObjectMapSizeTester.class);
		testers.add(Short2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, V>, Map.Entry<Short, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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