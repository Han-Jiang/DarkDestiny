package com.darkdensity.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * @ClassName: ImageLoader
 * @Description: Load Image
 * @author Team A1,Birmingham University
 * @date 2014/2/1 16:57:41
 * 
 * **************************
 * Attributes:
 * private static Map<String, Image> IMAGE_CACHE = new HashMap<String, Image>();
 * **************************
 * Methods:
 * public static Image loadImage(String filePath) {}
 */
public class ImageLoader {
	// cache for the images
	private static Map<String, Image> IMAGE_CACHE = new HashMap<String, Image>();
	
	
	/**
	 * 
	* @Title: loadImage 
	* @Description: load image file with cache 
	* @param String filePath
	* @return Image image that required
	* @throws
	 */
	public static Image loadImage(String filePath) {
		if (!IMAGE_CACHE.containsKey(filePath)) {
			Image image = new ImageIcon(filePath).getImage();
			if (image != null) {
				IMAGE_CACHE.put(filePath, image);
			} else {
				return null;
			}
		}
		return IMAGE_CACHE.get(filePath);
	}
	
	/**
	* @Title: resize 
	* @Description: resize the image and return a buffered image
	* @param Image srcImg image to be resized
	* @param int  w  height after resized
	* @param int  h  width after resized
	* @return Image    
	* @throws
	 */
	public static Image resize(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	public static ImageIcon getImageIcon(String filePath){
		ImageIcon imageIcon = new ImageIcon(filePath);
		return imageIcon;
	}
}
