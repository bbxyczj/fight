package fight.sort;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * @author zhengliu
 * @createTime 2018/9/17
 */
public class SortTest2 {

//    private static final ForkJoinPool FORKJOIN_POOL = ForkJoinPool.commonPool();
    // 单桶初始化大小
    private static final int MAXINITPERBUCKET = 100000;
    // 桶的数量
    private static final int MAXBUCKET = 1000;

    private static int resources[] = new int[MAXBUCKET *MAXINITPERBUCKET ];

    static {
        // 随机生成待排序的数组元素
        Random random = new Random();
        int len = MAXINITPERBUCKET * MAXBUCKET;
        for(int index = 0 ; index < len; index++) {
            resources[index] = random.nextInt(len);
        }
    }



    static class  SortTask extends RecursiveTask<int[]>{

        //每个线程最大处理长度
        private int maxNum=2;
        private int[] ints;

        SortTask(int[] ints){
            this.ints=ints;
        }

        @Override
        protected int[] compute() {
            if(ints.length<maxNum){
                //直接排序
//                return insertSort(ints);
//                return mpSort(ints);
                Arrays.sort(ints);
                return ints;
//                quickSort(resources,0,resources.length-1);
            }
            SortTask sortTask1=new SortTask(Arrays.copyOf(ints,ints.length/maxNum));
            SortTask sortTask2=new SortTask(Arrays.copyOfRange(ints,ints.length/maxNum+1,ints.length));
            sortTask1.fork();
            sortTask2.fork();
            int[] join = sortTask1.join();
            int[] join1 = sortTask2.join();
            return joinInts(join1,join);
        }
    }



    //归并排序
    private static int[] joinInts(int array1[] , int array2[]) {
        int destInts[] = new int[array1.length + array2.length];
        int array1Len = array1.length;
        int array2Len = array2.length;
        int destLen = destInts.length;

        // 只需要以新的集合destInts的长度为标准，遍历一次即可
        for(int index = 0 , array1Index = 0 , array2Index = 0 ; index < destLen ; index++) {
            //这一步是为了不发生数组下标越界，一旦一个数组取完，则赋最大值
            int value1 = array1Index >= array1Len?Integer.MAX_VALUE:array1[array1Index];
            int value2 = array2Index >= array2Len?Integer.MAX_VALUE:array2[array2Index];
            // 如果条件成立，说明应该取数组array1中的值
            if(value1 < value2) {
                array1Index++;
                destInts[index] = value1;
            }
            // 否则取数组array2中的值
            else {
                array2Index++;
                destInts[index] = value2;
            }
        }

        return destInts;
    }

    //冒泡排序
    private static int[] mpSort(int[] ints){
        for (int i=0;i<ints.length;i++){
            for (int j=ints.length-1;j>i;j--){
                int anInt = ints[i];
                int anInt1 = ints[j];
                if(anInt>anInt1){
                    swap(ints,i,j);
                }
            }
        }
        return ints;
    }

    //插入排序
    private static int[] insertSort(int[] ints){
        for (int i=1;i<ints.length;i++){
            for (int j=i;(j>0)&&(ints[j]<ints[j-1]);j--){
                swap(ints,j,j-1);
            }
        }
        return ints;
    }

    //快速排序
    private static void quickSort(int[] resources,int left,int right){
        if(left>=right){
            return;
        }
        int i=left,j=right;
        int temp = resources[i];
        while (i<j){

            //本来就是右边的
            while (resources[j]>temp&&i<j){
                j--;
            }
            if(i<j){
                resources[i]=resources[j];
                i++;
            }
            //本来就是左边的
            while (resources[i]<temp&&i<j){
                i++;
            }
            if(i<j){
                resources[j]=resources[i];
                j--;
            }
        }
        resources[i]=temp;
        quickSort(resources,left,i-1);
        quickSort(resources,i+1,right);
    }


    static class QuickTask extends RecursiveAction{

        private int left;
        private int right;

        QuickTask(int left,int right){
            this.left=left;
            this.right=right;
        }
        @Override
        protected void compute() {
            if(left>=right){
                return;
            }
            int i=left,j=right;
            int temp = resources[i];
            while (i<j){
                //本来就是右边的
                while (resources[j]>temp&&i<j){
                    j--;
                }
                if(i<j){
                    resources[i]=resources[j];
                    i++;
                }
                //本来就是左边的
                while (resources[i]<temp&&i<j){
                    i++;
                }
                if(i<j){
                    resources[j]=resources[i];
                    j--;
                }
            }
            resources[i]=temp;
            QuickTask quickTask=new QuickTask(left,i-1);
            QuickTask quickTask2=new QuickTask(i+1,right);
            quickTask.fork();
            quickTask2.fork();
            quickTask.join();
            quickTask2.join();
        }
    }


    private static void swap(int[] ints,int i,int j){
        int anInt = ints[i];
        ints[i]=ints[j];
        ints[j]=anInt;
    }



    public static void main(String[] args) {


        long l = System.currentTimeMillis();
//        SortTask sortTask = new SortTask(resources);
//        ForkJoinTask<int[]> submit = FORKJOIN_POOL.submit(sortTask);
//        int[] ints=new int[1];
//        try {
//            ints = submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        System.out.println("归并排序"+(System.currentTimeMillis()-l));

        long y = System.currentTimeMillis();
//        int[] sort = mpSort(resources);
        System.out.println("简单排序"+(System.currentTimeMillis()-y));

        long y1 = System.currentTimeMillis();
//        int[] sort1 = insertSort(resources);
        System.out.println("插入排序"+(System.currentTimeMillis()-y1));

        long y3 = System.currentTimeMillis();
        Arrays.sort(resources);
        System.out.println("系统排序"+(System.currentTimeMillis()-y3));

        long y4 = System.currentTimeMillis();
        quickSort(resources,0,resources.length-1);
        System.out.println("快速排序"+(System.currentTimeMillis()-y4));

        PriorityQueue<Integer> a=new PriorityQueue<>();
        a.poll();
        long y5 = System.currentTimeMillis();
//        QuickTask quickTask=new QuickTask(0,resources.length-1);
//        FORKJOIN_POOL.submit(quickTask);
//        try {
//            FORKJOIN_POOL.awaitTermination(2,TimeUnit.MINUTES);
//            FORKJOIN_POOL.shutdown();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("多线程快速排序"+(System.currentTimeMillis()-y5));

    }




}