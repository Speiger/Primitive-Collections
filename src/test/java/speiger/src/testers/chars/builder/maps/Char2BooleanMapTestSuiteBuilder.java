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
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2BooleanMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2BooleanMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Char2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Boolean> {
	public static Char2BooleanMapTestSuiteBuilder using(TestChar2BooleanMapGenerator generator) {
		return (Char2BooleanMapTestSuiteBuilder) new Char2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2BooleanMapClearTester.class);
		testers.add(Char2BooleanMapComputeTester.class);
		testers.add(Char2BooleanMapComputeIfAbsentTester.class);
		testers.add(Char2BooleanMapComputeIfPresentTester.class);
		testers.add(Char2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Char2BooleanMapContainsKeyTester.class);
		testers.add(Char2BooleanMapContainsValueTester.class);
		testers.add(Char2BooleanMapEntrySetTester.class);
		testers.add(Char2BooleanMapEqualsTester.class);
		testers.add(Char2BooleanMapForEachTester.class);
		testers.add(Char2BooleanMapGetTester.class);
		testers.add(Char2BooleanMapGetOrDefaultTester.class);
		testers.add(Char2BooleanMapHashCodeTester.class);
		testers.add(Char2BooleanMapIsEmptyTester.class);
		testers.add(Char2BooleanMapMergeTester.class);
		testers.add(Char2BooleanMapPutTester.class);
		testers.add(Char2BooleanMapPutAllTester.class);
		testers.add(Char2BooleanMapPutAllArrayTester.class);
		testers.add(Char2BooleanMapPutIfAbsentTester.class);
		testers.add(Char2BooleanMapRemoveTester.class);
		testers.add(Char2BooleanMapRemoveEntryTester.class);
		testers.add(Char2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Char2BooleanMapReplaceTester.class);
		testers.add(Char2BooleanMapReplaceAllTester.class);
		testers.add(Char2BooleanMapReplaceEntryTester.class);
		testers.add(Char2BooleanMapSizeTester.class);
		testers.add(Char2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Boolean>, Map.Entry<Character, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected BooleanCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestBooleanCollectionGenerator generator) {
		return BooleanCollectionTestSuiteBuilder.using(generator);
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

	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}