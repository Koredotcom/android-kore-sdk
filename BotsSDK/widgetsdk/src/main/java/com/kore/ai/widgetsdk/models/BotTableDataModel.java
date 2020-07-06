package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 30-Oct-17.
 */

public class BotTableDataModel {

    /*private List<Header> headers = null;
    private List<List<String>> rows = null;
    */

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    /*public class Header{
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }

            public String getAlignment() {
                return alignment;
            }

            public void setAlignment(String alignment) {
                this.alignment = alignment;
            }

            public String title;
            public double percentage;
            public String alignment;
        }
        public List<Header> getHeaders() {
            return headers;
        }

        public void setHeaders(List<Header> headers) {
            this.headers = headers;
        }

        public List<List<String>> getRows() {
            return rows;
        }

        public void setRows(List<List<String>> rows) {
            this.rows = rows;
        }*/
    private List<String> values = null;



}
