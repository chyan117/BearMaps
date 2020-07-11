package bearmaps.proj2ab;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import edu.princeton.cs.algs4.Stopwatch;
public class ArrayHeapMinPQTest{
    @Test
    public void test_random(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        for(int i=0; i<1000; i++){
            double ram = -Math.random()*1000;
            naive.add(i, ram);
            C.add(i, ram);
        }
        int num=0;
        while(C.size()>0){
            num = num+1;
            Assert.assertEquals(C.removeSmallest(), naive.removeSmallest());
        }
    }
    @Test
    public void test_random1(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        for(int i=0; i<10000; i++){
            double ram = Math.random()*1000;
            naive.add(i, ram);
            C.add(i, ram);
        }
        for(int i=0; i<10000; i++){
            double ram = -Math.random()*1000;
            naive.changePriority(i, ram);
            C.changePriority(i, ram);
        }
        int num=0;
        while(C.size()>0){
            num = num+1;
            Assert.assertEquals(C.removeSmallest(), naive.removeSmallest());
        }
    }
    @Test
    public void test_random2(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        for(int i=0; i<300; i++){
            double ram = Math.random()*1000;
            naive.add(i, ram);
            C.add(i, ram);
        }
        for(int i=0; i<300; i++){
            double ram = -Math.random()*1000;
            naive.changePriority(i, ram);
            C.changePriority(i, ram);
        }
        int num=0;
        for(int i=400; i<450; i++){
            naive.add(i, i-200);
            C.add(i, i-200);
        }
        while(C.size()>0){
            num = num+1;
            Assert.assertEquals(C.removeSmallest(), naive.removeSmallest());
        }
    }
    @Test
    public void test_random3(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        for(int i=0; i<300; i++){
            double ram = Math.random()*10000;
            naive.add(i, ram);
            C.add(i, ram);
        }
        for(int i=0; i<300; i++){
            double ram = -Math.random()*10000;
            naive.changePriority(i, ram);
            C.changePriority(i, ram);
        }
        int num=0;
        while(C.size()>0){
            num = num+1;
            Assert.assertEquals(C.removeSmallest(), naive.removeSmallest());
        }
    }
    @Test
    public void test_Augment(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        for(int i=0; i<32; i++){
            C.add(i, i);
        }
        int num=0;
        while(C.size()>0){
            Assert.assertEquals(C.removeSmallest(), (Integer) num);
            num = num+1;
        }
    }
    @Test
    public void test_add1(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(0, 1);
        C.add(2, 2);
        C.add(3, 3);
        C.add(4, 4);
        C.add(5, 5);
        C.add(6, 6);
        Assert.assertTrue(C.size()==7);
        Assert.assertFalse(C.contains(7));
        Assert.assertTrue(C.contains(1));
        Assert.assertTrue(C.contains(2));
        Assert.assertTrue(C.contains(3));
        Assert.assertTrue(C.contains(4));
        Assert.assertTrue(C.contains(1));
    }
    @Test
    public void test_add_again(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(1, 0);//It should print out illegal Argument exception
    }
    @Test
    public void test_add2(){
        ArrayHeapMinPQ<String> C = new ArrayHeapMinPQ<>();
        C.add("ab", 1);
        C.add("cd", 1);
        C.add("ef", 2);
        C.add("fjiosjf", 3);
        C.add("acad", 4);
        C.add("mkotkmgo", 5);
        C.add("woifjo", 6);
        Assert.assertTrue(C.size()==7);
        Assert.assertFalse(C.contains("aaa"));
        Assert.assertTrue(C.contains("cd"));
        Assert.assertTrue(C.contains("ef"));
        Assert.assertTrue(C.contains("fjiosjf"));
        Assert.assertTrue(C.contains("mkotkmgo"));
        Assert.assertTrue(C.contains("woifjo"));
    }
    @Test
    public void test_breakties(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(0, 1);
        C.add(2, 1);
        C.add(3, 1);
        C.add(4, 1);
        C.add(5, 1);
        C.add(6, 1);
        Assert.assertTrue(C.size()==7);
        C.removeSmallest();
        C.removeSmallest();
        C.removeSmallest();
        C.removeSmallest();
        C.removeSmallest();
    }

