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
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ByteMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2ByteMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2ByteMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Char2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Byte> {
	public static Char2ByteMapTestSuiteBuilder using(TestChar2ByteMapGenerator generator) {
		return (Char2ByteMapTestSuiteBuilder) new Char2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2ByteMapClearTester.class);
		testers.add(Char2ByteMapComputeTester.class);
		testers.add(Char2ByteMapComputeIfAbsentTester.class);
		testers.add(Char2ByteMapComputeIfPresentTester.class);
		testers.add(Char2ByteMapSupplyIfAbsentTester.class);
		testers.add(Char2ByteMapContainsKeyTester.class);
		testers.add(Char2ByteMapContainsValueTester.class);
		testers.add(Char2ByteMapEntrySetTester.class);
		testers.add(Char2ByteMapEqualsTester.class);
		testers.add(Char2ByteMapForEachTester.class);
		testers.add(Char2ByteMapGetTester.class);
		testers.add(Char2ByteMapGetOrDefaultTester.class);
		testers.add(Char2ByteMapHashCodeTester.class);
		testers.add(Char2ByteMapIsEmptyTester.class);
		testers.add(Char2ByteMapMergeTester.class);
		testers.add(Char2ByteMapPutTester.class);
		testers.add(Char2ByteMapAddToTester.class);
		testers.add(Char2ByteMapPutAllTester.class);
		testers.add(Char2ByteMapPutAllArrayTester.class);
		testers.add(Char2ByteMapPutIfAbsentTester.class);
		testers.add(Char2ByteMapRemoveTester.class);
		testers.add(Char2ByteMapRemoveEntryTester.class);
		testers.add(Char2ByteMapRemoveOrDefaultTester.class);
		testers.add(Char2ByteMapReplaceTester.class);
		testers.add(Char2ByteMapReplaceAllTester.class);
		testers.add(Char2ByteMapReplaceEntryTester.class);
		testers.add(Char2ByteMapSizeTester.class);
		testers.add(Char2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Byte>, Map.Entry<Character, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected ByteCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestByteCollectionGenerator generator) {
		return ByteCollectionTestSuiteBuilder.using(generator);
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