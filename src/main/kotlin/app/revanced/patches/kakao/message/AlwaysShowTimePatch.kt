package app.revanced.patches.kakao.message

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.message.fingerprints.AlwaysShowTimeFingerprint
import app.revanced.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Patch(
    name = "Always show message written time",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object AlwaysShowTimePatch : BytecodePatch(
    setOf(AlwaysShowTimeFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = AlwaysShowTimeFingerprint.result!!.mutableMethod
        val instruction = method.getInstructions()
            .find {
                instruction -> instruction.opcode === Opcode.INVOKE_VIRTUAL && instruction.getReference<MethodReference>().toString().contains("hideDate")
            }

        method.removeInstruction(instruction!!.location.index)
    }
}