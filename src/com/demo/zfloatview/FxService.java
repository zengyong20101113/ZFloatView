package com.demo.zfloatview;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FxService extends Service {
	
	private static final String TAG = "FxService";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate");
		createFloatView();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	
	WindowManager mWindowManager;

	@SuppressLint("ServiceCast")
	private void createFloatView() {
		
		wmParams = new WindowManager.LayoutParams();
		 //获取的是WindowManagerImpl.CompatModeWrapper  
		mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WALLPAPER_SERVICE);
		Log.d(TAG, "mWindowManager--->" + mWindowManager);  
		
		//设置window type  
        wmParams.type = LayoutParams.TYPE_PHONE; 
        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
       //调整悬浮窗显示的停靠位置为左侧置顶 
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity  
        wmParams.x = 0;
        wmParams.y = 0;
        
        //设置悬浮窗口长宽数据    
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT; 
        
        /*// 设置悬浮窗口长宽数据 
        wmParams.width = 200; 
        wmParams.height = 80;*/ 
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局 
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_view, null);
      //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        
      //浮动窗口按钮  
//        Button button = (Button) mFloatLayout.findViewById(R.id.fv_button);
//        
//        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),  View.MeasureSpec  
//                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        		
//        Log.d(TAG, "Width/2--->" + button.getMeasuredWidth()/2);  
//        Log.d(TAG, "Height/2--->" + button.getMeasuredHeight()/2);  
//        
//        button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				 Toast.makeText(FxService.this, "onClick", Toast.LENGTH_SHORT).show();
//				
//			}
//		});
	}
	
	@Override  
    public void onDestroy()   
    {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        if(mFloatLayout != null)  
        {  
            //移除悬浮窗口  
            mWindowManager.removeView(mFloatLayout);  
        }  
    }  
}
