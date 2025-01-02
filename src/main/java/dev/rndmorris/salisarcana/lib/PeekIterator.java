package dev.rndmorris.salisarcana.lib;

import java.util.Iterator;

import com.google.common.collect.PeekingIterator;

public class PeekIterator<E> implements PeekingIterator<E> {

    private final Iterator<E> iterator;

    private boolean peeking = false;
    private E peekedValue = null;

    public PeekIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        if (peeking) {
            return true;
        }
        return iterator.hasNext();
    }

    public E next() {
        if (peeking) {
            final var result = peekedValue;
            peeking = false;
            peekedValue = null;
            return result;
        }
        return iterator.next();
    }

    public E peek() {
        if (peeking) {
            return peekedValue;
        }
        peeking = true;
        peekedValue = iterator.next();
        return peekedValue;
    }

    @Override
    public void remove() {

    }
}
