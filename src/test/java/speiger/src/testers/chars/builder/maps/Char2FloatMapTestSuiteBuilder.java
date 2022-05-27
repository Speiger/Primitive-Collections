package speiger.src.testers.chars.builder.maps;

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
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2FloatMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2FloatMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2FloatMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Char2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Float> {
	public static Char2FloatMapTestSuiteBuilder using(TestChar2FloatMapGenerator generator) {
		return (Char2FloatMapTestSuiteBuilder) new Char2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2FloatMapClearTester.class);
		testers.add(Char2FloatMapComputeTester.class);
		testers.add(Char2FloatMapComputeIfAbsentTester.class);
		testers.add(Char2FloatMapComputeIfPresentTester.class);
		testers.add(Char2FloatMapSupplyIfAbsentTester.class);
		testers.add(Char2FloatMapContainsKeyTester.class);
		testers.add(Char2FloatMapContainsValueTester.class);
		testers.add(Char2FloatMapEntrySetTester.class);
		testers.add(Char2FloatMapEqualsTester.class);
		testers.add(Char2FloatMapForEachTester.class);
		testers.add(Char2FloatMapGetTester.class);
		testers.add(Char2FloatMapGetOrDefaultTester.class);
		testers.add(Char2FloatMapHashCodeTester.class);
		testers.add(Char2FloatMapIsEmptyTester.class);
		testers.add(Char2FloatMapMergeTester.class);
		testers.add(Char2FloatMapPutTester.class);
		testers.add(Char2FloatMapAddToTester.class);
		testers.add(Char2FloatMapPutAllTester.class);
		testers.add(Char2FloatMapPutAllArrayTester.class);
		testers.add(Char2FloatMapPutIfAbsentTester.class);
		testers.add(Char2FloatMapRemoveTester.class);
		testers.add(Char2FloatMapRemoveEntryTester.class);
		testers.add(Char2FloatMapRemoveOrDefaultTester.class);
		testers.add(Char2FloatMapReplaceTester.class);
		testers.add(Char2FloatMapReplaceAllTester.class);
		testers.add(Char2FloatMapReplaceEntryTester.class);
		testers.add(Char2FloatMapSizeTester.class);
		testers.add(Char2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
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