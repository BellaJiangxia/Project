package com.vastsoft.yingtai.module.org.product.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.org.product.entity.FOrgProduct;
import com.vastsoft.yingtai.module.org.product.entity.TOrgProduct;

public interface OrgProductMapper {
	// 产品
	public Integer insertProduct(TOrgProduct tos);

	public TOrgProduct selectProductById(Map<String, Object> prms);

	public TOrgProduct selectProductByIdAndLock(long pid);

	public void modifyProductStatus(Map<String, Object> prms);

	public int selectOrgProductCount(Map<String, Object> mapArg);

	public List<FOrgProduct> selectOrgProduct(Map<String, Object> mapArg);
}
