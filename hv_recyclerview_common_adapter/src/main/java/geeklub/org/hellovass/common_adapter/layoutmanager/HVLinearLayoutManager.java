package geeklub.org.hellovass.common_adapter.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;

/**
 * Created by HelloVass on 16/4/25.
 */
public class HVLinearLayoutManager extends LinearLayoutManager implements HVLayoutManager {

  public HVLinearLayoutManager(Context context) {
    super(context);
  }

  public HVLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, reverseLayout);
  }

  public HVLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
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

  }
}
