package app.revanced.patches.kakao.message.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object DeletedMessageFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.STATIC,
    parameters = listOf("I"),
    opcodes = listOf(Opcode.IF_LEZ,
        Opcode.AND_INT_LIT16,
        Opcode.IF_EQZ,
        Opcode.SGET_OBJECT,
        Opcode.RETURN_OBJECT,
        Opcode.IF_LEZ,
        Opcode.CONST,
        Opcode.AND_INT_2ADDR,
        Opcode.IF_EQZ,
        Opcode.SGET_OBJECT,
        Opcode.RETURN_OBJECT,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.ARRAY_LENGTH,
        Opcode.CONST_4,
        Opcode.IF_GE,
        Opcode.AGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_NE,
        Opcode.RETURN_OBJECT,
        Opcode.ADD_INT_LIT8,
        Opcode.GOTO,
        Opcode.SGET_OBJECT,
        Opcode.RETURN_OBJECT)
)