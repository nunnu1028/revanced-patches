package app.revanced.patches.kakao.message

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.kakao.message.fingerprints.ChatlogClassFingerprint
import app.revanced.patches.kakao.message.fingerprints.HookTimeFingerprint
import com.android.tools.smali.dexlib2.Opcode

@Patch(
    name = "Show that message is deleted or hidden",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object HookTimePatch : BytecodePatch(
    setOf(HookTimeFingerprint, ChatlogClassFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = HookTimeFingerprint.result!!.mutableMethod
        val chatlogInstruction = method.getInstructions().filter { instruction -> instruction.opcode === Opcode.IGET_OBJECT }[1]
        val chatLogClass = ChatlogClassFingerprint.result!!.mutableMethod.definingClass

        method.addInstructionsWithLabels(
            chatlogInstruction.location.index + 1,
            """
                const-string v2, ""
                check-cast v0, $chatLogClass
                
                iget-boolean v1, v0, $chatLogClass->isHidden:Z
                if-eqz v1, :cond_check_deleted
                const-string v2, " [Hidden]"
                goto :cond_return_value
                
                :cond_check_deleted
                iget-boolean v1, v0, $chatLogClass->isRealDeleted:Z
                if-eqz v1, :cond_return_value
                const-string v2, " [Deleted]"
            """.trimIndent(),
            ExternalLabel("cond_return_value", method.getInstruction(chatlogInstruction.location.index + 1))
        )

        val resultInstruction = method.getInstructions().find { instruction -> instruction.opcode === Opcode.MOVE_RESULT_OBJECT }
        method.removeInstruction(resultInstruction!!.location.index)
        method.addInstructions(
            resultInstruction.location.index,
            """
                move-result-object v0
                new-instance v1, Ljava/lang/StringBuilder;
                invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V
                invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;
                invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;
                move-result-object v0
            """.trimIndent()
        )
    }
}