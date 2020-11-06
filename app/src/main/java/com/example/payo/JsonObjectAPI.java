package com.example.payo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonObjectAPI {
    @SerializedName("data")
    public ArrayList<Data> data = new ArrayList<>();

    public class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("email")
        private String email;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("avatar")
        private String avatar;

        public int getObjectId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
