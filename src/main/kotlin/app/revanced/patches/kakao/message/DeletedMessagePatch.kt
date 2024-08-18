package app.revanced.patches.kakao.message

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.kakao.message.fingerprints.DeletedMessageFingerprint
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.Instruction

@Patch(
    name = "Passing check if the message is deleted or hidden",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object DeletedMessagePatch : BytecodePatch(
    setOf(DeletedMessageFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = DeletedMessageFingerprint.result!!.mutableMethod

        val instructions = method.getInstructions()
            .filter {
                instruction -> instruction.opcode === Opcode.SGET_OBJECT
            }
        val condInstruction = method.getInstructions()
            .find {
                instruction -> instruction.opcode === Opcode.INVOKE_STATIC
            } as Instruction
        val label = ExternalLabel("cond_return_value", condInstruction)

        method.removeInstruction(instructions[0].location.index + 1)
        method.removeInstruction(instructions[0].location.index)
        method.addInstructionsWithLabels(
            instructions[0].location.index,
            """
                and-int/lit16 p0, p0, -0x4001
                goto :cond_return_value
            """.trimIndent(),
            label
        )

        method.removeInstruction(instructions[1].location.index + 1)
        method.removeInstruction(instructions[1].location.index)
        method.addInstructionsWithLabels(
            instructions[1].location.index,
            """
                const v1, -0x8001
                and-int/2addr p0, v1
                goto :cond_return_value
            """.trimIndent(),
            label
        )
    }
}