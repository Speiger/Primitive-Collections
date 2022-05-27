package speiger.src.testers.chars.utils;


import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharSet;

@SuppressWarnings("javadoc")
public class MinimalCharSet extends MinimalCharCollection implements CharSet {
	public static MinimalCharSet of(char...array) {
		return MinimalCharSet.of(CharArrayList.wrap(array));
	}
	
	public static MinimalCharSet of(CharIterable iterable)
	{
		CharList list = new CharArrayList();
		for(CharIterator iter = iterable.iterator();iter.hasNext();)
		{
			char key = iter.nextChar();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalCharSet(list.toCharArray(new char[list.size()]));
	}
	
	protected MinimalCharSet(char[] contents) {
		super(contents);
	}
	
	@Override
	public CharSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CharSet) {
			CharSet that = (CharSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (CharIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Character.hashCode(iter.nextChar());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(char o) {
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(iter.nextChar() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}