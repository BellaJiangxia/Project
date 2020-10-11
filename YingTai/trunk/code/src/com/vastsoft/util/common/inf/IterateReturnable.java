package com.vastsoft.util.common.inf;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;
import com.vastsoft.util.exception.ReturnIterateException;

public interface IterateReturnable<T> {
	public void run(T t) throws ReturnIterateException, BreakIterateException, BaseException;
}
