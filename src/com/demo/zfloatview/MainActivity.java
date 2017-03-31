package com.demo.zfloatview;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView imageview;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		Button start = (Button) findViewById(R.id.button1);
		Button remove = (Button) findViewById(R.id.button2);
		Button popwindow = (Button) findViewById(R.id.button3);

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(MainActivity.this,
				// FxService.class);
				// 启动FxService
				// startService(intent);
				// finish();

				if (mFloatLayout == null) {
					createFloatView();
				}

			}
		});

		remove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// //uninstallApp("com.phicomm.hu");
				// Intent intent = new Intent(MainActivity.this,
				// FxService.class);
				// //终止FxService
				// stopService(intent);

				if (mFloatLayout != null) {
					mWindowManager.removeView(mFloatLayout);
					mFloatLayout = null;
				}
			}
		});

		popwindow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

				int width = wm.getDefaultDisplay().getWidth();
				int height = wm.getDefaultDisplay().getHeight();

				initPopupWindowView();

				popupwindow.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, 0, 0);
				System.out.println("-------initPopupWindowView----v.getRootView----" + v.getRootView() + "----width  :"
						+ width + "----height :" + height);

			}
		});

	}

	private static final String TAG = "FloatWindowTest";
	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	LinearLayout mFloatLayout;

	private void createFloatView() {
		// 获取LayoutParams对象
		wmParams = new WindowManager.LayoutParams();

		// 获取的是LocalWindowManager对象
		mWindowManager = this.getWindowManager();
		Log.i(TAG, "mWindowManager1--->" + this.getWindowManager());
		// mWindowManager = getWindow().getWindowManager();
		Log.i(TAG, "mWindowManager2--->" + getWindow().getWindowManager());

		// 获取的是CompatModeWrapper对象
		// mWindowManager = (WindowManager)
		// getApplication().getSystemService(Context.WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager3--->" + mWindowManager);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.CENTER;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = this.getLayoutInflater();// LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_view, null);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// setContentView(R.layout.main);
		// 浮动窗口按钮
		// Button button = (Button) mFloatLayout.findViewById(R.id.fv_button);

		// Log.d(TAG, "button" + button);
		// Log.d(TAG, "button--parent-->" + button.getParent());
		// Log.d(TAG, "button--parent--parent-->" +
		// button.getParent().getParent());
		// 绑定触摸移动监听

		// 绑定点击监听
		// button.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(MainActivity.this, "onClick",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		imageview = (ImageView) mFloatLayout.findViewById(R.id.iv);

		imageview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("***** isMove = "+isMove +" , isLeft="+isLeft+" , isHide="+isHide);
				if (isMove) {
					handler.sendEmptyMessageDelayed(1, 2 * 1000);
					isMove=false;
				}else if(isHide){
					imageview.setImageResource(R.drawable.dk_suspend_icon_normal);
					isHide = false;
				}else{
					Toast.makeText(getApplicationContext(), "弹出对话框", 1).show();
				}
				System.out.println("---imageview-   onClick---  ");
			}
		});

		imageview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

				sceenWidth = wm.getDefaultDisplay().getWidth();
				sceenHeight = wm.getDefaultDisplay().getHeight();

				int action = event.getAction();
				System.out.println("  onTouch   "+action);
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					isMove = false;
					mTouchStartX = (int) event.getRawX();
					mTouchStartY = (int) event.getRawY();
					mStartX = (int) event.getX();
					mStartY = (int) event.getY();
					if(isHide){
						imageview.setImageResource(R.drawable.dk_suspend_icon_normal);
					}
					
//					System.out.println("----  ACTION_DOWN  ----- mTouchStartX = " + mTouchStartX + " , mTouchStartY=" + mTouchStartY
//							+ " , mStartX=" + mStartX+ " , mStartY=" + mStartY);
					break;
				case MotionEvent.ACTION_MOVE:
					mTouchCurrentX = (int) event.getRawX();
					mTouchCurrentY = (int) event.getRawY();
					wmParams.x += mTouchCurrentX - mTouchStartX;
					wmParams.y += mTouchCurrentY - mTouchStartY;
					mWindowManager.updateViewLayout(ll, wmParams);

					mTouchStartX = mTouchCurrentX;
					mTouchStartY = mTouchCurrentY;
					
