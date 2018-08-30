package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.cloudoc.share.yybpg.customview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : Vic
 * time   : 2018/08/12
 * desc   :
 */
public class DaFeiJiGameView extends SurfaceView implements
        SurfaceHolder.Callback, View.OnTouchListener, Runnable {
    FeijiImage selectFeijiImage;
    boolean stopState = false;
    /**
     * 自己主飞机
     */
    private Bitmap my;
    /**
     * 爆炸的效果图
     */
    private Bitmap baozha;
    private Bitmap bg;
    /**
     * 敌机
     */
    private Bitmap diren;
    /**
     * 子弹
     */
    private Bitmap zidan;
    /**
     * 二级缓存
     */
    private Bitmap erjihuancun;
    /**
     * 屏幕宽高
     */
    private int display_w;
    private int display_h;
    private ArrayList<GameImage> gameImages = new ArrayList<>();
    private ArrayList<Zidan> zidans = new ArrayList<>();
    private SoundPool poll = null;
    private int sound_bomb = 0;
    private int sound_gameover = 0;
    private int sound_shot = 0;
    private SurfaceHolder holder;
    private boolean state;
    private Thread thread;

    /**
     * 出敌机的速度
     */
    private int chudishu = 30;
    /**
     * 敌机移动的速度
     */
    private int dijiyidong = 10;
    /**
     * 下一关分数
     */
    private int nextGuanScore = 50;

    private int guanqia = 1;

    private int fenshu = 0;
    public DaFeiJiGameView(Context context) {
        this(context, null);
    }

    public void stop() {
        stopState = true;
    }

    public void start(){
        stopState = false;
        thread.interrupt();
    }

    public DaFeiJiGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        display_w = width;
        display_h = height;

        init();
        state = true;
        this.holder = holder;
        thread = new Thread(this);
        thread.start();


    }

    private void init() {

        my = BitmapFactory.decodeResource(getResources(), R.mipmap.my);
        baozha = BitmapFactory.decodeResource(getResources(), R.mipmap.baozha);
        bg = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_feiji);
        diren = BitmapFactory.decodeResource(getResources(), R.mipmap.diren);
        zidan = BitmapFactory.decodeResource(getResources(), R.mipmap.zidan);
        //二级缓存产生照片
        erjihuancun = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.ARGB_8888);
        gameImages.add(new BeijingImage(bg));
        gameImages.add(new FeijiImage(my));
        gameImages.add(new DijiImage(diren, baozha));

        poll = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
        sound_bomb = poll.load(getContext(), R.raw.bomb, 1);
        sound_gameover = poll.load(getContext(), R.raw.gameover, 1);
        sound_shot = poll.load(getContext(), R.raw.shot, 1);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        state = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (GameImage game : gameImages) {
                if (game instanceof FeijiImage) {
                    FeijiImage feiji = (FeijiImage) game;
                    if (event.getX() > feiji.getX() && (feiji.getX() + feiji.getWidth()) > event.getX()
                            && event.getY() > feiji.getY() && (feiji.getY() + feiji.getHeight()) > event.getY()) {
                        selectFeijiImage = feiji;
                    } else {
                        selectFeijiImage = null;
                    }
                    break;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (selectFeijiImage != null) {
                selectFeijiImage.setX((int) (event.getX() - (selectFeijiImage.getWidth() / 2)));
                selectFeijiImage.setY((int) (event.getY() - selectFeijiImage.getHeight() / 2));
            }
        } else {
            selectFeijiImage = null;
        }
        return true;
    }

    @Override
    public void run() {
        Paint p1 = new Paint();
        int diren_num = 0;
        int zidan_num = 0;
        p1.setAntiAlias(true);

        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setTextSize(60);
        p2.setDither(true);
        p2.setColor(Color.YELLOW);
        try {
            while (state) {

                try{
                    while (stopState) {
                        Thread.sleep(10000000);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                Canvas newcanvas = new Canvas(erjihuancun);

                if (selectFeijiImage != null) {
                    if (zidan_num == 5) {
                        zidans.add(new Zidan(zidan, selectFeijiImage));
                        zidan_num = 0;
                    }
                    zidan_num++;
                }
                for (GameImage image : (List<GameImage>) gameImages.clone()) {
                    if (image instanceof DijiImage) {
                        ((DijiImage) image).shoudaogongji(zidans);
                    }
                    newcanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p1);
                }

                for (GameImage image : (List<GameImage>) zidans.clone()) {
                    newcanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p1);
                }


                newcanvas .drawText("分" + fenshu,0,60,p2);
                newcanvas .drawText("关" + guanqia,0,120,p2);
                newcanvas .drawText("下" + nextGuanScore,0,180,p2);
                if (diren_num == chudishu) {
                    diren_num = 0;
                    gameImages.add(new DijiImage(diren, baozha));
                }
                diren_num++;

                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(erjihuancun, 0, 0, p1);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private interface GameImage {
        Bitmap getBitmap();

        int getX();

        int getY();
    }

    private class SoundPlay extends Thread {
        int index = 0;

        public SoundPlay(int index) {
            this.index = index;
        }

        @Override
        public void run() {

            poll.play(index, 1, 1, 1, 0, 1);
        }
    }

    private class Zidan implements GameImage {

        private Bitmap zidan;
        private FeijiImage feiji;
        private int x;
        private int y;

        public Zidan(Bitmap zidan, FeijiImage feijiImage) {
            this.zidan = zidan;
            this.feiji = feijiImage;
            x = feiji.getX() + feiji.getWidth() / 2 - 8;
            y = feiji.getY() - zidan.getHeight();
            new SoundPlay(sound_shot).start();
        }

        @Override
        public Bitmap getBitmap() {
            y -= 50;
            if (y <= -10) {
                zidans.remove(this);
            }
            return zidan;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }

    private class BeijingImage implements GameImage {

        private Bitmap bg;
        private Bitmap newBitmap;
        private int height = 0;

        public BeijingImage(Bitmap bg) {
            this.bg = bg;
            newBitmap = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.ARGB_8888);
        }

        @Override
        public Bitmap getBitmap() {
            Paint p = new Paint();
            p.setAntiAlias(true);

            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()), new Rect(0, height, display_w, display_h + height), p);
            canvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()), new Rect(0, -display_h + height, display_w, height), p);
            height++;
            if (height == display_h) {
                height = 0;
            }

            return newBitmap;

        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }
    }


    private class FeijiImage implements GameImage {
        private Bitmap my;
        private int width;
        private int height;
        private int x;
        private int y;

        private List<Bitmap> bitmaps = new ArrayList<>();
        private int index = 0;
        private int num = 0;

        private FeijiImage(Bitmap my) {
            this.my = my;
            width = my.getWidth() / 4;
            height = my.getHeight();

            bitmaps.add(Bitmap.createBitmap(my, 0, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, my.getWidth() / 4, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 2, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 3, 0, my.getWidth() / 4, my.getHeight()));

            x = (display_w - my.getWidth() / 4) / 2;
            y = display_h - my.getHeight() - 30;

        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public Bitmap getBitmap() {
            Bitmap bitmap = bitmaps.get(index);
            if (num == 7) {
                index++;
                if (index == bitmaps.size()) {
                    index = 0;
                }
                num = 0;
            }
            num++;
            return bitmap;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private class DijiImage implements GameImage {
        private Bitmap diren = null;
        private Bitmap baozha = null;
        private List<Bitmap> bitmaps = new ArrayList<>();
        private List<Bitmap> baozhas = new ArrayList<>();
        private int x;
        private int y;
        private int width;
        private int height;
        private int index = 0;
        private int num = 0;
        private boolean state = false;

        public DijiImage(Bitmap diren, Bitmap baozha) {
            this.diren = diren;
            this.baozha = baozha;

            bitmaps.add(Bitmap.createBitmap(diren, 0, 0, diren.getWidth() / 4, diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren, diren.getWidth() / 4, 0, diren.getWidth() / 4, diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren, (diren.getWidth() / 4) * 2, 0, diren.getWidth() / 4, diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren, (diren.getWidth() / 4) * 3, 0, diren.getWidth() / 4, diren.getHeight()));

            baozhas.add(Bitmap.createBitmap(baozha, 0, 0, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, baozha.getWidth() / 4, 0, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, (baozha.getWidth() / 4) * 2, 0, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, (baozha.getWidth() / 4) * 3, 0, baozha.getWidth() / 4, baozha.getHeight() / 2));

            baozhas.add(Bitmap.createBitmap(baozha, 0, baozha.getHeight() / 2, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, baozha.getWidth() / 4, baozha.getHeight() / 2, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, (baozha.getWidth() / 4) * 2, baozha.getHeight() / 2, baozha.getWidth() / 4, baozha.getHeight() / 2));
            baozhas.add(Bitmap.createBitmap(baozha, (baozha.getWidth() / 4) * 3, baozha.getHeight() / 2, baozha.getWidth() / 4, baozha.getHeight() / 2));

            width = diren.getWidth() / 4;
            height = diren.getHeight();

            y = -diren.getHeight();
            Random random = new Random();
            x = random.nextInt(display_w - (diren.getWidth() / 4));
        }

        public void shoudaogongji(ArrayList<Zidan> zidans) {
            if (!state) {
                for (GameImage zidan : (List<GameImage>) zidans.clone()) {
                    if (zidan.getX() > x && zidan.getY() > y
                            && zidan.getX() < x + width &&
                            zidan.getY() < y + height) {
                        new SoundPlay(sound_bomb).start();
                        zidans.remove(zidan);
                        state = true;
                        bitmaps = baozhas;
                        if(fenshu / 50 == guanqia) {
                            guanqia++;
                            dijiyidong ++;
                            nextGuanScore += 50;
                            if(guanqia == 10) {
                                stopState = true;
                                new SoundPlay(sound_gameover);
                            }
                        }
                        fenshu += 10;
                        break;
                    }
                }
            }
        }

        @Override
        public Bitmap getBitmap() {
            Bitmap bitmap = bitmaps.get(index);
            if (num == 7) {
                index++;
                if (index == baozhas.size() && state) {
                    gameImages.remove(this);
                }
                if (index == bitmaps.size()) {
                    index = 0;
                }
                num = 0;
            }
            y += dijiyidong;

            if (y > display_h) {
                gameImages.remove(this);
            }

            num++;

            return bitmap;
        }


        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }
}
