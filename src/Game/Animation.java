package Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Animation{
	public BufferedImage[][]spriteSheet;

	private int frameCount=0;
	int xpos =0;
	int ypos =0;
	int xScale = 1;
	int yScale = 1;
	int xmove;
	int ymove;
	int delay = 0;
	int index = 0;
	private int currentFrame = 0;
	int cutWidth = 0;
	private long timeForNextFrame=0;
	private long LastFrameTime = 0;
	boolean additive = false;
	boolean loopAnim = false;
	public Animation(BufferedImage[][] sprites, int numFrames, int frameDelay, int spriteIndex, int x, int y, boolean loop, int xIncreasePerFrame, int yIncreasePerFrame){
		xmove = xIncreasePerFrame;
		ymove = yIncreasePerFrame;

		spriteSheet=sprites;
		index = spriteIndex;
		frameCount=numFrames;
		delay=frameDelay;
		xpos=x;
		ypos=y;
		loopAnim=loop;
		//LastFrameTime=System.currentTimeMillis();
	}

	public void update(){

		if(this.timeForNextFrame <= System.currentTimeMillis()){
			this.currentFrame++;
			ypos=ypos+xmove;
			xpos=xpos+ymove;
			if(this.currentFrame>=this.frameCount){
				if(loopAnim){
					this.currentFrame=0;
				}
				else{
					//this.currentFrame=this.frameCount-1;

				}
			}
			//LastFrameTime = System.currentTimeMillis();
			timeForNextFrame = LastFrameTime + delay;
		}
	}
	public int getCurrentFrame(){
		return this.currentFrame;
	}
	public void updatePosition(int x, int y){
		this.xpos=x;
		this.ypos=y;
	}
	public int getFrameCount(){
		return this.frameCount;
	}
	public void Draw(Graphics g){

		this.update();
		//System.out.println("FrameCount="+this.frameCount);

		//if(this.LastFrameTime+delay >= System.currentTimeMillis()){
		if(this.currentFrame<this.frameCount){
			if(!additive){
			g.drawImage(spriteSheet[currentFrame][index], xpos, ypos, spriteSheet[currentFrame][0].getWidth()*xScale, spriteSheet[currentFrame][0].getHeight()*yScale, null);
			}
			else{
				for(int i = currentFrame; i>0;i--){
					g.drawImage(spriteSheet[i][index], xpos+(i*(spriteSheet[currentFrame][0].getWidth()*xScale)), ypos, spriteSheet[currentFrame][0].getWidth()*xScale, spriteSheet[currentFrame][0].getHeight()*yScale, null);
				}
			}


			this.LastFrameTime=System.currentTimeMillis();
		}
		//}

	}
}
