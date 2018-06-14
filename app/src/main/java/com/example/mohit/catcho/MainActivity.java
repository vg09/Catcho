package com.example.mohit.catcho;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView black;
    private ImageView pink;
    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;




    //position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int blackX;
    private int blackY;
    private int pinkX;
    private int pinkY;

    //speed
    private int boxSpeed;
    private int orangeSpeed;
    private int blackSpeed;
    private int pinkSpeed;



    //score
    private int score = 0;

    //Initialise Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Sound sound;

    // Checking of status
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new Sound(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);

        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        black = (ImageView) findViewById(R.id.black);
        pink = (ImageView) findViewById(R.id.pink);

        //For screen size

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth=size.x;
        screenHeight=size.y;

        // Now Speed for devices

        boxSpeed=Math.round(screenHeight / 60F);
        orangeSpeed=Math.round(screenHeight / 60F);
        blackSpeed=Math.round(screenHeight / 60F);
        pinkSpeed=Math.round(screenHeight / 45F);

     //   Log.v("SPEED_BOX",boxSpeed+"");
     //   Log.v("SPEED_Orange",orangeSpeed+"");
     //   Log.v("SPEED_Black",blackSpeed+"");
     //   Log.v("SPEED_Pink", pinkSpeed + "")


//Move to out of screen
        orange.setX(-80);
        orange.setY(-80);
        black.setX(-80);
        black.setY(-80);
        pink.setX(-80);
        pink.setY(-80);

        scoreLabel.setText("Score :0");
        //Temporary
        boxY = 500;
    }


    public void changePos() {

        hitCheck();

        //Orange
        orangeX -= orangeSpeed;
        if(orangeX < 0) {
            orangeX = screenWidth + 25;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
            orange.setX(orangeX);
            orange.setY(orangeY);

        //Black

        blackX -= blackSpeed;
        if(blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //Pink

        pinkX -= pinkSpeed;
        if(pinkX < 0) {
            pinkX = screenWidth + 1500;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);



        //MOve the box
        if (action_flg == true) {
            boxY -= boxSpeed;
        } else {
            boxY += boxSpeed;          //Releasing the box
        }

        if (boxY < 0) boxY=0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;
        box.setY(boxY);

        scoreLabel.setText("Score :" + score);
    }

        public void hitCheck() {

            int orangeCenterX = orangeX + orange.getWidth()/2;
            int orangeCenterY = orangeY + orange.getHeight()/2;

            if (0 <= orangeCenterX && orangeCenterX <= boxSize &&
                    boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize) {

                score +=10;
                orangeX = -10;
                sound.playHitSound();
            }

//Pink
            int pinkCenterX = pinkX + pink.getWidth()/2;
            int pinkCenterY = pinkY + pink.getHeight()/2;

            if (0 <= pinkCenterX && pinkCenterX <= boxSize &&
                    boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

                score += 15;
                pinkX = -15;
                sound.playHitSound();
            }
//Pink
            int blackCenterX = blackX + black.getWidth()/2;
            int blackCenterY = blackY + black.getHeight()/2;

            if (0 <= blackCenterX && blackCenterX <= boxSize &&
                    boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {

               //Timer Stop
                timer.cancel();;
                timer=null;
                sound.playOverSound();
                //Result
                 Intent intent = new Intent(getApplicationContext(),result.class);
                    intent.putExtra("SCORE",score);
                    startActivity(intent);

            }

    }

    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {

            start_flg =true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY=(int)box.getY();

            boxSize=box.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }

            else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    // Disable Return Button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}



