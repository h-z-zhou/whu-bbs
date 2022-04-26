package com.wuda.bbs.ui.campus.toolkit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.CampusBuilding;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.utils.campus.FreeRoomParser;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.ServerURL;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class FreeRoomFragment extends ToolFragment {

    //    List<CampusBuilding> campusBuildingList;
    Map<String, List<CampusBuilding>> campusBuilds;
    Button campus_btn;
    Button building_btn;
    Button date_btn;
    Button search_btn;

    LinearLayout chipRoot_ll;
    ChipGroup lesson1_2_cg;
    ChipGroup lesson3_5_cg;
    ChipGroup lesson6_8_cg;
    ChipGroup lesson11_13_cg;

    String campus;
    String buildingId;
    String date;
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FreeRoomFragment() {
        campusBuilds = new LinkedHashMap<>();
        // Required empty public constructor
    }

    public static FreeRoomFragment newInstance() {
        FreeRoomFragment fragment = new FreeRoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_free_room, container, false);
        campus_btn = view.findViewById(R.id.freeRoom_campus_btn);
        building_btn = view.findViewById(R.id.freeRoom_building_btn);
        date_btn = view.findViewById(R.id.freeRoom_date_btn);
        search_btn = view.findViewById(R.id.freeRoom_search_btn);

        chipRoot_ll = view.findViewById(R.id.freeRoom_chipRoot);
        lesson1_2_cg = view.findViewById(R.id.freeRoom_lesson1_2_chipGroup);
        lesson3_5_cg = view.findViewById(R.id.freeRoom_lesson3_5_chipGroup);
        lesson6_8_cg = view.findViewById(R.id.freeRoom_lesson6_8_chipGroup);
        lesson11_13_cg = view.findViewById(R.id.freeRoom_lesson11_13_chipGroup);

        date = sdf.format(System.currentTimeMillis());
        date_btn.setText(date);

        requestBuildingsFromServer();

        eventBinding();

        return view;
    }

    private void eventBinding() {

        campus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu campusMenu = new PopupMenu(getContext(), campus_btn);
                for (String campusItem: campusBuilds.keySet()) {
                    campusMenu.getMenu().add(campusItem);
                }
                campusMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        campus = (String) item.getTitle();
                        campus_btn.setText(campus);
                        return false;
                    }
                });
                campusMenu.show();
            }
        });

        building_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu buildingMenu = new PopupMenu(getContext(), building_btn);

                List<CampusBuilding> buildings;
                if (campus != null) {
                    buildings = campusBuilds.get(campus);
                    if (buildings == null)
                        return;
                    for (int i=0; i<buildings.size(); i++) {
                        buildingMenu.getMenu().add(1, Menu.NONE, i, buildings.get(i).getName());
                    }
                } else {
                    buildings = new ArrayList<>();
                    for (String key: campusBuilds.keySet()) {
                        buildings.addAll(campusBuilds.get(key));
                    }
                    for (int i=0; i<buildings.size(); i++) {
                        buildingMenu.getMenu().add(1, Menu.NONE, i, buildings.get(i).getCampus() + " | " + buildings.get(i).getName());
                    }
                }
                buildingMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        building_btn.setText(item.getTitle());
                        buildingId = buildings.get(item.getOrder()).getId();
