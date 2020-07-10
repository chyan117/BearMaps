package bearmaps;
import java.util.*;
public class KDTree implements PointSet{
    private int size;
    private Node Root;
    public KDTree(List<Point> points){
        int depth = 0;
        size = 0;
        for(int i=0; i<points.size(); i++){
            if(i==0){
                Root = new Node(points.get(i));
                Root.Set_depth(depth);
                size = size+1;
            }
            else{
                Node T= Root;
                depth=0;
                Node new_one = new Node(points.get(i));
                while(T!=null){
                    //judge odd or even
                    //even
                    if(depth%2==0) {
                        //Bigger than in x aix
                        if(Double.compare(T.P.getX(), new_one.P.getX()) >0){
                            if(T.LeftChild==null){
                                T.LeftChild = new_one;
                                depth = depth+1;
                                T.LeftChild.Set_depth(depth);
                                size = size+1;
                                break;
                            }
                            T = T.LeftChild;
                        }
                        else{
                            if(T.RightChild==null){
                                T.RightChild = new_one;
                                size = size+1;
                                depth = depth+1;
                                T.RightChild.Set_depth(depth);
                                break;
                            }
                            T = T.RightChild;
                        }
                        depth = depth+1;
                    }
                    else if(depth%2!=0){
                        //Bigger than in y aix
                        if(Double.compare(T.P.getY(), new_one.P.getY()) >0){
                            if(T.LeftChild==null){
                                T.LeftChild = new_one;
                                size = size+1;
                                depth = depth+1;
                                T.LeftChild.Set_depth(depth);
                                break;
                            }
                            T = T.LeftChild;
                        }
                        else{
                            if(T.RightChild==null){
                                T.RightChild = new_one;
                                size = size+1;
                                depth = depth+1;
                                T.RightChild.Set_depth(depth);
                                break;
                            }
                            T = T.RightChild;
                        }
                        depth = depth+1;
                    }
                }
            }
        }
    }
    //This should take O(logN) time on average, where N is the number of points.
    @Override
    public Point nearest(double x, double y){
        Point goal = new Point(x, y);
        return helperMethond( Root,  goal, Root).P;
    }
    private Node helperMethond(Node N, Point goal, Node Best){
        if(N==null){
            return Best;
        }
        if(Point.distance(N.P, goal)< Point.distance(Best.P, goal)){
            Best = N;
        }
        Node GoodSide = new Node();
        Node BadSide= new Node();
        //judge the orientation
        //even -> compare x aix
        if(N.get_depth()%2==0){
            //Look left
            if(goal.getX() < N.P.getX()){
                GoodSide = N.LeftChild;
                BadSide = N.RightChild;
            }
            //Look right
            else{
                GoodSide = N.RightChild;
                BadSide = N.LeftChild;
            }
        }
        //even -> compare y aix
        if(N.get_depth()%2==1){
            //Look down
            if(goal.getY() < N.P.getY()){
                GoodSide = N.LeftChild;
                BadSide = N.RightChild;
            }
            //Look up
            else{
                GoodSide = N.RightChild;
                BadSide = N.LeftChild;
            }
        }
        Best = helperMethond(GoodSide, goal, Best);
        if(BadSide==null){
            return Best;
        }
        //if it is necessary to count the bad side
        //x aix
        if(N.get_depth()%2==0){
            double x = Math.pow( (N.P.getX() - goal.getX()), 2);
            if (x < Point.distance(Best.P, goal)) {
                Best = helperMethond(BadSide, goal, Best);
            }
        }
        //y aix
        if (N.get_depth() % 2 == 1) {
            double x = Math.pow((N.P.getY() - goal.getY()), 2);
            if (x < Point.distance(Best.P, goal)) {
                Best = helperMethond(BadSide, goal, Best);
            }
        }

        return Best;
    }
    private class Node{
        Point P;
        Node LeftChild;
        Node RightChild;
        private int depth;
        Node(Point P){
            this.P = P;
        }
        void Set_depth(int i){
            this.depth = i;
        }
        int get_depth(){
            return depth;
        }
        Node(){
        }
    }
}
