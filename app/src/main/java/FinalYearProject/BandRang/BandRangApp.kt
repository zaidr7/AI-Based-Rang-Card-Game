package FinalYearProject.BandRang

import FinalYearProject.BandRang.util.PrefsHelper

class BandRangApp: android.app.Application(){
    override fun onCreate() {
        super.onCreate()
        PrefsHelper.init(this)////initialize preferences
    }
}