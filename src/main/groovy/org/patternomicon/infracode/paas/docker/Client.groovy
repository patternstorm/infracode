package org.patternomicon.infracode.paas.docker

import okhttp3.ResponseBody
import org.patternomicon.infracode.paas.docker.model.Container
import org.patternomicon.infracode.paas.docker.model.ContainerDefinition
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Client {
    @GET("/version")
    Call<Version> getVersion()
    @POST("/build")
    Call<ResponseBody> createImage(@Query("remote") String dockerFileURI, @Query("t") String tag)
    @GET("/images/json")
    Call<Image[]> getImage(@Query("filters") String name)
    @POST("/containers/create")
    Call<ResponseBody> createContainer(@Body ContainerDefinition containerDef, @Query("name") name)
    @GET("/containers/{id}/json")
    Call<Container> getContainer(@Path("id") id)
}