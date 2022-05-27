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
import speiger.src.collections.chars.maps.abstracts.AbstractChar2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2IntNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2IntMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2IntSortedMapGenerator;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedChar2IntMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestChar2IntSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Char2IntNavigableMap createSubMap(Char2IntSortedMap sortedMap, char firstExclusive, char lastExclusive) {
	    	Char2IntNavigableMap map = (Char2IntNavigableMap) sortedMap;
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
	        return (Char2IntNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestChar2IntSortedMapGenerator {
		TestChar2IntSortedMapGenerator parent;
		Bound to;
		Bound from;
		char firstInclusive;
		char lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestChar2IntSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Char2IntSortedMap map = parent.create();
			entryComparator = DerivedChar2IntMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getCharKey();
			lastInclusive = samples.get(samples.size() - 1).getCharKey();
		}
		
		@Override
		public Char2IntSortedMap create(Entry... elements) {
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
		
		Char2IntSortedMap createSubMap(Char2IntSortedMap map, char firstExclusive, char lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestChar2IntSortedMapGenerator
	{
		TestChar2IntSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestChar2IntSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Char2IntNavigableMap create(Entry... elements) {
			return ((Char2IntNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Character, Integer>> order(List<Map.Entry<Character, Integer>> insertionOrder) {
			ObjectList<Map.Entry<Character, Integer>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestChar2IntMapGenerator
	{
		TestChar2IntMapGenerator parent;
		
		public MapGenerator(TestChar2IntMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Character, Integer>> order(List<Map.Entry<Character, Integer>> insertionOrder) {
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
		public Char2IntMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Char2IntMap.Entry> {
		TestChar2IntMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
			generator = (TestChar2IntMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).char2IntEntrySet();
		}
	}

	public static TestCharSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
		if (inner.getInnerGenerator() instanceof TestChar2IntSortedMapGenerator) {
			CharSet set = ((TestChar2IntSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof CharNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof CharSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestCharNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
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
		TestChar2IntSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
			super(inner);
			generator = (TestChar2IntSortedMapGenerator) inner.getInnerGenerator();
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
		TestChar2IntMapGenerator generator;
		CharSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
			generator = (TestChar2IntMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2IntMap.Entry> samples = generator.getSamples();
			this.samples = new CharSamples(samples.e0().getCharKey(), samples.e1().getCharKey(), samples.e2().getCharKey(), samples.e3().getCharKey(), samples.e4().getCharKey());
		}

		@Override
		public CharSamples getSamples() {
			return samples;
		}

		@Override
		public CharIterable order(CharList insertionOrder) {
			int value = generator.getSamples().e0().getIntValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2IntMap.BasicEntry(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}

		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			int value = generator.getSamples().e0().getIntValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2IntMap.BasicEntry(key, value));
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
				result[index] = new AbstractChar2IntMap.BasicEntry(elements[index++], entry.getIntValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public CharSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2IntMap.BasicEntry((Character) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestIntCollectionGenerator {
		TestChar2IntMapGenerator generator;
		IntSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>> inner) {
			generator = (TestChar2IntMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2IntMap.Entry> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntValue(), samples.e1().getIntValue(), samples.e2().getIntValue(), samples.e3().getIntValue(), samples.e4().getIntValue());
		}
		
		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new IntComparator() {
				@Override
				public int compare(int key, int value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(int entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getIntValue() == entry) return i;
					}
					throw new IllegalArgumentException("Char2IntMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Integer>() {
				@Override
				public int compare(Integer key, Integer value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Integer entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getIntValue() == entry.intValue()) return i;
					}
					throw new IllegalArgumentException("Char2IntMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public IntCollection create(int... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2IntMap.BasicEntry(entry.getCharKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public IntCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2IntMap.BasicEntry(entry.getKey(), (Integer)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Character, Integer>> entryObjectComparator(Comparator<Character> keyComparator) {
		return new Comparator<Map.Entry<Character, Integer>>() {
			@Override
			public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
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