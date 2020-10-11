package com.vastsoft.yingtai.module.returnvisit;

import com.vastsoft.util.common.SingleClassConstant;

public class QuestionType extends SingleClassConstant
{
	protected QuestionType(int iCode, String strName)
	{
		super(iCode, strName);
	}

	public final static QuestionType SINGLE_CHECK = new QuestionType(1, "单选");
	public final static QuestionType MULTI_CHECK = new QuestionType(2, "多选");
	public final static QuestionType ANSWER_TEXT = new QuestionType(3, "回答");
	public final static QuestionType ANSWER_SCORE = new QuestionType(4, "打分");

	public static QuestionType parseCode(int iCode)
	{
		if (iCode == 1)
			return QuestionType.SINGLE_CHECK;
		else if (iCode == 2)
			return QuestionType.MULTI_CHECK;
		else if (iCode == 3)
			return QuestionType.ANSWER_TEXT;
		else if (iCode == 4)
			return QuestionType.ANSWER_SCORE;
		else
			return null;
	}
}