//                        Log.d("id", Integer.valueOf(item.getOrder()).toString());
                        return false;
                    }
                });
                buildingMenu.show();
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        date = sdf.format(selection);
                        date_btn.setText(date);
                    }
                });

                datePicker.show(getParentFragmentManager(), datePicker.toString());
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildingId==null || date==null) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("请选择教学楼和日期")
                            .setCancelable(false)
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    requestFreeRoomFromServer();
                }
            }
        });
    }

    private void requestBuildingsFromServer() {

        ProgressDialog loadingDialog = new ProgressDialog(getContext());
//        loadingDialog.setCancelable(false);
//        loadingDialog.setIndeterminate(true);
        loadingDialog.setTitle("初始化中");
        loadingDialog.show();

        HttpUtil.sendOkHttpRequest(ServerURL.FREE_ROOM_BUILDING, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                List<CampusBuilding> campusBuildingList = FreeRoomParser.parseBuilding(response.body().string());

                for (CampusBuilding building: campusBuildingList) {
                    if(!campusBuilds.containsKey(building.getCampus())) {
                        campusBuilds.put(building.getCampus(), new ArrayList<>());
                    }
                    campusBuilds.get(building.getCampus()).add(building);
                }

                loadingDialog.dismiss();
            }
        });
    }

    private void requestFreeRoomFromServer() {

        ProgressDialog loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage("巨慢，等着吧...");
        loadingDialog.show();

        HttpUtil.sendOkHttpRequest(ServerURL.FREE_ROOM_DATA + "?build=" + buildingId + "&" + "date=" + date, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            new AlertDialog.Builder(getContext())
                                    .setTitle("出错啦")
                                    .setMessage(e.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                loadingDialog.dismiss();
                ResponseBody body = response.body();
                if (body != null) {
                    List<List<String>> rooms = FreeRoomParser.parseRoom(body.string());
                    putRooms2ChipGroup(rooms);
                    Log.d("rooms", rooms.toString());
                }
            }
        });
    }

    private void putRooms2ChipGroup(List<List<String>> rooms) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (chipRoot_ll.getVisibility() == View.GONE) {
                        chipRoot_ll.setVisibility(View.VISIBLE);
                    }

                    // 1 - 2
                    lesson1_2_cg.removeAllViews();
                    List<String> lesson1_2 = intersection(rooms.get(0), rooms.get(1));
                    if (lesson1_2.isEmpty()) {
                        Chip chip = new Chip(requireContext());
                        chip.setCheckable(false);
                        chip.setText("没有哟");
                        lesson1_2_cg.addView(chip);
                    } else {
                        for (String room: lesson1_2) {
                            Chip chip = new Chip(requireContext());
                            chip.setCheckable(false);
                            chip.setText(room);
                            lesson1_2_cg.addView(chip);
                        }
                    }
                    // 3 - 5
                    lesson3_5_cg.removeAllViews();
                    List<String> lesson3_5 = intersection(rooms.get(2), intersection(rooms.get(3), rooms.get(4)));
                    if (lesson3_5.isEmpty()) {
                        Chip chip = new Chip(requireContext());
                        chip.setCheckable(false);
                        chip.setText("没有哟");
                        lesson3_5_cg.addView(chip);
                    } else {
                        for (String room: lesson3_5) {
                            Chip chip = new Chip(requireContext());
                            chip.setCheckable(false);
                            chip.setText(room);
                            lesson3_5_cg.addView(chip);
                        }
                    }
                    // 6 - 8
                    lesson6_8_cg.removeAllViews();
                    List<String> lesson6_8 = intersection(rooms.get(5), intersection(rooms.get(6), rooms.get(7)));
                    if (lesson6_8.isEmpty()) {
                        Chip chip = new Chip(requireContext());
                        chip.setCheckable(false);
                        chip.setText("没有哟");
                        lesson6_8_cg.addView(chip);
                    } else {
                        for (String room: lesson6_8) {
                            Chip chip = new Chip(requireContext());
                            chip.setCheckable(false);
                            chip.setText(room);
                            lesson6_8_cg.addView(chip);
                        }
                    }
                    // 11 - 13
                    lesson11_13_cg.removeAllViews();
                    List<String> lesson11_13 = intersection(rooms.get(10), intersection(rooms.get(11), rooms.get(12)));
                    if (lesson11_13.isEmpty()) {
                        Chip chip = new Chip(requireContext());
                        chip.setCheckable(false);
                        chip.setText("没有哟");
                        lesson11_13_cg.addView(chip);
                    } else {
                        for (String room: lesson11_13) {
                            Chip chip = new Chip(requireContext());
                            chip.setCheckable(false);
                            chip.setText(room);
                            lesson11_13_cg.addView(chip);
                        }
                    }
                }
            });
        }
    }

    private List<String> intersection(List<String> l1, List<String> l2) {
        List<String> rst = new ArrayList<>(l1);
        rst.removeAll(l2);
        rst.addAll(l1);

        return rst;
    }}