/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.ui.dao.KeyWord;
import org.lowcarbon.soda.util.KeyWordDBUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class SearchActivity extends RxAppCompatActivity {

    public final static String ACTION_SEARCH_START = "org.lowcarbon.soda.ACTION_START_START";
    public final static String ACTION_SEARCH_DESTINATION = "org.lowcarbon.soda.ACTION_SEARCH_DESTINATION";

    @BindView(R.id.search)
    SearchView mSearchView;

    @BindView(R.id.recyclerview_search)
    RecyclerView mRecyclerView;
    SearchAdapter mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent);
    }

    private void initView(Intent intent) {
        String action = intent.getAction();
        if (ACTION_SEARCH_START.equals(action)) {
            mSearchView.setQueryHint("请输入起点");
        } else if (ACTION_SEARCH_DESTINATION.equals(action)) {
            mSearchView.setQueryHint("请输入目标点");
        }
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //将搜索结果加入历史搜索结果并返回
                KeyWordDBUtil.getInstance().insertHistorySearchKeywordsToDb(
                        new KeyWord(query, 1, System.currentTimeMillis(), KeyWord.TYPE_HISTORY)
                );
                Intent data = new Intent();
                data.setData(Uri.parse(query));
                setResult(RESULT_OK, data);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    return false;
                }
                mAdapter.clear();
                Observable.just(newText)
                        .flatMap(new Func1<String, Observable<KeyWord>>() {
                            @Override
                            public Observable<KeyWord> call(String s) {
                                List<KeyWord> data = KeyWordDBUtil.getInstance().queryMatchKeywords(s);
                                return Observable.from(data);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .compose(SearchActivity.this.<KeyWord>bindToLifecycle())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .subscribe(new Action1<KeyWord>() {
                            @Override
                            public void call(KeyWord keyWord) {
                                mAdapter.add("associate:" + keyWord.getKeyword());
                            }
                        });
                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 5);
            }
        });
        mRecyclerView.setAdapter(mAdapter = new SearchAdapter());
        mAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int type, int position) {
                if (type == SearchAdapter.TYPE_CLEAR) {
                    KeyWordDBUtil.getInstance().clearHisotySearchKeyWords();
                    mAdapter.clear();
                } else if (type == SearchAdapter.TYPE_HISTORY) {
                    mSearchView.setQuery(mAdapter.getItem(position).replace("history:", ""), false);
                } else if (type == SearchAdapter.TYPE_ASSOCIATE) {
                    mSearchView.setQuery(mAdapter.getItem(position).replace("associate", ""), false);
                }
            }
        });

        loadHistorySearch();
    }

    /**
     * 加载历史搜索词汇
     */
    private void loadHistorySearch() {
        Observable.just(KeyWord.TYPE_HISTORY).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer type) {
                return Observable.from(KeyWordDBUtil.getInstance().queryKeywords(type));
            }
        }).subscribeOn(Schedulers.io())
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if (mAdapter.getItemCount() > 0) {
                            mAdapter.add("clear:");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mAdapter.add("history:" + s);
                    }
                });
    }

}
