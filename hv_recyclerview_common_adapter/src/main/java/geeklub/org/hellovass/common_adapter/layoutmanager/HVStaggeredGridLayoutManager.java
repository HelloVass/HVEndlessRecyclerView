package geeklub.org.hellovass.common_adapter.layoutmanager;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;

/**
 * Created by HelloVass on 16/4/25.
 */
public class HVStaggeredGridLayoutManager extends StaggeredGridLayoutManager
    implements HVLayoutManager {

  public HVStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public HVStaggeredGridLayoutManager(int spanCount, int orientation) {
    super(spanCount, orientation);
  }

  @Override public int findHVLastVisibleItemPosition() {
    int[] lastPositions = new int[getSpanCount()];
    findLastVisibleItemPositions(lastPositions);
    return findMax(lastPositions);
  }

  @Override public int getHVItemCount() {
    return getItemCount();
  }

  @Override public void setBaseRcvAdapter(BaseRcvAdapter baseRcvAdapter) {

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
}
