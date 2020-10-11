package com.vastsoft.yingtai.action;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.opensymphony.xwork2.ActionContext;
import com.vastsoft.util.vcg.VcgService;
import com.vastsoft.yingtai.core.BaseYingTaiAction;

/**
 * @author jyb
 */
public class VcAction extends BaseYingTaiAction
{
	/**
	 * 验证码的用处，由前端传入。将导致，验证码在session存放不同的key里。
	 */
	private String strVcType = null;

	public void setVcType(String strVcType)
	{
		this.strVcType = strVcType;
	}

	/**
	 * 获取验证码图片
	 * 
	 * 必须参数 vcType : 验证码用途，取值范围 : “login”
	 * 
	 * @return
	 */
	public String execute()
	{
		return "vc";
	}

	public InputStream getImageStream() throws Exception
	{
		if (this.strVcType != null)
		{
			String strVc = VcgService.instance.genRandomCode(4, true);
			RenderedImage imgVc = (RenderedImage) VcgService.instance.genImage(strVc, 90, 30, new Color(0xee, 0xee, 0xee), true);

			ByteArrayOutputStream osBa = new ByteArrayOutputStream();
			ImageOutputStream osImg = ImageIO.createImageOutputStream(osBa);

			ImageIO.write(imgVc, "jpeg", osImg);

			ByteArrayInputStream isBa = new ByteArrayInputStream(osBa.toByteArray());

			if (this.strVcType.equalsIgnoreCase("login"))
			{
				ActionContext.getContext().getSession().put("login_vc", strVc);
			}

			return isBa;
		}
		else
		{
			return null;
		}
	}

}
