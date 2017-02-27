package com.st.BlueSTSDK.Example;

import android.content.Context;
import android.content.Intent;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import static com.st.BlueSTSDK.Example.NodeContainerFragment.NODE_TAG;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Features.FeatureAcceleration;
import com.st.BlueSTSDK.Features.FeatureGyroscope;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;
/**
 * Created by origin on 2017/2/16.
 */

public class DrawActivity  extends AppCompatActivity {
    /**
     * tag used for retrieve the NodeContainerFragment
     */
    private final static String NODE_FRAGMENT = DebugConsoleActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";
    /**
     * node that will stream the data
     */
    private Node mNode;

    private WindowManager mWM;
    private double adjust= 0.7;
    private ImageView ivCursor;
    private boolean init=false;
    private WindowManager.LayoutParams mParams;

    private int x = 0;

    private int y = 0;
    /**
     * fragment that manage the node connection and avoid a re connection each time the activity
     * is recreated
     */
    private NodeContainerFragment mNodeContainer;
    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, DrawActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        String nodeTag = getIntent().getStringExtra(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);
        //create or recover the NodeContainerFragment
        if (savedInstanceState == null) {
            Intent i = getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());
            getFragmentManager().beginTransaction().add(mNodeContainer, NODE_FRAGMENT).commit();
        } else {
            mNodeContainer = (NodeContainerFragment) getFragmentManager().findFragmentByTag(NODE_FRAGMENT);
        }
        mWM = (WindowManager)this.getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        ivCursor = new ImageView(this.getBaseContext());//new一个ImageView控件
        ivCursor.setImageResource(R.drawable.board_generic);//设置ImageView的图片，这个鼠标指针图片我事先已经放到drawable文件夹下面了
        mParams = new WindowManager.LayoutParams();//对ivCursor对象的参数描述对象
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;//宽度自适应
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;//高度自适应
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//设置成不能获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE//设置成不能触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//设置成界面保持点亮
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        /*if(!mNode.isEnableNotification(mNode.getFeature(FeatureGyroscope.class))) {//Ensure the ADPCM is EnableNotification
            mNode.getFeature(FeatureGyroscope.class).addFeatureListener(mDrawListener);
            mNode.enableNotification(mNode.getFeature(FeatureGyroscope.class));//EnableNotification ADPCM
        }*/
        /**if(!mNode.isEnableNotification(mNode.getFeature(FeatureAcceleration.class))) {//Ensure the ADPCM is EnableNotification
            mNode.getFeature(FeatureAcceleration.class).addFeatureListener(mDrawListener);
            mNode.enableNotification(mNode.getFeature(FeatureAcceleration.class));//EnableNotification ADPCM
        }*/
        if(!mNode.isEnableNotification(mNode.getFeature(FeatureGyroscope.class))) {//Ensure the ADPCM is EnableNotification
            mNode.getFeature(FeatureGyroscope.class).addFeatureListener(mDrawListener);
            mNode.enableNotification(mNode.getFeature(FeatureGyroscope.class));//EnableNotification ADPCM
        }
        if(!init)
        {
            System.out.println("Debug:AB");
            x += 100;
            y += 100;
            mParams.x = x;//相对于屏幕原点的x轴距离
            mParams.y = y;//相对于屏幕原点的y轴距离
            mWM.addView(ivCursor, mParams);
            System.out.println("Debug:AC");
            x -= 50;
            y -= 50;
            mParams.x = x;//相对于屏幕原点的x轴距离
            mParams.y = y;//相对于屏幕原点的y轴距离
            mWM.updateViewLayout(ivCursor, mParams);
            System.out.println("Debug:AD");
            init=true;
        }
        System.out.println("Debug:0");
    }
    @Override
    public void onBackPressed(){
        mNodeContainer.keepConnectionOpen(true);
        mWM.removeView(ivCursor);
        /*if(mNode.isEnableNotification(mNode.getFeature(FeatureGyroscope.class))) {//Ensure the ADPCM is disableNotification
            mNode.getFeature(FeatureGyroscope.class).removeFeatureListener(mDrawListener);
            mNode.disableNotification(mNode.getFeature(FeatureGyroscope.class));//disableNotification ADPCM
        }*/
        /*if(mNode.isEnableNotification(mNode.getFeature(FeatureAcceleration.class))) {//Ensure the ADPCM is disableNotification
            mNode.getFeature(FeatureAcceleration.class).removeFeatureListener(mDrawListener);
            mNode.disableNotification(mNode.getFeature(FeatureAcceleration.class));//disableNotification ADPCM
        }*/
        if(mNode.isEnableNotification(mNode.getFeature(FeatureGyroscope.class))) {//Ensure the ADPCM is disableNotification
            mNode.getFeature(FeatureGyroscope.class).removeFeatureListener(mDrawListener);
            mNode.disableNotification(mNode.getFeature(FeatureGyroscope.class));//disableNotification ADPCM
        }
        init=false;
        mNodeContainer.keepConnectionOpen(true);
        super.onBackPressed();
    }
    private final Feature.FeatureListener mDrawListener = new Feature.FeatureListener() {
        @Override
        public void onUpdate(final Feature f, final Feature.Sample sample) {
            System.out.println("Debug:A");
            /*if(f.getName().equals("Accelerometer")){
                System.out.println("Debug:B");
                FeatureAcceleration Acceleration = mNode.getFeature(FeatureAcceleration.class);
                final float TempX = Acceleration.getAccX(sample);//get Acceleration
                final float TempY = Acceleration.getAccY(sample);//get Acceleration
                final float TempZ = Acceleration.getAccZ(sample);//get Acceleration
                System.out.println("Debug:TempX"+TempX);
                System.out.println("Debug:TempY"+TempY);
                DrawActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mParams.x += (int)(TempX*adjust);//相对于屏幕原点的x轴距离
                        mParams.y += (int)(TempY*adjust);//相对于屏幕原点的y轴距离
                        System.out.println("Debug:mParams.x"+mParams.x);
                        System.out.println("Debug:mParams.y"+mParams.y);
                        System.out.println("Debug:C");
                        //mWM.removeView(ivCursor);
                        //mWM.addView(ivCursor,mParams);
                        //mWM.addView(ivCursor, mParams);
                        mWM.updateViewLayout(ivCursor, mParams);
                        System.out.println("Debug:D");
                    }

                });
            }*/
            if(f.getName().equals("Gyroscope")){
                System.out.println("Debug:B");
                FeatureGyroscope Gyroscope = mNode.getFeature(FeatureGyroscope.class);
                final float TempX = Gyroscope.getGyroX(sample);//get GyroscopeX
                final float TempY = Gyroscope.getGyroY(sample);//get GyroscopeY
                final float TempZ = Gyroscope.getGyroZ(sample);//get GyroscopeZ
                System.out.println("Debug:TempX"+TempX);
                System.out.println("Debug:TempY"+TempY);
                DrawActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mParams.x += (int)(-TempZ*adjust);//相对于屏幕原点的x轴距离
                        mParams.y += (int)(TempX*adjust*16.0/9.0);//相对于屏幕原点的y轴距离
                        System.out.println("Debug:mParams.x"+mParams.x);
                        System.out.println("Debug:mParams.y"+mParams.y);
                        System.out.println("Debug:C");
                        //mWM.removeView(ivCursor);
                        //mWM.addView(ivCursor,mParams);
                        mWM.updateViewLayout(ivCursor, mParams);
                        System.out.println("Debug:D");
                    }

                });
            }
            }
        };
    }

