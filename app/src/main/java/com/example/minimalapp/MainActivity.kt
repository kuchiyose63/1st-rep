package com.example.minimalapp

import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : Activity() {

    private var jamMasuk = 8
    private var menitMasuk = 0
    private var jamKeluar = 17
    private var menitKeluar = 0

    private lateinit var btnJamMasuk: Button
    private lateinit var btnJamKeluar: Button
    private lateinit var tvHasilDurasi: TextView
    private lateinit var tvHasilBiaya: TextView
    private lateinit var tvKeteranganTarif: TextView
    private lateinit var rgKendaraan: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set jam keluar default ke waktu saat ini
        val now = Calendar.getInstance()
        jamKeluar = now.get(Calendar.HOUR_OF_DAY)
        menitKeluar = now.get(Calendar.MINUTE)

        val scrollView = ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#F5F7FA"))
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        // Judul Aplikasi
        val tvTitle = TextView(this).apply {
            text = "Kalkulator Tarif Parkir"
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#1A202C"))
            setPadding(0, 0, 0, 24)
        }
        root.addView(tvTitle)

        // Pilihan Jenis Kendaraan
        val tvLabelKendaraan = TextView(this).apply {
            text = "1. Pilih Jenis Kendaraan:"
            textSize = 15f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#2D3748"))
            setPadding(0, 8, 0, 12)
        }
        root.addView(tvLabelKendaraan)

        rgKendaraan = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
        }

        val rbMotor = RadioButton(this).apply {
            id = 1
            text = "Motor  "
            textSize = 16f
            isChecked = true
        }
        val rbMobil = RadioButton(this).apply {
            id = 2
            text = "Mobil  "
            textSize = 16f
        }
        val rbTruk = RadioButton(this).apply {
            id = 3
            text = "Truk"
            textSize = 16f
        }

        rgKendaraan.addView(rbMotor)
        rgKendaraan.addView(rbMobil)
        rgKendaraan.addView(rbTruk)
        root.addView(rgKendaraan)

        // Input Jam Masuk
        val tvLabelMasuk = TextView(this).apply {
            text = "2. Waktu Masuk:"
            textSize = 15f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#2D3748"))
            setPadding(0, 28, 0, 8)
        }
        root.addView(tvLabelMasuk)

        btnJamMasuk = Button(this).apply {
            text = formatWaktu(jamMasuk, menitMasuk)
            textSize = 18f
            setOnClickListener {
                TimePickerDialog(this@MainActivity, { _, hour, minute ->
                    jamMasuk = hour
                    menitMasuk = minute
                    text = formatWaktu(jamMasuk, menitMasuk)
                    hitungParkir()
                }, jamMasuk, menitMasuk, true).show()
            }
        }
        root.addView(btnJamMasuk)

        // Input Jam Keluar + Tombol "Sekarang"
        val tvLabelKeluar = TextView(this).apply {
            text = "3. Waktu Keluar:"
            textSize = 15f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#2D3748"))
            setPadding(0, 24, 0, 8)
        }
        root.addView(tvLabelKeluar)

        val rowKeluar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        btnJamKeluar = Button(this).apply {
            text = formatWaktu(jamKeluar, menitKeluar)
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setOnClickListener {
                TimePickerDialog(this@MainActivity, { _, hour, minute ->
                    jamKeluar = hour
                    menitKeluar = minute
                    text = formatWaktu(jamKeluar, menitKeluar)
                    hitungParkir()
                }, jamKeluar, menitKeluar, true).show()
            }
        }

        val btnSekarang = Button(this).apply {
            text = "Jam Sekarang"
            textSize = 14f
            setOnClickListener {
                val cal = Calendar.getInstance()
                jamKeluar = cal.get(Calendar.HOUR_OF_DAY)
                menitKeluar = cal.get(Calendar.MINUTE)
                btnJamKeluar.text = formatWaktu(jamKeluar, menitKeluar)
                hitungParkir()
            }
        }

        rowKeluar.addView(btnJamKeluar)
        rowKeluar.addView(btnSekarang)
        root.addView(rowKeluar)

        // Tombol Hitung Manual
        val btnHitung = Button(this).apply {
            text = "HITUNG BIAYA PARKIR"
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            setBackgroundColor(Color.parseColor("#2B6CB0"))
            setTextColor(Color.WHITE)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 36, 0, 24)
            }
            layoutParams = params
            setOnClickListener { hitungParkir() }
        }
        root.addView(btnHitung)

        // Kartu Hasil Perhitungan
        val cardHasil = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(36, 36, 36, 36)
        }

        tvHasilDurasi = TextView(this).apply {
            text = "Durasi Parkir: -"
            textSize = 16f
            setTextColor(Color.parseColor("#4A5568"))
            setPadding(0, 0, 0, 12)
        }

        tvHasilBiaya = TextView(this).apply {
            text = "Total Biaya: Rp 0"
            textSize = 26f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#2F855A"))
            setPadding(0, 0, 0, 12)
        }

        tvKeteranganTarif = TextView(this).apply {
            text = "Rincian tarif akan tampil di sini"
            textSize = 13f
            setTextColor(Color.parseColor("#718096"))
        }

        cardHasil.addView(tvHasilDurasi)
        cardHasil.addView(tvHasilBiaya)
        cardHasil.addView(tvKeteranganTarif)
        root.addView(cardHasil)

        scrollView.addView(root)
        setContentView(scrollView)

        // Otomatis hitung ulang jika ganti jenis kendaraan
        rgKendaraan.setOnCheckedChangeListener { _, _ ->
            hitungParkir()
        }

        // Hitung tampilan awal
        hitungParkir()
    }

    private fun formatWaktu(jam: Int, menit: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", jam, menit)
    }

    private fun hitungParkir() {
        val totalMenitMasuk = jamMasuk * 60 + menitMasuk
        var totalMenitKeluar = jamKeluar * 60 + menitKeluar

        // Jika jam keluar lebih kecil dari jam masuk, anggap lewat tengah malam (hari berikutnya)
        if (totalMenitKeluar < totalMenitMasuk) {
            totalMenitKeluar += 24 * 60
        }

        var selisihMenit = totalMenitKeluar - totalMenitMasuk
        if (selisihMenit == 0) {
            // Jika waktu masuk dan keluar persis sama, anggap minimal 1 menit (jam pertama)
            selisihMenit = 1
        }

        val durasiJam = selisihMenit / 60
        val sisaMenit = selisihMenit % 60

        // Tentukan tarif berdasarkan kendaraan terpilih
        val (namaKendaraan, tarifJamPertama, tarifLebih1Jam, tarifMaksimal) = when (rgKendaraan.checkedRadioButtonId) {
            1 -> TarifParkir("Motor", 3000, 5000, 7000)
            2 -> TarifParkir("Mobil", 5000, 8000, 10000)
            else -> TarifParkir("Truk", 6000, 9000, 15000)
        }

        // Logika penentuan biaya:
        // <= 60 menit -> Jam pertama
        // > 60 menit s/d 120 menit -> 1 jam lebih
        // > 120 menit -> Tarif maksimal
        val (biayaAkhir, statusTier) = when {
            selisihMenit <= 60 -> tarifJamPertama to "Tarif Jam Pertama (≤ 1 jam)"
            selisihMenit <= 120 -> tarifLebih1Jam to "Tarif > 1 Jam (1–2 jam)"
            else -> tarifMaksimal to "Tarif Maksimal (> 2 jam)"
        }

        val rupiahFormat = NumberFormat.getNumberInstance(Locale("in", "ID"))
        val biayaFormatted = "Rp ${rupiahFormat.format(biayaAkhir)}"

        tvHasilDurasi.text = "Durasi: $durasiJam jam $sisaMenit menit"
        tvHasilBiaya.text = "Total: $biayaFormatted"
        tvKeteranganTarif.text = "Kendaraan: $namaKendaraan • $statusTier\n" +
                "(Jam ke-1: Rp ${rupiahFormat.format(tarifJamPertama)} | >1 Jam: Rp ${rupiahFormat.format(tarifLebih1Jam)} | Maks: Rp ${rupiahFormat.format(tarifMaksimal)})"
    }

    data class TarifParkir(
        val nama: String,
        val jamPertama: Int,
        val lebihSatuJam: Int,
        val maksimal: Int
    )
}
