package com.wuda.bbs.ui.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.FavorBoard;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.logic.dao.FavorBoardDao;
import com.wuda.bbs.ui.adapter.BoardEntranceListAdapter;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardEntranceListFragment extends Fragment {

    SwipeRefreshLayout entrance_srl;
    ExpandableListView entrance_elv;
    boolean isFavorBoardRequested = false;

    public BoardEntranceListFragment() {
    }

    public static BoardEntranceListFragment newInstance() {
        BoardEntranceListFragment fragment = new BoardEntranceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board_entrance_list, container, false);

        entrance_srl = view.findViewById(R.id.boardEntrance_swipeRefreshLayout);
        entrance_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestFavorBoardsFromServer();
                requestDetailBoardsFromServer();
            }
        });
        entrance_elv = view.findViewById(R.id.boardEntrance_expandableListView);

        queryAllBoardFromDB();

        return  view;
    }

    private void queryAllBoardFromDB() {
        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

        if (getContext() == null) return;
        AppDatabase database = AppDatabase.getDatabase(getContext());

        FavorBoardDao favorBoardDao = database.getFavorBoardDao();
        List<FavorBoard> favorBoardList = favorBoardDao.loadAllFavorBoards();
        // Favor Board may empty
        if (favorBoardList.isEmpty() && !isFavorBoardRequested) {
            requestFavorBoardsFromServer();
            isFavorBoardRequested = true;
            return;
        }
        allBoardGroupMap.put("我的收藏", cast2BaseBoard(favorBoardList));

        DetailBoardDao detailBoardDao = database.getDetailBoardDao();
//        room group by ??
//        List<DetailBoard> detailBoardList = detailBoardDao.loadBoardsGroupBySection();
        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();
        if (detailBoardList.isEmpty()) {
            requestDetailBoardsFromServer();
            return;
        } else {
            for (DetailBoard detailBoard : detailBoardList) {
                String section = detailBoard.getSection();
                if (!allBoardGroupMap.containsKey(section)) {
                    allBoardGroupMap.put(section, new ArrayList<>());
                }
                allBoardGroupMap.get(section).add(detailBoard);
            }
        }
        database.close();

        List<String> sectionList = new ArrayList<>();
        sectionList.addAll(allBoardGroupMap.keySet());
        List<List<BaseBoard>> allBoardGroupList = new ArrayList<>();
        allBoardGroupList.addAll(allBoardGroupMap.values());

        entrance_elv.setAdapter(new BoardEntranceListAdapter(getContext(), sectionList, allBoardGroupList));

    }

    private void requestFavorBoardsFromServer() {

        entrance_srl.setRefreshing(true);

        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.get("favor").enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = response.body().string();
                    if (text.equals(NetConst.FAVOR_BOARD_ERROR)) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "收藏版块需登录后获取", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

//                    hadRequestFavor = true;

                    List<BaseBoard> favorBoardList = XMLParser.parseFavorBoard(text);
                    if (getContext() == null)
                        return;
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();

                    // 清空，与云端保持同步
                    favorBoardDao.clearFavorBoardsByUsername(BBSApplication.getAccountId());

                    // cast => save to database
                    List<FavorBoard> castFavorBoardList = new ArrayList<>();
                    for (int i=0; i<favorBoardList.size(); ++i) {
                        castFavorBoardList.add((FavorBoard) favorBoardList.get(i));
                    }
                    favorBoardDao.insert(castFavorBoardList);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                queryAllBoardFromDB();
                                entrance_srl.setRefreshing(false);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            entrance_srl.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }


    private void requestDetailBoardsFromServer() {

        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.get("boards", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = response.body().string();

                    List<BaseBoard> detailBoardList = XMLParser.parseDetailBoard(text);
                    if (getContext() == null)
                        return;

                    DetailBoardDao detailBoardDao = AppDatabase.getDatabase(getContext()).getDetailBoardDao();
                    detailBoardDao.clearAll();
                    // cast => save to database
                    List<DetailBoard> castFavorBoardList = new ArrayList<>();
                    for (int i=0; i<detailBoardList.size(); ++i) {
                        castFavorBoardList.add((DetailBoard) detailBoardList.get(i));
                    }
                    detailBoardDao.insert(castFavorBoardList);



                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryAllBoardFromDB();
                            entrance_srl.setRefreshing(false);
                        }
                    });
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            entrance_srl.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    private List<BaseBoard> cast2BaseBoard(List<?> boards) {
        // FavorBoard / DetailBoard => BaseBoard
        List<BaseBoard> baseBoardList = new ArrayList<>();
        for (Object board: boards) {
            baseBoardList.add((BaseBoard) board);
        }
        return baseBoardList;
    }
}