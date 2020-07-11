package bearmaps.proj2ab;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import edu.princeton.cs.algs4.Stopwatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {
    @Test
    public void Simple_Test(){
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(4, 4);
        KDTree nn = new KDTree(List.of(p1, p2, p3, p4, p5 ,p6));
        NaivePointSet tt = new NaivePointSet(List.of(p1, p2, p3, p4, p5 ,p6));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        Point res = tt.nearest(3.0, 4.0);
        assertTrue(res.equals(ret));
    }
    @Test
    public void Random_Test(){
        LinkedList<Point> S = new LinkedList<Point>();
        for(int i=0; i<100_000; i++) {
            double x = Math.random()*100_000+1;
            double y = Math.random()*100_000+1;
            Point p = new Point(x, y);
            S.addLast(p);
        }
        NaivePointSet nn = new NaivePointSet(S);
        KDTree tt = new KDTree(S);
        for(int j=0; j<100_000; j++) {
            double x = Math.random()*10_000+1;
            double y = Math.random()*10_000+1;
            Point N_answer = nn.nearest(x, y);
            Point M_answer = tt.nearest(x, y);
            assertTrue(N_answer.equals(M_answer));
        }
    }
    @Test
    public void Simple_Speed(){
        LinkedList<Point> S = new LinkedList<Point>();
        for(int i=0; i<100_000; i++) {
            double x = Math.random()*100_000+1;
            double y = Math.random()*100_000+1;
            Point p = new Point(x, y);
            S.addLast(p);
        }

        NaivePointSet nn = new NaivePointSet(S);
        KDTree tt = new KDTree(S);
        Stopwatch sw = new Stopwatch();
        for(int j=0; j<10_000; j++) {
            double x = Math.random()*10_000+1;
            double y = Math.random()*10_000+1;
            Point N_answer = tt.nearest(x, y);
        }
        System.out.println("the timing of KDTree is " + sw.elapsedTime());
        Stopwatch sp = new Stopwatch();
        for(int j=0; j<10_000; j++) {
            double x = Math.random()*10_000+1;
            double y = Math.random()*10_000+1;
            Point M_answer = nn.nearest(x, y);
        }
        System.out.println("the timing of Naive is " + sp.elapsedTime());
    }
}
