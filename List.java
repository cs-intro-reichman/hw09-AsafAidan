/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if (this.first == null) {
            return null;
        }
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCD = new CharData(chr);
        Node newN = new Node(newCD, first);
        this.first = newN;
        size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if (this.first == null){
            return "";
        }
        StringBuilder res = new StringBuilder("");
        res.append("(");
        Node currentNode = this.first;
        while (currentNode != null) {
            res.append(currentNode.cp.toString());
            if (currentNode.next != null) {
                res.append(" ");
            }
            currentNode = currentNode.next;
        }
        res.append(")");
        return res.toString();
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int count = 0;
        Node currentNode = this.first;
        while (currentNode != null) {
            if (currentNode.cp.chr == chr) {
                return count;
            }
            count++;
            currentNode = currentNode.next;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        Node currentNode = this.first;
        while (currentNode != null) {
            if (currentNode.cp.chr == chr ) {
                currentNode.cp.count++;
                return;
            }
            currentNode = currentNode.next;
        }
        addFirst(chr);
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
    Node prev = null;
    Node currentNode = this.first;
    while (currentNode != null) {
        if (currentNode.cp.chr == chr) {
            if (prev == null) {
                this.first = currentNode.next;
            } else {
                prev.next = currentNode.next;
            }
            size--; 
            return true; 
        }
        prev = currentNode;
        currentNode = currentNode.next;
    }
    return false; 
}
    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("the problematic index is: " + index);
        }
        Node currentNode = this.first;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next; 
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}