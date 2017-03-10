package com.app.data.type;

public class Tree<T> {

  T data;
  Tree<T> firstChild;
  Tree<T> nextSibling;

  public void add(Tree<T> node) {
    if(firstChild == null) {
      firstChild = node;
    } else {
      if(nextSibling == null) {
        nextSibling = node;
      } else {
        nextSibling.add(node);
      }
    }
  }

  public void remove(Tree<T> node) {
    // TODO when needed
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Tree<T> getFirstChild() {
    return firstChild;
  }

  public void setFirstChild(Tree<T> firstChild) {
    this.firstChild = firstChild;
  }

  public Tree<T> getNextSibling() {
    return nextSibling;
  }

  public void setNextSibling(Tree<T> nextSibling) {
    this.nextSibling = nextSibling;
  }


}
