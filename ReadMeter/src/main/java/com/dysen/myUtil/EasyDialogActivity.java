package com.dysen.myUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dysen.mylibrary.utils.EasyDialog;
import com.dysen.qj.wMeter.R;

/**
 * 自定义 Dialog
 */
public class EasyDialogActivity extends ActionBarActivity implements View.OnClickListener
{
    private RelativeLayout rlBackground;
    private Button btnTopLeft;
    private Button btnTopRight;
    private Button btnMiddleTop;
    private Button btnMiddleLeft;
    private Button btnMiddleRight;
    private Button btnMiddleBottom;
    private Button btnBottomLeft;
    private Button btnBottomRight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_dialog);
        iniComponent();
    }

    private void iniComponent()
    {
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
        btnTopLeft = (Button) findViewById(R.id.btnTopLeft);
        btnTopRight = (Button) findViewById(R.id.btnTopRight);
        btnMiddleTop = (Button) findViewById(R.id.btnMiddleTop);
        btnMiddleLeft = (Button) findViewById(R.id.btnMiddleLeft);
        btnMiddleRight = (Button) findViewById(R.id.btnMiddleRight);
        btnMiddleBottom = (Button) findViewById(R.id.btnMiddleBottom);
        btnBottomLeft = (Button) findViewById(R.id.btnBottomLeft);
        btnBottomRight = (Button) findViewById(R.id.btnBottomRight);

        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
        btnMiddleTop.setOnClickListener(this);
        btnMiddleLeft.setOnClickListener(this);
        btnMiddleRight.setOnClickListener(this);
        btnMiddleBottom.setOnClickListener(this);
        btnBottomLeft.setOnClickListener(this);
        btnBottomRight.setOnClickListener(this);
        rlBackground.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int[] location = new int[2];
                location[0] = (int) event.getX();
                location[1] = (int) event.getY();
                location[1] = location[1] + getActionBarHeight() + getStatusBarHeight();
                Toast.makeText(getApplicationContext(), "x:" + location[0] + " y:" + location[1], Toast.LENGTH_SHORT).show();

//                View easyView = PhotoDemoActivity.this.getLayoutInflater().inflate(R.layout.layout_tip_list_view, null);

                new EasyDialog(getApplicationContext())
//                        .setLayout(easyView)
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.background_color_black))
                        .setLocation(location)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.gray))
                        .show();

//                ListView listView = (ListView) easyView.findViewById(R.id.lvList);
//                List<String> items = new ArrayList<String>();
//                for(int i = 0; i < 20; i++)
//                {
//                    items.add(""+i);
//                }
//                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(PhotoDemoActivity.this, android.R.layout.simple_list_item_1, items);
//                listView.setAdapter(itemsAdapter);

                return false;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnTopLeft:
                View view = this.getLayoutInflater().inflate(R.layout.layout_dialog_left, null);
                new EasyDialog(getApplication())
//                        .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                        .setLayout(view)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.background_color_black))
//                        .setLocation(new location[])//point in screen
                        .setLocationByAttachedView(btnTopLeft)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                        .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                        .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(true)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.transparent))
                        .show();
                break;

            case R.id.btnTopRight:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.background_color_black))
                        .setLocationByAttachedView(btnTopRight)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 350, 400, 0)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 350, 0, 400)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.transparent))
                        .setOnEasyDialogDismissed(new EasyDialog.OnEasyDialogDismissed()
                        {
                            @Override
                            public void onDismissed()
                            {
                                Toast.makeText(getApplicationContext(), "dismiss", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnEasyDialogShow(new EasyDialog.OnEasyDialogShow()
                        {
                            @Override
                            public void onShow()
                            {
                                Toast.makeText(getApplicationContext(), "show", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.btnMiddleTop:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blue))
                        .setLocationByAttachedView(btnMiddleTop)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 1000, -800, 100, -50, 50, 0)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 500, 0, -800)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.pink))
                        .show();
                break;
            case R.id.btnMiddleLeft:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple))
                        .setLocationByAttachedView(btnMiddleLeft)
                        .setGravity(EasyDialog.GRAVITY_RIGHT)
                        .setAnimationAlphaShow(300, 0.0f, 1.0f)
                        .setAnimationAlphaDismiss(300, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.gray))
                        .show();
                break;
            case R.id.btnMiddleRight:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.red))
                        .setLocationByAttachedView(btnMiddleRight)
                        .setGravity(EasyDialog.GRAVITY_LEFT)
                        .setAnimationAlphaShow(300, 0.0f, 1.0f)
                        .setAnimationAlphaDismiss(300, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.gray))
                        .show();
                break;
            case R.id.btnMiddleBottom:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.brown))
                        .setLocationByAttachedView(btnMiddleBottom)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 1000, 800, -100, -50, 50, 0)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 500, 0, 800)
                        .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(true)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.gray))
                        .show();
                break;
            case R.id.btnBottomLeft:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.pink))
                        .setLocationByAttachedView(btnBottomLeft)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setAnimationAlphaShow(600, 0.0f, 1.0f)
                        .setAnimationAlphaDismiss(600, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.transparent))
                        .show();
                break;
            case R.id.btnBottomRight:
                new EasyDialog(getApplicationContext())
                        .setLayoutResourceId(R.layout.layout_dialog_left)
                        .setBackgroundColor(getApplicationContext().getResources().getColor(R.color.yellow))
                        .setLocationByAttachedView(btnBottomRight)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 300, 400, 0)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 300, 400, 0)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 300, 0, 400)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 300, 0, 400)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getApplicationContext().getResources().getColor(R.color.transparent))
                        .show();
                break;
        }
    }

    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getActionBarHeight()
    {
        return this.getSupportActionBar().getHeight();
    }
}