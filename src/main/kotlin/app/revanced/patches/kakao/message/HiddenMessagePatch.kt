package app.revanced.patches.kakao.message

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.message.fingerprints.HiddenMessageFingerprint
import app.revanced.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.Reference


@Patch(
    name = "Passing set message type to hidden",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object HiddenMessagePatch : BytecodePatch(
    setOf(HiddenMessageFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = HiddenMessageFingerprint.result!!.mutableMethod
        val checkTypeInstruction = method.getInstructions()
            .find {
                instruction -> instruction.opcode === Opcode.SGET_OBJECT && instruction.getReference<Reference>().toString().contains("Reply")
            }
        val chatTypeFeedInstruction = method.getInstructions()
            .find {
                instruction -> instruction.opcode === Opcode.SGET_OBJECT && instruction.getReference<Reference>().toString().contains("Feed")
            }
        val tempInstruction = method.getInstructions().last { instruction ->
            instruction.opcode === Opcode.CONST_STRING && instruction.getReference<Reference>().toString().contains("enc :")
        }
        val chatTypeRegister = (chatTypeFeedInstruction as OneRegisterInstruction).registerA
        val resultRegister = (method.getInstruction(checkTypeInstruction!!.location.index - 1) as OneRegisterInstruction).registerA
        val tempRegister = (tempInstruction as OneRegisterInstruction).registerA

        method.addInstructions(
            checkTypeInstruction.location.index,
            """
                move/16 v$chatTypeRegister, v$resultRegister
                const v$tempRegister, 0x8000
                add-int/2addr v$chatTypeRegister, v$tempRegister
            """.trimIndent()
        )

//        val replaceStringInstruction = method.getInstructions().last { instruction ->
//            instruction.opcode === Opcode.INVOKE_STATIC
//        }
//
//        method.removeInstruction(replaceStringInstruction.location.index - 1)
    }
}