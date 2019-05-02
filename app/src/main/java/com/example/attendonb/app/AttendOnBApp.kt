package com.example.attendonb.app

import android.app.Application
import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.example.attendonb.network.BasicAuthInterceptor
import com.example.attendonb.realm.RealmConfigFile
import com.example.attendonb.realm.RealmDbMigration
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import okhttp3.OkHttpClient
import java.io.File
import io.realm.Realm
import io.realm.RealmConfiguration

class AttendOnBApp : Application() {


    var realm: Realm? = null

    companion object {

        var app: AttendOnBApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        //        MultiDex.install(app);
        //        initRealm(); //--> [1]order is must
        //        setRealmDefaultConfiguration(); //--> [2]order is must
        //        intializeSteatho();
        //        deleteCache(app);   ///for developing        ##################
        //        initializeDepInj(); ///intializing Dagger Dependancy


    }


    /**
     * use this method in case initializing object by --new ()-- keyword
     *
     * @param app app Context
     */
    fun init(app: Application) {
        AttendOnBApp.app = app as AttendOnBApp
    }


    fun getApp(): AttendOnBApp {
        if (app != null) {
            return app as AttendOnBApp
        }
        throw NullPointerException("u should init AppContext  first")
    }

    /**
     * delete App Cache and NetWork Cache
     */
    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }

    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }

    }

    /**
     * initialize Realm Instance this method called after Realm.init(context)
     * Note -->this migrate change of tables if Happened
     *
     * @param isFirstLaunch true as app first Time launched
     * @param realmModules  --->realm module graph represent realm objects ex[]
     *
     *
     * Now you can return Realm instance through App by Calling ----> Realm.getDefaultInstance()
     */
    fun setRealmDefaultConfiguration(isFirstLaunch: Boolean, realmModules: Any) {
        if (isFirstLaunch) {
            val realmConfiguration = RealmConfiguration.Builder().schemaVersion(RealmConfigFile.REALM_VERSION.toLong()).migration(RealmDbMigration()).modules(realmModules).build()
            Realm.setDefaultConfiguration(realmConfiguration)
        }
    }

    protected override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        //        MultiDex.install(this);
    }


    fun initRealm() {
        Realm.init(this)
    }


    /**
     * Inspect your Realm Tables through Google Browzer
     */
    fun intializeSteatho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())

    }


//    private void initializeDepInj() {
//        if (appComponent == null) {
//            appComponent = DaggerAppComponent.builder()
//                    .mainAppModule(new MainAppModule(this)).build();
////            appComponent.inject(this);  //this App don't Need Any Dependancyes
//        }
//    }


    fun initFastAndroidNetworking(userToken: String?, userType: String, lang: String, context: Context) {

        /**
         * initializing block to add authentication to your Header Request
         */
        if (userToken != null) {
            val basicAuthInterceptor = BasicAuthInterceptor(context)
            basicAuthInterceptor.setUserToken(userToken)
            basicAuthInterceptor.setUserType(userType)
            basicAuthInterceptor.setLang(lang)
            val okHttpClient = OkHttpClient().newBuilder()
                    .addNetworkInterceptor(basicAuthInterceptor)
                    .build()
            AndroidNetworking.initialize(context, okHttpClient)
        } else {
            /**
             * default initialization
             */
            AndroidNetworking.initialize(context)
        }
    }

}