    @Test
    public void test_getSmallest(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(0, 1);
        C.add(2, 2);
        C.add(3, 3);
        C.add(4, 4);
        C.add(5, 5);
        C.add(6, 6);
        C.add(7, -1);
        Assert.assertEquals(C.getSmallest(), (Integer)7);
    }
    @Test
    public void test_removeSmallest(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(0, 0);
        C.add(2, 2);
        C.add(3, 3);
        C.add(4, 4);
        C.add(5, 5);
        C.add(6, 6);
        C.add(7, -1);
        Assert.assertEquals(C.removeSmallest(), (Integer)7);
        Assert.assertTrue(C.size()==7);
        Assert.assertFalse(C.contains(7));
        Assert.assertEquals(C.removeSmallest(), (Integer)0);
        Assert.assertTrue(C.size()==6);
        Assert.assertFalse(C.contains(0));
        Assert.assertEquals(C.removeSmallest(), (Integer)1);
        Assert.assertTrue(C.size()==5);
        Assert.assertFalse(C.contains(1));
        Assert.assertEquals(C.removeSmallest(), (Integer)2);
        Assert.assertTrue(C.size()==4);
        Assert.assertFalse(C.contains(2));
        Assert.assertEquals(C.removeSmallest(), (Integer)3);
        Assert.assertTrue(C.size()==3);
        Assert.assertFalse(C.contains(3));
        Assert.assertEquals(C.removeSmallest(), (Integer)4);
        Assert.assertTrue(C.size()==2);
        Assert.assertFalse(C.contains(4));
        Assert.assertEquals(C.removeSmallest(), (Integer)5);
        Assert.assertTrue(C.size()==1);
        Assert.assertFalse(C.contains(5));
        Assert.assertEquals(C.removeSmallest(), (Integer)6);
        Assert.assertTrue(C.size()==0);
        Assert.assertFalse(C.contains(6));
    }
    @Test
    public void test_changePriority(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        C.add(1, 1);
        C.add(0, 0);
        C.add(2, 2);
        C.add(3, 3);
        C.add(4, 4);
        C.add(5, 5);
        C.add(6, 6);
        C.changePriority(6, -6);
        C.changePriority(5, -5);
        C.changePriority(4, -4);
        C.changePriority(3, -3);
        C.changePriority(2, -2);
        C.changePriority(0, -0);
        C.changePriority(1, -1);
        Assert.assertTrue(C.removeSmallest()==6);
        Assert.assertTrue(C.removeSmallest()==5);
        Assert.assertTrue(C.removeSmallest()==4);
        Assert.assertTrue(C.removeSmallest()==3);
        Assert.assertTrue(C.getSmallest()==2);
        Assert.assertTrue(C.removeSmallest()==2);
        Assert.assertTrue(C.removeSmallest()==1);
    }

    @Test
    public void test_speed(){
        ArrayHeapMinPQ<Integer> C = new ArrayHeapMinPQ<>();
        Stopwatch sx = new Stopwatch();
        for (int i = 0; i < 10000; i += 1) {
            C.add(i, -i);
        }
        System.out.println("Total time elapsed of quick method to test add is : " + sx.elapsedTime() +  " seconds.");
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 10000; i += 1) {
            C.getSmallest();
        }
        System.out.println("Total time elapsed of quick method to test getSmallest is : " + sw.elapsedTime() +  " seconds.");
        NaiveMinPQ<Integer> D = new NaiveMinPQ<>();
        for (int i = 0; i < 10000; i += 1) {
            D.add(i, -i);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i += 1) {
            D.getSmallest();
        }
        long end = System.currentTimeMillis();
        System.out.println("Total time elapsed of naive method to test getSmallest is: " + (end - start)/1000.0 +  " seconds.");

        Stopwatch sp = new Stopwatch();
        for (int i = 0; i < 10000; i += 1) {
            C.removeSmallest();
        }
        System.out.println("Total time elapsed of quick method to test removeSmallest is : " + sp.elapsedTime() +  " seconds.");

        long starp = System.currentTimeMillis();
        for (int i = 0; i < 10000; i += 1) {
            D.removeSmallest();
        }
        long endp = System.currentTimeMillis();
        System.out.println("Total time elapsed of naive method to test removeSmallest is: " + (endp - starp)/1000.0 +  " seconds.");



        ArrayHeapMinPQ<Integer> S = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> K = new NaiveMinPQ<>();
        for (int i = 0; i < 100000; i += 1) {
            S.add(i,  i);
        }
        Stopwatch sd = new Stopwatch();
        for (int i = 0; i < 100000; i += 1) {
            S.changePriority((int)(Math.random()*100000),  (Math.random()*100000));
        }
        System.out.println("Total time elapsed of quick method to test Changepriority is : " + sd.elapsedTime() +  " seconds.");

        for (int i = 0; i < 100000; i += 1) {
            K.add(i,  i);
        }
        long stard = System.currentTimeMillis();
        for (int i = 0; i < 100000; i += 1) {
            K.changePriority( (int)(Math.random()*100000),  (Math.random()*100000)) ;
        }
        long endd = System.currentTimeMillis();
        System.out.println("Total time elapsed of naive method to test Changepriority is: " + (endd - stard)/1000.0 +  " seconds.");
    }

    public static void main(String[] args) {
        ArrayHeapMinPQ<Integer> A = new ArrayHeapMinPQ<>();
        A.add(1, 1);
        A.add(2, 2);
        A.add(5, 5);
        A.add(6, 6);
        A.add(9, 9);
        A.add(4, 4);
        A.add(3, 3);
        A.add(7, 7);
        A.add(10, 10);
        A.add(8, 8);
        A.add(-1, -1);
        A.removeSmallest();
        A.changePriority(9, -1);
        A.removeSmallest();
        A.changePriority(1, 11);
        A.changePriority(10, 50);
    }
}