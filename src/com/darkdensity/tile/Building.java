package com.darkdensity.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.PlayerRole;

public class Building extends Tile {
	private String buildingName;
	private BufferedImage[] bufferedImages;
	private int imageOffset;
	private JPanel hintsPanel;
	private boolean isShowHintsPanel;
	private BufferedImage hintsPanelBI; 
	private Resource resource;
	private int progress;
	private int progressMaximum;
	
	public Building() {
		super();
//		hintsPanel = new JPanel();
//		hintsPanel.setSize(100, 100);
//		hintsPanel.setLocation(0, 0);
//		hintsPanel.setVisible(false);
//		hintsPanel.setBackground(Color.RED);
//		add(hintsPanel);
//		this.isShowHintsPanel = false;
//		this.hintsPanelBI = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		this.progressMaximum = 100;
		this.role = PlayerRole.NEUTRAL;
		this.reveal = 0;
	}

	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * 
	* @Title: initBuildingImage 
	* @Description: initial the building image 
	* @param @param buildingName
	* @param @throws IOException
	* @return void    
	* @throws
	 */
	public void initBuildingImage(String buildingName) throws IOException {
		this.buildingName = buildingName;
		this.bufferedImages = Constant.getFrames(Config.BUILDING_PATH
				+ buildingName + ".gif", Config.BUILDING_PATH + buildingName
				+ "_highlight.gif", 1, 1);
		this.imageOffset = 1;
		this.tileHeight = bufferedImages[0].getHeight();
		this.tileWidth = bufferedImages[0].getWidth();
		this.setSize(tileWidth, tileHeight);
	}

	/**
	 * 
	* @Title: setBuildingName 
	* @Description: TODO(What the method do) 
	* @param @param buildingName
	* @return void    
	* @throws
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	@Override
	public BufferedImage getImage() {
		return bufferedImages[((focusing || entered) ? imageOffset : 0)];
	}


	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
		this.progress = 50;
	}
	
	public void clearResurce(){
		this.resource = null;
	}
	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getProgressMaximum() {
		return progressMaximum;
	}

	public void setProgressMaximum(int progressMaximum) {
		this.progressMaximum = progressMaximum;
	}

	@Override
	public void progressUpdate(int n) {
		if(this.resource != null){
			progress += n/10;
			if(progress >= progressMaximum){
				progress = progressMaximum;
				if(Config.IS_SERVER){
					tileManager.ScavengeSupplies(this.UUID, resource.getFood(), resource.getWood(), resource.getIron(), resource.getSurvivor(), PlayerRole.SURVIVOR);
				}
				this.resource = null;
			} else if (progress < 0){
				progress = 0;
				if(Config.IS_SERVER){
					tileManager.ScavengeSupplies(this.UUID, 0, 0, 0, resource.getSurvivor(), PlayerRole.ZOMBIE);
				}
				this.resource = null;
			}
		}
	}

	@Override
	public void destroyTile() {
	}
}
