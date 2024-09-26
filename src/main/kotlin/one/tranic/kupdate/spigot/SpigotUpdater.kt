package one.tranic.kupdate.spigot

import okhttp3.OkHttpClient
import okhttp3.Request
import one.tranic.kupdate.UpdateUtils
import one.tranic.kupdate.Updater
import one.tranic.kupdate.Entry

class SpigotUpdater: Updater {
    private val client = OkHttpClient()

    override fun getUpdate(resourceId: String, localVersion: String): Entry<Int, String> {
        var latestEntry: Entry<Int, String>?
        try {
            val request = Request.Builder()
                .url("https://api.spigotmc.org/legacy/update.php?resource=$resourceId")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                .build()

            latestEntry = client.newCall(request).execute().use {
                if (!it.isSuccessful) return@use Entry(-1, it.body!!.string())
                Entry(UpdateUtils.cmpVer(localVersion, it.body!!.string()), "")
            }
        } catch (e: Exception) {
            latestEntry = Entry(-1, e.message!!)
        }
        return latestEntry!!
    }

    override fun updater(resourceId: String) {

    }
}