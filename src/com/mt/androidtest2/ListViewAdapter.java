package com.mt.androidtest2;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    ArrayList <HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    ArrayList <Method> mMethodList = new ArrayList<Method>();
    private LayoutInflater listContainer;
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private int mMode=0;
    private Context mContext=null;
    public ListViewAdapter(Context context) {
        listContainer = LayoutInflater.from(context);
        metric  = context.getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
        mContext = context.getApplicationContext();
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setMode(int mode){
    	mMode = mode;
    }
    
    public void setupList(ArrayList<HashMap<String, Object>> list) {
    	mList.clear();
    	for(int i = 0;i<list.size();i++){
    		mList.add(list.get(i));
    	}
    }

	public void setupList(String [] mArrayFT){
		mList.clear();
		HashMap<String, Object> map = null;
		for(int i=0;i<mArrayFT.length;i++){
			map = new HashMap<String, Object>();
			map.put("itemText", mArrayFT[i]);
			mList.add(map);
		}
	}
    
	/**
	 * 反射获取对象内部的所有方法
	 * @param obj
	 */
	public void setupList(Object obj){
		mList.clear();
		mMethodList.clear();
		String methodName=null;
		HashMap<String, Object> map = null;
		Class<?> mClass = obj.getClass();
		Method [] mMethods = mClass.getDeclaredMethods();
		for(Method mMethod:mMethods){
			methodName = mMethod.getName();
			map = new HashMap<String, Object>();
			map.put("itemText", methodName);
			mList.add(map);
			mMethodList.add(mMethod);
		}
	}
	
	/**
	 * 在有ScrollView存在的时候，ListView显示全部内容而不是收缩。
	 * 说明：默认情况下Android是禁止在ScrollView中放入另外的ScrollView的，它的高度是无法计算的。
	 * 解决思路：在设置完ListView的Adapter后，根据ListView的子项目重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView，这样它的高度就正确了
	 * 注意事项：
	 * 1)子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
	 * 2)在ScrollView中嵌套ListView(或者ScrollView)的另外一个问题就是，子ScrollView中无法滑动的(如果它没有显示完全的话)，因为滑动事件会被父ScrollView吃掉，如果想要让子ScrollView也可以滑动，只能强行截取滑动事件
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            switch(mMode){
        	case 1:
        		view = listContainer.inflate(R.layout.item_getview, parent,false);
           	break;
        	case 2:
        		view = listContainer.inflate(R.layout.item_getview_function, parent,false);
           	break;           	
            }
        }else {
        	view = convertView;
        }
        if(1==mMode){
			ImageView image = (ImageView)view.findViewById(R.id.menu_img);
	        TextView title = (TextView)view.findViewById(R.id.menu_label);
	        Object obj = mList.get(position).get("itemImage");
	        if(obj instanceof Drawable){
	        	image.setImageDrawable((Drawable)obj);
	        }else if(obj instanceof Integer){
	        	image.setImageResource((Integer)obj);
				view.setBackgroundColor(mContext.getResources().getColor(R.color.wheat));
	        }
	        title.setText((String) mList.get(position).get("itemText"));
	        setLayoutParams(image);
        }else if(2==mMode){
        	TextView mTvFT = (TextView)view.findViewById(R.id.text_ft);
        	mTvFT.setText((String) mList.get(position).get("itemText"));
        }
        return view;
    }
    /**
     * setLayoutParams: Define the LayoutParams of mView to avoid being too big to display
     * @param mView
     */
    public void setLayoutParams(View mView){
    	ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*0.3);//144;
    	lp.height = (int)(mDensityDpi*0.3);//144;
    	mView.setLayoutParams(lp);
    }
}
