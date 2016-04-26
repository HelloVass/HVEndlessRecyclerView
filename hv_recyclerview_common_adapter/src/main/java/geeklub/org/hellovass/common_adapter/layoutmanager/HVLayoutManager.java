package geeklub.org.hellovass.common_adapter.layoutmanager;

import geeklub.org.hellovass.common_adapter.BaseRcvAdapter;

/**
 * Created by HelloVass on 16/4/25.
 */
public interface HVLayoutManager {

  int findHVLastVisibleItemPosition();

  int getHVItemCount();

  void setBaseRcvAdapter(BaseRcvAdapter baseRcvAdapter);
}
