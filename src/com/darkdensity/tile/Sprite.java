package com.darkdensity.tile;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.GridMapManager;
import com.darkdensity.maprender.Grid;
import com.darkdensity.path.Path;
import com.darkdensity.path.SimpleAStarSearch;
import com.darkdensity.path.SubPath;
import com.darkdensity.path.SubPathManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.Direction;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.setting.Constant.SpriteState;
import com.darkdensity.sound.Sound;
import com.darkdensity.util.Pair;

public abstract class Sprite extends Tile {
	protected SpriteState state;
	protected Direction direction;
	protected HashMap<SpriteState, Animation> animations;
	protected Animation currentAnimation;
	protected int x, y; // position
	protected int vx, vy; // velocity
	protected int dx, dy; // destination
	protected int attack, speed;
	protected Tile targetTile;
	private SubPathManager subPathManager;
	protected Sound sound;
	protected String sound_die_path;
	protected String sound_attack_path;
	protected String sound_wlak_path;

	protected int STEP_VELOCITY;
	protected ArrayList<Point> simpleAStarPath;
	protected ArrayList<Point> subPath;
	private Path path;
	protected boolean stop;
	Point nextPoint;
	Point nextCrossPoint;
	protected ExecutorService executor;
	protected Runnable r;
	protected long timerCounter = 0;

	public Sprite() throws IOException {
		super();
		this.x = 0;
		this.y = 0;
		this.dx = 0;
		this.dy = 0;
		this.direction = Direction.SOUTH;
		this.setLocation(x, y);
		this.animations = new HashMap<SpriteState, Animation>();
		// this.state = SpriteState.MOVE;
		this.executor = Executors.newFixedThreadPool(Constant.NTHREDS);
		this.simpleAStarPath = new ArrayList<Point>();
		this.nextPoint = new Point(x, y);
		this.subPathManager = GameWorld.getSubPathManager();
		this.path = new Path();
	}

	protected void initAnimation(String filePath, Boolean friend) {
		
		String highlight = "_highlight_" + ((friend) ? "green" : "red")
				+ ".png";
		
		if(Config.IS_COLOR_BLIND_MODE){
			highlight = "_highlight_" + ((friend) ? "sky" : "orange")
					+ ".png";
		}
		
		animations.put(SpriteState.MOVE, new Animation(
				filePath + "walking.png", filePath + "walking" + highlight, 8,
				8));
		animations.put(SpriteState.DEAD, new Animation(filePath + "dead.png",
				filePath + "dead" + highlight, 8, 1));
		setState(SpriteState.MOVE);
		this.tileWidth = this.getImage().getWidth();
		this.tileHeight = this.getImage().getHeight();
		this.setSize(tileWidth, tileHeight);
	}

	public Point getNextPoint() {
		return nextPoint;
	}

	public void toNextPoint() {
		// System.out.println("toNextPoint"+nextPoint);
		Point thisPoint = new Point(x, y);
		int distanceX = nextPoint.x - getX();
		int distanceY = nextPoint.y - getY();

		// walk a step according to the direction
		if (distanceX == 0 && distanceY >= 0) {
			direction = Direction.SOUTH;
		} else if (distanceX == 0 && distanceY < 0) {
			direction = Direction.NORTH;
		} else if (distanceX > 0 && distanceY == 0) {
			direction = Direction.EAST;
		} else if (distanceX > 0 && distanceY < 0) {
			direction = Direction.NORTH_EAST;
		} else if (distanceX > 0 && distanceY > 0) {
			direction = Direction.SOUTH_EAST;
		} else if (distanceX < 0 && distanceY == 0) {
			direction = Direction.WEST;
		} else if (distanceX < 0 && distanceY < 0) {
			direction = Direction.NORTH_WEST;
		} else if (distanceX < 0 && distanceY > 0) {
			direction = Direction.SOUTH_WEST;
		}
		this.stop = false;

		if (nextPoint.x == x) {
			vx = 0;
			if (Math.abs(nextPoint.y - y) > STEP_VELOCITY)
				vy = (nextPoint.y - y) > 0 ? STEP_VELOCITY
						: ((-1) * STEP_VELOCITY);
			else
				vy = 0;
		} else {
			double atanA = Math.atan((double) (nextPoint.y - y)
					/ (nextPoint.x - x));
			// System.out.println("atanA" + atanA + "vx"+Math.cos(atanA) *
			// STEP_VELOCITY + "vy" + (Math.sin(atanA) * STEP_VELOCITY));
			vx = (int) Math.abs(Math.round(Math.cos(atanA) * STEP_VELOCITY));
			vy = (int) Math.abs(Math.round(Math.sin(atanA) * STEP_VELOCITY));
			vx = (nextPoint.x - x) > 0 ? vx : (-1) * vx;
			vy = (nextPoint.y - y) > 0 ? vy : (-1) * vy;
		}

		// System.out.println("v ("+vx+","+vy+")");
		// System.out.println("distance ("+distanceX+","+distanceY+")");
		Point desPoint = new Point(dx, dy);

		// facing direction
		this.setDirection(direction);
		currentAnimation.setDirection(direction);
		Point prevPosPoint = new Point(x, y);
		x += vx;
		y += vy;
		GridMapManager.gridMap.updateGrid(this, prevPosPoint);
		currentAnimation.setRunning(true);
		currentAnimation.update(System.currentTimeMillis());
	}

