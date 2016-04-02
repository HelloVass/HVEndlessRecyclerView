# HVEndlessRecyclerView

[![](https://jitpack.io/v/HelloVass/HVEndlessRecyclerView.svg)](https://jitpack.io/#HelloVass/HVEndlessRecyclerView)

## Easy way implements loadmore 



> Thanks [Cube-Sdk](http://cube-sdk.liaohuqiu.net/cn/load-more/)，秋哥的 loadmore 模块真的写得很好！

### 1.Click 事件支持

```java
    // 支持 Click 事件
    mSampleAdapter.setOnItemClickListener(new OnRcvItemClickListener() {
      @Override public void onItemClick(View v, int position) {
        Toast.makeText(MainActivity.this, "点击事件支持，item position -->>" + position,
            Toast.LENGTH_SHORT).show();
      }
    });
```

``` java
    // 支持 LongClick 事件
    mSampleAdapter.setOnItemLongClickListener(new OnRcvItemLongClickListener() {
      @Override public boolean onItemLongClick(View view, int position) {
        Toast.makeText(MainActivity.this, "长按点击事件支持，item position -->>" + position,
            Toast.LENGTH_SHORT).show();
        return true;
      }
    });

```

### 2. 使用默认的 footer

```java
    mRecyclerView.setAdapter(mSampleAdapter);
    mRecyclerView.useDefaultFooter(); // 请在 setAdapter 之后设置默认的 footer
```

### 3.加载更多数据

```java
    mRecyclerView.setLoadMoreHandler(new ILoadMoreHandler() {
          @Override public void onLoadMore() {
            simulateLoadMoreData();
        }
      });
```


### 4. 加载完成之后，设置状态

- 内容是否为空

- 是否有更多数据

```java
/**
   * 
   * @param isEmpty fetch 的数据列表是否为空
   * @param hasMore 是否还有更多数据
   */
  @Override public void onLoadMoreCompleted(boolean isEmpty, boolean hasMore) {
  
  }
```

### 5.加载失败

```java
/**
   * 
   * @param errorCode 错误码
   * @param errorMsg 错误信息
   */
  @Override public void onLoadMoreFailed(int errorCode, String errorMsg) {
  
  }
```

## 嫌弃我写的 Footer 不好看，你可以自定义 Footer 啊！

```java
/**
 *
 * 对 LoadMoreView 的抽象
 */
public interface ILoadMoreUIHandler {

  // 加载中
  void onLoading();
  // 加载完毕
  void onLoadFinish(boolean isEmpty, boolean hasMore);
  // 加载出错
  void onLoadError(int errorCode, String errorMsg);
  // 等待被点击加载更多
  void onWaitToLoadMore();
}
```

### 笔者的善意，nice footer and header

1.当 LayoutManager 为 `LinearLayoutManager` 时

<img src="./design/LinearLayoutManagerDemo.gif" width="500px"/>

2.当 LayoutManager 为 `GridLayoutManager` 时

<img src="./design/GridLayoutManagerDemo.gif" width="500px"/>

3.当 LayoutManager 为 `StaggeredGridLayoutManager` 时

// 请自行想象...


### Tips
具体可以参考 DefaultLoadMoreFooterView 这个[栗子](https://github.com/HelloVass/HVEndlessRecyclerView/blob/master/hv_endless_recyclerview%2Fsrc%2Fmain%2Fjava%2Fgeeklub%2Forg%2Fhellovass%2Fendless_recyclerview%2Fwidget%2FDefaultLoadMoreFooterView.java)！






