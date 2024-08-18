package app.revanced.patches.kakao.message.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.Opcode

internal object SetChatTypeMethodFingerprint : MethodFingerprint(
    parameters = listOf("I"),
    returnType = "V",
    opcodes = listOf(
        Opcode.IPUT,
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IPUT_OBJECT,
        Opcode.RETURN_VOID
    )
)