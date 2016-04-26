package geeklub.org.hellovass.common_adapter.listener;

import android.support.v7.widget.RecyclerView;
import geeklub.org.hellovass.common_adapter.layoutmanager.HVLayoutManager;

/**
 * Created by HelloVass on 15/10/27.
 */
public abstract class OnRcvScrollListener extends RecyclerView.OnScrollListener {

  private static final String TAG = OnRcvScrollListener.class.getSimpleName();

  private HVLayoutManager mHVLayoutManager;

  public OnRcvScrollListener(HVLayoutManager hvLayoutManager) {
    if (hvLayoutManager == null) {
      throw new IllegalStateException("hvLayoutManager can't be null");
    }
    mHVLayoutManager = hvLayoutManager;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    int totalItemCount = mHVLayoutManager.getHVItemCount();

    if (mHVLayoutManager.findHVLastVisibleItemPosition() >= totalItemCount - 1) {
      onBottom();
    }
  }

  public abstract void onBottom();
}
