package app.revanced.patches.kakao.changemodel.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint

internal object ChangeModelFingerprint : MethodFingerprint(
    strings = listOf("MODEL", "\\s", "-", "US", "toUpperCase(...)"),
    customFingerprint = { _, classDef ->
        classDef.methods.indexOf(classDef.methods.last()) > 2
    }
)