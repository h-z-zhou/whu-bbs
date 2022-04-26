package com.wuda.bbs.utils.campus;

import com.wuda.bbs.logic.bean.campus.CampusBuilding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreeRoomParser {
    public static List<CampusBuilding> parseBuilding(String response) {
        List<CampusBuilding> campusBuildingList = new ArrayList<>();

        try {
            JSONObject responseJson = new JSONObject(response);
            if (responseJson.getInt("code") == 200) {
                JSONArray campusList = responseJson.getJSONArray("data");
                for (int i=0; i<campusList.length(); i++) {
                    JSONObject campus = campusList.getJSONObject(i);

                    String campusName = campus.getString("text");

                    JSONArray buildingList = campus.getJSONArray("children");

                    for (int j=0; j<buildingList.length(); j++) {
                        JSONObject building = buildingList.getJSONObject(j);
                        String id = building.getString("id");
                        String name = building.getString("text");
                        campusBuildingList.add(new CampusBuilding(id, name, campusName));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return campusBuildingList;
    }


    public static List<List<String>> parseRoom(String response) {

        List<List<String>> rooms = new ArrayList<>();

        try {
            JSONObject responseJson = new JSONObject(response);
            if (responseJson.getInt("code") == 200) {
                JSONObject data = responseJson.getJSONObject("data");
                for (int i=0; i<data.length(); i++) {
                    JSONArray roomListJson = data.getJSONArray(String.valueOf(i+1));
                    List<String> roomList = new ArrayList<>();
                    for (int j=0; j<roomListJson.length(); j++) {
                        JSONObject room = roomListJson.getJSONObject(j);
                        String name = room.getString("roomName");
                        roomList.add(name);
                    }
                    rooms.add(roomList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rooms;

    }
}
