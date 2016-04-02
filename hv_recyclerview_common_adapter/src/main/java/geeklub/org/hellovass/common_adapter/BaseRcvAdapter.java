package geeklub.org.hellovass.common_adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import geeklub.org.hellovass.common_adapter.listener.OnRcvItemClickListener;
import geeklub.org.hellovass.common_adapter.listener.OnRcvItemLongClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloVass on 15/10/27.
 */
public abstract class BaseRcvAdapter<DATA> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

  private static final String TAG = BaseRcvAdapter.class.getSimpleName();

  private static final int TYPE_OFFSET_HEADER = 10000;

  private static final int TYPE_OFFSET_FOOTER = 20000;

  protected Context mContext;

  private List<DATA> mDataList;

  private List<View> mHeaderViews = new ArrayList<>();

  private List<View> mFooterViews = new ArrayList<>();

  private OnRcvItemClickListener mOnItemClickListener;

  private OnRcvItemLongClickListener mOnItemLongClickListener;

  public BaseRcvAdapter(Context context, List<DATA> dataList) {
    this.mContext = context;
    this.mDataList = dataList;
  }

  public void setOnItemClickListener(OnRcvItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public void setOnItemLongClickListener(OnRcvItemLongClickListener onItemLongClickListener) {
    mOnItemLongClickListener = onItemLongClickListener;
  }

  public void setDataList(List<DATA> dataList) {
    mDataList = dataList;
  }

  public List<DATA> getDataList() {
    return mDataList;
  }

  public void appendDataList(List<DATA> dataList) {
    mDataList.addAll(dataList);
  }

  public void clearDataList() {
    mDataList.clear();
  }

  public boolean isDataListEmpty() {
    return mDataList == null || mDataList.isEmpty();
  }

  public DATA getDataItem(int position) {
    if (mDataList == null || position < 0 || position >= mDataList.size()) {
      return null;
    }
    return mDataList.get(position);
  }

  @Override public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    Log.i(TAG, "viewType -->>>" + viewType);

    if (viewType >= TYPE_OFFSET_FOOTER) { // 如果是 Footer
      return new BaseRecyclerViewHolder(mFooterViews.get(viewType - TYPE_OFFSET_FOOTER));
    }

    if (viewType >= TYPE_OFFSET_HEADER) { // 如果是 Header
      return new BaseRecyclerViewHolder(mHeaderViews.get(viewType - TYPE_OFFSET_HEADER));
    }

    // 如果是“Content”列表中的数据
    final BaseRecyclerViewHolder viewHolder = new BaseRecyclerViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false));

    viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mOnItemClickListener != null) {
          mOnItemClickListener.onItemClick(v,
              viewHolder.getAdapterPosition() - getHeaderViewCount());
        }
      }
    });

    viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        return mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(v,
            viewHolder.getAdapterPosition() - getHeaderViewCount());
      }
    });

    return viewHolder;
  }

  @Override public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

    int dataItemPosition = getDataItemPosition(position);

    if (dataItemPosition != -1) { // 当前是“Content Item”
      DATA data = getDataItem(dataItemPosition);
      convert(holder, data, getDataItemViewTypeHV(data));
    }
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);

    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

    if (manager instanceof GridLayoutManager) {

      final GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);

      gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {

          if (getItemViewType(position) >= TYPE_OFFSET_FOOTER) {
            return gridLayoutManager.getSpanCount();
          }

          if (getItemViewType(position) >= TYPE_OFFSET_HEADER) {
            return gridLayoutManager.getSpanCount();
          }

          return 1;
        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(BaseRecyclerViewHolder holder) {
    super.onViewAttachedToWindow(holder);

    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

    if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {

      StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutParams =
          (StaggeredGridLayoutManager.LayoutParams) layoutParams;

      if (getItemViewType(holder.getLayoutPosition()) >= TYPE_OFFSET_FOOTER) {
        staggeredGridLayoutParams.setFullSpan(true);
      }

      if (getItemViewType(holder.getLayoutPosition()) >= TYPE_OFFSET_HEADER) {
        staggeredGridLayoutParams.setFullSpan(true);
      }

      staggeredGridLayoutParams.setFullSpan(false);
    }

    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
      StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
      p.setFullSpan(getItemViewType(holder.getLayoutPosition()) >= TYPE_OFFSET_FOOTER);
    }
  }

  @Override public int getItemViewType(int position) {

    int headerViewCount = getHeaderViewCount(); // 得到“header”的个数

    int footerViewCount = getFooterViewCount(); // 得到”Footer“的个数

    int totalDataCount = getDataItemCount(); // 得到“Content”的个数

    if (headerViewCount > 0 && position < headerViewCount) {
      return position + TYPE_OFFSET_HEADER;
    }

    position -= headerViewCount;
    int dataPosition = position;

    if (position < totalDataCount) { // 如果“position”处于“Content”范围，return Content Item 对应的 type
      return getDataItemViewTypeHV(getDataItem(dataPosition));
    } else {

      position -= totalDataCount;
      if (position < footerViewCount) {
        return position + TYPE_OFFSET_FOOTER;
      }
    }

    if (position == 0) {
      return 0;
    }

    throw new IndexOutOfBoundsException(
        "Invalid index " + dataPosition + ", size is " + getDataItemCount());
  }

  private int getDataItemPosition(int position) {

    int headerViewCount = getHeaderViewCount(); // 得到“header”的个数

    int totalDataCount = getDataItemCount(); // 得到“Content”的个数

    if (headerViewCount > 0 && position < headerViewCount) {
      return -1; // 如果是 Header Item
    }

    position -= headerViewCount;

    if (position < totalDataCount) {
      return position; // 如果是 Content Item
    } else {
      return -1; // 如果是 Footer Item
    }
  }

  @Override public int getItemCount() {
    return getHeaderViewCount() + getDataItemCount() + getFooterViewCount();
  }

  public int getHeaderViewCount() {
    return mHeaderViews.size();
  }

  /**
   * 得到“Content中item”的个数
   */
  public int getDataItemCount() {
    if (mDataList == null) {
      return 0;
    }
    return mDataList.size();
  }

  public int getFooterViewCount() {
    return mFooterViews.size();
  }

  /**
   * 添加“Header”
   */
  public void addHeaderView(View view) {
    for (int i = 0; i < mHeaderViews.size(); i++) {
      View headerView = mHeaderViews.get(i);
      if (headerView == view) {
        return;
      }
    }
    mHeaderViews.add(view);
  }

  /**
   * 添加“Footer”
   */
  public void addFooterView(View view) {
    for (int i = 0; i < mFooterViews.size(); i++) {
      View footerView = mFooterViews.get(i);
      if (footerView == view) {
        return;
      }
    }
    mFooterViews.add(view);
  }

  /**
   * @param holder ViewHolder
   * @param data Model
   * @param dataItemViewTypeHV data 对应的“Type”字段
   */
  protected abstract void convert(BaseRecyclerViewHolder holder, DATA data, int dataItemViewTypeHV);

  /**
   * 返回“DATA”中对应的“type”字段
   */
  protected abstract int getDataItemViewTypeHV(DATA data);

  /**
   * 返回“item”的“布局Id”
   */
  protected abstract int getLayoutResId(int viewType);
}
