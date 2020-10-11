package com.vastsoft.util.vcg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * VCG Verify Code Generator 验证码生成器
 * 
 * @author JoeRadar
 * 
 */
public class VcgService
{
	public final static VcgService instance = new VcgService();

	private VcgService()
	{
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param iCharCount
	 *            字符串长度
	 * @param bNumberOnly
	 *            是否是纯数字
	 * @return 随机字符串
	 */
	public String genRandomCode(int iCharCount, boolean bNumberOnly)
	{
		final char[] arrayPureDigtal =
		{ '7', '2', '5', '3', '9', '1', '6', '8', '4' ,'0'};

		final char[] arrayBaseChars =
		{ '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
				'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'm', 'n',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

		int iCntOfArray = arrayBaseChars.length;
		char[] arrayVerCodes = new char[iCharCount];

		Random random = new Random();
		for (int i = 0; i < iCharCount; i++)
		{
			if (bNumberOnly)
				arrayVerCodes[i] = arrayPureDigtal[random.nextInt(10)];
			else
				arrayVerCodes[i] = arrayBaseChars[random.nextInt(iCntOfArray)];
		}

		return String.valueOf(arrayVerCodes);
	}

	/**
	 * 根据字符串生成图片
	 * 
	 * @param strVerifyCode
	 *            可见字符串
	 * @param iWidth
	 *            图片宽度
	 * @param iHeight
	 *            图片高度
	 * @param bDisturbBackground
	 *            是否扰乱背景
	 * @return 图片对象
	 */
	public Image genImage(String strVerifyCode, int iWidth, int iHeight, Color clrBk, boolean bDisturbBackground)
	{
		int iWidthPerChar = iWidth / strVerifyCode.length() - 1;
		int iFontHeight = iHeight - 8;

		// 验证码图像
		// BufferedImage buffVcImg = new BufferedImage(iWidth, iHeight,
		// BufferedImage.TYPE_INT_ARGB);
		// Graphics2D g2dVc = buffVcImg.createGraphics();
		// g2dVc.setBackground(Color.WHITE);
		// buffVcImg =
		// g2dVc.getDeviceConfiguration().createCompatibleImage(iWidth, iHeight,
		// Transparency.TRANSLUCENT);
		// g2dVc.dispose();
		// g2dVc = buffVcImg.createGraphics();

		// 最终图像
		BufferedImage buffImg = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = buffImg.createGraphics();

		// 填充背景色
		g2d.setColor(new Color(0xee, 0xee, 0xee));
		g2d.fillRect(0, 0, iWidth, iHeight);

		// 画边框。
		// graphic.setColor(Color.BLACK);
		// graphic.drawRect(0, 0, iWidth - 1, iHeight - 1);

		// 平滑绘图
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Random random = new Random();

		if (bDisturbBackground)
		{
			// 设置干扰线条的宽度
			BasicStroke arrayStoke[] =
			{ new BasicStroke(0.5f), new BasicStroke(1.0f), new BasicStroke(0.5f), new BasicStroke(1.5f) };

			// 随机产生干扰线，使图象中的认证码不易被其它程序探测到。
			for (int i = 0; i < 20; i++)
			{
				g2d.setStroke(arrayStoke[random.nextInt(4)]);

				// 用随机产生的颜色将验证码绘制到图像中。
				g2d.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));

				int x = random.nextInt(iWidth);
				int y = random.nextInt(iHeight);
				int xl = random.nextBoolean() ? random.nextInt(15) : -random.nextInt(15);
				int yl = random.nextBoolean() ? random.nextInt(10) : -random.nextInt(10);
				g2d.drawLine(x, y, x + xl, y + yl);
			}
		}

		String[] arrayStrFont =
		{ "courier", "arial", "sans", "serif", "monospace" };
		int[] arrayStyle =
		{ Font.PLAIN, Font.BOLD, Font.ITALIC, Font.ITALIC | Font.BOLD };

		// 绘制字符串
		for (int i = 0; i < strVerifyCode.length(); i++)
		{
			// 设置字体。
			Font fontTxt = new Font(arrayStrFont[random.nextInt(5)], arrayStyle[random.nextInt(4)], iFontHeight);
			g2d.setFont(fontTxt);

			String strRand = String.valueOf(strVerifyCode.charAt(i));
			// 产生随机的颜色分量来构造颜色值。
			int valRed = random.nextInt(200);
			int valGreen = random.nextInt(50);
			int valBlue = random.nextInt(50);

			// 用随机产生的颜色将验证码绘制到图像中。
			g2d.setColor(new Color(valRed, valGreen, valBlue));

			// 旋转文字
			double dfAngle = (random.nextInt(12) - 6) * (Math.PI / 180.0f);
			g2d.rotate(dfAngle, i * iWidthPerChar, iHeight - 5);

			// 绘制文字
			g2d.drawString(strRand, i * iWidthPerChar + 1, iHeight - 5);

			// 撤销旋转
			g2d.rotate(-dfAngle);
		}

		g2d.dispose();

		// buffVcImg = this.twistImage(buffVcImg);
		// g2d.drawImage(buffImg, null, 0, 0);

		return buffImg;
	}

	/**
	 * 
	 * @Description:正弦曲线Wave扭曲图片
	 * @since 1.0.0
	 * @Date:2012-3-1 下午12:49:47
	 * @return BufferedImage
	 */
	@SuppressWarnings("unused")
	private BufferedImage twistImage(BufferedImage buffImg)
	{
		Random random = new Random();

		double dMultValue = random.nextInt(8) + 3;// 波形的幅度倍数，越大扭曲的程序越高，一般为3
		double dPhase = random.nextInt(8);// 波形的起始相位，取值区间（0-2＊PI）

		BufferedImage destBi = new BufferedImage(buffImg.getWidth(), buffImg.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < destBi.getWidth(); i++)
		{
			for (int j = 0; j < destBi.getHeight(); j++)
			{
				int nOldX = getXPosition4Twist(dPhase, dMultValue, destBi.getHeight(), i, j);
				int nOldY = j;
				if (nOldX >= 0 && nOldX < destBi.getWidth() && nOldY >= 0 && nOldY < destBi.getHeight())
				{
					destBi.setRGB(nOldX, nOldY, buffImg.getRGB(i, j));
				}
			}
		}
		return destBi;
	}

	/**
	 * 
	 * @Description:获取扭曲后的x轴位置
	 * @since 1.0.0
	 * @Date:2012-3-1 下午3:17:53
	 * @param dPhase
	 * @param dMultValue
	 * @param height
	 * @param xPosition
	 * @param yPosition
	 * @return int
	 */
	private int getXPosition4Twist(double dPhase, double dMultValue, int height, int xPosition, int yPosition)
	{
		double PI = 3.1415926535897932384626433832799; // 此值越大，扭曲程度越大
		double dx = (PI * yPosition) / height + dPhase;
		double dy = Math.sin(dx);

		return xPosition + (int) (dy * dMultValue);
	}

	/**
	 * 测试用主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		VcgService srv = new VcgService();

		String str = srv.genRandomCode(4, false);

		Image img = srv.genImage(str, 90, 25, new Color(0xee, 0xee, 0xee), true);

		File file = new File("d:\\test.bmp");
		try
		{
			ImageIO.write((RenderedImage) img, "bmp", file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
