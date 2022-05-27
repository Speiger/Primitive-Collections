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
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2CharMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2CharMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2CharMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Character> {
	public static Int2CharMapTestSuiteBuilder using(TestInt2CharMapGenerator generator) {
		return (Int2CharMapTestSuiteBuilder) new Int2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2CharMapClearTester.class);
		testers.add(Int2CharMapComputeTester.class);
		testers.add(Int2CharMapComputeIfAbsentTester.class);
		testers.add(Int2CharMapComputeIfPresentTester.class);
		testers.add(Int2CharMapSupplyIfAbsentTester.class);
		testers.add(Int2CharMapContainsKeyTester.class);
		testers.add(Int2CharMapContainsValueTester.class);
		testers.add(Int2CharMapEntrySetTester.class);
		testers.add(Int2CharMapEqualsTester.class);
		testers.add(Int2CharMapForEachTester.class);
		testers.add(Int2CharMapGetTester.class);
		testers.add(Int2CharMapGetOrDefaultTester.class);
		testers.add(Int2CharMapHashCodeTester.class);
		testers.add(Int2CharMapIsEmptyTester.class);
		testers.add(Int2CharMapMergeTester.class);
		testers.add(Int2CharMapPutTester.class);
		testers.add(Int2CharMapAddToTester.class);
		testers.add(Int2CharMapPutAllTester.class);
		testers.add(Int2CharMapPutAllArrayTester.class);
		testers.add(Int2CharMapPutIfAbsentTester.class);
		testers.add(Int2CharMapRemoveTester.class);
		testers.add(Int2CharMapRemoveEntryTester.class);
		testers.add(Int2CharMapRemoveOrDefaultTester.class);
		testers.add(Int2CharMapReplaceTester.class);
		testers.add(Int2CharMapReplaceAllTester.class);
		testers.add(Int2CharMapReplaceEntryTester.class);
		testers.add(Int2CharMapSizeTester.class);
		testers.add(Int2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Character>, Map.Entry<Integer, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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