package geeklub.org.hellovass.endless_recyclerview.loadmore;

/**
 * Created by HelloVass on 15/11/19.
 *
 * 对 LoadMoreView 的抽象
 */
public interface ILoadMoreUIHandler {

  void onLoading();

  void onLoadFinish(boolean isEmpty, boolean hasMore);

  void onLoadError(int errorCode, String errorMsg);

  void onWaitToLoadMore();
}
