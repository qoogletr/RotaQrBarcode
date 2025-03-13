# Android Advanced Barcode Scanner

Modern Android barkod ve QR kod tarayıcı uygulaması. HTTP üzerinden taranan kodları farklı formatlarda gönderebilme özelliğine sahiptir.

## Özellikler

- 🎯 Barkod ve QR kod tarama
- 📤 Çoklu veri gönderim formatları (Plain Text, JSON, XML, CSV)
- 🎨 Modern Material Design 3 arayüzü
- 📱 Android 5.0 ve üzeri sürüm desteği
- ⚡ Otomatik ve manuel tarama modu
- 💾 Tarama geçmişi ve yerel depolama
- 🌐 Özelleştirilebilir sunucu ayarları
- 🔋 Çevrimdışı çalışabilme
- 📊 Detaylı istatistikler

## Ekran Görüntüleri

[Ekran görüntüleri buraya eklenecek]

## Kurulum

1. Android Studio'yu açın
2. 'Get from Version Control' seçeneğini seçin
3. URL'ye şunu yapıştırın: `https://github.com/qoogletr/android-barcode-scanner.git`
4. 'Clone' butonuna tıklayın

## Kullanım

### Sunucu Ayarları

Ayarlar menüsünden:
- Sunucu IP adresi
- Port numarası
- Endpoint (opsiyonel)
- Veri gönderim formatı seçilebilir

### Veri Formatları

1. **Plain Text**
```
23456667888888900
```

2. **Simple JSON**
```json
{
    "value": "23456667888888900"
}
```

3. **Detailed JSON**
```json
{
    "value": "23456667888888900",
    "timestamp": "2024-03-07 02:54:43",
    "type": "BARCODE"
}
```

4. **JSON with Device Info**
```json
{
    "value": "23456667888888900",
    "timestamp": "2024-03-07 02:54:43",
    "type": "BARCODE",
    "device": {
        "id": "device_id",
        "model": "Pixel 6",
        "manufacturer": "Google",
        "os": "Android 13"
    }
}
```

5. **XML**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<scan>
    <value>23456667888888900</value>
    <timestamp>2024-03-07 02:54:43</timestamp>
    <type>BARCODE</type>
</scan>
```

6. **CSV**
```
23456667888888900,2024-03-07 02:54:43,BARCODE
```

## Geliştirme

### Gereksinimler

- Android Studio Hedgehog | 2023.1.1 veya üzeri
- Android SDK 21-34
- Kotlin 1.9.0 veya üzeri

### Bağımlılıklar

```gradle
dependencies {
    // Android core dependencies
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    
    // Barcode scanning
    implementation 'com.google.mlkit:barcode-scanning:17.2.0'
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
    
    // HTTP client
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    
    // Camera
    implementation 'androidx.camera:camera-camera2:1.3.1'
    implementation 'androidx.camera:camera-lifecycle:1.3.1'
    implementation 'androidx.camera:camera-view:1.3.1'
}
```

## Katkıda Bulunma

1. Bu repository'yi fork edin
2. Feature branch'i oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır - Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## İletişim

QoogleTr - [@qoogletr](https://github.com/qoogletr)

Proje Linki: [https://github.com/qoogletr/android-barcode-scanner](https://github.com/qoogletr/android-barcode-scanner)