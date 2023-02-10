package assignment2;

import java.util.NoSuchElementException;

/**
 *
 * This is a generic class representing a list of objects.
 * The operations on the list are as follows:
 * - adding and removing elements from the left and from the right.
 * - reversing the list
 * - obtaining the middle element
 * - getting the size of the list
 * 
 * **All operations must run in O(1) time.**
 */
public class MyLinkedList<T> {
	private Node<T> head;
	private Node<T> tail;
	private Node<T> mid;
	private int length;
	private boolean reverse;
	private final String RIGHT = "Right";
	private final String LEFT = "Left";

	static class Node<T> {
		T data;
		Node<T> next;
		Node<T> prev;

		Node(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}

	/**
	 * The constructor creates an empty list
	 */
	public MyLinkedList() {
		this.head = null;
		this.tail = null;
		this.mid = null;
		this.length = 0;
		this.reverse = false;
	}

	/**
	 * Adds the new item to the left of the list. 
	 */
	public void addLeft(T item) {
		String dir = LEFT;
		if (this.reverse) {
			dir = RIGHT;
		}
		addItem(item, dir);
	}

	/**
	 * Adds the new item to the right of the list. 
	 */
	public void addRight(T item) {
		String dir = RIGHT;
		if (this.reverse) {
			dir = LEFT;
		}
		addItem(item, dir);
	}

	/**
	 *
	 * @param item
	 * @param direction
	 * @throws IllegalArgumentException
	 */
	private void addItem(T item, String direction) throws IllegalArgumentException{
		if (direction != RIGHT && direction != LEFT) {
			throw new IllegalArgumentException();
		}
		Node<T> newNode = new Node<>(item);
		this.length++;
		if (direction == RIGHT) {
			if (this.tail == null) {
				this.head = newNode;
				this.tail = newNode;
				this.mid = newNode;
			} else {
				newNode.prev = this.tail;
				this.tail.next = newNode;
				this.tail = newNode;

				if ((this.length % 2) == 0) {
					this.mid = this.mid.next;
				}
			}
		} else if (direction == LEFT) {
			if (this.head == null) {
				this.head = newNode;
				this.tail = newNode;
				this.mid = newNode;
			} else {
				newNode.next = this.head;
				this.head.prev = newNode;
				this.head = newNode;

				if (this.length == 2) {
					return;
				}
				if ((this.length % 2) != 0) {
					this.mid = this.mid.prev;
				}
			}
		}
	}

	/**
	 * Removes the leftmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeLeft() throws NoSuchElementException {
		String dir = LEFT;
		if (this.reverse) {
			dir = RIGHT;
		}
		try {
			return removeItem(dir);
		} catch (NoSuchElementException e) {	// Rethrow exception
			throw new NoSuchElementException();
		}
	}

	/**
	 * Removes the rightmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeRight() throws NoSuchElementException{
		String dir = RIGHT;
		if (this.reverse) {
			dir = LEFT;
		}
		try {
			return removeItem(dir);
		} catch (NoSuchElementException e) {	// rethrow exception
			throw new NoSuchElementException();
		}
	}

	/**
	 *
	 * @param direction
	 * @return
	 * @throws NoSuchElementException
	 * @throws IllegalArgumentException
	 */
	private T removeItem(String direction) throws NoSuchElementException, IllegalArgumentException {
		T retVal = null;
		if (direction != RIGHT && direction != LEFT) {
			throw new IllegalArgumentException();
		}
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		this.length--;

		if (direction == RIGHT) {
			retVal = this.tail.data;
			if (this.tail.prev != null) {	// Check if prev node exists before shifting to it
				this.tail = this.tail.prev;
				this.tail.next = null;
				if (this.length % 2 != 0) {
					this.mid = this.mid.prev;
				}
			} else {						// LinkedList is empty
				this.tail = null;
				this.head = null;
				this.mid = null;
			}
		} else if (direction == LEFT) {
			retVal = this.head.data;
			if (this.head.next != null) {	// Check if next node exists before shifting to it
				this.head = this.head.next;
				this.head.prev = null;
				if ((this.length % 2) == 0) {
					this.mid = this.mid.next;
				}
			} else {						// LinkedList is empty
				this.head = null;
				this.mid = null;
				this.tail = null;
			}
		}
		return retVal;
	}


	/**
	 * Reverses the list
	 */
	public void reverse() {
		if (this.reverse) {
			this.reverse = false;
			if (this.length > 0) {
				if (this.length % 2 == 0) {
					this.mid = this.mid.next;
				}
			}
		} else {
			this.reverse = true;
			if (this.length > 0) {
				if (this.length % 2 == 0) {
					this.mid = this.mid.prev;
				}
			}
		}


	}

	/**
	 * Returns the item in the middle of the list.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T getMiddle() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return this.mid.data;
	}

	/**
	 * Returns the size of the list.
	 */
	public int size() {
		return this.length;
	}

	/**
	 * Returns true if list is empty, and returns false otherwise.
	 */
	public boolean isEmpty() {
		if (this.length == 0) {
			return true;
		}
		return false;
	}

}
