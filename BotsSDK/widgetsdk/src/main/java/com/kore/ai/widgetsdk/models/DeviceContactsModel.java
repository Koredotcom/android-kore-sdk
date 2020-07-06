package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceContactsModel {

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("email")
        @Expose
        private List<EmailItem> email = null;

        @SerializedName("phone")
        @Expose
        private List<PhoneItem> phone = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<EmailItem> getEmail() {
            return email;
        }

        public void setEmail(List<EmailItem> email) {
            this.email = email;
        }

        public List<PhoneItem> getPhone() {
            return phone;
        }

        public void setPhone(List<PhoneItem> phone) {
            this.phone = phone;
        }


}
