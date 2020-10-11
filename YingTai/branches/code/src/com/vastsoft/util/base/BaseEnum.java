package com.vastsoft.util.base;

/**
 * @author jben
 *
 */
public interface BaseEnum<T extends Enum<?>> {
	
	public int getCode();

	public String getName();
}
