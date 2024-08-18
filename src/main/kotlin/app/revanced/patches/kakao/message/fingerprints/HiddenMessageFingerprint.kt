package app.revanced.patches.kakao.message.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object HiddenMessageFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.STATIC,
    strings = listOf("feedType", "previous_message", "previous_enc", "enc : %s, %s")
)