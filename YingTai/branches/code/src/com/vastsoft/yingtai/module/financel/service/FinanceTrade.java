package com.vastsoft.yingtai.module.financel.service;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public interface FinanceTrade {
    /**
     * 划账，此方法未提交数据库操作
     *
     * @param passport  用户身份
     * @param diagnosis 诊断对象
     * @param session   数据库会话
     * @throws BaseException
     */
    public void cashIn(Passport passport, TDiagnosis diagnosis, SqlSession session) throws BaseException;

    /**
     * 冻结资金，本方法不检查权限和用户及机构有效性，请在调用之前检查，本方法将直接对资金进行操作
     *
     * @param passport  用户身份
     * @param diagnosis 诊断对象
     * @param dfPrice   金额
     * @param session   数据库会话
     * @throws BaseException
     */
    public void freezePrice(Passport passport, TDiagnosis diagnosis, double dfPrice, SqlSession session)
            throws BaseException;

//	/**创建一个账户,本方法不进行权限验证和机构验证，请在调用之前检查，本方法将直接对资金进行操作
//	 * @param passport 身份认证
//	 * @param lOrgId 机构ID
//	 * @param session 数据库绘画
//	 * @return 创建的账户信息
//	 * @throws BaseException
//	 */
//	public TFinanceAccount createAccount(Passport passport, long lOrgId, SqlSession session) throws BaseException;

    /**
     * 解冻资金，将资金归还给申请机构账户,本方法不进行权限验证和机构验证，请在调用之前检查，本方法将直接对资金进行操作
     *
     * @param passport  身份认证
     * @param diagnosis 诊断
     * @param session   数据库会话
     * @throws BaseException
     */
    public void unFreezePrice(Passport passport, TDiagnosis diagnosis, SqlSession session) throws BaseException;

}
