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
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2IntMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2IntMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2IntMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Integer> {
	public static Short2IntMapTestSuiteBuilder using(TestShort2IntMapGenerator generator) {
		return (Short2IntMapTestSuiteBuilder) new Short2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2IntMapClearTester.class);
		testers.add(Short2IntMapComputeTester.class);
		testers.add(Short2IntMapComputeIfAbsentTester.class);
		testers.add(Short2IntMapComputeIfPresentTester.class);
		testers.add(Short2IntMapSupplyIfAbsentTester.class);
		testers.add(Short2IntMapContainsKeyTester.class);
		testers.add(Short2IntMapContainsValueTester.class);
		testers.add(Short2IntMapEntrySetTester.class);
		testers.add(Short2IntMapEqualsTester.class);
		testers.add(Short2IntMapForEachTester.class);
		testers.add(Short2IntMapGetTester.class);
		testers.add(Short2IntMapGetOrDefaultTester.class);
		testers.add(Short2IntMapHashCodeTester.class);
		testers.add(Short2IntMapIsEmptyTester.class);
		testers.add(Short2IntMapMergeTester.class);
		testers.add(Short2IntMapPutTester.class);
		testers.add(Short2IntMapAddToTester.class);
		testers.add(Short2IntMapPutAllTester.class);
		testers.add(Short2IntMapPutAllArrayTester.class);
		testers.add(Short2IntMapPutIfAbsentTester.class);
		testers.add(Short2IntMapRemoveTester.class);
		testers.add(Short2IntMapRemoveEntryTester.class);
		testers.add(Short2IntMapRemoveOrDefaultTester.class);
		testers.add(Short2IntMapReplaceTester.class);
		testers.add(Short2IntMapReplaceAllTester.class);
		testers.add(Short2IntMapReplaceEntryTester.class);
		testers.add(Short2IntMapSizeTester.class);
		testers.add(Short2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Integer>, Map.Entry<Short, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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