//					System.out.println("----  ACTION_MOVE  ----- mTouchCurrentX = " + mTouchCurrentX + " , mTouchCurrentY=" + mTouchCurrentY
//							+ " , wmParams.x=" + wmParams.x+ " , wmParams.y=" + wmParams.y
//							+ " , mTouchStartX=" + mTouchStartX+ " , mTouchStartY=" + mTouchStartY);
					isMove = true;
					break;
				case MotionEvent.ACTION_UP:
//					mStopX = (int) event.getX();
//					mStopY = (int) event.getY();
//					System.out.println("  mStartX=" + mStartX+ " , mStartY=" + mStartY);
//					System.out.println("  mStopX=" + mStopX+ " , mStopY=" + mStopY);
//					if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
//						isMove = true;
//						
//					}
					
					mTouchCurrentX = (int) event.getRawX();
					mTouchCurrentY = (int) event.getRawY();
					if (mTouchCurrentX < (sceenWidth / 2)) {
						wmParams.x = 0;
						wmParams.y += mTouchCurrentY - mTouchStartY;
						isLeft = true;
//						System.out
//								.println("----  ACTION_UP  -----  mStopX = " + mStopX + " , mStopY=" + mStopY + " , mTouchCurrentX="
//										+ mTouchCurrentX + " , mTouchCurrentY=" + mTouchCurrentY + " , mTouchStartX="
//										+ mTouchStartX + " , mTouchStartY=" + mTouchStartY + " ,width/2=" + width/2);
					} else {
						isLeft = false;
						wmParams.x = sceenWidth-v.getRight();
						wmParams.y += mTouchCurrentY - mTouchStartY;
//						System.out.println("++++++++  mStopX = " + mStopX + " , mStopY=" + mStopY + " , mTouchCurrentX="
//								+ mTouchCurrentX + " , mTouchCurrentY=" + mTouchCurrentY + " , mTouchStartX="
//								+ mTouchStartX + " , mTouchStartY=" + mTouchStartY + " , width/2=" + width/2);
					}
					
					mWindowManager.updateViewLayout(ll, wmParams);
//					System.out.println("*******  wmParams.x = "+wmParams.x+" , ******* wmParams.y = "+wmParams.y);
//					System.out.println("******* v.getRight()="+v.getRight()+" , v.getLeft()="+v.getLeft()+" , v.getTop()="+v.getTop()+" , v.getBottom()="+v.getBottom());
					break;
				}
				return false;
			}
		});

		ll = (LinearLayout) mFloatLayout.findViewById(R.id.ll_float);

	}

	// 开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
	private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY,sceenWidth,sceenHeight;
	// 开始时的坐标和结束时的坐标（相对于自身控件的坐标）
	private int mStartX, mStartY, mStopX, mStopY;
	private boolean isMove,isHide;// isMove判断悬浮窗是否移动,isHide判断是否隐藏悬浮窗

	private boolean isLeft = false;//控制悬浮窗是在左边还是在右边
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			System.out.println("------handleMessage------");
			if (isLeft) {
				imageview.setImageResource(R.drawable.dk_suspension_left_window_normal);
				isHide = true;
			}else{
				imageview.setImageResource(R.drawable.dk_suspension_right_window_normal);
				
				wmParams.x = sceenWidth;
				wmParams.y += mTouchCurrentY - mTouchStartY;
				mWindowManager.updateViewLayout(ll, wmParams);
				isHide = true;
			}
		};
	};

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;

	private PopupWindow popupwindow;
	private View customView;

	public void initPopupWindowView() {
		// 获取自定义布局文件pop.xml的视图
		customView = getLayoutInflater().inflate(R.layout.popwindow, null, false);

		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置动画效果 [R.style.AnimationFade]
		popupwindow.setAnimationStyle(R.style.AnimationFade);

		// 需要设置一下此参数，点击外边可消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		// 点击窗外可取消
		popupwindow.setTouchable(true);
		popupwindow.setOutsideTouchable(true);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// mWindowManager.removeView(mFloatLayout);
	}
}
