package app.revanced.patches.kakao.misc.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object LogReqPacketFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    returnType = "V",
    strings = listOf("locoReq", "session collapsed", "not connected")
)

internal object LogResPacketFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC.value,
    returnType = "V",
    strings = listOf("status", "reason")
)