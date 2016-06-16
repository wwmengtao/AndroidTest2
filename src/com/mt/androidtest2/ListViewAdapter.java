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
	 * �����ȡ�����ڲ������з���
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
	 * ����ScrollView���ڵ�ʱ��ListView��ʾȫ�����ݶ�����������
	 * ˵����Ĭ�������Android�ǽ�ֹ��ScrollView�з��������ScrollView�ģ����ĸ߶����޷�����ġ�
	 * ���˼·����������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶ȣ�Ȼ��Ѹ߶�����ΪLayoutParams���ø�ListView���������ĸ߶Ⱦ���ȷ��
	 * ע�����
	 * 1)��ListView��ÿ��Item������LinearLayout�������������ģ���Ϊ������Layout(��RelativeLayout)û����дonMeasure()�����Ի���onMeasure()ʱ�׳��쳣��
	 * 2)��ScrollView��Ƕ��ListView(����ScrollView)������һ��������ǣ���ScrollView���޷�������(�����û����ʾ��ȫ�Ļ�)����Ϊ�����¼��ᱻ��ScrollView�Ե��������Ҫ����ScrollViewҲ���Ի�����ֻ��ǿ�н�ȡ�����¼�
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
