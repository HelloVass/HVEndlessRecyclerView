package geeklub.org.hellovass.endless_recyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import geeklub.org.hellovass.endless_recyclerview.R;
import geeklub.org.hellovass.endless_recyclerview.loadmore.ILoadMoreUIHandler;

/**
 * Created by HelloVass on 15/11/19.
 *
 * 自定义的 LoadMoreView，如果嫌麻烦，可以用我写的
 */
public class DefaultLoadMoreFooterView extends RelativeLayout implements ILoadMoreUIHandler {

  private static final String TAG = DefaultLoadMoreFooterView.class.getSimpleName();

  private TextView mLoadingHint;

  public DefaultLoadMoreFooterView(Context context) {
    this(context, null);
  }

  public DefaultLoadMoreFooterView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DefaultLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    View.inflate(getContext(), R.layout.listitem_default_load_more_footer, this);
    mLoadingHint = (TextView) findViewById(R.id.loading_text);
    // 设置样式
    setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    setBackgroundResource(R.drawable.footer_default_load_more);
  }

  @Override public void onLoading() {
    setVisibility(VISIBLE);
    mLoadingHint.setText("加载中...");
  }

  @Override public void onLoadFinish(boolean isEmpty, boolean hasMore) {
    if (!hasMore) {
      setVisibility(VISIBLE);
      if (isEmpty) {
        mLoadingHint.setText("数据为空");
      } else {
        mLoadingHint.setText("没有更多数据");
      }
    } else {
      setVisibility(GONE);
    }
  }

  @Override public void onLoadError(int errorCode, String errorMsg) {
    setVisibility(VISIBLE);
    mLoadingHint.setText("加载出错，点击重试");
  }

  @Override public void onWaitToLoadMore() {
    setVisibility(VISIBLE);
    mLoadingHint.setText("点击加载更多");
  }
}
