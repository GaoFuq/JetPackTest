# JetPackTest
jetpack组件测试demo


# 初衷
想要把ViewModel+DataBinding+LiveData有机结合，提炼出一个可复用的模板，或者说套路。
我的一个思路是把 LiveData对象，通过DataBinding对象的binding.setXxx传入布局文件。我尝
试确实这样可以把数据传进去，但是我却不知道如何使用这个数据。希望有人指点迷津。


# 这里分享我搞了半天撸出来的xxx
## 主要有三个类
1. **SuperBindingViewHolder**   => *ViewHolder的基类*
2. **RVBindingAdapter** => *RecycleView Adapter的基类*
3. **BindingCustomAttribute** => *DataBinding的自定义属性*

# 直接上代码
## SuperBindingViewHolder
```
/**
 * ViewHolder基类
 */
public class SuperBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public  T getBinding() {
        return binding;
    }

    private final T binding;
    
    public SuperBindingViewHolder(View itemView) {
   	    //构造方法，传入item布局文件生成的view
        super(itemView);
        //通过DataBindingUtil.bind()方法，使DataBinding绑定布局，并且返回ViewDataBinding的子类对象
        binding = DataBindingUtil.bind(itemView);
    }
    
}
```
## RVBindingAdapter

```
public abstract class RVBindingAdapter<T> extends RecyclerView.Adapter<SuperBindingViewHolder> {


    protected Context mContext;
    private LayoutInflater mInflater;
    protected List<T> mDataList = new ArrayList<>();//数据集合
    private int BR_id;//数据的实体类在BR文件中的id。在绑定到xml中后，在**！项目编译后 ！**会自动生成这个BR文件。

    public RVBindingAdapter(Context context, int br_id) {//构造方法传入上下文 和 BR_id
        mContext = context;
        BR_id = br_id;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getLayoutId(), parent, false);
        return new SuperBindingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperBindingViewHolder holder, final int position) {
    	//把数据实体类的信息传递给xml文件，同时把item在recycleView中对应位置的数据传过去
        holder.getBinding().setVariable(BR_id,mDataList.get(position));
        //立即执行绑定
        holder.getBinding().executePendingBindings();
        setPresentor(holder,position);
    }

	//设置item里面的view的事件
    public abstract void setPresentor(SuperBindingViewHolder holder, int position);
    
	//设置item布局文件id
    public abstract int getLayoutId();

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<T> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);

        if(position != (getDataList().size())){ // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position,this.mDataList.size()-position);
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}
```
## BindingCustomAttribute
```
public class BindingCustomAttribute {

    @BindingAdapter({"url"})
    public static void setImgUrl(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
}

```
# 使用三步走
0.普通的RecycleView，layout根布局的item文件

1.创建数据集合，获取数据,把数据给adapter
2.创建adapter
3.给RecycleView设置adapter
#### 布局文件
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <RelativeLayout
        android:id="@+id/rl_web"
        android:visibility="gone"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="3">
        <WebView
            android:id="@+id/webView"
            android:background="#90909090"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />
        <TextView
            android:id="@+id/tv_close"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="X"
            android:textSize="20sp"
            android:paddingStart="8dp"
            android:paddingEnd="8dp"
            android:paddingTop="3dp"
            android:paddingBottom="3dp"
            android:layout_alignParentEnd="true"
            android:background="#456963"
            android:textColor="#800"
            />
    </RelativeLayout>

</LinearLayout>
```

### item布局
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="news"
            type="com.gfq.jetpacktest.News.ResultBean" />
        <variable
            name="presenter"
            type="com.gfq.jetpacktest.MainActivity_2.Presenter" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:orientation="vertical"
        android:padding="10dp"
        tools:context=".MainActivity">

        <TextView
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{news.title}"
            android:textSize="20sp" />

        <TextView
            android:id="@+id/time"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{news.passtime}"
            />

        <ImageView
            android:id="@+id/img"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{presenter.openWebView}"
            url="@{news.image}"/>

        <View
            android:layout_width="match_parent"
            android:layout_height="10dp"
            android:layout_marginTop="10dp"
            android:background="#90909090" />
    </LinearLayout>
</layout>
```
### MainActivity
```

public class MainActivity_2 extends AppCompatActivity {
    RVBindingAdapter<News.ResultBean> adapter;
    List<News.ResultBean> list;
    WebView webView;
    TextView close;
    RelativeLayout rlWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        
        //无关代码...
        
        list = new ArrayList<>();//装数据的集合
        init();
        adapter = new RVBindingAdapter<News.ResultBean>(this, BR.news) {
            @Override
            public void setPresentor(SuperBindingViewHolder holder, int position) {
            	//给item里面的view绑定事件
                holder.getBinding().setVariable(BR.presenter, new Presenter(position));
            }

            @Override
            public int getLayoutId() {
                return R.layout.activity_item2;
            }
        };
		//设置适配器
        ((RecyclerView) findViewById(R.id.rv)).setAdapter(adapter);

    
    private void init() {
        HttpUtil.executeMethod(HttpUtil.api().getWangYiNews("1", "20"), new HttpUtil.OnCallBack<News>() {
            @Override
            public void onSucceed(News data) {
                //获取数据
                list = data.getResult();
                //把数据设置给adapter
                adapter.setDataList(list);
            }
        });
    }

    public class Presenter {
        int position;
        
        public Presenter(int position) {
            this.position = position;
        }
        //在这里定义具体的事件行为
        //注意：方法的参数一定要有(View v)
         public void openWebView(View view){
                rlWeb.setVisibility(View.VISIBLE);
                webView.onResume();
                webView.loadUrl(adapter.getDataList().get(position).getPath());
        }

```
