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
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2FloatMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2FloatMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2FloatMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Int2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Float> {
	public static Int2FloatMapTestSuiteBuilder using(TestInt2FloatMapGenerator generator) {
		return (Int2FloatMapTestSuiteBuilder) new Int2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2FloatMapClearTester.class);
		testers.add(Int2FloatMapComputeTester.class);
		testers.add(Int2FloatMapComputeIfAbsentTester.class);
		testers.add(Int2FloatMapComputeIfPresentTester.class);
		testers.add(Int2FloatMapSupplyIfAbsentTester.class);
		testers.add(Int2FloatMapContainsKeyTester.class);
		testers.add(Int2FloatMapContainsValueTester.class);
		testers.add(Int2FloatMapEntrySetTester.class);
		testers.add(Int2FloatMapEqualsTester.class);
		testers.add(Int2FloatMapForEachTester.class);
		testers.add(Int2FloatMapGetTester.class);
		testers.add(Int2FloatMapGetOrDefaultTester.class);
		testers.add(Int2FloatMapHashCodeTester.class);
		testers.add(Int2FloatMapIsEmptyTester.class);
		testers.add(Int2FloatMapMergeTester.class);
		testers.add(Int2FloatMapPutTester.class);
		testers.add(Int2FloatMapAddToTester.class);
		testers.add(Int2FloatMapPutAllTester.class);
		testers.add(Int2FloatMapPutAllArrayTester.class);
		testers.add(Int2FloatMapPutIfAbsentTester.class);
		testers.add(Int2FloatMapRemoveTester.class);
		testers.add(Int2FloatMapRemoveEntryTester.class);
		testers.add(Int2FloatMapRemoveOrDefaultTester.class);
		testers.add(Int2FloatMapReplaceTester.class);
		testers.add(Int2FloatMapReplaceAllTester.class);
		testers.add(Int2FloatMapReplaceEntryTester.class);
		testers.add(Int2FloatMapSizeTester.class);
		testers.add(Int2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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