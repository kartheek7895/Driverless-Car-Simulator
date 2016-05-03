import java.util.ArrayList;
import org.iiitb.es103_15.traffic.*;

public class SafeRoad extends Road
{

    public SafeRoad(int dir, Intersection start, Intersection end, boolean entryStart, boolean entryEnd)
    {
        super(dir, start, end, entryStart, entryEnd);
        dirs = new int[2];
        lastFront = new Car[2];
        dirs[0] = dir;
        dirs[1] = RoadGrid.getOppDir(dir);
        lastFront[0] = lastFront[1] = null;
    }

    public SafeRoad(int dir, Intersection start, Intersection end)
    {
        this(dir, start, end, true, true);
    }

    private static int getDist(Car c1, Car c2, int dir)
    {
        int dist = 0;
        Coords p1 = c1.getPos();
        Coords p2 = c2.getPos();
        dist = (p2.x - p1.x) + (p2.y - p1.y);
        if(dir == 0 || dir == 3)
            dist *= -1;
        return dist;
    }

    public synchronized void checkCollisions()
    {
        for(int dir = 0; dir < 2; dir++)
        {
            ArrayList cars = getCarsL(dirs[dir]);
            if(cars.size() > 0)
            {
                Car firstCar = (Car)cars.get(0);
                if(lastFront[dir] != null && firstCar != lastFront[dir])
                    firstCar.carInFront(null);
                lastFront[dir] = firstCar;
                for(int i = 1; i < cars.size(); i++)
                {
                    Car thisCar = (Car)cars.get(i);
                    thisCar.carInFront(firstCar);
                    firstCar = thisCar;
                }

            } else
            {
                lastFront[dir] = null;
            }
        }

    }

    private int dirs[];
    private Car lastFront[];
}