package org.bellard.bpg.core;

import org.bellard.bpg.exception.BusinessException;

public interface BusinessProcessor<I, O> extends Processor<I, O, BusinessException> {

}
