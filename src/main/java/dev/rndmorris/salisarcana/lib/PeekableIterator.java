package dev.rndmorris.salisarcana.lib;

import java.util.Iterator;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.NotImplementedException;

import com.google.common.collect.PeekingIterator;

public class PeekableIterator<E> implements PeekingIterator<E> {

    private final @Nonnull Iterator<E> wrappedIterator;

    private boolean peeking = false;
    private E peekedValue = null;

    public PeekableIterator(Iterator<E> wrappedIterator) {
        this.wrappedIterator = wrappedIterator;
    }

    @Override
    public E peek() {
        if (peeking) {
            return peekedValue;
        }
        peekedValue = wrappedIterator.next();
        peeking = true;
        return peekedValue;
    }

    @Override
    public boolean hasNext() {
        return peeking || wrappedIterator.hasNext();
    }

    @Override
    public E next() {
        if (peeking) {
            final var result = peekedValue;
            peekedValue = null;
            peeking = false;
            return result;
        }
        return wrappedIterator.next();
    }

    @Override
    public void remove() {
        throw new NotImplementedException("Not worth implementing right now");
    }
}
