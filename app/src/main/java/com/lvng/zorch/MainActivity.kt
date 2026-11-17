package com.lvng.zorch

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 13, 25)
    private val panel = Color.rgb(17, 28, 53)
    private val panel2 = Color.rgb(23, 37, 68)
    private val pink = Color.rgb(242, 27, 104)
    private val white = Color.rgb(246, 247, 252)
    private val muted = Color.rgb(170, 180, 204)
    private val prefs by lazy { getSharedPreferences("lvng_zorch", MODE_PRIVATE) }
    private lateinit var page: LinearLayout
    private var selectedTab = "home"
    private var macroActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showHome()
    }

    private fun shape(color: Int, radius: Float = 22f, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius * resources.displayMetrics.density
            if (stroke != null) setStroke((1 * resources.displayMetrics.density).toInt(), stroke)
        }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun label(value: String, size: Float = 15f, color: Int = white, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setLineSpacing(dp(2).toFloat(), 1f)
        }

    private fun actionButton(value: String, onTap: () -> Unit): TextView =
        TextView(this).apply {
            text = value
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(white)
            setTypeface(typeface, Typeface.BOLD)
            setPadding(dp(16), dp(12), dp(16), dp(12))
            background = shape(pink, 30f)
            setOnClickListener { onTap() }
        }

    private fun card(title: String, subtitle: String? = null, body: View? = null) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = shape(panel)
            setPadding(dp(18), dp(16), dp(18), dp(16))
        }
        box.addView(label(title, 17f, white, true))
        if (subtitle != null) {
            val sub = label(subtitle, 13f, muted)
            sub.setPadding(0, dp(7), 0, 0)
            box.addView(sub)
        }
        if (body != null) {
            val lp = LinearLayout.LayoutParams(-1, -2)
            lp.topMargin = dp(12)
            box.addView(body, lp)
        }
        val lp = LinearLayout.LayoutParams(-1, -2)
        lp.bottomMargin = dp(14)
        page.addView(box, lp)
    }

    private fun base(tab: String, title: String) {
        selectedTab = tab
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(18), dp(12), dp(18), dp(10))
        }
        val header = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), dp(8), dp(4), dp(16))
        }
        val mark = label("◢", 32f, pink, true)
        header.addView(mark)
        val brand = label("  LVNG - ZORCH\n  $title", 15f, white, true)
        header.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(label("ZORCH", 11f, pink, true))
        outer.addView(header)

        val scroll = ScrollView(this).apply { isFillViewport = true; clipToPadding = false }
        page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(4), 0, dp(18)) }
        scroll.addView(page)
        outer.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            background = shape(panel, 32f)
            setPadding(dp(4), dp(5), dp(4), dp(5))
        }
        listOf(Triple("home", "▣", "Game"), Triple("account", "●", "Akun"), Triple("settings", "⚙", "Setelan")).forEach { item ->
            val active = item.first == tab
            val b = TextView(this).apply {
                text = "${item.second}\n${item.third}"
                gravity = Gravity.CENTER
                textSize = 12f
                setTextColor(if (active) white else muted)
                setTypeface(typeface, if (active) Typeface.BOLD else Typeface.NORMAL)
                setPadding(dp(8), dp(10), dp(8), dp(10))
                if (active) background = shape(pink, 28f)
                setOnClickListener {
                    when (item.first) { "home" -> showHome(); "account" -> showAccount(); else -> showSettings() }
                }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, -2, 1f))
        }
        outer.addView(nav)
        setContentView(outer)
    }

    private fun section(value: String) {
        val t = label(value.uppercase(), 14f, muted, true)
        t.setPadding(dp(4), dp(6), 0, dp(10))
        page.addView(t)
    }

    private fun row(title: String, subtitle: String, onClick: () -> Unit) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = shape(panel2, 16f)
            setPadding(dp(14), dp(13), dp(14), dp(13))
            setOnClickListener { onClick() }
        }
        box.addView(label(title, 15f, white, true))
        box.addView(label(subtitle, 12f, muted))
        val lp = LinearLayout.LayoutParams(-1, -2); lp.bottomMargin = dp(9)
        page.addView(box, lp)
    }

    private fun showHome() {
        base("home", "Game Library")
        card("KREDIT  •  UNLIMITED", "Tidak ada batas penggunaan dan tidak ada pembelian dalam aplikasi.")
        section("Perpustakaan Game")
        val game = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = shape(panel2)
            setPadding(dp(18), dp(18), dp(18), dp(18))
            addView(label("🔥   Free Fire", 19f, white, true))
            addView(label("com.dts.freefireth  •  Game eksternal", 12f, muted))
            setOnClickListener { launchGame() }
        }
        val lp = LinearLayout.LayoutParams(-1, -2); lp.bottomMargin = dp(14)
        page.addView(game, lp)
        card("MODE GAME", "Pilih halaman pengaturan untuk mengelola preset kontrol.", actionButton("Buka Key Mapping") { showKeyMapping() })
        card("STATUS", "LVNG - ZORCH berjalan dalam mode lokal. Fitur yang membutuhkan layanan eksternal ditandai di dalam aplikasi.")
    }

    private fun showAccount() {
        base("account", "Akun Saya")
        card("LVNG - ZORCH", "Akun lokal • Paket gratis • Semua halaman aplikasi terbuka")
        card("FITUR APLIKASI", "Tidak ada iklan, paywall, paket premium, atau alur pembelian dalam aplikasi.")
        card("KREDIT", "UNLIMITED", label("∞", 44f, pink, true))
        card("IDENTITAS", "Nama aplikasi: LVNG - ZORCH\nVersi: 1.0.0\nPenyimpanan: lokal di perangkat")
    }

    private fun showSettings() {
        base("settings", "Pengaturan")
        card("STATUS LAYANAN", "Mode lokal aktif • Tidak terhubung ke backend Nebula")
        section("Pengaturan Dasar")
        row("Mengelola Key Mapping", "Kelola daftar preset lokal") { showKeyMapping() }
        row("Pengaturan Sensitivitas", "Atur nilai sensitivitas untuk preset lokal") { showSensitivity() }
        row("Macro Record", "Panel kontrol dan status rekaman lokal") { showMacro() }
        row("Pengaturan Tambahan", "Preferensi tampilan dan aplikasi") { showAdditional() }
        row("Bahasa", "System Default") { Toast.makeText(this, "Bahasa mengikuti sistem perangkat.", Toast.LENGTH_SHORT).show() }
        section("Dukungan")
        card("LVNG - ZORCH", "Build komunitas independen. Hubungkan backend sendiri jika diperlukan.")
    }

    private fun showKeyMapping() {
        base("settings", "Key Mapping")
        val mappings = prefs.getString("mappings", "Movement · WASD|Fire · Button 1|Aim · Button 2|Jump · Space")!!
            .split("|").filter { it.isNotBlank() }.toMutableList()
        card("KEY MAPPING", "Preset lokal untuk menyimpan label kontrol. Tidak mengirim input ke game.")
        mappings.forEachIndexed { index, item ->
            row(item, "Preset ${index + 1}") {
                mappings.removeAt(index)
                prefs.edit().putString("mappings", mappings.joinToString("|")).apply()
                showKeyMapping()
            }
        }
        card("TAMBAH PRESET", "Gunakan tombol untuk menambahkan contoh preset kontrol.", actionButton("+ Tambah preset") {
            mappings.add("Custom · ${mappings.size + 1}")
            prefs.edit().putString("mappings", mappings.joinToString("|")).apply()
            showKeyMapping()
        })
    }

    private fun showSensitivity() {
        base("settings", "Sensitivitas")
        card("SENSITIVITY", "Nilai tersimpan lokal sebagai preferensi. Tidak mengubah sensitivitas game secara otomatis.")
        listOf("General", "Red Dot", "2x Scope", "4x Scope", "Sniper").forEach { key ->
            val current = prefs.getInt("sens_$key", 50)
            val wrap = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; background = shape(panel); setPadding(dp(16), dp(12), dp(16), dp(12)) }
            val line = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            line.addView(label(key, 15f, white, true), LinearLayout.LayoutParams(0, -2, 1f))
            val value = label(current.toString(), 14f, pink, true)
            line.addView(value)
            wrap.addView(line)
            val seek = SeekBar(this).apply {
                max = 100
                progress = current
                progressTintList = android.content.res.ColorStateList.valueOf(pink)
                thumbTintList = android.content.res.ColorStateList.valueOf(pink)
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) { value.text = p.toString(); if (fromUser) prefs.edit().putInt("sens_$key", p).apply() }
                    override fun onStartTrackingTouch(sb: SeekBar?) {}
                    override fun onStopTrackingTouch(sb: SeekBar?) {}
                })
            }
            wrap.addView(seek)
            val lp = LinearLayout.LayoutParams(-1, -2); lp.bottomMargin = dp(12)
            page.addView(wrap, lp)
        }
    }

    private fun showMacro() {
        base("settings", "Macro Record")
        card("MACRO RECORDER", "Panel demo untuk mencatat status sesi secara lokal. Tidak merekam atau mengirim sentuhan ke game.")
        val status = label(if (macroActive) "● RECORDING" else "○ READY", 20f, if (macroActive) pink else muted, true)
        card("STATUS", if (macroActive) "Sesi demo sedang aktif." else "Belum ada sesi aktif.", status)
        card("KONTROL", "Mulai dan hentikan status sesi demo.", actionButton(if (macroActive) "■ Stop" else "● Mulai Record") {
            macroActive = !macroActive
            showMacro()
        })
    }

    private fun showAdditional() {
        base("settings", "Pengaturan Tambahan")
        val keys = listOf("Getaran tombol", "Konfirmasi sebelum keluar", "Tampilkan tips")
        keys.forEach { key ->
            val enabled = prefs.getBoolean("pref_$key", true)
            row(key, if (enabled) "Aktif" else "Nonaktif") {
                prefs.edit().putBoolean("pref_$key", !prefs.getBoolean("pref_$key", true)).apply()
                showAdditional()
            }
        }
    }

    private fun launchGame() {
        val candidates = listOf("com.dts.freefireth", "com.dts.freefiremax")
        val intent = candidates.firstNotNullOfOrNull { packageManager.getLaunchIntentForPackage(it) }
        if (intent != null) startActivity(intent)
        else Toast.makeText(this, "Free Fire tidak ditemukan. Instal game terlebih dahulu.", Toast.LENGTH_LONG).show()
    }
}
