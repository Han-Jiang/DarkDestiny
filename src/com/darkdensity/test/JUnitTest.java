package com.darkdensity.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.darkdensity.setting.Config;

public class JUnitTest {
	
	@Test
	public void MapTest() {
//		Map map = new Map("BigMap");
//		assertEquals(4864, map.getMapWidthPx());
//		assertEquals(3840, map.getMapHeightPx());
	}
	
	
	@Test
	public void ConfigTest() {
		
		assertEquals(1024, Config.PANEL_HEIGHT);
		assertEquals(1024,Config. PANEL_WIDTH);
		assertEquals(768,Config.PANEL_HEIGHT);
		assertEquals(20,Config. SENSITIVE_AREA );
		assertEquals(30,Config. SCREEN_MOVE_SPEED);
		assertEquals(true,Config.IS_FULL_SCREEN);
		assertEquals("com.darkdensity.setting.NLS",Config.NLS_NAME);
		
	}
	
	

}
