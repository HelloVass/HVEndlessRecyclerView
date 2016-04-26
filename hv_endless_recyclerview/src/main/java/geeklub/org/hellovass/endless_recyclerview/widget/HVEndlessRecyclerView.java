package geeklub.org.hellovass.endless_recyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;
import geeklub.org.hellovass.common_adapter.layoutmanager.HVLayoutManager;
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

  private boolean mIsLoading = false; // 是否正在加载

  private boolean mHasMore = true; // 是否还有更多数据

  private boolean mAutoLoadMore = true; // 是否自动加载更多

  private boolean mLoadError = false; // 是否加载失败

  private boolean mIsEmpty = false; // list 是否为空

  private ILoadMoreUIHandler mLoadMoreUIHandler;

  private ILoadMoreHandler mLoadMoreHandler;

  private BaseRcvAdapter<?> mBaseRcvAdapter;

  private HVLayoutManager mHVLayoutManager;

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

  public void setHVLayoutManager(HVLayoutManager layoutManager) {
    super.setLayoutManager((LayoutManager) layoutManager);
    mHVLayoutManager = layoutManager;
  }

  public void setHVAdapter(BaseRcvAdapter adapter) {
    super.setAdapter(adapter);
    mBaseRcvAdapter = adapter;
  }

  public void useDefaultFooter() {
    DefaultLoadMoreFooterView loadMoreFooterView = new DefaultLoadMoreFooterView(getContext());
    loadMoreFooterView.setVisibility(GONE);
    setLoadMoreView(loadMoreFooterView);
    setLoadMoreUIHandler(loadMoreFooterView);
  }

  /**
   * 设置 footer
   *
   * @param view footer
   */
  @Override public void setLoadMoreView(View view) {

    if (mBaseRcvAdapter == null) {
      throw new IllegalStateException("must set BaseRcvAdapter first!");
    }

    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        tryToPerformLoadMore();
      }
    });

    addLoadMoreFooterView(view);
  }

  @Override public void setAutoLoadMore(boolean autoLoadMore) {
    mAutoLoadMore = autoLoadMore;
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

  private void setInternalOnScrollListener() {

    addOnScrollListener(new OnRcvScrollListener(mHVLayoutManager) {

      @Override public void onBottom() {

        if (mLoadError) { // 如果加载出错，return
          return;
        }
        if (mAutoLoadMore) { // 如果mAutoLoadMore被设置为true
          tryToPerformLoadMore();
        } else {
          if (mHasMore) { // 如果有更多数据
            mLoadMoreUIHandler.onWaitToLoadMore(); // 显示”点击加载更多“
          }
        }
      }
    });
  }

  private void tryToPerformLoadMore() {

    if (mIsLoading) { // 如果正在加载中，return
      return;
    }

    if (!mHasMore && !mIsEmpty) { // 如果没有更多数据并且数据为空
      return;
    }

    mIsLoading = true;

    if (mLoadMoreUIHandler != null) {
      mLoadMoreUIHandler.onLoading(); // 显示正在加载中
    }

    if (mLoadMoreHandler != null) {
      mLoadMoreHandler.onLoadMore(); // 开始加载数据
    }
  }

  private void addLoadMoreFooterView(View view) {
    mBaseRcvAdapter.addFooterView(view);
  }
}
