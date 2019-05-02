package com.example.attendonb.app

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import java.io.File

class AttendOnBApp  {


    var realm: Realm? = null
    var app: PhLogApp? = null

    private var networkStateChangeManager: NetworkStateChangeManager? = null
    private var networkChangeBroadcastReceiver: NetworkChangeBroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        this.app = this
        //        MultiDex.install(app);
        //        initRealm(); //--> [1]order is must
        //        setRealmDefaultConfiguration(); //--> [2]order is must
        //        intializeSteatho();
        //        deleteCache(app);   ///for developing        ##################
        //        initializeDepInj(); ///intializing Dagger Dependancy

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkStateChangeManager = NetworkStateChangeManager(this)
            networkStateChangeManager!!.listen()
        } else {
            networkChangeBroadcastReceiver = NetworkChangeBroadcastReceiver()
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(networkChangeBroadcastReceiver, filter)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (networkStateChangeManager != null)
                networkStateChangeManager!!.stopListening()
        } else {
            if (networkChangeBroadcastReceiver != null)
                unregisterReceiver(networkChangeBroadcastReceiver)
        }
    }

    /**
     * use this method in case initializing object by --new ()-- keyword
     *
     * @param app app Context
     */
    fun init(app: Application) {
        PhLogApp.app = app as PhLogApp
    }


    fun getApp(): PhLogApp {
        if (app != null) {
            return app
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
            val realmConfiguration = RealmConfiguration.Builder().schemaVersion(RealmConfigFile.REALM_VERSION).migration(RealmDbMigration()).modules(realmModules).build()
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
