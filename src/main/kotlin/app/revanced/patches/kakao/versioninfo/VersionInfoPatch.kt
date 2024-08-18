package app.revanced.patches.kakao.versioninfo

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.versioninfo.fingerprints.VersionInfoFingerprint
import app.revanced.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.Reference

@Patch(
    name = "Replacing version info",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object VersionInfoPatch : BytecodePatch(
    setOf(VersionInfoFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = VersionInfoFingerprint.result?.mutableMethod
        val instruction = method?.getInstructions()?.find { instruction -> instruction.opcode === Opcode.CONST_STRING }

        val versionString = (instruction as OneRegisterInstruction).getReference<Reference>().toString()
        method.replaceInstruction(
            instruction.location.index,
            "const-string v${instruction.registerA}, \"$versionString - Revanced\""
        )
    }
}