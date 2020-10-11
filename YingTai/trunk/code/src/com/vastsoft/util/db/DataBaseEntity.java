package com.vastsoft.util.db;

import java.io.Serializable;

public interface DataBaseEntity extends Serializable {

	/** 完整性检查 */
	public boolean integrityCheck();

}
