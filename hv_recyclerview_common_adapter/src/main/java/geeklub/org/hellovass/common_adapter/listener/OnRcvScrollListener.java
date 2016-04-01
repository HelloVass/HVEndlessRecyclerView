package geeklub.org.hellovass.common_adapter.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by HelloVass on 15/10/27.
 *
 * 自定义的 ScrollListener，适配了 LayoutManagerType 是 LinearLayoutManager、
 * GridLayoutManager、StaggeredGridLayoutManager 时，item 滑动到底部的监听
 */
public abstract class OnRcvScrollListener extends RecyclerView.OnScrollListener {

  private static final String TAG = OnRcvScrollListener.class.getSimpleName();

  private enum LayoutManagerType {
    Linear,
    Grid,
    StaggeredGrid
  }

  private LayoutManagerType mLayoutManagerType;

  private int mLastVisibleItemPosition;

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

    if (mLayoutManagerType == null) {
      if (layoutManager instanceof GridLayoutManager) {
        mLayoutManagerType = LayoutManagerType.Grid;
      } else if (layoutManager instanceof LinearLayoutManager) {
        mLayoutManagerType = LayoutManagerType.Linear;
      } else if (layoutManager instanceof StaggeredGridLayoutManager) {
        mLayoutManagerType = LayoutManagerType.StaggeredGrid;
      } else {
        throw new RuntimeException("Unsupported LayoutManager used.");
      }
    }

    switch (mLayoutManagerType) {

      case Linear:
        mLastVisibleItemPosition =
            ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        break;

      case Grid:
        mLastVisibleItemPosition =
            ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        break;

      case StaggeredGrid:
        StaggeredGridLayoutManager staggeredGridLayoutManager =
            (StaggeredGridLayoutManager) layoutManager;
        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
        staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
        mLastVisibleItemPosition = findMax(lastPositions);
        break;
    }

    int totalItemCount = layoutManager.getItemCount();

    if (mLastVisibleItemPosition >= totalItemCount - 1) {
      onBottom();
    }
  }

  private int findMax(int[] lastPositions) {
    int max = lastPositions[0];
    for (int value : lastPositions) {
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  /**
   * item 滑动到底部时会回调这个方法
   */
  public abstract void onBottom();
}
