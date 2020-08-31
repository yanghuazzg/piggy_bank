package com.fertilizer.yh.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author mike.yang
 * @className SpinLockTest
 * @package com.fertilizer.yh.juc.lock
 * @describe 自旋锁代码验证
 * @date 2020/8/31 17:07:00
 */
public class SpinLockTest {
    /**
     * 原子引用线程
     */
    AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    public void lockTest() {
        //获取当前线程
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t 线程获取锁");
        //CAS 同步并交换
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println(Thread.currentThread().getName() + "\t 尝试获取锁...");
        }
    }

    public void unLockTest() {
        //获取当前线程
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t 线程释放锁");
    }

    public static void main(String[] args) {
        final SpinLockTest test = new SpinLockTest();

        new Thread(() -> {
            test.lockTest();
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test.unLockTest();
        }, "A1").start();

        new Thread(() -> {
            test.lockTest();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test.unLockTest();
        }, "A2").start();
    }
}
