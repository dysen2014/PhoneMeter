package com.dysen.load;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.login_register.LoginDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.nfcdemo.NFCDemoActivity;
import com.dysen.qj.wMeter.R;
import com.dysen.type.about.AboutActivity;
import com.dysen.type.about.AppIntroActivity;
import com.dysen.type.about.CompanyIntroActivity;
import com.dysen.type.dataSys.DataSysActivity;
import com.dysen.type.dataSys.StatisticsActivity;
import com.dysen.type.meterSys.BookActivity;
import com.dysen.type.meterSys.FillMeterActivity;
import com.dysen.type.meterSys.MeterSysActivity;
import com.dysen.type.meterSys.SelectMIdActivity;
import com.dysen.type.meterSys.UpdateActivity;
import com.dysen.type.meterSys.systemSet.SystemSetActivity;
import com.dysen.type.user.UserActivity;

public class MainActivity extends MyActivityTools{

    String HTTP_IP ;
    public static String userName, loginId;
    boolean isLoginSuccess;
    TextView tvLoginJoin;
    Button btnLoginJoin;
    AlertDialog alert;
    LinearLayout llLogin;
    Intent intent;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 无title

        setContentView(R.layout.activity_main);

        //透明状态栏
        StatusBarUtil.setTransparent(this);

//        TextView tv = (TextView) this.findViewById(R.id.tv_user);
//        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
//        tv.setTypeface(iconfont);

        HTTP_IP = this.getText(R.string.HTTP_IP).toString();
        System.out.println("IP地址：" + HTTP_IP);

        drawer = (DrawerLayout) bindView(R.id.drawer_layout);
//        StatusBarUtil.setColorForDrawerLayout(this, drawer, R.color.transparent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.other2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) bindView(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (!isLoginSuccess) {
                    myDialog("请登录");
                }else {
                    // Handle navigation view item clicks here.
                    int id = item.getItemId();

                    if (id == R.id.nav_book) {//表册
                        startActivity(new Intent(MainActivity.this, BookActivity.class));

                    } else if (id == R.id.nav_nfc) {//nfc抄表
                        startActivity(new Intent(MainActivity.this, NFCDemoActivity.class));

                    } else if (id == R.id.nav_count_find) {//统计查询
                        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));

                    } else if (id == R.id.nav_update) {//数据上传
                        startActivity(new Intent(MainActivity.this, UpdateActivity.class));

                    } else if (id == R.id.nav_payment) {//缴费记录
                        Intent intent = new Intent(MainActivity.this, SelectMIdActivity.class);
                        intent.putExtra("type", "payment");
                        startActivity(intent);

                    } else if (id == R.id.nav_fillorleakage) {//补抄漏抄
                        startActivity(new Intent(MainActivity.this, FillMeterActivity.class));

                    }else if (id == R.id.nav_company_intro){//公司简介
                        startActivity(new Intent(MainActivity.this, CompanyIntroActivity.class));

                    }else if (id == R.id.nav_app_intro){//应用介绍
                        startActivity(new Intent(MainActivity.this, AppIntroActivity.class));

                    }
                }
//                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.setBackgroundResource(R.drawable.blg);

        View v =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvLoginJoin = (TextView) v.findViewById(R.id.tv_login_join);
        btnLoginJoin = (Button) v.findViewById(R.id.btn_login_join);
        llLogin = (LinearLayout) v.findViewById(R.id.ll_login);
        btnLoginJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginSuccess)
                    myDialog("您要重新登录");
                else
                myDialog("请登录");
            }
        });

        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 按钮按下，将抽屉打开
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * sen dy
     *
     * 2015-6-23 下午4:06:27
     *
     * info: 提示用户登录,注册
     */
    public void myDialog(String str) {
        View v = LayoutInflater.from(this).inflate(R.layout.login_register,
                null);

        Button btnCancel, btnOkPress;
        TextView textView;
        textView = (TextView) v.findViewById(R.id.tv_hint_title);
//        if (!"".equals(str))
            textView.setText(str);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel_normal);
        btnOkPress = (Button) v.findViewById(R.id.btn_ok_press);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
            }
        });
        btnOkPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();

                startActivityForResult(new Intent(MainActivity.this, LoginDemo.class), 1);
//				isLoginSuccess = true;
//				btnLoginJoin
//						.setBackgroundResource(R.drawable.pressed);
//				tvLoginJoin.setText(Html
//						.fromHtml("<font  color=\"green\"><u>"
//								+ userName + "</u></font>"));// "登录成功"
            }
        });

//        alert = new AlertDialog.Builder(this).create();
//                setView(v).show();
//        alert.show();
//        alert.getWindow().setContentView(v);
        startActivityForResult(new Intent(MainActivity.this, LoginDemo.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        loginId = data.getExtras().getString("login_id");
        userName = data.getExtras().getString("user_name");
        isLoginSuccess = data.getExtras().getBoolean("flag_login");

        if (isLoginSuccess) {

            btnLoginJoin
                    .setBackgroundResource(R.drawable.pressed);
            tvLoginJoin.setText(Html
                    .fromHtml("<font  color=\"green\"><u>"
                            + userName + "</u></font>"));// "登录成功"
            // 得到新Activity关闭后返回的数据
        }else {

        }
    }

    public void systemSet(View v){

        if (alert != null)
            if(alert.isShowing()) {
                alert.dismiss();
            }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
//        ToastDemo.myHint(this, "", 5);
            startActivity(new Intent(this, SystemSetActivity.class));
        }
    }

    /**
     *	dysen
     *	2015-8-27 下午2:56:40
     *	info: 无线自动抄表系统(WAMR) wireless auto meter read
     */
    public void btnWAMR(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, MeterSysActivity.class);
            intent.putExtra("login_id", loginId);
            intent.putExtra("user_name", userName);

            startActivity(intent);
//			startActivityForResult(intent, 2);
        }
    }

    public void btnDataSM(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, DataSysActivity.class);
            intent.putExtra("login_id", loginId);

            startActivity(intent);
        }
    }

    public void btnAbout(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, AboutActivity.class);
            intent.putExtra("login_id", loginId);

            startActivity(intent);
        }
    }
}
