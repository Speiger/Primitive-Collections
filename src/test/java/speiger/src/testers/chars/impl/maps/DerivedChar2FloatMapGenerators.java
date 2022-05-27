package speiger.src.testers.chars.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2FloatNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatSortedMap;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2FloatMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2FloatSortedMapGenerator;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedChar2FloatMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestChar2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Char2FloatNavigableMap createSubMap(Char2FloatSortedMap sortedMap, char firstExclusive, char lastExclusive) {
	    	Char2FloatNavigableMap map = (Char2FloatNavigableMap) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Char2FloatNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestChar2FloatSortedMapGenerator {
		TestChar2FloatSortedMapGenerator parent;
		Bound to;
		Bound from;
		char firstInclusive;
		char lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestChar2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Char2FloatSortedMap map = parent.create();
			entryComparator = DerivedChar2FloatMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getCharKey();
			lastInclusive = samples.get(samples.size() - 1).getCharKey();
		}
		
		@Override
		public Char2FloatSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getCharKey(), parent.aboveSamplesLesser().getCharKey());
		}
		
		Char2FloatSortedMap createSubMap(Char2FloatSortedMap map, char firstExclusive, char lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestChar2FloatSortedMapGenerator
	{
		TestChar2FloatSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestChar2FloatSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Char2FloatNavigableMap create(Entry... elements) {
			return ((Char2FloatNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Character, Float>> order(List<Map.Entry<Character, Float>> insertionOrder) {
			ObjectList<Map.Entry<Character, Float>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestChar2FloatMapGenerator
	{
		TestChar2FloatMapGenerator parent;
		
		public MapGenerator(TestChar2FloatMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Character, Float>> order(List<Map.Entry<Character, Float>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Char2FloatMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Char2FloatMap.Entry> {
		TestChar2FloatMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
			generator = (TestChar2FloatMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).char2FloatEntrySet();
		}
	}

	public static TestCharSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
		if (inner.getInnerGenerator() instanceof TestChar2FloatSortedMapGenerator) {
			CharSet set = ((TestChar2FloatSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof CharNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof CharSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestCharNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
			super(inner);
		}
		
		@Override
		public CharNavigableSet create(char... elements) {
			return (CharNavigableSet) super.create(elements);
		}

		@Override
		public CharNavigableSet create(Object... elements) {
			return (CharNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestCharSortedSetGenerator {
		TestChar2FloatSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
			super(inner);
			generator = (TestChar2FloatSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public CharSortedSet create(char... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public CharSortedSet create(Object... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public char belowSamplesLesser() {
			return generator.belowSamplesLesser().getCharKey();
		}

		@Override
		public char belowSamplesGreater() {
			return generator.belowSamplesGreater().getCharKey();
		}

		@Override
		public char aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getCharKey();
		}

		@Override
		public char aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getCharKey();
		}
	}

	public static class MapKeySetGenerator implements TestCharSetGenerator {
		TestChar2FloatMapGenerator generator;
		CharSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
			generator = (TestChar2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new CharSamples(samples.e0().getCharKey(), samples.e1().getCharKey(), samples.e2().getCharKey(), samples.e3().getCharKey(), samples.e4().getCharKey());
		}

		@Override
		public CharSamples getSamples() {
			return samples;
		}

		@Override
		public CharIterable order(CharList insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2FloatMap.BasicEntry(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}

		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2FloatMap.BasicEntry(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}
		
		@Override
		public CharSet create(char... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2FloatMap.BasicEntry(elements[index++], entry.getFloatValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public CharSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2FloatMap.BasicEntry((Character) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestFloatCollectionGenerator {
		TestChar2FloatMapGenerator generator;
		FloatSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>> inner) {
			generator = (TestChar2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatValue(), samples.e1().getFloatValue(), samples.e2().getFloatValue(), samples.e3().getFloatValue(), samples.e4().getFloatValue());
		}
		
		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new FloatComparator() {
				@Override
				public int compare(float key, float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry)) return i;
					}
					throw new IllegalArgumentException("Char2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Float>() {
				@Override
				public int compare(Float key, Float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry.floatValue())) return i;
					}
					throw new IllegalArgumentException("Char2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public FloatCollection create(float... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2FloatMap.BasicEntry(entry.getCharKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public FloatCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2FloatMap.BasicEntry(entry.getKey(), (Float)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Character, Float>> entryObjectComparator(Comparator<Character> keyComparator) {
		return new Comparator<Map.Entry<Character, Float>>() {
			@Override
			public int compare(Map.Entry<Character, Float> a, Map.Entry<Character, Float> b) {
				if(keyComparator == null) {
					return Character.compare(a.getKey().charValue(), b.getKey().charValue());
				}
				return keyComparator.compare(a.getKey().charValue(), b.getKey().charValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(CharComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Character.compare(a.getCharKey(), b.getCharKey());
				}
				return keyComparator.compare(a.getCharKey(), b.getCharKey());
			}
		};
	}
}