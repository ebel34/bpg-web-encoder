package org.bellard.bpg.core;


public interface Processor<I, O, E extends Exception> {

	public O process(I data) throws E;

}
