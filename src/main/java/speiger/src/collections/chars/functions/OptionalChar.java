package speiger.src.collections.chars.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalChar {
	private static final OptionalChar EMPTY = new OptionalChar();
	
	private final boolean isPresent;
	private final char value;
	
	private OptionalChar() {
		this.isPresent = false;
		this.value = (char)0;
	}
	
	private OptionalChar(char value) {
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalChar empty() {
		return EMPTY;
	}
	
	public static OptionalChar of(char value) {
		return new OptionalChar(value);
	}
	
	public char getAsChar() {
		if(!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isEmpty() {
		return !isPresent;
	}
	
	public void ifPresent(CharConsumer consumer) {
		if(isPresent) consumer.accept(value);
	}
	
    public void ifPresentOrElse(CharConsumer action, Runnable emptyAction) {
        if (isPresent) action.accept(value);
        else emptyAction.run();
    }
	
	public char orElse(char other) {
		return isPresent ? value : other;
	}
	
	public char orElseGet(CharSupplier other) {
		return isPresent ? value : other.getAsChar();
	}
	
	public char orElseThrow() {
        if (!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
    public <X extends Throwable> char orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if(isPresent) return value;
    	else throw exceptionSupplier.get();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == this) return true;
    	if(obj instanceof OptionalChar) {
    		OptionalChar other = (OptionalChar)obj;
    		return (isPresent && other.isPresent ? value == other.value : isPresent == other.isPresent);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return isPresent ? Character.hashCode(value) : 0;
    }
    
    @Override
    public String toString() {
    	return isPresent ? "OptionalChar["+value+"]" : "OptionalChar.empty";
    }
}