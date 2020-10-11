package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class FileStatus extends SingleClassConstant {

	protected FileStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, FileStatus> mapFileStatus = new HashMap<Integer, FileStatus>();

	public static final FileStatus VALID = new FileStatus(1, "有效");
	public static final FileStatus INVALID = new FileStatus(2, "无效");

	static {
		FileStatus.mapFileStatus.put(VALID.getCode(), VALID);
		FileStatus.mapFileStatus.put(INVALID.getCode(), INVALID);
	}

	public static FileStatus parseCode(int iCode) {
		return FileStatus.mapFileStatus.get(iCode);
	}

	public static List<FileStatus> getAll() {
		return new ArrayList<FileStatus>(FileStatus.mapFileStatus.values());
	}

}
