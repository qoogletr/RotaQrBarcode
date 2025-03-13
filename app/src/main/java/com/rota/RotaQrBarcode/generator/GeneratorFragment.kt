// ... önceki kodlar ...

    private fun showGeneratedCode(bitmap: Bitmap) {
        binding.generatedImageView.alpha = 0f
        binding.generatedImageView.setImageBitmap(bitmap)
        
        // Fade in animasyonu
        binding.generatedImageView.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
            
        // Butonları göster
        binding.shareButton.apply {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay(200)
                .start()
        }
        
        binding.saveButton.apply {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay(300)
                .start()
        }
    }

    private fun setupGenerateButtonAnimation() {
        binding.generateButton.setOnClickListener { view ->
            // Butonu küçült ve büyüt
            view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            // Animasyon bittikten sonra QR kodu oluştur
                            val content = binding.contentInput.text.toString()
                            if (content.isNotEmpty()) {
                                when (binding.formatGroup.checkedRadioButtonId) {
                                    R.id.qr_radio -> generateQRCode(content)
                                    R.id.barcode_radio -> generateBarcode(content)
                                }
                            }
                        }
                        .start()
                }
                .start()
        }
    }

// ... önceki kodlar ...