package geeklub.org.hellovass.common_adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by HelloVass on 15/10/30.
 *
 * 辅助类，不用继承这个 ViewHolder
 */
public final class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

  private SparseArray<View> mViewSparseArray;

  private View mConvertView;

  public BaseRecyclerViewHolder(View itemView) {
    super(itemView);
    this.mConvertView = itemView;
    this.mViewSparseArray = new SparseArray<>();
  }

  /**
   * 通过传入的 viewId 找到相应的 子控件
   *
   * @param viewId childView 的 ID
   * @param <V> childView
   */
  public <V extends View> V getView(int viewId) {
    View view = mViewSparseArray.get(viewId);
    if (view == null) {
      view = mConvertView.findViewById(viewId);
      mViewSparseArray.put(viewId, view);
    }
    return (V) view;
  }

  public View getConvertView() {
    return mConvertView;
  }
}
