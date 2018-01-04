package org.patternomicon.infracode.paas.docker

import okhttp3.ResponseBody
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Client {
    @GET("/version")
    Call<Version> getVersion()
    @POST("/build")
    Call<ResponseBody> createImage(@Query("remote") String dockerFileURI, @Query("t") String tag)
    @GET("/images/json")
    Call<Image[]> getImage(@Query("filters") String name)
}