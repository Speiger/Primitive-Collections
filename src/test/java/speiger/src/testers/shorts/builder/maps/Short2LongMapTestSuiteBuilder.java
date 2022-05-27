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
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2LongMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2LongMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2LongMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Long> {
	public static Short2LongMapTestSuiteBuilder using(TestShort2LongMapGenerator generator) {
		return (Short2LongMapTestSuiteBuilder) new Short2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2LongMapClearTester.class);
		testers.add(Short2LongMapComputeTester.class);
		testers.add(Short2LongMapComputeIfAbsentTester.class);
		testers.add(Short2LongMapComputeIfPresentTester.class);
		testers.add(Short2LongMapSupplyIfAbsentTester.class);
		testers.add(Short2LongMapContainsKeyTester.class);
		testers.add(Short2LongMapContainsValueTester.class);
		testers.add(Short2LongMapEntrySetTester.class);
		testers.add(Short2LongMapEqualsTester.class);
		testers.add(Short2LongMapForEachTester.class);
		testers.add(Short2LongMapGetTester.class);
		testers.add(Short2LongMapGetOrDefaultTester.class);
		testers.add(Short2LongMapHashCodeTester.class);
		testers.add(Short2LongMapIsEmptyTester.class);
		testers.add(Short2LongMapMergeTester.class);
		testers.add(Short2LongMapPutTester.class);
		testers.add(Short2LongMapAddToTester.class);
		testers.add(Short2LongMapPutAllTester.class);
		testers.add(Short2LongMapPutAllArrayTester.class);
		testers.add(Short2LongMapPutIfAbsentTester.class);
		testers.add(Short2LongMapRemoveTester.class);
		testers.add(Short2LongMapRemoveEntryTester.class);
		testers.add(Short2LongMapRemoveOrDefaultTester.class);
		testers.add(Short2LongMapReplaceTester.class);
		testers.add(Short2LongMapReplaceAllTester.class);
		testers.add(Short2LongMapReplaceEntryTester.class);
		testers.add(Short2LongMapSizeTester.class);
		testers.add(Short2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Long>, Map.Entry<Short, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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