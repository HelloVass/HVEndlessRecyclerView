package geeklub.org.hellovass.endless_recyclerview.loadmore;

import android.view.View;

/**
 * Created by HelloVass on 15/11/19.
 */
public interface ILoadMoreContainer {

  void setAutoLoadMore(boolean autoLoadMore);

  void setLoadMoreView(View view);

  void setLoadMoreUIHandler(ILoadMoreUIHandler loadMoreUIHandler);

  void setLoadMoreHandler(ILoadMoreHandler loadMoreHandler);

  void onLoadMoreCompleted(boolean isEmpty, boolean hasMore);

  void onLoadMoreFailed(int errorCode, String errorMsg);
}
