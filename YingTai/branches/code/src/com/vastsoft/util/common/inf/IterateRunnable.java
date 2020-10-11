package com.vastsoft.util.common.inf;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;

public interface IterateRunnable<T> {
	public void run(T t) throws BreakIterateException, BaseException;
}
