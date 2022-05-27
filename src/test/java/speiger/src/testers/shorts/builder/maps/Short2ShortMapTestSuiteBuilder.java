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
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ShortMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2ShortMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Short> {
	public static Short2ShortMapTestSuiteBuilder using(TestShort2ShortMapGenerator generator) {
		return (Short2ShortMapTestSuiteBuilder) new Short2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2ShortMapClearTester.class);
		testers.add(Short2ShortMapComputeTester.class);
		testers.add(Short2ShortMapComputeIfAbsentTester.class);
		testers.add(Short2ShortMapComputeIfPresentTester.class);
		testers.add(Short2ShortMapSupplyIfAbsentTester.class);
		testers.add(Short2ShortMapContainsKeyTester.class);
		testers.add(Short2ShortMapContainsValueTester.class);
		testers.add(Short2ShortMapEntrySetTester.class);
		testers.add(Short2ShortMapEqualsTester.class);
		testers.add(Short2ShortMapForEachTester.class);
		testers.add(Short2ShortMapGetTester.class);
		testers.add(Short2ShortMapGetOrDefaultTester.class);
		testers.add(Short2ShortMapHashCodeTester.class);
		testers.add(Short2ShortMapIsEmptyTester.class);
		testers.add(Short2ShortMapMergeTester.class);
		testers.add(Short2ShortMapPutTester.class);
		testers.add(Short2ShortMapAddToTester.class);
		testers.add(Short2ShortMapPutAllTester.class);
		testers.add(Short2ShortMapPutAllArrayTester.class);
		testers.add(Short2ShortMapPutIfAbsentTester.class);
		testers.add(Short2ShortMapRemoveTester.class);
		testers.add(Short2ShortMapRemoveEntryTester.class);
		testers.add(Short2ShortMapRemoveOrDefaultTester.class);
		testers.add(Short2ShortMapReplaceTester.class);
		testers.add(Short2ShortMapReplaceAllTester.class);
		testers.add(Short2ShortMapReplaceEntryTester.class);
		testers.add(Short2ShortMapSizeTester.class);
		testers.add(Short2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Short>, Map.Entry<Short, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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