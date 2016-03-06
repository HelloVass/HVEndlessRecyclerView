package geeklub.org.hellovass.endless_recyclerview.sample;

import android.content.Context;
import android.widget.TextView;
import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;
import geeklub.org.hellovass.common_adapter.BaseRecyclerViewHolder;
import java.util.List;

/**
 * Created by HelloVass on 15/11/10.
 */
public class SampleAdapter extends BaseRcvAdapter<String> {

  private static final int ITEM_VIEW_TYPE_1 = 123;

  public SampleAdapter(Context context, List<String> strings) {
    super(context, strings);
  }

  @Override protected void convert(BaseRecyclerViewHolder holder, String caption,int viewType) {
    TextView title = holder.getView(R.id.tv_caption);
    title.setText(caption);
  }

  @Override protected int getItemViewTypeHV(String s) {
    return ITEM_VIEW_TYPE_1;
  }

  @Override protected int getLayoutResId(int i) {
    return R.layout.item_sample;
  }
}
