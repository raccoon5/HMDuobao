package com.haomeng.duobao;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.haomeng.duobao.fragment.DingdanFragment;
import com.haomeng.duobao.fragment.MashangFragment;
import com.haomeng.duobao.fragment.MeFragment;
import com.haomeng.duobao.fragment.MengzhuFragment;

public class MainActivity extends AppCompatActivity {
    private Button mBtn0;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    private MengzhuFragment mFragment0;
    private MashangFragment mFragment1;
    private DingdanFragment mFragment2;
    private MeFragment mFragment3;
    private FragmentManager fManager;
    private int select = -1;

    /** 上一次点击返回键的时间记录 */
    private long mLastBackKeyDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setListener();
        init();
    }

    private void findView() {
        mBtn0 = (Button) findViewById(R.id.main_bottom_btn0);
        mBtn1 = (Button) findViewById(R.id.main_bottom_btn1);
        mBtn2 = (Button) findViewById(R.id.main_bottom_btn2);
        mBtn3 = (Button) findViewById(R.id.main_bottom_btn3);
    }

    private void setListener() {
        mBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChioceItem(0);
            }
        });

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChioceItem(1);
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChioceItem(2);
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChioceItem(3);
            }
        });
    }

    private void init() {
        fManager = getSupportFragmentManager();
        setChioceItem(0);
    }

    // 定义一个选中一个item后的处理
    protected void setChioceItem(int index) {
        if (select == index) {
            return;
        }
        select = index;
        // 重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (mFragment0 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mFragment0 = new MengzhuFragment();
                    transaction.add(R.id.main_framelayout, mFragment0);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragment0);
                }
                break;

            case 1:
                if (mFragment1 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mFragment1 = new MashangFragment();
                    transaction.add(R.id.main_framelayout, mFragment1);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragment1);
                }
                break;

            case 2:
                if (mFragment2 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mFragment2 = new DingdanFragment();
                    transaction.add(R.id.main_framelayout, mFragment2);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragment2);
                }
                break;

            case 3:
                if (mFragment3 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mFragment3 = new MeFragment();
                    transaction.add(R.id.main_framelayout, mFragment3);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragment3);
                }
                break;
        }
        transaction.commit();
    }

    // 隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (mFragment0 != null) {
            transaction.hide(mFragment0);
        }
        if (mFragment1 != null) {
            transaction.hide(mFragment1);
        }
        if (mFragment2 != null) {
            transaction.hide(mFragment2);
        }
        if (mFragment3 != null) {
            transaction.hide(mFragment3);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 连续点击退出客户端
            long now = System.currentTimeMillis();
            if (now - mLastBackKeyDown < 1000) {
                finish();
                return true;
            } else {
                mLastBackKeyDown = now;
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
