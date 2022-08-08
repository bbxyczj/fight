package fight.concurrent;



import com.lmax.disruptor.*;
import fight.utils.ThreadPoolUtils;
import sun.misc.Contended;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @Author XE-CZJ
 * @Date 2022/8/5 11:08
 */
public class DisruptorTest {
    public static void main(String[] args) throws Exception {
        // 队列中的元素
        class Element {
            @Contended
            private String value;


            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            int i = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread" + String.valueOf(i++));
            }
        };

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = (element, sequence, endOfBatch) -> {
            System.out.println(Thread.currentThread().getName()+"--消费者: " + Thread.currentThread().getName() + ": " + element.getValue() + ": " + sequence);
                Thread.sleep(1000);
        };
        WorkHandler<Element> handler1 = (element) -> {
            System.out.println(Thread.currentThread().getName()+"--消费者: " + Thread.currentThread().getName() + ": " + element.getValue());
            Thread.sleep(1000);
        };


        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 16;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.MULTI, strategy);

        disruptor.handleEventsWithWorkerPool(new WorkHandler<Element>() {
            @Override
            public void onEvent(Element element) throws Exception {
                System.out.println(Thread.currentThread().getName()+"--消费者: " + Thread.currentThread().getName() + ": " + element.getValue());
                Thread.sleep(1000);
            }
        });

//         设置EventHandler
        disruptor.handleEventsWithWorkerPool(handler1,handler1,handler1,handler1,handler1);

        // 启动disruptor的线程
        disruptor.start();
//        for (int i = 0; i < 1000; i++) {
//            disruptor.publishEvent((element, sequence) -> {
//                element.setValue("我是第" + sequence + "个");
//                System.out.println(Thread.currentThread().getName()+"生产者----当前的sequence" + sequence);
//
//            });
//        }
        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            ThreadPoolUtils.myThreadPool.execute(() -> {
                long next = ringBuffer.next();
                Element element = ringBuffer.get(next);
                element.setValue("我是第" + finalI + "个");
                System.out.println(Thread.currentThread().getName()+"生产者----当前的sequence" + next);
                ringBuffer.publish(next);
            });
        }
//        disruptor.shutdown();
    }
}

