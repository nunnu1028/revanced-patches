package app.revanced.patches.kakao.misc.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.or
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.revanced.util.getReference
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.Reference

internal object ShopTabFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    returnType = "V",
    strings = listOf("webtoon"),
    customFingerprint = { method, _ ->
        !method.toMutable().getInstructions().any {
            it.opcode === Opcode.CONST_STRING && !it.getReference<Reference>().toString().contains("webtoon")
        }
    }
)