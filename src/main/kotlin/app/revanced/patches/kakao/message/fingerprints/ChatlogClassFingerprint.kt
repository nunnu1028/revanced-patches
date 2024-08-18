package app.revanced.patches.kakao.message.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint

internal object ChatlogClassFingerprint : MethodFingerprint(
    strings = listOf("%s.thumbnail")
)