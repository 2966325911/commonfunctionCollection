package com.cloudoc.share.yybpg.customview;

import android.content.Intent;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
   public void printMainThreadGroup(){
       ThreadGroup group = Thread.currentThread().getThreadGroup();
       System.out.println(Thread.currentThread().getName() + "线程所在的线程组" + group.getName());
   }

   public void stopThreadByThreadGroup(){
       ThreadGroup tg = new ThreadGroup(" 线程组1");
       for(int i = 0 ; i < 10;i++) {
           new Thread(tg,"子线程" + (i + 1)){
               @Override
               public void run() {
                   while(!currentThread().isInterrupted()) {
                       System.out.println(currentThread().getName() + "打印");
                   }
               }
           }.start();
       }

       try {
           Thread.sleep(3000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       tg.interrupt();
   }

   public void testThreadGroup(){
//       printMainThreadGroup();
       stopThreadByThreadGroup();
   }



   public void threadLocalTest(){
       final ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
           @Override
           protected Integer initialValue() {
               return 0;
           }
       };
       System.out.println(Thread.currentThread().getName() + "===threadLocal.get()==" +
       threadLocal.get());
       for(int i = 0 ; i < 5;i++) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   while(threadLocal.get() <= 5) {
                       System.out.println(Thread.currentThread().getName() + "的value值"
                        + threadLocal.get());
                       threadLocal.set(threadLocal.get() + 1);
                   }
               }
           },"线程"+ (i+1)).start();
       }
   }


   public void testThreadLocal(){
       threadLocalTest();
   }

   @Test
   public void test(){
//       System.out.println(Math.round(-12.5) + "==" + Math.round(11.5));
//
//       int result = 0;
//       int i = 2;
//       switch (i) {
//           case 1:
//               result = result + i;
//           case 2:
//               result = result + i*2;
//           case 3:
//               result = result + i*3;
//       }
//
//       System.out.println(result);

//       Integer[] intArray = {1,2,3,4,5,6};
       List<String> list = new ArrayList<>();
       for(int i = 0 ; i  < 10;i++) {
           list.add("this is " + i);
       }
       printList(list);
       Collections.reverse(list);
       printList(list);
   }

   private <E> void printArray(E[] inputArray) {
       for(E element : inputArray) {
           System.out.print(element + ",");
       }
    }

    private <T> void printList(List<T> list){
       for(int i = 0 ; i < list.size();i++) {
           T t = list.get(i);
           System.out.print(t.toString() + "==");
       }
    }

    private void readBigFile(){

        FileInputStream fileIn = null;
        ByteBuffer byteBuffer = null;
        FileChannel channel = null;
        try {
           fileIn = new FileInputStream("big.txt");
            byteBuffer = ByteBuffer.allocate(65535);
            channel = fileIn.getChannel();
            int bytes = -1;
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY,0,channel.size());
            do{
                bytes = channel.read(byteBuffer);
                if(bytes != -1) {
                    byte[] array = new byte[bytes];
                    byteBuffer.flip();
                    byteBuffer.get(array);
                    byteBuffer.clear();
                    System.out.println(new String(array));
                }
            } while (bytes > 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            byteBuffer.clear();
            try {
                channel.close();
                fileIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}