package dev.lucasnlm.antimine.common.level.data

import androidx.annotation.Keep

@Keep
enum class DifficultyPreset(val text: String) {
    Standard("STANDARD"),
    Beginner("BEGINNER"),
    Intermediate("INTERMEDIATE"),
    Expert("EXPERT"),
    Custom("CUSTOM")
}
