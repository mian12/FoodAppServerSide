package com.solution.alnahar.foodappserverside.Common;


import com.solution.alnahar.foodappserverside.model.Request;
import com.solution.alnahar.foodappserverside.model.User;
import com.solution.alnahar.foodappserverside.remote.APIService;
import com.solution.alnahar.foodappserverside.remote.RetrofitClient;

public class Common {

    public static User currentUser;

    public static Request currentRequest;

    public static String UPDATE = "Update";
    public static String DELETE = "Delete";

    public static String BASE_URL_FCM = "https://fcm.googleapis.com/";


    public  static APIService getFCMClient()
    {

         return RetrofitClient.getRetrofit(BASE_URL_FCM).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        String res = "";

        switch (status) {

            case "0":
                res = "Placed";
                break;
            case "1":
                res = "On my way";
                break;
            case "2":
                res = "Shipped";
                break;
        }
        return res;
    }
}
