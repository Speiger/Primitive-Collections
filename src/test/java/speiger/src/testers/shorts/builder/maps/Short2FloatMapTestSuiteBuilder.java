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
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2FloatMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2FloatMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Short2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Float> {
	public static Short2FloatMapTestSuiteBuilder using(TestShort2FloatMapGenerator generator) {
		return (Short2FloatMapTestSuiteBuilder) new Short2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2FloatMapClearTester.class);
		testers.add(Short2FloatMapComputeTester.class);
		testers.add(Short2FloatMapComputeIfAbsentTester.class);
		testers.add(Short2FloatMapComputeIfPresentTester.class);
		testers.add(Short2FloatMapSupplyIfAbsentTester.class);
		testers.add(Short2FloatMapContainsKeyTester.class);
		testers.add(Short2FloatMapContainsValueTester.class);
		testers.add(Short2FloatMapEntrySetTester.class);
		testers.add(Short2FloatMapEqualsTester.class);
		testers.add(Short2FloatMapForEachTester.class);
		testers.add(Short2FloatMapGetTester.class);
		testers.add(Short2FloatMapGetOrDefaultTester.class);
		testers.add(Short2FloatMapHashCodeTester.class);
		testers.add(Short2FloatMapIsEmptyTester.class);
		testers.add(Short2FloatMapMergeTester.class);
		testers.add(Short2FloatMapPutTester.class);
		testers.add(Short2FloatMapAddToTester.class);
		testers.add(Short2FloatMapPutAllTester.class);
		testers.add(Short2FloatMapPutAllArrayTester.class);
		testers.add(Short2FloatMapPutIfAbsentTester.class);
		testers.add(Short2FloatMapRemoveTester.class);
		testers.add(Short2FloatMapRemoveEntryTester.class);
		testers.add(Short2FloatMapRemoveOrDefaultTester.class);
		testers.add(Short2FloatMapReplaceTester.class);
		testers.add(Short2FloatMapReplaceAllTester.class);
		testers.add(Short2FloatMapReplaceEntryTester.class);
		testers.add(Short2FloatMapSizeTester.class);
		testers.add(Short2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Float>, Map.Entry<Short, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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