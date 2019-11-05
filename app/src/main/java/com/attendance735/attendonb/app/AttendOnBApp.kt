package com.attendance735.attendonb.app

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.attendance735.attendonb.network.BasicAuthInterceptor
import com.attendance735.attendonb.realm.RealmConfigFile
import com.attendance735.attendonb.realm.RealmDbMigration
import com.attendance735.attendonb.utilites.networkstatus.NetworkChangeBroadcastReceiver
import com.attendance735.attendonb.utilites.networkstatus.NetworkStateChangeManager
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import okhttp3.OkHttpClient
import java.io.File
import io.realm.Realm
import com.google.android.gms.security.ProviderInstaller
import com.attendance735.attendonb.utilites.PrefUtil
import com.attendance735.attendonb.utilites.Utilities
import io.realm.RealmConfiguration
import javax.net.ssl.SSLContext


class AttendOnBApp : MultiDexApplication() {


    var realm: Realm? = null
    private var networkStateChangeManager: NetworkStateChangeManager? = null
    private var networkChangeBroadcastReceiver: NetworkChangeBroadcastReceiver? = null

    companion object {

        var app: AttendOnBApp? = null

    }

    override fun onCreate() {
        super.onCreate()


//        Utilities().printHashKey(app)
        /**
         * Required for Network Authority Access prior Api 19
         * */
        ProviderInstaller.installIfNeeded(getApplicationContext());
        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine();
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        app = this
        AttendOnBApp.app = app as AttendOnBApp

//        initFastAndroidNetworking(null,baseContext)
        //        initRealm(); //--> [1]order is must
        //        setRealmDefaultConfiguration(); //--> [2]order is must
        //        intializeSteatho();
        //        deleteCache(app);   ///for developing        ##################
        //        initializeDepInj(); ///intializing Dagger Dependancy

        if (Build.VERSION.SDK_INT == 19) {
            try {
                ProviderInstaller.installIfNeeded(this)
            } catch (ignored: Exception) {
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkStateChangeManager = NetworkStateChangeManager(this)
            networkStateChangeManager?.listen()
        } else {
            networkChangeBroadcastReceiver = NetworkChangeBroadcastReceiver()
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(networkChangeBroadcastReceiver, filter)
        }

        Utilities.changeAppLanguage(this, PrefUtil.getAppLanguage(this))

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


    fun initFastAndroidNetworking(userToken: String?, context: Context) {

        /**
         * initializing block to add authentication to your Header Request
         */
        if (userToken != null) {
            val basicAuthInterceptor = BasicAuthInterceptor(context)
            basicAuthInterceptor.setUserToken(userToken)
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