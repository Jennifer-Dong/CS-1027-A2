/**
 * 
 * @author rdong26
 *This class implements a stack using an array.
 * @param <T>
 */
public class ArrayStack<T> implements ArrayStackADT<T> {
	private T[] stack;
	public static String sequence;
	private int top;

	public ArrayStack() {
		sequence = "";
		stack = (T[]) new Object[14];
		top = -1;
	}
	
	/**
	 * 
	 * @param initialCapacity --> This is a given initial capacity for the array
	 */
	public ArrayStack(int initialCapacity) {
		sequence = "";
		stack  = (T[]) new Object[initialCapacity];
		top = -1;
	}
	
	/**  Adds one element to the top of this stack. 
	*   @param dataItem data item to be pushed onto stack
	*/
	public void push(T dataItem) {
		if(size()==stack.length) {
			int bigSize;
			if (stack.length < 50) {
				bigSize = size()+10;
			} else {
				bigSize = size()*2;
			}
			T[] newStack = (T[]) new Object[bigSize];
			for (int i=0; i<size(); i++) {
				newStack[i] = stack[i];
			}
			
			stack = (T[]) new Object[bigSize];
			for (int i=0; i<size(); i++) {
				stack[i] = newStack[i];
			}
		}
		stack[++top] = dataItem;
		
		if (dataItem instanceof MapCell) {
			sequence += "push" + ((MapCell)dataItem).getIdentifier();
		} else {
			sequence += "push" + dataItem.toString();
		}
	}
	
	/**  Removes and returns the top element from this stack. 
	*   @return T data item removed from the top of the stack
	*/
	public T pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("Stack");
		}
		T result = stack[top];
		stack[top]=null;
		top--;
		
		if (size()<(stack.length)/4) {
			if (stack.length <=28) {
				T[] newStack = (T[]) new Object[14];
				for (int i=0; i<size();i++) {
					newStack[i] = stack[i];
				}
				stack = (T[]) new Object[newStack.length];
				for (int i=0; i<size();i++) {
					stack[i] = newStack[i];
				}
			} else {
				T[] newStack = (T[]) new Object[stack.length/2];
				for (int i=0; i<size();i++) {
					newStack[i] = stack[i];
				}
				stack = (T[]) new Object[newStack.length];
				for (int i=0; i<size();i++) {
					stack[i] = newStack[i];
				}
			}
		}
		
		if (result instanceof MapCell) {
			sequence += "pop" + ((MapCell)result).getIdentifier();
		} else {
			sequence += "pop" + result.toString();
		}
		
		return result;
	}
	
	/**  Returns without removing the top element of this stack. 
	*   @return T data item on top of the stack
	*/
	public T peek() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("Stack");
		}
		return stack[top];
	}

	/**  Returns true if this stack contains no elements. 
	*   @return true if the stack is empty; false otherwise 
	*/
	public boolean isEmpty() {
		if (top==-1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**  Returns the number of data items in this stack. 
	*   @return int number of data items in this stack
	*/
	public int size() {
		return top+1;
	}
	
	/** Returns the length of this stack.
	 * 
	 * @return int length of this stack.
	 */
	public int length() {
		return stack.length;
	}
	
	/**  Returns a string representation of this stack. 
	*   @return String representation of this stack
	*/
	public String toString() {
		String str = "Stack: ";
		str += stack[0];
		for (int i=1;i<size();i++) {
			str+=", "+stack[i];
		}
		return str;
	}
}
