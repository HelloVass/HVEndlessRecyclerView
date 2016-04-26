package geeklub.org.hellovass.common_adapter.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;

/**
 * Created by HelloVass on 16/4/25.
 */
public class HVGridLayoutManager extends GridLayoutManager implements HVLayoutManager {

  public HVGridLayoutManager(Context context, int spanCount) {
    super(context, spanCount);
  }

  public HVGridLayoutManager(Context context, int spanCount, int orientation,
      boolean reverseLayout) {
    super(context, spanCount, orientation, reverseLayout);
  }

  public HVGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override public int findHVLastVisibleItemPosition() {
    return findLastVisibleItemPosition();
  }

  @Override public int getHVItemCount() {
    return getItemCount();
  }

  @Override public void setBaseRcvAdapter(BaseRcvAdapter baseRcvAdapter) {
    setSpanSizeLookup(new MySpanSizeLookup(baseRcvAdapter, getSpanCount()));
  }

  private static class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private BaseRcvAdapter mBaseRcvAdapter;

    private int mSpanCount;

    public MySpanSizeLookup(BaseRcvAdapter baseRcvAdapter, int spanCount) {
      mBaseRcvAdapter = baseRcvAdapter;
      mSpanCount = spanCount;
    }

    @Override public int getSpanSize(int position) {
      if (mBaseRcvAdapter.isFooter(position) || mBaseRcvAdapter.isHeader(position)) {
        return mSpanCount;
      }
      return 1;
    }
  }
}
