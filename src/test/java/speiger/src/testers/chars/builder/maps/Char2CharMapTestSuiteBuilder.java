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
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2CharMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2CharMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2CharMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Char2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Character> {
	public static Char2CharMapTestSuiteBuilder using(TestChar2CharMapGenerator generator) {
		return (Char2CharMapTestSuiteBuilder) new Char2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2CharMapClearTester.class);
		testers.add(Char2CharMapComputeTester.class);
		testers.add(Char2CharMapComputeIfAbsentTester.class);
		testers.add(Char2CharMapComputeIfPresentTester.class);
		testers.add(Char2CharMapSupplyIfAbsentTester.class);
		testers.add(Char2CharMapContainsKeyTester.class);
		testers.add(Char2CharMapContainsValueTester.class);
		testers.add(Char2CharMapEntrySetTester.class);
		testers.add(Char2CharMapEqualsTester.class);
		testers.add(Char2CharMapForEachTester.class);
		testers.add(Char2CharMapGetTester.class);
		testers.add(Char2CharMapGetOrDefaultTester.class);
		testers.add(Char2CharMapHashCodeTester.class);
		testers.add(Char2CharMapIsEmptyTester.class);
		testers.add(Char2CharMapMergeTester.class);
		testers.add(Char2CharMapPutTester.class);
		testers.add(Char2CharMapAddToTester.class);
		testers.add(Char2CharMapPutAllTester.class);
		testers.add(Char2CharMapPutAllArrayTester.class);
		testers.add(Char2CharMapPutIfAbsentTester.class);
		testers.add(Char2CharMapRemoveTester.class);
		testers.add(Char2CharMapRemoveEntryTester.class);
		testers.add(Char2CharMapRemoveOrDefaultTester.class);
		testers.add(Char2CharMapReplaceTester.class);
		testers.add(Char2CharMapReplaceAllTester.class);
		testers.add(Char2CharMapReplaceEntryTester.class);
		testers.add(Char2CharMapSizeTester.class);
		testers.add(Char2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Character>, Map.Entry<Character, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
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