	public void stop() {
		// System.out.println("stop");
		this.stop = true;
		vx = 0;
		vy = 0;
		nextPoint = new Point(x, y);
		dx = x;
		dy = y;
		if (subPath != null)
			subPath.clear();
		if (simpleAStarPath != null)
			simpleAStarPath.clear();
		nextCrossPoint = null;
		currentAnimation.stop();
	}

	public Animation getAnimation() {
		return currentAnimation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = this.dx = x;
		this.setLocation(x, y);
		this.nextPoint = new Point(x, y);
	}

	public void setY(int y) {
		this.y = this.dy = y;
		this.setLocation(x, y);
		this.nextPoint = new Point(x, y);
	}

	public int getVx() {
		return vx;
	}

	public int getVy() {
		return vy;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public void setVy(int vy) {
		this.vy = vy;
	}

	@Override
	public float getDefence() {
		return 0;
	}

	public int getDestinationY() {
		return dy;
	}

	public int getDestinationX() {
		return dx;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void update(long elapsedTime) throws IOException { // next point
		if (targetTile != null && isTouched(targetTile)
				&& !targetTile.isDestroyed()) {
//			if(Config.DEBUGMODE)
//				System.out.println("touch the target"+targetTile.getClass().getName());
			stop();
			timerCounter += elapsedTime;
			if (timerCounter > Config.UPDATE_PROGRESS_ELAPSEDTIME) {
				timerCounter = 0;
				tileManager.updateTileProgress(targetTile.getUUID(),
						(role == PlayerRole.SURVIVOR) ? this.attack
								: -this.attack);
			}
			return;
		}

		Point thisPoint = new Point(x, y);
		if(Config.DEBUGMODE){
//			System.out.println("vx"+vx+"vy"+vy);
//			System.out.println("thisPoint in Sprite"+thisPoint);
//			System.out.println("nextPoint in Sprite"+nextPoint);
		}
		if (path.isEnd() || nextPoint == null) {
			stop();
			return;
		}

		/** to update the next point if needed **/
		if (vx == 0 && vy == 0
				|| Math.abs(thisPoint.x - nextPoint.x) <= Math.abs(vx)
				&& Math.abs(thisPoint.y - nextPoint.y) < Math.abs(vy)
				||
				Math.abs(thisPoint.x - nextPoint.x) < Math.abs(vx)
				&& Math.abs(thisPoint.y - nextPoint.y) <= Math.abs(vy)){
		    nextPoint = path.spriteNextPoint(thisPoint);   
		}

		if (nextPoint == null) {
			stop();
			return;
		}

		toNextPoint();
	}

	public void setDestination(final int dx, final int dy) throws IOException {
		// System.out.println("Set Destination call"+" destination: " + dx + " "
		// + dy);
		if (!isDestroyed()) {
			this.dx = dx;
			this.dy = dy;
			Point thisPoint = new Point(x, y);
			Point desPoint = new Point(dx, dy);
		
			if (Math.abs(x - dx) <= 1 && Math.abs(y - dx) <= 1)
				return;

			/** no block between this point and destination point **/
			if (!GridMapManager.gridMap.blockInBetween(thisPoint, desPoint,
					(int) (thisPoint.distance(desPoint)))) {
				// System.out.println("/**go there directly**/");
				path = new Path(thisPoint, desPoint, new ArrayList<Point>());
				return;
			}

			/**
			 * there is block between this point and destination point, so do
			 * the simple Astar Search
			 **/
			 r = new Runnable() {
				@Override
				public void run() {
					SimpleAStarSearch simpleAStarSearch = null;
					try {

					Point thisPoint = new Point((int)x,(int)y);
					Point desPoint = new Point((int)dx,(int)dy);
					//System.out.println("in set destination");
//					System.out.println("************SIMPLE ASTAR***********");
					simpleAStarSearch = new SimpleAStarSearch(thisPoint,desPoint);
					simpleAStarSearch.Search();
					ArrayList<Point> simpleAstarPath = simpleAStarSearch.getSolutionPath();
					ArrayList<Point> simpleAstarPath2 = new ArrayList<Point>();
					int index = 0;
					Pair<Point,Point> pair = subPathManager.findPair(thisPoint);
					if(simpleAstarPath != null){
						for(int i=1;i<simpleAstarPath.size();i++){
							if(pair!= null && (pair.fst.equals(simpleAstarPath.get(i-1)) &&
									pair.snd.equals(simpleAstarPath.get(i))
									||
									pair.snd.equals(simpleAstarPath.get(i-1)) &&
									pair.fst.equals(simpleAstarPath.get(i))) ){
								index = i;
								break;
							}
						}
						for(int i=index;i<simpleAstarPath.size();i++){
							simpleAstarPath2.add(simpleAstarPath.get(i));
						}
					}else{
						simpleAstarPath2 = null;
					}
	//					/** get the path **/
						path = new Path(thisPoint, desPoint, simpleAstarPath2);
						
	
//						System.out.println("simple astar path 2"+simpleAstarPath2);
//						System.out.println("************before***********");
//						System.out.println("PATH:");
//						System.out.println(path.getPath());
					
//						System.out.println("************sprite***********");
//						while(path.isEnd()==false){
//							System.out.println(path.spriteNextPoint(thisPoint) + " ");
//						}
////						
//						
//						System.out.println("************subPathManager change***********");
//						SubPath subPath2 = subPathManager.getPath(new Pair<Point,Point>(simpleAstarPath2.get(0),simpleAstarPath2.get(1)));
//						subPath2.clearPath();
//	//					subPathManager.getSubPaths().clear();
//						
//						System.out.println("************after***********");
//						System.out.println("PATH:");
//						System.out.println(path.getPath());
//						while(path.isEnd()==false){
//							System.out.println(path.spriteNextPoint(thisPoint, false) + " ");
//						}
						
						nextPoint = path.spriteNextPoint(thisPoint);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			};
			executor.execute(r);
		}
		
	}

	// if it is dead
	public void die() {
		setState(SpriteState.DEAD);
		this.destroyed = true;
	}

	boolean isTouched(Tile tile) {
		int sx = tile.getX();
		int sy = tile.getY();
		int ex = tile.getX() + tile.getTileWidth();
		int ey = tile.getY() + tile.getTileHeight();
		int x = this.getX();
		int y = this.getY();
		int width = this.tileWidth;
		int height = this.tileHeight;

		Rectangle rect1 = new Rectangle(sx, sy, ex, ey);
		Rectangle rect2 = new Rectangle(x, y, width, height);
		// determine whether the two tile has intersection
		if (rect1.intersects(rect2)) {
			// determine whether the tile has another tile's points
			if (tile.contains(x - sx, y - sy)
					|| tile.contains(x - sx, y + height - sy)
					|| tile.contains(x - sx + width, y - sy)
					|| tile.contains(x + width - sx, y + height - sy)) {
				return true;
			}
		}
		return false;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}

	public void setState(SpriteState state) {
		this.state = state;
		if (currentAnimation != null)
			currentAnimation.stop();
		this.currentAnimation = animations.get(state);
		this.currentAnimation.setDirection(direction);
	}

	@Override
	public BufferedImage getImage() {
		if (focusing || entered) {
			currentAnimation.focus();
		} else {
			currentAnimation.blur();
		}
		return currentAnimation.getCurrFrame();
	}

	public Tile getTargetTile() {
		return targetTile;
	}

	public void setTargetTile(Tile targetTile) {
		this.targetTile = targetTile;
		timerCounter = 0;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	@Override
	public void progressUpdate(int n) {
		updateHealth(n);
	}

	@Override
	public void processDestory() {
		super.processDestory();
		this.stop();
		executor.shutdownNow();
		this.die();
	}

	public void speak() {
		GameWorld.getSoundManager().getSoundPlayer(sound).play();
	}
	
	public void die_sound(){
		GameWorld.getSoundManager().getSoundPlayer(sound_die_path).play();
	}
}