package app.revanced.patches.kakao.detection.signature

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.detection.signature.fingerprints.VerifyingSignatureFingerprint

@Patch(
    name = "Disable checking app signature",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object VerifyingSignaturePatch : BytecodePatch(
    setOf(VerifyingSignatureFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = VerifyingSignatureFingerprint.result!!.mutableMethod

        method.addInstructions(0, """
            const/4 v0, 0x1
            return v0
        """.trimIndent())
    }
}