package bearmaps.proj2ab;
import java.util.*;
public class NaivePointSet implements PointSet{
    //This should take θ(N) time where N is the number of points.
    List<Point> PSet;
    @Override
    public Point nearest(double x, double y){
        Point nearest_P = new Point(x, y);
        if(PSet.isEmpty()){
            return null;
        }
        Point Min_point = new Point(PSet.get(0).getX(), PSet.get(0).getY());
        double min_dis = Point.distance(nearest_P, Min_point);
        for(Point S: PSet){
            if(Point.distance(S, nearest_P)< min_dis){
                min_dis = Point.distance(S, nearest_P);
                Min_point = S;
            }
        }
        return Min_point;
    }
    public NaivePointSet(List<Point> points){
        PSet = new LinkedList();
        for(Point s: points){
            PSet.add(s);
        }
    }
    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        Point p4 = new Point(3.1, 4.1);
        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3, p4));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY() ); // evaluates to 4.4
    }
}
