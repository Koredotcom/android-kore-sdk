package kore.botssdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceContactsModel {

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("email")
        @Expose
        private Email email = null;

        @SerializedName("phone")
        @Expose
        private Phone phone = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Phone getPhone() {
            return phone;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

}
