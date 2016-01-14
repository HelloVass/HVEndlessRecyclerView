package geeklub.org.hellovass.endless_recyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;
import geeklub.org.hellovass.common_adapter.listener.OnRcvScrollListener;
import geeklub.org.hellovass.endless_recyclerview.loadmore.ILoadMoreContainer;
import geeklub.org.hellovass.endless_recyclerview.loadmore.ILoadMoreHandler;
import geeklub.org.hellovass.endless_recyclerview.loadmore.ILoadMoreUIHandler;

/**
 * Created by HelloVass on 15/11/19.
 *
 * 自定义的带有 LoadMoreFooterView 的 RecyclerView
 */
public class HVEndlessRecyclerView extends RecyclerView implements ILoadMoreContainer {

  private boolean mIsLoading = false;

  private boolean mHasMore = true;

  private boolean mAutoLoadMore = true;

  private boolean mLoadError = false;

  private boolean mIsEmpty = false;

  private ILoadMoreUIHandler mLoadMoreUIHandler;

  private ILoadMoreHandler mLoadMoreHandler;

  private BaseRcvAdapter<?> mBaseRcvAdapter;

  public HVEndlessRecyclerView(Context context) {
    this(context, null);
  }

  public HVEndlessRecyclerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HVEndlessRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setInternalOnScrollListener();
  }

  private void setInternalOnScrollListener() {
    addOnScrollListener(new OnRcvScrollListener() {
      @Override public void onBottom() {
        if (mLoadError) {
          return;
        }
        if (mAutoLoadMore) {
          tryToPerformLoadMore();
        } else {
          if (mHasMore) {
            mLoadMoreUIHandler.onWaitToLoadMore();
          }
        }
      }
    });
  }

  public void useDefaultFooter() {
    DefaultLoadMoreFooterView loadMoreFooterView = new DefaultLoadMoreFooterView(getContext());
    loadMoreFooterView.setVisibility(GONE);
    setLoadMoreView(loadMoreFooterView);
    setLoadMoreUIHandler(loadMoreFooterView);
  }

  @Override public void setAutoLoadMore(boolean autoLoadMore) {
    mAutoLoadMore = autoLoadMore;
  }

  @Override public void setLoadMoreView(View view) {
    // 设置点击加载更多监听器
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        tryToPerformLoadMore();
      }
    });
    addLoadMoreFooterView(view);
  }

  @Override public void setLoadMoreUIHandler(ILoadMoreUIHandler loadMoreUIHandler) {
    mLoadMoreUIHandler = loadMoreUIHandler;
  }

  @Override public void setLoadMoreHandler(ILoadMoreHandler loadMoreHandler) {
    mLoadMoreHandler = loadMoreHandler;
  }

  @Override public void onLoadMoreCompleted(boolean isEmpty, boolean hasMore) {
    mLoadError = false;
    mIsLoading = false;
    mIsEmpty = isEmpty;
    mHasMore = hasMore;

    if (mLoadMoreUIHandler != null) {
      mLoadMoreUIHandler.onLoadFinish(isEmpty, hasMore);
    }
  }

  @Override public void onLoadMoreFailed(int errorCode, String errorMsg) {
    mLoadError = true;
    mIsLoading = false;

    if (mLoadMoreUIHandler != null) {
      mLoadMoreUIHandler.onLoadError(errorCode, errorMsg);
    }
  }

  private void tryToPerformLoadMore() {

    if (mIsLoading) {
      return;
    }

    if (!mHasMore && !mIsEmpty) {
      return;
    }

    mIsLoading = true;

    // 显示正在加载中
    if (mLoadMoreUIHandler != null) {
      mLoadMoreUIHandler.onLoading();
    }

    // 开始加载数据
    if (mLoadMoreHandler != null) {
      mLoadMoreHandler.onLoadMore();
    }
  }

  private void addLoadMoreFooterView(View view) {
    mBaseRcvAdapter.addFooterView(view);
  }

  public void setHVEndlessRecyclerViewAdapter(BaseRcvAdapter<?> adapter) {
    mBaseRcvAdapter = adapter;
    setAdapter(adapter);
  }


}
