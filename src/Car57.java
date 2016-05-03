import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;

import org.iiitb.es103_15.traffic.*;
import org.iiitb.es103_15.traffic.TrafficSignal.SignalListener;


//@SuppressWarnings("unused")
public class Car57 extends Car implements SignalListener {
	
	private int state;
	Car nearby_car;
	Road my_road;
	Intersection next_inter=null;
	TrafficControl tc=null;
	Random rand=new Random();
	
	public void setInitialPos(Road r, Coords loc, int dir) {
		super.setInitialPos(r, loc,dir);
		super.setRoad(r,dir);
		}
	
	@Override
	public void accelerate(float d,int duration){
		super.accelerate(d, duration);
		}
	
	@Override
	public void accelerate(float d){
		super.accelerate(d);
		}
	
	@Override
	public void carInFront(Car obstacle) {
		nearby_car = obstacle;
		super.carInFront(obstacle);
		}

	@Override
	public void updatePos() {
		super.updatePos();
		accelerate(20);
		float currspeed=this.getSpeed();
		if(currspeed-this.getRoad().getSpeedLimit()>=1)
			accelerate(((getRoad().getSpeedLimit()-currspeed)*10)-10);
		else
			accelerate(10);
		my_road=this.getRoad();
		next_inter=this.getNextInter();
		ArrayList<Integer> possible=new ArrayList<Integer>();
		TrafficControl tc=next_inter.getTrafficControl();
		if(getDis(getNextInter().getCoords())<15){
			accelerate(-400);
			for(int i=0;i<4;i++){
				if(next_inter.getRoads()[i]!=null && next_inter.getRoads()[i]!=my_road)
					possible.add(i);
				}
			int dir=possible.get(rand.nextInt(possible.size()));
			if(next_inter.getTrafficControl()!=null){
				if(state==1&&next_inter.isOccupied()!=true){
					if(tc.getType()==0 && tc!=null){
						synchronized(tc){
							((TrafficSignal)tc).removeListener(this, RoadGrid.getOppDir(this.getDir()));
							}
						}
					synchronized(next_inter){
						crossIntersection(next_inter,dir);
						}
					}
					else
						accelerate(-1000);
				}
			else
					crossIntersection(next_inter,dir);		
		}
		int d;
		if(nearby_car !=null){
			d=getDis(nearby_car.getPos());
			if(d<=30)
				accelerate(-1000);
		}
	}
	
	@Override
	public void onChanged(int currState) {
		if(currState==1)
			state=1;
		else if(currState==0)
			state=0;
		}
	private int getDis(Coords c) {
		int dist=Math.abs((getPos().x-c.x)+(getPos().y-c.y));
		return dist;
		}
	private Intersection getNextInter() {
		if(getDir()==getRoad().getDir())
			next_inter=getRoad().getEndIntersection();
		else 
			next_inter=getRoad().getStartIntersection();
		
		tc = next_inter.getTrafficControl();
		if(tc!=null && tc.getType()==0){
			synchronized(tc){
			((TrafficSignal)tc).addListener(this,RoadGrid.getOppDir(this.getDir()));
			state = ((TrafficSignal)tc).getSignalState(RoadGrid.getOppDir(this.getDir()));
			}
		}
		return next_inter;
		}
	@Override
	public void paint(Graphics g) {
		ArrayList<Color> col=new ArrayList<Color>();
		col.add(Color.BLUE);
		col.add(Color.BLACK);
		col.add(Color.ORANGE);
		col.add(Color.GREEN);
		col.add(Color.RED);
		col.add(Color.MAGENTA);
		col.add(Color.PINK);
		col.add(Color.YELLOW);
		int ccol=rand.nextInt(col.size());
	    g.setColor(col.get(ccol));
	    super.paint(g);
	  }
	}