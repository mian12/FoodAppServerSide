package com.solution.alnahar.foodappserverside.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.model.Token;

public class MyFireasebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

      String refreshedToken=  FirebaseInstanceId.getInstance().getToken();

      if (Common.currentUser.getPhone()!=null)
      updateToServer(refreshedToken);
    }

    private void updateToServer(String tokenRefreshed) {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference tokens_db_ref= database.getReference("Tokens");

        Token token=new Token(tokenRefreshed,false); // because this token is send from  server side thats why is true

        tokens_db_ref.child(Common.currentUser.getPhone()).setValue(token);
    }
}
