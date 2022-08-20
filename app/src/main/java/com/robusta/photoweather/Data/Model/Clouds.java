
package com.robusta.photoweather.Data.Model;

import com.google.gson.annotations.Expose;

public class Clouds {

    @Expose
    private Long all;

    public Long getAll() {
        return all;
    }

    public void setAll(Long all) {
        this.all = all;
    }

}
