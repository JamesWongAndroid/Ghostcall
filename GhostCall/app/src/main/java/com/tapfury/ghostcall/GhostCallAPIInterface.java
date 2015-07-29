package com.tapfury.ghostcall;

import com.tapfury.ghostcall.BackgroundEffects.BackgroundEffectsData;
import com.tapfury.ghostcall.SoundEffects.SoundEffectsData;
import com.tapfury.ghostcall.User.UserData;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Ynott on 7/28/15.
 */
public interface GhostCallAPIInterface {

    @GET("/credit_packages")
    List<CreditPackagesData> getCreditPackagesData();

    @GET("/effects")
    List<SoundEffectsData> getsoundEffectsList();

    @GET("/packages?type=new")
    List<NumberPackagesData> getNewNumberPackages();

    @GET("/packages?type=extend")
    List<NumberPackagesData> getExtendNumberPackages();

    @GET("/backgrounds")
    List<BackgroundEffectsData> getBackgroundEffectsList();

    @GET("/user")
    UserData getUserData();

    @GET("/user")
    void getUserData(Callback<UserData> callBack);
}