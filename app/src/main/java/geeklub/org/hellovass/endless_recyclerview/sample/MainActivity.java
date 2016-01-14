package geeklub.org.hellovass.endless_recyclerview.sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import geeklub.org.hellovass.common_adapter.listener.OnRcvItemClickListener;
import geeklub.org.hellovass.common_adapter.listener.OnRcvItemLongClickListener;
import geeklub.org.hellovass.endless_recyclerview.loadmore.ILoadMoreHandler;
import geeklub.org.hellovass.endless_recyclerview.widget.HVEndlessRecyclerView;
import geeklub.org.hellovass.endless_recyclerview.widget.HVSwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.swipe_refresh_layout) HVSwipeRefreshLayout mRefreshLayout;

  @Bind(R.id.recycler_view) HVEndlessRecyclerView mRecyclerView;

  @Bind(R.id.toolbar) Toolbar mToolbar;

  private SampleAdapter mSampleAdapter;

  private List<String> mDataSource;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mDataSource = new ArrayList<>();
    mSampleAdapter = new SampleAdapter(this, mDataSource);
    mSampleAdapter.setOnItemClickListener(new OnRcvItemClickListener() {
      @Override public void onItemClick(View v, int position) {
        Toast.makeText(MainActivity.this, "点击事件支持，item position -->>" + position,
            Toast.LENGTH_SHORT).show();
      }
    });
    mSampleAdapter.setOnItemLongClickListener(new OnRcvItemLongClickListener() {
      @Override public void onItemLongClick(View view, int position) {
        Toast.makeText(MainActivity.this, "长按点击事件支持，item position -->>" + position,
            Toast.LENGTH_SHORT).show();
      }
    });

    setUpToolbar();
    setUpRecyclerView();
    setUpRefreshLayout();
  }

  private void setUpToolbar() {
    setSupportActionBar(mToolbar);
  }

  private void setUpRefreshLayout() {
    mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .map(new Func1<Long, Object>() {
              @Override public Object call(Long aLong) {
                fetchingNewData();
                mRefreshLayout.setRefreshing(false);
                mSampleAdapter.notifyDataSetChanged();
                return null;
              }
            })
            .subscribe();
      }
    });

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        getDummyDataSource();
        mRefreshLayout.setRefreshing(false);
        mSampleAdapter.notifyDataSetChanged();
      }
    }, 2000);
  }

  private void setUpRecyclerView() {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setHVEndlessRecyclerViewAdapter(mSampleAdapter);
    mRecyclerView.useDefaultFooter();// 注意设置 FooterView 的设置顺序
    mRecyclerView.setLoadMoreHandler(new ILoadMoreHandler() {
      @Override public void onLoadMore() {
        simulateLoadMoreData();
      }
    });
  }

  private List<String> getDummyDataSource() {
    for (int i = 1; i <= 10; i++) {
      mDataSource.add("Hello HVEndlessRecyclerView -->>" + i);
    }
    return mDataSource;
  }

  private void fetchingNewData() {
    mDataSource.clear();
    for (int i = 0; i <= 1; i++) {
      mDataSource.add("刷新得到的数据 -->>" + System.currentTimeMillis());
    }
  }

  private void loadMoreData() {
    for (int i = 0; i < 1; i++) {
      mDataSource.add("加载更多数据 -->>" + i);
    }
  }

  private void simulateLoadMoreData() {
    Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
        .map(new Func1<Long, Object>() {
          @Override public Object call(Long aLong) {
            loadMoreData();
            mRecyclerView.onLoadMoreCompleted(false, true);
            mSampleAdapter.notifyDataSetChanged();
            return null;
          }
        })
        .subscribe();
  }
}
